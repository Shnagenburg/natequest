package NateQuest;

import java.io.IOException;
import java.io.Serializable;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Collidables.Collidable;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.SpriteMapped;

public class Entity {
	public int id = -1;
	public Collidable collidable;
	public Drawable drawable;
	public Vector2f position = new Vector2f();

	public GameScreen parentScreen;

	protected boolean bIsMarkedForDeletion;
	protected boolean bDeletionImmunity;

	// If this is false, the entity will be wiped and wont be saved
	public boolean saveThisEntity;
	public boolean bIsSleeping = false;

	public Entity() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Entity(GameScreen gs) {
		parentScreen = gs;

		bIsMarkedForDeletion = false;
		bDeletionImmunity = false;

		saveThisEntity = true;
		if (gs != null) {
			gs.entities.add(this);
			setID();
		}
	}

	private void setID() {
		id = 0;
		while (levelContainsId(id)) {
			id++;
		}
	}

	private boolean levelContainsId(int id) {
		this.id = id;
		for (Entity e : parentScreen.entities) {
			if (e.getName().equals(this.getName()) && e != this)
				return true;
		}
		return false;
	}

	public void update() {

	}

	public boolean getIsMarkedForDeletion() {
		if (bDeletionImmunity)
			return false;

		return bIsMarkedForDeletion;
	}

	public void markForDeletion() {
		parentScreen.stuffToDelete.add(this);
		bIsMarkedForDeletion = true;
	}

	public void setIsMarkedForDeletion(boolean value) {
		bIsMarkedForDeletion = value;
	}

	public boolean getDeletionImmunity() {
		return bDeletionImmunity;
	}

	public void setDeletionImmunity(boolean value) {
		bDeletionImmunity = value;
	}

	public void removeEntity() {
		if (drawable != null)
			drawable.removeProperty();

		if (collidable != null)
			collidable.removeProperty();

		parentScreen.entities.remove(this);
	}

	public void attach(GameScreen parentScreen) {
		this.parentScreen = parentScreen;
		parentScreen.entities.add(this);
		if (drawable != null && !parentScreen.drawables.contains(drawable)) {
			drawable.addThisToDrawables();
		}
		if (collidable != null
				&& !parentScreen.collidables.contains(collidable)) {
			parentScreen.collidables.add(this);
		}
	}

	public String toString() {
		return this.getClass().getSimpleName() + "_" + id;
	}

	public String getName() {
		return this.getClass().getSimpleName() + "_" + id;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		if (drawable != null)
			drawable.setPositionRounded(x, y);
		if (collidable != null) {
			collidable.setPosition(x, y);
		}
	}
}
