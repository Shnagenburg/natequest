package NateQuest.Tiling.Tiles;

import java.io.IOException;
import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Collidables.Collidable;
import NateQuest.HUD.HealthBar;
import NateQuest.Tiling.TileCompressionData;
import NateQuest.Tiling.TileMap;
import NateQuest.Tiling.TilePainter;

public class Tile implements Serializable {

	public boolean isBlended = false;
	public final static int TILE_SIZE = 64;
	public final static String[] tileImageMap = { "grass3", "sand2",
			"blueporcelain", "stone", "crackedstone", "purplerug", "glass",
			"wood", "porcelain1", "crackedstonegems", "gemfloor", "redrug2",
			"greenporcelain", "molten1", "greymarble", "bluemarbel",
			"orangerug", "pinkcracked" };

	public enum BasicType {
		GRASS, SAND, WATER, STONE, CRACKEDSTONE, REDRUG, GLASS, ROAD, ROADX, ROADT, ROADTURN, SAND_EDGE, SAND_CORNER, MOLTEN, GREY_MARBLE, BLUE_MARBEL, ORANGE_RUG, BLUE_ROOF;
	}

	public BasicType type;

	public int myI = -1;
	public int myJ = -1;
	public TileMap parentMap;
	// MultiSprited drawable;
	public Sprite tileSprite;
	public boolean isCollidable;
	float rotation = 0;

	public Tile(TileMap parentMap, BasicType type, int i, int j,
			TileCompressionData data) {
		this(parentMap, data.getType(), i, j, data.isBlended, data
				.getRotation());
	}

	public Tile(TileMap parentMap) {
		this.parentMap = parentMap;
	}

	public Tile(TileMap parentMap, BasicType type, int i, int j) {
		this(parentMap, type, i, j, false, 0);
	}

	public Tile(TileMap parentMap, BasicType type, int i, int j,
			boolean isBlending, float rotation) {
		myI = i;
		myJ = j;
		this.rotation = rotation;
		this.parentMap = parentMap;
		this.type = type;
		isCollidable = false;
		isBlended = isBlending;
		setUpSprite(isBlending);

	}

	protected void setUpSprite(boolean isBlending) {
		if (type == null)
			return;

		String name = tileImageMap[type.ordinal()];
		float oldRotation = 0;
		if (tileSprite != null) {
			oldRotation = tileSprite.getRotation();
		}

		if (CUtil.ResourcePool.isBlendedImage(name) && isBlending) {
			tileSprite = TileBlends.getTileBlend(this, parentMap, name);
		} else {
			if (name.equals("water3frames"))
				tileSprite = new SheetedSprite(
						CUtil.ResourcePool.getImageByName("water3frames"), 3,
						400);
			else
				tileSprite = new Sprite(CUtil.ResourcePool.getImageByName(name));
		}

		if (parentMap == null)
			return;

		if (!isBlending)
			tileSprite.setRotation(oldRotation + rotation);
		tileSprite.camera = parentMap.parentChunkMap.parentScreen.camera;
		anchor(myI, myJ);
		parentMap.drawableSpriteMapped.sprites[myI][myJ] = tileSprite;
	}

	public void anchor(int x, int y) {

		myI = x;
		myJ = y;
		// if (drawable != null)
		// {
		// drawable.mSprite.setPosition((x * TILE_SIZE) + parentMap.origin.x, (y
		// * TILE_SIZE) + parentMap.origin.y);
		// }
		if (tileSprite != null) {
			tileSprite.setPosition((x * TILE_SIZE) + parentMap.origin.x,
					(y * TILE_SIZE) + parentMap.origin.y);
		}

		parentMap.tiles[myI][myJ] = this;
	}

	public void onWalkOver() {

	}

	public Tile createTileFromOther(Tile oldTile, boolean isBlending) {
		return new Tile(oldTile.parentMap, this.type, oldTile.myI, oldTile.myJ,
				isBlending, 0);
	}

	public Tile createTileFromParams(TileMap parent, int i, int j,
			boolean isBlending) {
		return new Tile(parent, this.type, i, j, isBlending, 0);
	}

	public void reblend() {
		setUpSprite(isBlended);
	}

}
