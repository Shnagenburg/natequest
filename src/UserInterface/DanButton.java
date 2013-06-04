package UserInterface;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.MouseManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.Collidables.Collidable;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;

public class DanButton extends Entity {

	public boolean isHit = false;
	boolean isPressed = false;
	boolean isPrimed = false;
	DrawableWithText dwt;
	Sprite spriteNorm, spriteHover, spriteDown;

	public DanButton(GameScreen gs, Vector2f pos, int width, int height,
			String message) {
		super(gs);
		position = pos;
		spriteNorm = new Sprite(ImageCreator.createButton(width, height, 1));
		spriteNorm.bIsCameraed = false;
		spriteNorm.setPositionRounded(position.x, position.y);
		spriteHover = new Sprite(ImageCreator.createButton(width, height, 2));
		spriteHover.bIsCameraed = false;
		spriteHover.setPositionRounded(position.x, position.y);
		spriteDown = new Sprite(ImageCreator.createButton(width, height, 3));
		spriteDown.bIsCameraed = false;
		spriteDown.setPositionRounded(position.x, position.y);
		dwt = new DrawableWithText(this, spriteNorm, LayerConstants.HUD,
				message);
		dwt.setPositionRounded(position.x, position.y);

		drawable = dwt;
		collidable = new Collidable(this);
		collidable.matchSprite(spriteNorm);
		saveThisEntity = false;

	}

	public void setPosition(int x, int y) {
		spriteDown.setPosition(x, y);
		spriteHover.setPosition(x, y);
		spriteNorm.setPosition(x, y);
		dwt.setPosition(x, y);
		collidable.matchSprite(spriteNorm);
	}

	public String getMessage() {
		return dwt.getText();
	}

	public void setMessage(String s) {
		dwt.setText(s);
	}

	public void update() {
		int mX = MouseManager.iX;
		int mY = MouseManager.iY;
		if (collidable.isColliding(mX, mY) && !MouseManager.getLeftClicked()) {
			drawable.mSprite = spriteHover;
			isPrimed = true;
		} else if (collidable.isColliding(mX, mY)
				&& MouseManager.getLeftClicked() && isPrimed) {
			drawable.mSprite = spriteDown;
			isPressed = true;
		} else {
			isPrimed = false;
			isPressed = false;
			drawable.mSprite = spriteNorm;
		}

		if (collidable.isColliding(mX, mY)
				&& MouseManager.getDownUpLeftMouseClick() && isPressed) {
			isHit = true;
		} else {
			isHit = false;
		}
	}

}
