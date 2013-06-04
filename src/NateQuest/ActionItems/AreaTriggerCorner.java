package NateQuest.ActionItems;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;

public class AreaTriggerCorner extends Entity {

	AreaTrigger parentArea;

	public AreaTriggerCorner(AreaTrigger parent) {
		parentArea = parent;
		drawable = new Drawable(this, new Sprite("squareicon"),
				LayerConstants.HUD);
		collidable = new CollidableCircle(this, drawable.mSprite.getWidth());
		saveThisEntity = false;
		collidable.mask = CollidableMasks.EDITOR;
	}

	// Changes the parent's area
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		drawable.setPositionRounded(x, y);
		collidable.setPosition(x, y);
		parentArea.anchorTrigger();
	}

	// When dragging the parent around
	public void shiftViaParent(float x, float y) {
		position.x = x;
		position.y = y;
		drawable.setPositionRounded(x, y);
		collidable.setPosition(x, y);
	}

	public float getXOffset() {
		return parentArea.position.x - position.x;
	}

	public float getYOffset() {
		return parentArea.position.y - position.y;
	}
}
