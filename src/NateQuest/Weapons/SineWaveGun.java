package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.SoundManager;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.GoodSineWaveProjectile;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.EnemyProjectiles.SineWaveProjectile;
import NateQuest.VFX.Smoke;

public class SineWaveGun extends RangedWeapon {

	final float FIRE_RATE = 500;

	public SineWaveGun(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);
		setUpIcon("sinewavepickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "sinwaveeqp1";
		fireSound = SoundManager.heartgun;
	}

	public void fireWeapon() {
		super.fireWeapon();
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		new GoodSineWaveProjectile(parentScreen, pos, vel);
		new Smoke(parentScreen, pos);
	}

}
