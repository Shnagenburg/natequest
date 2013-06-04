package NateQuest.Tiling;

import java.io.Serializable;
import java.util.ArrayList;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Tiling.PaintModeButton.Type;

public class PaintModeSelector implements Serializable {

	ArrayList<PaintModeButton> buttons;
	PaintModeButton selectedButton = null;
	TilePainter parent;

	public PaintModeSelector(TilePainter parent) {
		this.parent = parent;
		buttons = new ArrayList<PaintModeButton>();
		buttons.add(new PaintModeButton(Type.PEN, new Sprite(CUtil.ResourcePool
				.getImageByName("penicon")), this));
		buttons.add(new PaintModeButton(Type.BOX, new Sprite(CUtil.ResourcePool
				.getImageByName("squareicon")), this));
		buttons.add(new PaintModeButton(Type.CIRCLE, new Sprite(
				CUtil.ResourcePool.getImageByName("circleicon")), this));
		buttons.add(new PaintModeButton(Type.ERASE, new Sprite(
				CUtil.ResourcePool.getImageByName("eraseicon")), this));
		anchor();
		selectedButton = buttons.get(0);
	}

	public void update() {
		if (MouseManager.getLeftClicked()) {
			int x = MouseManager.iX;
			int y = MouseManager.iY;

			for (PaintModeButton b : buttons) {
				if (b.drawable.mSprite.isInsideSprite(x, y)) {
					selectedButton = b;
					anchor();
					selectedButton.drawable.mSprite.setRotation(45);
				}
			}
		}
	}

	public void setDrawableStatus(boolean isInvis) {
		for (PaintModeButton b : buttons) {
			b.drawable.bIgnore = isInvis;
		}
	}

	private void anchor() {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).drawable.mSprite.setPosition(i * 32 + 150, 32);
			buttons.get(i).drawable.mSprite.setRotation(0);
		}
	}

	public boolean hitSelector() {
		int x = MouseManager.iX;
		int y = MouseManager.iY;

		for (PaintModeButton b : buttons) {
			if (b.drawable.mSprite.isInsideSprite(x, y)) {
				return true;
			}
		}
		return false;
	}
}
