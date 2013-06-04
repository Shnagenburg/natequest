package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.TrackingRocket;

public class RocketLauncher extends RangedWeapon {

	final float FIRE_RATE = 300;

	public RocketLauncher(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);

		setUpIcon("rocketlauncherpickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "rocketlauncher1";
		fireSound = SoundManager.rocketgun;
	}

	public void update() {
		super.update();
	}

	public void fireWeapon() {
		super.fireWeapon();

		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f target = crossheir.position;

		Vector2f vel = new Vector2f(pos.x - target.x, pos.y - target.y);
		vel = vel.normalise();
		vel = vel.negate();

		new TrackingRocket(parentScreen, pos, vel, crossheir);
	}

}
