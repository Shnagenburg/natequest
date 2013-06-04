package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.SoundManager;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.MagnetProjectile;
import NateQuest.Projectiles.StarMagnetProjectile;
import NateQuest.Projectiles.StarProjectile;
import NateQuest.Projectiles.TrackingRocket;
import NateQuest.VFX.Smoke;

public class StarRifle extends RangedWeapon {

	final float FIRE_RATE = 150;
	StarMagnetProjectile lead;

	public StarRifle(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);
		setUpIcon("magnetpickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "magnetguneqp1";
		fireSound = SoundManager.heartgun;
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

		new StarProjectile(parentScreen, pos, vel, lead);
	}

	public void fireWeaponSecondary() {
		super.fireWeaponSecondary();

		SoundManager.playSound(SoundManager.hookshotgun, 0.8f, 1.0f);

		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		if (lead != null)
			lead.markForDeletion();
		lead = new StarMagnetProjectile(parentScreen, pos, vel);
		new Smoke(parentScreen, pos);
	}

}
