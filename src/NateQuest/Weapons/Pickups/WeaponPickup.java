package NateQuest.Weapons.Pickups;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.Weapons.Armaments;
import NateQuest.Weapons.RangedWeapon;

public class WeaponPickup extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7355977520936658916L;
	Hero hero;
	RangedWeapon weapon;

	public WeaponPickup() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WeaponPickup(GameScreen gs) {
		super(gs);
	}

	public void setup() {
		collidable = new Collidable(this);
		collidable.matchSprite(drawable.mSprite);
		collidable.mask = CollidableMasks.EVENT;
	}

	public void update() {
		super.update();
		grabHero();
		if (hero.collidable.isColliding(collidable)) {
			pickupWeapon();
		}
	}

	public void pickupWeapon() {
		Armaments arms = (Armaments) parentScreen.findEntity(Armaments.class);
		arms.addWeapon(weapon);
		this.markForDeletion();
	}

	public void grabHero() {
		if (hero == null) {
			hero = (Hero) parentScreen.findEntity(Hero.class);
		}
	}
}
