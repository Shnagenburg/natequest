package NateQuest.Weapons;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.StarProjectile;

public class Armaments extends Entity {

	ArrayList<RangedWeapon> rangedWeapons = new ArrayList<RangedWeapon>();
	RangedWeapon currentWeapon;
	Hero parentHero;

	public Armaments(GameScreen gs) {
		super(gs);
		// rangedWeapons.add( new Gun(gs, (Hero) gs.findEntity(Hero.class)));
		// rangedWeapons.add( new RocketLauncher(gs, parent));
		// rangedWeapons.add( new Shotgun(gs, parent));
		// rangedWeapons.add( new StarRifle(gs, parent));
		// rangedWeapons.add( new Hookshot(gs, parent));
		// rangedWeapons.add( new MagnetGun(gs, parent));
		// rangedWeapons.add( new SineWaveGun(gs, parent));
		// currentWeapon = rangedWeapons.get(0);

		drawable = new Drawable(this, new Sprite("noimage"), LayerConstants.HUD);
		saveThisEntity = false;
	}

	private void getHero() {
		if (parentHero == null) {
			parentHero = (Hero) parentScreen.findEntity(Hero.class);
			for (RangedWeapon r : rangedWeapons) {
				r.parentHero = parentHero;
			}
		}
	}

	public void update() {
		getHero();
		if (currentWeapon != null) {
			currentWeapon.update();
			weaponSwap();
		}
	}

	public void weaponSwap() {
		int i = -1;
		if (KeyManager.isKeyHit(Input.KEY_1))
			i = 0;
		else if (KeyManager.isKeyHit(Input.KEY_2))
			i = 1;
		else if (KeyManager.isKeyHit(Input.KEY_3))
			i = 2;
		else if (KeyManager.isKeyHit(Input.KEY_4))
			i = 3;
		else if (KeyManager.isKeyHit(Input.KEY_5))
			i = 4;
		else if (KeyManager.isKeyHit(Input.KEY_6))
			i = 5;
		if (i != -1) {
			if (rangedWeapons.size() > i) {
				weaponSwap(i);
			}
		}
	}

	@Override
	public void attach(GameScreen parentScreen) {
		super.attach(parentScreen);
		for (RangedWeapon r : rangedWeapons) {
			r.parentScreen = parentScreen;
		}
	}

	public void addWeapon(RangedWeapon weapon) {
		rangedWeapons.add(weapon);
		weapon.parentHero = parentHero;
		currentWeapon = rangedWeapons.get(rangedWeapons.size() - 1);
		drawable.mSprite = currentWeapon.iconSprite;
		setUpHerosDrawable();
	}

	public void resetSprites() {
		getHero();
		if (rangedWeapons.size() > 0) {
			int index = rangedWeapons.indexOf(currentWeapon);
			if (index >= 0) {
				weaponSwap(index);
			}
		}
	}

	private void weaponSwap(int index) {
		currentWeapon = rangedWeapons.get(index);
		drawable.mSprite = currentWeapon.iconSprite;
		setUpHerosDrawable();
	}

	private void setUpHerosDrawable() {
		MultiSprited ms = (MultiSprited) parentHero.drawable;
		ms.swapSprite(0, currentWeapon.equippedSpriteName);
		parentHero.setPosition(parentHero.position.x, parentHero.position.y);
	}

	public void copyStatusFromOtherArms(Armaments arms) {
		int curIndex = arms.rangedWeapons.indexOf(arms.currentWeapon);
		for (RangedWeapon weapon : arms.rangedWeapons) {
			if (!alreadyHasGun(weapon.getClass())) {
				weaponClassToNewWeapon(weapon.getClass());
			}
		}
		Debugger.print("curr index: " + curIndex);
		for (int i = 0; i < rangedWeapons.size(); i++) {
			Debugger.peek(rangedWeapons.get(i));
		}
		weaponSwap(curIndex);
	}

	private void weaponClassToNewWeapon(Class c) {
		if (c == Gun.class) {
			rangedWeapons.add(new Gun(parentScreen, (Hero) parentScreen
					.findEntity(Hero.class)));
		}
		if (c == RocketLauncher.class) {
			rangedWeapons.add(new RocketLauncher(parentScreen,
					(Hero) parentScreen.findEntity(Hero.class)));
		}
		if (c == SineWaveGun.class) {
			rangedWeapons.add(new SineWaveGun(parentScreen, (Hero) parentScreen
					.findEntity(Hero.class)));
		}
		if (c == StarRifle.class) {
			rangedWeapons.add(new StarRifle(parentScreen, (Hero) parentScreen
					.findEntity(Hero.class)));
		}
		if (c == Shotgun.class) {
			rangedWeapons.add(new Shotgun(parentScreen, (Hero) parentScreen
					.findEntity(Hero.class)));
		}
		if (c == Hookshot.class) {
			rangedWeapons.add(new Hookshot(parentScreen, (Hero) parentScreen
					.findEntity(Hero.class)));
		}
	}

	private boolean alreadyHasGun(Class c) {
		for (RangedWeapon weapon : rangedWeapons) {
			if (weapon.getClass() == c) {
				return true;
			}
		}
		return false;
	}
}
