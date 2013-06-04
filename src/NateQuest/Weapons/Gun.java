package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.TrackingRocket;
import NateQuest.VFX.Smoke;
import NateQuest.VFX.VisualFX;

public class Gun extends RangedWeapon {

	final float FIRE_RATE = 200;

	public Gun(GameScreen gs, Hero parent) {
		super(gs, parent);

		setUpIcon("lasergunpickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "heartgun1";
		fireSound = SoundManager.heartgun;
	}

	public void update() {
		super.update();
	}

	public void fireWeapon() {
		super.fireWeapon();
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		new HeartProjectile(parentScreen, pos, vel);
		new Smoke(parentScreen, pos);
	}

}
