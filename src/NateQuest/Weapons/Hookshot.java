package NateQuest.Weapons;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.SoundManager;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.HookProjectile;
import NateQuest.VFX.Smoke;

public class Hookshot extends RangedWeapon {

	final float PULL_SPEED = 0.5f;
	final float FIRE_RATE = 300;
	float smokeTimer = 0;
	float MAX_SMOKE_TIMER = 50f;
	boolean isHooking;
	HookProjectile hook;

	public Hookshot(GameScreen gs, Hero parentHero) {
		super(gs, parentHero);

		setUpIcon("hookshotpickup");
		fireRate = FIRE_RATE;
		equippedSpriteName = "hookshot1";
		fireSound = SoundManager.hookshotgun;
	}

	public void update() {
		if (hook != null && MouseManager.getUpDownLeftMouseClick()) {
			// Debugger.print("locked");
			hook.lockHook();
			isHooking = true;
		}
		super.update();

		if (MouseManager.getUpDownLeftMouseClick()) {
			// hook = null;
		}
	}

	public void fireWeapon() {
		if (hook != null)
			return;

		super.fireWeapon();
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f vel = getVectorToCrossheir();

		hook = new HookProjectile(parentScreen, pos, vel, this);
		new Smoke(parentScreen, pos);
	}

	public void pullHero() {
		if (isHooking) {
			makeSmoke();
			Vector2f heroPos = parentScreen.hero.position;
			Vector2f vecToHook = CUtil
					.getNormaledVector(heroPos, hook.position);
			if (heroPos.distanceSquared(hook.position) > 100) {
				heroPos.x += vecToHook.x * CUtil.ElapsedTime * PULL_SPEED;
				heroPos.y += vecToHook.y * CUtil.ElapsedTime * PULL_SPEED;
			} else {
				// Debugger.print("done pulling");
				hook.markForDeletion();
				finishHooking();
			}
		}
	}

	public void finishHooking() {
		this.hook = null;
		isHooking = false;
	}

	private void makeSmoke() {
		smokeTimer += CUtil.ElapsedTime;
		if (smokeTimer > MAX_SMOKE_TIMER) {
			smokeTimer = 0;
			new Smoke(parentScreen, parentHero.position);
		}
	}

}
