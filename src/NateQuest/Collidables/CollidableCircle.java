package NateQuest.Collidables;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import Utility.MathUtility;

public class CollidableCircle extends Collidable {

	int radius;
	int radiusSquared;

	public CollidableCircle(Entity parentEntity, int radius) {
		super(parentEntity);
		this.radius = radius;
		radiusSquared = radius * radius;
	}

	@Override
	public void setPosition(Vector2f position) {
		this.boundingBox.setCenterX(position.x);
		this.boundingBox.setCenterY(position.y);
	}

	// Crafts a circle from the sprite and checks that
	public boolean isColliding(Sprite s) {
		float xsquare = (s.getX() - parentEntity.position.x);
		xsquare *= xsquare;
		float ysquare = (s.getY() - parentEntity.position.y);
		ysquare *= ysquare;

		float dist = xsquare + ysquare;
		if (dist < radiusSquared) {
			return true;
		}
		return false;
	}

	public boolean isColliding(Collidable c) {
		return c.isColliding(this);
	}

	public boolean isColliding(CollidableCircle c) {
		float distance = MathUtility.distance(boundingBox.getCenterX(),
				c.boundingBox.getCenterX(), boundingBox.getCenterY(),
				c.boundingBox.getCenterY());
		if (distance < radius + c.radius) {
			return true;
		}
		return false;
	}

	public boolean isColliding(Vector2f p) {
		float distance = MathUtility.distance(p.x, boundingBox.getCenterX(),
				p.y, boundingBox.getCenterY());
		if (distance < radius) {
			return true;
		}
		return false;
	}

	public String toString() {
		return "CircPos: ( " + boundingBox.getCenterX() + ", "
				+ boundingBox.getCenterY() + ") radius: " + radius;
	}

}
