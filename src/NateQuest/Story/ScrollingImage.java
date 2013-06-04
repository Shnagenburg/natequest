package NateQuest.Story;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.Fader;
import NateQuest.Drawables.LayerConstants;
import Utility.MathUtility;

public class ScrollingImage extends Entity {

	float scrollSpeed = 0.1f;
	Vector2f destination = new Vector2f();
	Vector2f velocity = new Vector2f();
	Fader fader;

	public ScrollingImage(GameScreen gs) {
		super(gs);
	}

	public void setImage(String imageName) {
		this.setImage(imageName, 999);
	}

	public void setImage(String imageName, int layer) {
		drawable = new Drawable(this, new Sprite(imageName), layer);
		this.setPosition(position.x, position.y);
	}

	public void update() {
		if (!withinDistanceOfOne(position.x, destination.x)) {
			position.x += CUtil.ElapsedTime * velocity.x * scrollSpeed;
		}
		if (!withinDistanceOfOne(position.y, destination.y)) {
			position.y += CUtil.ElapsedTime * velocity.y * scrollSpeed;
		}
		setPosition(position.x, position.y);
		checkAndCorrectTooFar();
		handleFader();
	}

	public void fadeIn(float duration) {
		fader = new Fader(drawable.mSprite);
		fader.fadeIn(duration);
	}

	public boolean isAtDestination() {
		if (withinDistanceOfOne(position.x, destination.x)
				&& withinDistanceOfOne(position.y, destination.y)) {
			return true;
		}
		return false;
	}

	public void setDestination(float x, float y) {
		destination = new Vector2f(x, y);
		velocity = CUtil.getNormaledVector(position, destination);
	}

	public void setPositionAndDestination(float x, float y) {
		setDestination(x, y);
		setPosition(x, y);
	}

	public void jumpToDestination() {
		setPosition(destination.x, destination.y);
	}

	private boolean withinDistanceOfOne(float src, float trg) {
		if (Math.abs(src - trg) < 1) {
			return true;
		}
		return false;
	}

	private void checkAndCorrectTooFar() {
		if (checkTooFar()) {
			setPosition(destination.x, destination.y);
		}
	}

	private boolean checkTooFar() {
		if (velocity.x > 0 && position.x > destination.x) {
			return true;
		} else if (velocity.x < 0 && position.x < destination.x) {
			return true;
		} else if (velocity.y > 0 && position.y > destination.y) {
			return true;
		} else if (velocity.y < 0 && position.y < destination.y) {
			return true;
		} else {
			return false;
		}
	}

	private void handleFader() {
		if (fader != null) {
			fader.update();
		}
	}

}
