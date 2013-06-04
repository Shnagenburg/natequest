package NateQuest.Drawables;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.text.StyleContext.SmallAttributeSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import GameClasses.Sprite;
import NateQuest.Entity;

public class MultiSprited extends Drawable implements Serializable {

	ArrayList<Sprite> mSprites;

	public MultiSprited(Entity parentEntity, Sprite sprite, int priority) {
		super(parentEntity, sprite, priority);

	}

	public void draw(Graphics g) {
		super.draw(g);

		if (bIgnore)
			return;
		if (mSprites != null)
			for (int i = 0; i < mSprites.size(); i++)
				mSprites.get(i).draw(g);

	}

	public void update() {
		mSprite.update();
		for (Sprite s : mSprites) {
			s.update();
		}
	}

	public void addSprite(Sprite s) {
		if (mSprites == null) {
			mSprites = new ArrayList<Sprite>();
		}
		s.camera = parentEntity.parentScreen.camera;
		mSprites.add(s);
	}

	public void setPosition(int x, int y) {
		mSprite.setPosition(x, y);
		if (mSprites == null)
			return;
		for (Sprite s : mSprites) {
			s.setPosition(x, y);
		}
	}

	public void setPositionRounded(float x, float y) {
		mSprite.setPositionRounded(x, y);
		for (Sprite s : mSprites) {
			s.setPositionRounded(x, y);
		}
	}

	public void setRotation(float angle) {
		mSprite.setRotation(angle);
		for (Sprite s : mSprites) {
			s.setRotation(angle);
		}
	}

	public void resetCamera() {
		mSprite.camera = parentEntity.parentScreen.camera;
		for (Sprite s : mSprites) {
			s.camera = parentEntity.parentScreen.camera;
		}
	}

	@Override
	public void flashSprite(Color color, float duration) {
		super.flashSprite(color, duration);
		for (Sprite s : mSprites) {
			s.flashColor(color, duration);
		}
	}

	public void swapSprite(int index, String newSpriteName) {
		if (mSprites != null && mSprites.size() > index) {
			Sprite s = new Sprite(newSpriteName);
			s.camera = parentEntity.parentScreen.camera;
			mSprites.set(index, s);
		}
	}

}
