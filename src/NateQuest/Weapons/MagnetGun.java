package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.MagnetProjectile;
import NateQuest.VFX.Smoke;

public class MagnetGun extends RangedWeapon {

	final float PULL_SPEED = 0.5f;
	final float FIRE_RATE = 300;
	MagnetProjectile leadOne, leadTwo;

	public MagnetGun(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);

		setUpIcon("magnetpickup");
		fireRate = FIRE_RATE;

	}

	public void update() {
		super.update();
		handleLeads();
	}

	public void handleLeads() {
		if (leadOne == null || leadTwo == null || !leadOne.isTagged
				|| !leadTwo.isTagged)
			return;

		if (leadOne.position.distanceSquared(leadTwo.position) > 100) {
			Debugger.print("PULLING");
			Vector2f leadOneTracj = CUtil.getNormaledVector(
					leadOne.target.position, leadTwo.target.position);
			Vector2f leadTwoTracj = CUtil.getNormaledVector(
					leadTwo.target.position, leadOne.target.position);
			leadOne.target.position.x += leadOneTracj.x * CUtil.ElapsedTime
					* PULL_SPEED;
			leadOne.target.position.y += leadOneTracj.y * CUtil.ElapsedTime
					* PULL_SPEED;

			leadTwo.target.position.x += leadTwoTracj.x * CUtil.ElapsedTime
					* PULL_SPEED;
			leadTwo.target.position.y += leadTwoTracj.y * CUtil.ElapsedTime
					* PULL_SPEED;
		} else {
			Debugger.print("done pulling");
			leadOne.target.takeDamage(leadOne);
			leadTwo.target.takeDamage(leadTwo);
			leadOne.markForDeletion();
			leadTwo.markForDeletion();
			leadOne = null;
			leadTwo = null;

		}

	}

	public void fireWeapon() {
		super.fireWeapon();
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		if (leadOne != null)
			leadOne.markForDeletion();
		leadOne = new MagnetProjectile(parentScreen, pos, vel);
		new Smoke(parentScreen, pos);
	}

	public void fireWeaponSecondary() {
		super.fireWeaponSecondary();

		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		if (leadTwo != null)
			leadTwo.markForDeletion();
		leadTwo = new MagnetProjectile(parentScreen, pos, vel);
		new Smoke(parentScreen, pos);
	}

}
