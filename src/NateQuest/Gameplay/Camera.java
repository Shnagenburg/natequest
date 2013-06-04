package NateQuest.Gameplay;

import java.io.Serializable;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.text.ZoneView;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Tiling.ChunkMap;
import NateQuest.Tiling.TileMap;
import NateQuest.Tiling.Tiles.Tile;

public class Camera {

	final float NORMAL_PAN_SPEED = 0.010f;
	final float PAINTER_PAN_SPEED = 0.054f;
	public float PAN_SPEED;
	public float sensitivity = 1.0f;

	public int xRounded, xRoundedWithZoom;
	public int yRounded, yRoundedWithZoom;
	public float yOffset;
	public float xOffset;
	public float xBorder;
	public float yBorder;
	private float fZoom;
	public float xMax, xMin;
	public float yMax, yMin;
	public GameScreen parentScreen;
	public boolean bIgnoreMaxes = false;

	public Camera(GameScreen gs) {
		parentScreen = gs;
		xRounded = 0;
		yRounded = 0;
		yOffset = 0;
		xOffset = 0;
		fZoom = 1.0f;
		setNormalBounds();
	}

	public void setNormalBounds() {
		xBorder = 350;
		yBorder = 150;
		PAN_SPEED = NORMAL_PAN_SPEED;
	}

	public void setPainterBounds() {
		xBorder = 16;
		yBorder = 16;
		PAN_SPEED = PAINTER_PAN_SPEED;
	}

	public void jumpToPosition(int x, int y) {
		xOffset = x;
		xRounded = Math.round(x);
		xRoundedWithZoom = Math.round(xOffset * fZoom);

		yOffset = y;
		yRounded = Math.round(y);
		yRoundedWithZoom = Math.round(yOffset * fZoom);
	}

	public void shiftX(float xShift) {
		xOffset -= xShift * PAN_SPEED * sensitivity * CUtil.ElapsedTime;

		if (xOffset < -xMax && !bIgnoreMaxes) {
			// xOffset = -xMax;
			xOffset += Math.abs(xShift) * PAN_SPEED * sensitivity
					* CUtil.ElapsedTime * 4;
			if (xOffset > -xMax)
				xOffset = -xMax;

		} else if (xOffset > xMin && !bIgnoreMaxes) {
			// xOffset = xMin;
			xOffset += -Math.abs(xShift) * PAN_SPEED * sensitivity
					* CUtil.ElapsedTime * 4;
			if (xOffset < xMin)
				xOffset = xMin;
		}
		xRounded = Math.round(xOffset);
		xRoundedWithZoom = Math.round(xOffset * fZoom);
	}

	public void shiftY(float yShift) {
		yOffset -= yShift * PAN_SPEED * sensitivity * CUtil.ElapsedTime;
		if (yOffset < -yMax && !bIgnoreMaxes) {
			// yOffset = -yMax;
			yOffset += Math.abs(yShift) * PAN_SPEED * sensitivity
					* CUtil.ElapsedTime * 4;
			if (yOffset > -yMax)
				yOffset = -yMax;
		} else if (yOffset > yMin && !bIgnoreMaxes) {
			// yOffset = yMin;
			yOffset += -Math.abs(yShift) * PAN_SPEED * sensitivity
					* CUtil.ElapsedTime * 4;
			if (yOffset < yMin)
				yOffset = yMin;
		}
		yRounded = Math.round(yOffset);
		yRoundedWithZoom = Math.round(yOffset * fZoom);

	}

	public void update(Sprite heroSprite) {
		if ((heroSprite.getX() * fZoom) + xRoundedWithZoom < xBorder) {
			shiftX((heroSprite.getX() * fZoom) + xRoundedWithZoom - xBorder);
		} else if ((heroSprite.getX() * fZoom) + xRoundedWithZoom > CUtil.Dimensions.width
				- xBorder) {
			shiftX(((heroSprite.getX() * fZoom) + xRoundedWithZoom)
					- (CUtil.Dimensions.width - xBorder));
		} else {
			xMaxChecks();
		}

		if ((heroSprite.getY() * fZoom) + yRoundedWithZoom < yBorder) {
			shiftY(((heroSprite.getY() * fZoom) + yRoundedWithZoom) - yBorder);
		} else if ((heroSprite.getY() * fZoom) + yRoundedWithZoom > CUtil.Dimensions.height
				- yBorder) {
			shiftY(((heroSprite.getY() * fZoom) + yRoundedWithZoom)
					- (CUtil.Dimensions.height - yBorder));
		} else {
			yMaxChecks();
		}

		if (KeyManager.isKeyHit(Input.KEY_3))
			Debugger.print("y offset" + yOffset);
	}

