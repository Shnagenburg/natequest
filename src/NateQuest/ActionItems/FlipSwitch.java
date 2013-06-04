package NateQuest.ActionItems;

import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.Layer;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.SheetedSprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;

public class FlipSwitch extends Actionable {

	final String[] OPS = { "flipOn", "flipOff" };

	enum State {
		ON, OFF
	}

	Hero hero;
	SheetedSprite sprite;
	State currentState;

	public FlipSwitch() {
		this(CUtil.ScreenGettingLoaded);
	}

	public FlipSwitch(GameScreen gs) {
		super(gs);
		addOps(OPS);

		currentState = State.OFF;
		sprite = new SheetedSprite(CUtil.QuickImage("lever"), 2, 300);
		sprite.lock(0);
		drawable = new Drawable(this, sprite, LayerConstants.SUB_CHARACTERS);
		collidable = new Collidable(this);
		collidable.mask = CollidableMasks.EVENT;
		collidable.matchSprite(drawable.mSprite);

	}

	public void setPositionRounded(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
		drawable.setPositionRounded(position.x, position.y);
		collidable.matchSprite(drawable.mSprite);
	}

	public void update() {
		if (hero == null)
			hero = (Hero) parentScreen.findEntity(Hero.class);

		if (KeyManager.isKeyHit(Input.KEY_E)
				&& collidable.isColliding(hero.collidable)) {
			playerAction();
		}
	}

	public void playerAction() {
		if (currentState == State.OFF) {
			currentState = State.ON;
			sprite.lock(1);
			playActions(0);
		} else if (currentState == State.ON) {
			currentState = State.OFF;
			sprite.lock(0);
			playActions(1);
		}
	}

	@Override
	public void action(int actionID, Entity caller) {
		if (actionID == 0) {
			sprite.lock(1);
			playActions(0);
		} else if (actionID == 1) {
			sprite.lock(0);
			playActions(1);
		}
	}

}
