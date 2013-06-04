package NateQuest.Drawables;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import GameClasses.Sprite;
import NateQuest.Entity;
import VisualFX.SimpleText;

public class DrawableWithText extends Drawable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1443882710601742964L;
	SimpleText text;

	public DrawableWithText(Entity parentEntity, Sprite sprite, int priority,
			String message) {
		super(parentEntity, sprite, priority);
		text = new SimpleText(message, 0, 0, false);
		text.camera = parentEntity.parentScreen.camera;
	}

	public void draw(Graphics g) {
		if (mSprite != null)
			super.draw(g);

		if (bIgnore)
			return;
		text.draw(g);
	}

	public void setPosition(int x, int y) {
		if (mSprite != null) {
			super.setPosition(x, y);
			text.setPositionRounded(x - (mSprite.getWidth() * 0.45f), y
					- (mSprite.getHeight() * 0.35f));
		} else {
			text.setPosition(x, y);
		}
	}

	public void setPositionRounded(float x, float y) {
		if (mSprite != null) {
			super.setPositionRounded(x, y);
			text.setPositionRounded(x - (mSprite.getWidth() * 0.25f), y
					- (mSprite.getHeight() * 0.35f));
		} else {
			text.setPositionRounded(x, y);
		}
	}

	public void setDrawableStatus(boolean isInvis) {
		bIgnore = isInvis;
	}

	public String getText() {
		return text.getText();
	}

	public void setText(String message) {
		text.setText(message);
	}

	public void setTextColor(Color c) {
		text.setColor(c);
	}

	public void setIsCamerad(boolean iscam) {
		super.setIsCamerad(iscam);
		text.bIsCamered = iscam;
	}
}
