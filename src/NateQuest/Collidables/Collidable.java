package NateQuest.Collidables;

import java.io.Serializable;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.Property;

public class Collidable extends Property implements Serializable {

	public int mask = -1;
	public Rectangle boundingBox;

	public enum Section {
		BOTTOM_HALF
	}

	public Collidable(Entity parentEntity) {
		super(parentEntity);
		parentEntity.parentScreen.collidables.add(parentEntity);
		boundingBox = new Rectangle(0, 0, 0, 0);
	}

	public void setPosition(Vector2f pos) {
		boundingBox.setCenterX(pos.x);
		boundingBox.setCenterY(pos.y);
	}

	public void setPosition(float x, float y) {
		boundingBox.setCenterX(x);
		boundingBox.setCenterY(y);
	}

	public boolean isColliding(CollidableCircle c) {
		float xsquare = (c.parentEntity.position.x - boundingBox.getCenterX());
		xsquare *= xsquare;
		float ysquare = (c.parentEntity.position.y - boundingBox.getCenterY());
		ysquare *= ysquare;

		float dist = xsquare + ysquare;
		if (dist < c.radiusSquared) {
			return true;
		}
		return false;
	}

	public boolean isColliding(Collidable c) {
		return c.boundingBox.intersects(boundingBox);
	}

	public boolean isColliding(Sprite s) {
		if (boundingBox.getCenterX() - (boundingBox.getWidth() / 2) < s.getX()
				+ (s.getScale() * s.getWidth() / 2)
				&& boundingBox.getCenterX() + (boundingBox.getWidth() / 2) > s
						.getX() - (s.getScale() * s.getWidth() / 2)
				&&

				boundingBox.getCenterY() - (boundingBox.getHeight() / 2) < s
						.getY() + (s.getScale() * s.getHeight() / 2)
				&& boundingBox.getCenterY() + (boundingBox.getHeight() / 2) > s
						.getY() - (s.getScale() * s.getHeight() / 2)) {
			return true;
		}
		return false;
	}

	public boolean isColliding(Vector2f p) {
		if (boundingBox.contains(p.x, p.y)) {
			return true;
		}
		return false;
	}

	public boolean isColliding(int x, int y) {
		if (boundingBox.contains(x, y)) {
			return true;
		}
		return false;
	}

	public void removeProperty() {
		parentEntity.parentScreen.collidables.remove(parentEntity);
		super.removeProperty();
		// Debugger.print("asdasd");
	}

	public void matchSprite(Sprite s) {
		boundingBox.setWidth(s.getWidth() * s.getScale());
		boundingBox.setHeight(s.getHeight() * s.getScale());
		boundingBox.setCenterX(s.getX());
		boundingBox.setCenterY(s.getY());
	}

	public void matchSprite(Sprite s, float scale) {
		boundingBox.setWidth(s.getWidth() * s.getScale() * scale);
		boundingBox.setHeight(s.getHeight() * s.getScale() * scale);
		boundingBox.setCenterX(s.getX());
		boundingBox.setCenterY(s.getY());
	}

	public void matchSprite(Sprite s, Section sec) {
		switch (sec) {
		case BOTTOM_HALF:
			boundingBox.setWidth(s.getWidth() * s.getScale());
			boundingBox.setHeight((s.getHeight() * s.getScale()) / 2);
			boundingBox.setCenterX(s.getX());
			boundingBox.setCenterY(s.getY() + (s.getScaledHeight() / 4));
			break;
		}
	}

	public void matchRect(int x, int y, int width, int height) {
		boundingBox.setCenterX(x);
		boundingBox.setCenterY(y);
		boundingBox.setWidth(width);
		boundingBox.setHeight(height);
	}

	public String toString() {
		return "BoxPos: ( " + boundingBox.getCenterX() + ", "
				+ boundingBox.getCenterY() + ") w: " + boundingBox.getWidth()
				+ ", h: " + boundingBox.getHeight();
	}

}