	// returns true if the camera is on the point
	public boolean updatePanToPoint(float x, float y) {
		int specialXBorder = (CUtil.Dimensions.width / 2) - 10;
		int specialYBorder = (CUtil.Dimensions.height / 2) - 10;
		boolean noX = false;
		boolean noY = false;

		if ((x * fZoom) + xRoundedWithZoom < specialXBorder) {
			shiftX((x * fZoom) + xRoundedWithZoom - specialXBorder);
		} else if ((x * fZoom) + xRoundedWithZoom > CUtil.Dimensions.width
				- specialXBorder) {
			shiftX(((x * fZoom) + xRoundedWithZoom)
					- (CUtil.Dimensions.width - specialXBorder));
		} else {
			noX = true;
		}

		if ((y * fZoom) + yRoundedWithZoom < specialYBorder) {
			shiftY(((y * fZoom) + yRoundedWithZoom) - specialYBorder);
		} else if ((y * fZoom) + yRoundedWithZoom > CUtil.Dimensions.height
				- specialYBorder) {
			shiftY(((y * fZoom) + yRoundedWithZoom)
					- (CUtil.Dimensions.height - specialYBorder));
		} else {
			noY = true;
		}

		return noX && noY;
	}

	/*
	 * public void shiftXToPoint(float xShift) { xOffset -= xShift * PAN_SPEED *
	 * sensitivity * CUtil.ElapsedTime; xRounded = Math.round(xOffset);
	 * xRoundedWithZoom = Math.round( xOffset * fZoom ); } public void
	 * shiftYToPoint(float yShift) { yOffset -= yShift * PAN_SPEED * sensitivity
	 * * CUtil.ElapsedTime; yRounded = Math.round(yOffset); yRoundedWithZoom =
	 * Math.round( yOffset * fZoom ); }
	 */

	private void xMaxChecks() {
		if (xOffset < -xMax && !bIgnoreMaxes) {
			// xOffset = -xMax;
			xOffset += PAN_SPEED * sensitivity * CUtil.ElapsedTime * 40;
			if (xOffset > -xMax)
				xOffset = -xMax;

		} else if (xOffset > xMin && !bIgnoreMaxes) {
			// xOffset = xMin;
			xOffset -= PAN_SPEED * sensitivity * CUtil.ElapsedTime * 40;
			if (xOffset < xMin)
				xOffset = xMin;
		}
		xRounded = Math.round(xOffset);
		xRoundedWithZoom = Math.round(xOffset * fZoom);
	}

	private void yMaxChecks() {
		if (yOffset < -yMax && !bIgnoreMaxes) {
			// yOffset = -yMax;
			yOffset += PAN_SPEED * sensitivity * CUtil.ElapsedTime * 40;
			if (yOffset > yMax)
				yOffset = yMax;
		} else if (yOffset > yMin && !bIgnoreMaxes) {
			// yOffset = yMin;
			yOffset -= PAN_SPEED * sensitivity * CUtil.ElapsedTime * 40;
			if (yOffset < yMin)
				yOffset = yMin;
		}
		yRounded = Math.round(yOffset);
		yRoundedWithZoom = Math.round(yOffset * fZoom);
	}

	public void setMaxes(ChunkMap map) {
		xMax = (map.getChunks().length * TileMap.CHUNK_WIDTH * Tile.TILE_SIZE)
				- (Tile.TILE_SIZE / 2);
		xMax -= CUtil.Dimensions.width;
		// if (xMax < CUtil.Dimensions.width / 4)
		if (map.getChunks().length == 1) {
			// Debugger.print("buffed cam " + " max: " + xMax + " width: " +
			// CUtil.Dimensions.width);
			xMax += TileMap.CHUNK_WIDTH * Tile.TILE_SIZE * 0.25f;
			xMin += TileMap.CHUNK_WIDTH * Tile.TILE_SIZE * 0.25f;
		}
		yMax = (map.getChunks()[0].length * TileMap.CHUNK_HEIGHT * Tile.TILE_SIZE)
				- (Tile.TILE_SIZE / 2);
		yMax -= CUtil.Dimensions.height;
		// if (yMax < CUtil.Dimensions.height)
		if (map.getChunks()[0].length == 1) {
			// Debugger.print("buffed cam " + " max: " + xMax + " width: " +
			// CUtil.Dimensions.width);
			yMax += TileMap.CHUNK_HEIGHT * Tile.TILE_SIZE * 0.25f;
			yMin += TileMap.CHUNK_HEIGHT * Tile.TILE_SIZE * 0.25f;
		}
		yMin += Tile.TILE_SIZE / 2;
		xMin += Tile.TILE_SIZE / 2;

	}

	public void setZoom(float newZoom) {
		this.fZoom = newZoom;

	}

	public float getZoom() {
		return fZoom;
	}

}
