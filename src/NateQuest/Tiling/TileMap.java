package NateQuest.Tiling;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Drawables.SpriteMapped;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.Tile.BasicType;
import NateQuest.Tiling.Tiles.WallTile.WallType;
import NateQuest.Tiling.Tiles.WallTile;

public class TileMap {

	public static final int CHUNK_WIDTH = 12;
	public static final int CHUNK_HEIGHT = 12;

	public ChunkMap parentChunkMap;
	public Point origin = new Point(0, 0);
	public Tile[][] tiles;
	public SpriteMapped drawableSpriteMapped;
	public int myI;
	public int myJ;

	public TileMap(ChunkMap parentChunkMap, int x, int y,
			TileMapCompressedData data, int layer) {
		this.parentChunkMap = parentChunkMap;
		myI = x;
		myJ = y;
		makeTileDrawable(layer);
		origin.x = x * CHUNK_WIDTH * Tile.TILE_SIZE;
		origin.y = y * CHUNK_HEIGHT * Tile.TILE_SIZE;
		tiles = data.getTileMap(this);
		reblendMap();
	}

	public TileMap(ChunkMap parentChunkMap, int x, int y) {
		this.parentChunkMap = parentChunkMap;
		myI = x;
		myJ = y;
		makeTileDrawable(0);
		origin.x = x * CHUNK_WIDTH * Tile.TILE_SIZE;
		origin.y = y * CHUNK_HEIGHT * Tile.TILE_SIZE;
		fillMapBasic();
	}

	public TileMap(ChunkMap parentChunkMap, int x, int y, int overlay) {
		this.parentChunkMap = parentChunkMap;
		myI = x;
		myJ = y;
		makeTileDrawable(overlay);
		origin.x = x * CHUNK_WIDTH * Tile.TILE_SIZE;
		origin.y = y * CHUNK_HEIGHT * Tile.TILE_SIZE;
		fillMapWall();
	}

	public TileMap(ChunkMap parentChunkMap, int x, int y, int overlay,
			boolean isNulled) {
		this.parentChunkMap = parentChunkMap;
		myI = x;
		myJ = y;
		makeTileDrawable(overlay);
		origin.x = x * CHUNK_WIDTH * Tile.TILE_SIZE;
		origin.y = y * CHUNK_HEIGHT * Tile.TILE_SIZE;
		fillMapNull();
	}

