package NateQuest.Tiling;

import java.awt.Point;
import java.io.IOException;
import java.security.KeyManagementException;
import java.util.ArrayList;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Editor.Editor;
import NateQuest.Editor.EditorController;
import NateQuest.Tiling.Tiles.Tile;

public class TilePainter extends Editor {

	ChunkMap selectedMap;
	TilePalette palette;
	PaintModeSelector modeSelector;
	Point start, end;
	boolean bIsDrawing;
	public boolean isBlendMode = false;
	ArrayList<Tile> collectedTiles;
	ArrayList<Tile> backupTypes;
	DrawableWithText blendStatusText;

	public TilePainter(GameScreen gs) {
		super(gs);

		selectedMap = gs.map;

		palette = new TilePalette(this);
		modeSelector = new PaintModeSelector(this);
		bIsDrawing = false;
		collectedTiles = new ArrayList<Tile>();
		backupTypes = new ArrayList<Tile>();
		blendStatusText = new DrawableWithText(this, null, LayerConstants.HUD,
				"Not blending");
		blendStatusText.setPositionRounded(600, 600);
		saveThisEntity = false;
	}

	public void update() {
		boolean hitPalette = false;
		palette.update();
		modeSelector.update();

		int x = MouseManager.iX;
		int y = MouseManager.iY;

		if (palette.getTileFromPoint(x, y) != null) {
			hitPalette = true;
		}
		if (!hitPalette && !modeSelector.hitSelector()
				&& !parentController.editorSelector.anyOptionHit()) {
			paint();
		}
		updateLayerSelection();
		updateBlendStatus();
	}

	public void updateBlendStatus() {
		if (KeyManager.isKeyHit(Input.KEY_B)) {
			isBlendMode = !isBlendMode;
			if (isBlendMode)
				blendStatusText.setText("Blend mode active");
			else
				blendStatusText.setText("not blending");
		}
	}

	public void setDrawableStatus(boolean isInvis) {
		modeSelector.setDrawableStatus(isInvis);
		palette.drawable.bIgnore = isInvis;
		blendStatusText.setDrawableStatus(isInvis);
	}

	private void updateLayerSelection() {
		if (KeyManager.isKeyHit(Input.KEY_1)) {
			selectedMap = parentScreen.map;
			Debugger.print("Layer 1 selected");
		} else if (KeyManager.isKeyHit(Input.KEY_2)) {
			if (parentScreen.lowerlay == null) {
				parentScreen.createLowerlay();
			}
			selectedMap = parentScreen.lowerlay;
			Debugger.print("Layer 2 selected");
		} else if (KeyManager.isKeyHit(Input.KEY_4)) {
			if (parentScreen.overlay == null) {
				parentScreen.createOverlay();
			}
			selectedMap = parentScreen.overlay;
			Debugger.print("Layer 3 selected");
		}
	}

	private void paint() {
		int x = MouseManager.getCameredX();
		int y = MouseManager.getCameredY();

		if (palette.selectedTile != null) {
			PaintModeButton.Type type = modeSelector.selectedButton.type;
			switch (type) {
			case PEN:
				if (MouseManager.getLeftClicked()) {
					Tile t = selectedMap.getTileFromPoint(x, y);
					if (t != null) {
						if (palette.selectedTile != null)
							palette.selectedTile.createTileFromOther(t,
									isBlendMode);
					}
					if (t == null) {
						if (selectedMap.isNullTile(x, y)) {
							selectedMap.createTileOnPoint(x, y,
									palette.selectedTile, isBlendMode);
						} else {
							Debugger.print("miss");
						}
					}
				}
				break;
			case BOX:
				drawBox(x, y);
				break;
			case CIRCLE:
				rotateSprite(x, y);
				break;
			case ERASE:
				if (MouseManager.getLeftClicked()) {
					selectedMap.deleteTileOnPoint(x, y);
				}
				break;
			}
		}
	}

	public void drawBox(int x, int y) {
		if (bIsDrawing) {
			if (MouseManager.getLeftClicked()) {
				end.x = x;
				end.y = y;

				int sX = Math.min(start.x, end.x);
				int eX = Math.max(start.x, end.x);
				int sY = Math.min(start.y, end.y);
				int eY = Math.max(start.y, end.y);

				for (int k = 0; k < collectedTiles.size(); k++) {
					// collectedTiles.get(k).morph(backupTypes.get(k));
					Tile t = collectedTiles.get(k);
					collectedTiles.get(k).createTileFromOther(
							backupTypes.get(k), backupTypes.get(k).isBlended);
				}
				collectedTiles.clear();
				backupTypes.clear();

				for (int i = sX; i <= eX; i += 2) {
					for (int j = sY; j <= eY; j += 2) {
						Tile t = selectedMap.getTileFromPoint(i, j);
						if (t != null && !collectedTiles.contains(t)) {
							collectedTiles.add(t);
							backupTypes.add(t);
						}
						if (j + 4 >= eY) {
							j = eY;
						}
					}
					if (i + 4 >= eX) {
						i = eX;
					}
				}
				for (Tile e : collectedTiles) {
					if (e.type != palette.selectedTile.type)
						palette.selectedTile
								.createTileFromOther(e, isBlendMode);
				}

			} else {
				for (Tile t : collectedTiles) {
					// t.parentMap.tiles[t.myI][t.myJ]
					// = Tile.createTileFromType(t.parentMap,
					// palette.selectedTile.type, t.myI, t.myJ);
				}
				collectedTiles.clear();
				backupTypes.clear();
				bIsDrawing = false;
			}
		} else {
			if (MouseManager.getLeftClicked()) {
				bIsDrawing = true;
				start = new Point(x, y);
				end = new Point(x, y);
			}
		}
	}

	private void rotateSprite(int x, int y) {
		if (MouseManager.getUpDownLeftMouseClick()) {
			Tile t = selectedMap.getTileFromPoint(x, y);
			if (t != null) {
				if (palette.selectedTile != null) {
					Sprite s = t.tileSprite;
					s.setRotation(s.getRotation() + 90);
				}
			}
			if (t == null) {
				Debugger.print("miss");
			}
		}
	}

	@Override
	public void setActive(boolean isActive) {
		super.setActive(isActive);

		modeSelector.setDrawableStatus(!isActive);
		palette.drawable.bIgnore = !isActive;
		blendStatusText.setDrawableStatus(!isActive);

	}

	public String toString() {
		return "Tile Painter";
	}

}
