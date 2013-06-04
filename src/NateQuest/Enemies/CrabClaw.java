package NateQuest.Enemies;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.CrabShot;
import NateQuest.Projectiles.Projectile;
import NateQuest.VFX.DamageNumber;
import Utility.MathUtility;
import Utility.UtilityOscillator;
import Utility.UtilityTimer;

public class CrabClaw extends CrabArmature {
	/**
	 * 
	 */
	private static final long serialVersionUID = 209718185286333389L;
	int shotsPerVolley = 3;
	float shotSpread = 30;
	float shotOffset = -30;
	int health = 5;
	CollidableCircle collidable;

	public CrabClaw(GiantCrab parentCrab, Vector2f offset, String imagename) {
		super(parentCrab, offset, imagename);
		setTimeAndOscillator();
		int radius = Math.max(drawable.mSprite.getWidth(),
				drawable.mSprite.getHeight());
		collidable = new CollidableCircle(parentCrab, (int) (radius * 0.7f));
		collidable.removeProperty();
	}

	public void setTimeAndOscillator() {
		oscillator = new UtilityOscillator(-30, 30, 1000);
		// oscillator.setIsAbsoluteValued(true);
		timer = new UtilityTimer(400);
	}

	public void update() {
		super.update();
		oscillator.update();
		setRotation(oscillator.getValue());
		updateWeaponry();
		updateClawStrike();
	}

	public void updateWeaponry() {
		timer.update();
		if (timer.isUp()) {
			timer.reset();
			fireCrabGun();
		}
	}

	public void fireCrabGun() {
		for (int i = 0; i < shotsPerVolley; i++) {
			float angle = getTotalRotation();
			angle = angle
					+ MathUtility.rangify(-shotSpread, shotSpread,
							shotsPerVolley, i);
			angle += shotOffset;
			Vector2f vec = new Vector2f(angle);
			new CrabShot(parentCrab.parentScreen, position, vec.negate());
		}
	}

	public void updateClawStrike() {
		collidable.setPosition(position);
		Hero theHero = parentCrab.targetHero;
		if (theHero != null) {
			if (collidable.isColliding(theHero.position)
					&& !theHero.isGettingKnockedBack) {
				theHero.knockBack(
						CUtil.getNormaledVector(position, theHero.position),
						200);
				theHero.takeDamage(5);
			}
		}
	}

	@Override
	public void removeArmature() {
		super.removeArmature();
		collidable.removeProperty();
	}

}