	private void fillMapWall() {
		tiles = new Tile[CHUNK_WIDTH][CHUNK_HEIGHT];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				// if (i == 0 || i == CHUNK_WIDTH - 1 || j == CHUNK_HEIGHT - 1)
				// tiles[i][j] = new Tile(this, BasicType.GLASS, i, j);
			}
		}
	}

	private void fillMapNull() {
		tiles = new Tile[CHUNK_WIDTH][CHUNK_HEIGHT];
	}

	private void fillMapBasic() {
		tiles = new Tile[CHUNK_WIDTH][CHUNK_HEIGHT];
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				if (i > 1 && i < 8 && j > 1 && j < 8) {
					if (i > 3 && i < 6 && j > 3 && j < 6)
						tiles[i][j] = new Tile(this, BasicType.GRASS, i, j);
					else
						tiles[i][j] = new Tile(this, BasicType.SAND, i, j);
				} else if (i <= 1 || i >= 8 || j <= 1 || j >= 8) {
					if (i == 0 || i == CHUNK_WIDTH - 1 || j == 0
							|| j == CHUNK_HEIGHT - 1)
						tiles[i][j] = new Tile(this, BasicType.CRACKEDSTONE, i,
								j);
					else
						tiles[i][j] = new Tile(this, BasicType.WATER, i, j);
				}
			}
		}
	}

	private void makeTileDrawable(int overlay) {
		drawableSpriteMapped = new SpriteMapped(parentChunkMap, overlay);
		drawableSpriteMapped.createDoubleArray(CHUNK_WIDTH, CHUNK_HEIGHT);

	}

	// This is for when water is right next to sand, add the pretty overlap
	private void applyOverlaps() {
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				for (int k = 0; k < 4; k++) {
					applyOverLap(tiles[i][j], i, j, k);
				}
			}
		}
	}

	// dir: 0 = down, 1 = left, 2 = up, 3 = right
	private void applyOverLap(Tile source, int x, int y, int dir) {
		return;
		/*
		 * switch (dir) { case 0: y++; break; case 1: x--; break; case 2: y--;
		 * break; case 3: x++; break; } if (isInBounds(x, y)) { Tile target =
		 * tiles[x][y]; Sprite s = null; if (source.type == BasicType.WATER &&
		 * target.type == BasicType.SAND) { s = new
		 * Sprite(CUtil.ResourcePool.getImageByName("sandoverlapup")); } else if
		 * (source.type == BasicType.SAND && target.type == BasicType.GRASS) { s
		 * = new Sprite(CUtil.ResourcePool.getImageByName("grassoverlapup")); }
		 * if (s != null) { //s.setPosition(source.drawable.mSprite.getX(),
		 * source.drawable.mSprite.getY()); //s.setRotation((dir - 2)* 90);
		 * //source.drawable.addSprite(s); }
		 * 
		 * }
		 */
	}

	public boolean isInBounds(int x, int y) {
		return x >= 0 && x < CHUNK_WIDTH && y >= 0 && y < CHUNK_HEIGHT;
	}

	public void update() {
		for (int i = 0; i < CHUNK_WIDTH; i++) {
			for (int j = 0; j < CHUNK_HEIGHT; j++) {
				if (tiles[i][j] != null)
					tiles[i][j].tileSprite.update();
			}
		}
	}

	public Tile getTileFromPoint(float x, float y) {
		x = (x - origin.x) + (Tile.TILE_SIZE / 2);
		y = (y - origin.y) + (Tile.TILE_SIZE / 2);

		if (x < 0 || y < 0)
			return null;

		int i = (int) (x) / Tile.TILE_SIZE;
		int j = (int) (y) / Tile.TILE_SIZE;

		// Debugger.print("tilemap " + i + " " + j);

		if (i < 0 || j < 0 || i >= CHUNK_WIDTH || j >= CHUNK_WIDTH)
			return null;

		return tiles[i][j];
	}

	public boolean isNullTile(float x, float y) {
		x = x - origin.x;
		y = y - origin.y;
		int i = (int) (x + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;
		int j = (int) (y + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;

		// Debugger.print("tilemap " + i + " " + j);

		if (i < 0 || j < 0 || i >= CHUNK_WIDTH || j >= CHUNK_WIDTH)
			return false;

		return true;
	}

	public void createTileOnPoint(int x, int y, Tile prototype,
			boolean isBlending) {
		x = x - origin.x;
		y = y - origin.y;
		int i = (int) (x + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;
		int j = (int) (y + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;

		// Debugger.print("tilemap " + i + " " + j);

		if (i < 0 || j < 0 || i >= CHUNK_WIDTH || j >= CHUNK_WIDTH)
			return;

		prototype.createTileFromParams(this, i, j, isBlending);
	}

	public void deleteTileOnPoint(int x, int y) {
		x = x - origin.x;
		y = y - origin.y;
		int i = (int) (x + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;
		int j = (int) (y + (Tile.TILE_SIZE / 2)) / Tile.TILE_SIZE;

		// Debugger.print("tilemap " + i + " " + j);

		if (i < 0 || j < 0 || i >= CHUNK_WIDTH || j >= CHUNK_WIDTH)
			return;

		tiles[i][j] = null;
		drawableSpriteMapped.sprites[i][j] = null;
	}

	public void reblendMap() {
		for (int i = 0; i < tiles[0].length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j] != null) {
					tiles[i][j].reblend();
				}
			}
		}
	}

}
