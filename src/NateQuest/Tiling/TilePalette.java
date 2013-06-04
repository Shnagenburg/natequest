package NateQuest.Tiling;

import java.io.Serializable;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import NateQuest.Debugger;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.SpriteMapped;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.WallTile;

public class TilePalette implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6153403144149321726L;
	public static final int PAL_WIDTH = 3;
	public static final int PAL_HEIGHT = 9;
	public static final float PAL_SCALE = 0.5f;

	Tile selectedTile;
	SpriteMapped drawable;
	TilePainter parentPainter;
	Tile[][] tiles;

	public TilePalette(TilePainter parentPainter) {
		this.parentPainter = parentPainter;
		drawable = new SpriteMapped(parentPainter, LayerConstants.HUD);
		drawable.createDoubleArray(PAL_WIDTH, PAL_HEIGHT);
		drawable.setSaveThisProperty(true);
		tiles = new Tile[PAL_WIDTH][PAL_HEIGHT];
		anchor();
	}

	private void anchor() {
		int defShiftX = (int) ((Tile.TILE_SIZE / 2) * PAL_SCALE);
		int defShiftY = (int) ((Tile.TILE_SIZE / 2) * PAL_SCALE);
		int count = 0;

		Tile.BasicType[] types = Tile.BasicType.values();
		for (int i = 0; i < types.length; i++) {
			Tile t = new Tile(null, types[i], -1, -1);
			t.tileSprite.bIsCameraed = false;

			int xSlot = i % PAL_WIDTH;
			int ySlot = i / PAL_WIDTH;
			int x = (int) (defShiftX + (Tile.TILE_SIZE * PAL_SCALE * xSlot));
			int y = (int) (defShiftY + (Tile.TILE_SIZE * PAL_SCALE * ySlot));
			t.tileSprite.setPosition(x, y);
			t.tileSprite.setScale(PAL_SCALE);
			drawable.sprites[xSlot][ySlot] = t.tileSprite;
			tiles[xSlot][ySlot] = t;
		}
		count = types.length + PAL_WIDTH;
		WallTile.WallType[] wallTypes = WallTile.WallType.values();
		for (int k = 0; k < wallTypes.length; k++) {
			WallTile t = new WallTile(null, wallTypes[k], -1, -1, 0);
			t.tileSprite.bIsCameraed = false;

			int xSlot = (k + count) % PAL_WIDTH;
			int ySlot = (k + count) / PAL_WIDTH;
			int x = (int) (defShiftX + (Tile.TILE_SIZE * PAL_SCALE * xSlot));
			int y = (int) (defShiftY + (Tile.TILE_SIZE * PAL_SCALE * ySlot));
			t.tileSprite.setPosition(x, y);
			t.tileSprite.setScale(PAL_SCALE);
			drawable.sprites[xSlot][ySlot] = t.tileSprite;
			tiles[xSlot][ySlot] = t;
		}
	}

	public void update() {
		int x = MouseManager.iX;
		int y = MouseManager.iY;

		if (MouseManager.getLeftClicked()) {
			Tile t = getTileFromPoint(x, y);
			if (t != null) {
				if (selectedTile != null)
					selectedTile.tileSprite.setRotation(0);
				selectedTile = t;
				t.tileSprite.setRotation(45);
			}
		}
	}

	public Tile getTileFromPoint(int x, int y) {
		int i = (int) (x / (Tile.TILE_SIZE * PAL_SCALE));
		int j = (int) (y / (Tile.TILE_SIZE * PAL_SCALE));
		// Debugger.print("" + i + " " + j);

		if (i < 0 || i >= PAL_WIDTH || j < 0 || j >= PAL_HEIGHT) {
			return null;
		}
		return tiles[i][j];
	}

}
