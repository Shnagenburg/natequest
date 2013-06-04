package NateQuest.Tiling;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;

public class ChunkMap extends Entity {

	private int MAX_WIDTH = 2;
	private int MAX_HEIGHT = 2;
	final int CHUNK_BUFFER = 0;

	TileMap[][] chunks;
	int activeI;
	int activeJ;
	int overlay = 0;

	public ChunkMap(GameScreen gs, SavedChunkMap data, int overlay) {
		super(gs);
		this.overlay = overlay;
		MAX_HEIGHT = data.height;
		MAX_WIDTH = data.width;
		setup(data.mapData);
		saveThisEntity = false;
	}

	public ChunkMap(GameScreen gs, int width, int height, int overlay) {
		super(gs);
		MAX_HEIGHT = height;
		MAX_WIDTH = width;
		setup(overlay);
		saveThisEntity = false;
	}

	public ChunkMap(GameScreen gs) {
		super(gs);
		setup(0);
		saveThisEntity = false;

	}

	public ChunkMap(GameScreen gs, int overlay) {
		super(gs);
		setup(overlay);
		saveThisEntity = false;
	}

	public void setup(int overlay) {
		chunks = new TileMap[MAX_WIDTH][MAX_HEIGHT];
		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				if (overlay == LayerConstants.TILEMAP)
					chunks[i][j] = new TileMap(this, i, j);
				else
					chunks[i][j] = new TileMap(this, i, j, overlay, true);
			}
		}

		activeI = 0;
		activeJ = 0;
	}

	public void setup(TileMapCompressedData[][] data) {
		chunks = new TileMap[MAX_WIDTH][MAX_HEIGHT];
		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				chunks[i][j] = new TileMap(this, i, j, data[i][j], overlay);
			}
		}
		activeI = 0;
		activeJ = 0;
	}

	public void update() {
		int x = activeI;
		int y = activeJ;
		if (x + CHUNK_BUFFER > MAX_WIDTH) {
			x = MAX_WIDTH - (CHUNK_BUFFER + 1);
		} else if (x - CHUNK_BUFFER < 0) {
			x = CHUNK_BUFFER;
		}
		if (y + CHUNK_BUFFER > MAX_HEIGHT) {
			y = MAX_HEIGHT - (CHUNK_BUFFER + 1);
		} else if (y - CHUNK_BUFFER < 0) {
			y = CHUNK_BUFFER;
		}
		// Debugger.print("x: " + x + " y: " + y);
		// bounds
		for (int i = x - CHUNK_BUFFER; i < x + CHUNK_BUFFER + 1; i++) {
			for (int j = y - CHUNK_BUFFER; j < y + CHUNK_BUFFER + 1; j++) {
				if (chunks[i][j] != null)
					chunks[i][j].update();
			}
		}
	}

	public void setActiveChunk(float x, float y) {
		int newactiveI = (int) ((x + (Tile.TILE_SIZE / 2)) / (TileMap.CHUNK_WIDTH * Tile.TILE_SIZE));
		int newactiveJ = (int) ((y + (Tile.TILE_SIZE / 2)) / (TileMap.CHUNK_HEIGHT * Tile.TILE_SIZE));

		if (!isInBounds(newactiveI, newactiveJ))
			return;

		if (activeI != newactiveI || activeJ != newactiveJ) {
			activeI = newactiveI;
			activeJ = newactiveJ;
			for (int i = 0; i < MAX_WIDTH; i++) {
				for (int j = 0; j < MAX_HEIGHT; j++) {
					if (chunks[i][j] != null) {
						if (Math.abs(i - activeI) <= 1
								&& Math.abs(j - activeJ) <= 1)
							chunks[i][j].drawableSpriteMapped.bIgnore = false;
						else
							chunks[i][j].drawableSpriteMapped.bIgnore = true;
					}
				}
			}
		}
	}

	public TileMap getTileMapFromPoint(float x, float y) {
		int i = (int) ((x + (Tile.TILE_SIZE / 2)) / (TileMap.CHUNK_WIDTH * Tile.TILE_SIZE));
		int j = (int) ((y + (Tile.TILE_SIZE / 2)) / (TileMap.CHUNK_HEIGHT * Tile.TILE_SIZE));

		if (i < 0 || i >= MAX_WIDTH || j < 0 || j >= MAX_HEIGHT) {
			return null;
		}
		// Debugger.print("chunkmap " + " " + this + " chunks " + chunks);

		return chunks[i][j];
	}

	public Tile getTileFromPoint(float x, float y) {

		TileMap m = getTileMapFromPoint(x, y);

		if (m != null) {
			return m.getTileFromPoint(x, y);
		}
		return null;
	}

	private boolean isInBounds(int x, int y) {
		return x >= 0 && x < MAX_WIDTH && y >= 0 && y < MAX_HEIGHT;
	}

	public boolean isNullTile(float x, float y) {
		TileMap m = getTileMapFromPoint(x, y);
		if (m == null) {
			return false;
		} else {
			return m.isNullTile(x, y);
		}
	}

	public void createTileOnPoint(int x, int y, Tile prototype,
			boolean isBlending) {
		TileMap m = getTileMapFromPoint(x, y);
		m.createTileOnPoint(x, y, prototype, isBlending);
	}

	public void deleteTileOnPoint(int x, int y) {
		TileMap m = getTileMapFromPoint(x, y);
		if (m != null) {
			m.deleteTileOnPoint(x, y);
		}
	}

	public int getOverlay() {
		return overlay;
	}

	public int getWidth() {
		return MAX_WIDTH;
	}

	public int getHeight() {
		return MAX_HEIGHT;
	}

	public TileMap[][] getChunks() {
		return chunks;
	}

	public void reblend() {
		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				if (chunks[i][j] != null) {
					chunks[i][j].reblendMap();
				}
			}
		}
	}

}
