package NateQuest.Tiling.Tiles;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.newdawn.slick.Image;

import GameClasses.CUtil;
import GameClasses.Sprite;
import GameClasses.StackedSprite;
import NateQuest.Debugger;
import NateQuest.Drawables.Drawable;
import NateQuest.Tiling.TileMap;
import NateQuest.Tiling.Tiles.Tile.BasicType;
import NateQuest.Tiling.Tiles.WallTile.WallType;

public class TileBlends {

	public static Sprite getTileBlend(Tile tile, TileMap map, String imageName) {
		if (map == null)
			return new Sprite(CUtil.QuickImage(imageName));
		Sprite toReturn = null;
		Image topImage = null;
		Image bottomImage = null;
		BasicType typeToBlendWith = getTypeToBlendWith(tile, map);
		if (typeToBlendWith == BasicType.WATER)
			return new Sprite(CUtil.QuickImage(imageName));
		Tile prototype = new Tile(null, typeToBlendWith, -1, -1);
		// Tile prototype = new Tile(null, BasicType.REDRUG, -1, -1);
		BasicType typeBlendedWith = prototype.type;
		float angle = 0;
		int sandcount = 0;
		int dircount = 0;
		int i = tile.myI;
		int j = tile.myJ;
		if (isType(i - 1, j, typeBlendedWith, map)) {
			sandcount++;
			dircount += 1;
		}
		if (isType(i + 1, j, typeBlendedWith, map)) {
			sandcount++;
			dircount += 2;
		}
		if (isType(i, j + 1, typeBlendedWith, map)) {
			sandcount++;
			dircount += 4;
		}
		if (isType(i, j - 1, typeBlendedWith, map)) {
			sandcount++;
			dircount += 8;
		}

		switch (sandcount) {
		case 0:
			if (isType(i - 1, j - 1, typeBlendedWith, map)) {
				topImage = CUtil.QuickImage(imageName + "fadeinvertedBLEND");
				angle = -90 * 0;
			} else if (isType(i + 1, j - 1, typeBlendedWith, map)) {
				topImage = CUtil.QuickImage(imageName + "fadeinvertedBLEND");
				angle = 90 * 1;
			} else if (isType(i + 1, j + 1, typeBlendedWith, map)) {
				topImage = CUtil.QuickImage(imageName + "fadeinvertedBLEND");
				angle = 90 * 2;
			} else if (isType(i - 1, j + 1, typeBlendedWith, map)) {
				topImage = CUtil.QuickImage(imageName + "fadeinvertedBLEND");
				angle = 90 * 3;
			} else {

			}
			break;
		case 1:
			topImage = CUtil.QuickImage(imageName + "fadeupBLEND");
			break;
		case 2:
			topImage = CUtil.QuickImage(imageName + "fadecornerBLEND");
			break;
		case 3:
			toReturn = new Sprite(CUtil.QuickImage("grassblendsandthree"));
			break;
		case 4:
			toReturn = new Sprite(CUtil.QuickImage("grassblendsandfour"));
			break;
		}

		if (topImage == null) {
			toReturn = new Sprite(CUtil.QuickImage(imageName));
		} else {
			bottomImage = prototype.tileSprite.mTexture;
			toReturn = new StackedSprite(bottomImage, topImage);

			if (sandcount == 0)
				toReturn.setRotation(angle);
		}
		toReturn.camera = map.parentChunkMap.parentScreen.camera;
		toReturn = rotateBlendedSprite(toReturn, sandcount, dircount);
		return toReturn;
	}

	private static BasicType getTypeToBlendWith(Tile tile, TileMap map) {
		int[] counters = new int[BasicType.values().length];
		int i = tile.myI;
		int j = tile.myJ;

		for (int k = i - 1; k <= i + 1; k++) {
			for (int h = j - 1; h <= j + 1; h++) {
				BasicType t = getType(k, h, map);
				if (t != null && t != tile.type && t != BasicType.WATER)
					counters[t.ordinal()]++;
			}
		}

		int maxDex = 0;
		for (int l = 0; l < counters.length; l++) {
			if (counters[maxDex] < counters[l])
				maxDex = l;
		}
		return BasicType.values()[maxDex];
	}

	public static void makeBlendedDrawable(Drawable drawable, Tile tile,
			TileMap map) {

	}

	public static Sprite rotateBlendedSprite(Sprite toRotate,
			int otherTileCount, int dircount) {
		if (otherTileCount == 1) {
			if (dircount == 1) {
				toRotate.setRotation(-90 * 1);
			} else if (dircount == 2) {
				toRotate.setRotation(-90 * 3);
			} else if (dircount == 4) {
				toRotate.setRotation(-90 * 2);
			} else if (dircount == 8) {
				toRotate.setRotation(-90 * 4);
			}
		} else if (otherTileCount == 2) {
			if (dircount == 5) {
				toRotate.setRotation(-90 * 2);
			} else if (dircount == 6) {
				toRotate.setRotation(-90 * 3);
			} else if (dircount == 9) {
				toRotate.setRotation(-90 * 1);
			} else if (dircount == 10) {
				toRotate.setRotation(-90 * 4);
			}
		}
		return toRotate;
	}

	public static boolean isType(int i, int j, BasicType type, TileMap map) {
		if (map.isInBounds(i, j) && map.tiles[i][j] != null
				&& map.tiles[i][j].type == type)
			return true;
		return false;
	}

	public static boolean isType(int i, int j, WallType type, TileMap map) {
		if (map.isInBounds(i, j) && map.tiles[i][j] != null
				&& map.tiles[i][j] instanceof WallTile) {
			WallTile t = (WallTile) map.tiles[i][j];
			if (t.wallType == type) {
				return true;
			}
		}
		return false;
	}

	public static BasicType getType(int i, int j, TileMap map) {
		if (map.isInBounds(i, j) && map.tiles[i][j] != null
				&& map.tiles[i][j].getClass() == Tile.class)
			return map.tiles[i][j].type;
		else
			return null;
	}

	public static WallType getWallType(int i, int j, TileMap map) {
		if (map.isInBounds(i, j) && map.tiles[i][j] != null
				&& map.tiles[i][j].getClass() == WallTile.class) {
			WallTile t = (WallTile) map.tiles[i][j];
			return t.wallType;
		} else {
			return null;
		}
	}
}
