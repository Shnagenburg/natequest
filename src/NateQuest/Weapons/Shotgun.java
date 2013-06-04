package NateQuest.Weapons;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.SoundManager;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.ShotgunPellet;
import NateQuest.VFX.Smoke;

public class Shotgun extends RangedWeapon {

	final float FIRE_RATE = 800;

	public Shotgun(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);
		setUpIcon("shotgunpickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "shotguneqp1";
		fireSound = SoundManager.rocketgun;
	}

	public void fireWeapon() {
		super.fireWeapon();
		Random rand = new Random();
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();
		double base = vel.getTheta();
		Vector2f veltwo = new Vector2f(base + (rand.nextInt(120) - 60));
		Vector2f velthree = new Vector2f(base + (rand.nextInt(120) - 60));
		Vector2f velfour = new Vector2f(base + (rand.nextInt(120) - 60));
		Vector2f velfive = new Vector2f(base + (rand.nextInt(120) - 60));

		new ShotgunPellet(parentScreen, pos, vel);
		new ShotgunPellet(parentScreen, pos, veltwo);
		new ShotgunPellet(parentScreen, pos, velthree);
		new ShotgunPellet(parentScreen, pos, velfour);
		new ShotgunPellet(parentScreen, pos, velfive);
		new Smoke(parentScreen, pos);
	}

}
