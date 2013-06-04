package NateQuest.Tiling.Tiles;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Gameplay.Camera;
import NateQuest.Tiling.TileCompressionData;
import NateQuest.Tiling.TileMap;
import NateQuest.Tiling.Tiles.Tile.BasicType;

public class WallTile extends Tile {

	public final static String[] wallImageMap = { "brick2", "dunwall",
			"dunwall2", "ironbars", "woodwall", "greywall" };

	public enum WallType {
		BRICK, DUNWALL, DUNWALL_2, IRONBARS, BARWALL, TRUSSBLACK
	}

	public WallType wallType;

	public WallTile(TileMap parentMap, WallType type, int i, int j,
			TileCompressionData data) {
		this(parentMap, data.getWallType(), i, j, data.getRotation());
	}

	public WallTile(TileMap parentMap, WallType type, int i, int j,
			float rotation) {
		super(parentMap, null, i, j);
		this.isBlended = false;
		this.rotation = rotation;
		wallType = type;
		setUpSprite();
		isCollidable = true;
	}

	protected void setUpSprite() {
		if (wallType == null)
			return;

		String name = wallImageMap[wallType.ordinal()];

		if (isBlended) {
			tileSprite = TileBlends.getTileBlend(this, parentMap, name);
		} else {
			tileSprite = new Sprite(CUtil.ResourcePool.getImageByName(name));
		}

		if (parentMap == null)
			return;

		if (!isBlended)
			tileSprite.setRotation(tileSprite.getRotation() + rotation);
		tileSprite.camera = parentMap.parentChunkMap.parentScreen.camera;

		anchor(myI, myJ);
		parentMap.drawableSpriteMapped.sprites[myI][myJ] = tileSprite;
	}

	public WallTile createTileFromOther(Tile oldTile, boolean isBlending) {
		return new WallTile(oldTile.parentMap, this.wallType, oldTile.myI,
				oldTile.myJ, 0);
	}

	public WallTile createTileFromParams(TileMap parent, int i, int j,
			boolean isBlending) {
		return new WallTile(parent, this.wallType, i, j, 0);
	}
}
