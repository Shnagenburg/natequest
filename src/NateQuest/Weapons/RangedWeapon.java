package NateQuest.Weapons;

import java.io.Serializable;

import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.MouseManager;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Gameplay.Hero;

public class RangedWeapon implements Serializable {

	// public static Vector2f hudPosition = new Vector2f(950, 625);
	final float HUD_Y = 625;
	final float HUD_X = 950;

	protected float fireRate;
	float fireTimer = 0;
	float fireTimerSecondary = 0;
	String equippedSpriteName = "nate";

	GameScreen parentScreen;
	public Sprite iconSprite;
	Hero parentHero;
	Crossheir crossheir;
	Sound fireSound;

	public RangedWeapon(GameScreen gs, Hero parentHero) {

		this.parentHero = parentHero;
		this.crossheir = gs.crossheir;
		parentScreen = gs;
		fireSound = SoundManager.heartgun;
	}

	private void getCrossheir() {
		if (crossheir == null) {
			crossheir = (Crossheir) parentScreen.findEntity(Crossheir.class);
		}
	}

	protected void setUpIcon(String iconSpriteName) {
		iconSprite = new Sprite(CUtil.QuickImage(iconSpriteName));
		iconSprite.bIsCameraed = false;
		iconSprite.setPositionRounded(HUD_X, HUD_Y);
	}

	public Vector2f getVectorToCrossheir() {
		Vector2f pos = new Vector2f(parentHero.position.x,
				parentHero.position.y);
		Vector2f target = crossheir.position;

		Vector2f vel = new Vector2f(pos.x - target.x, pos.y - target.y);
		vel = vel.normalise();
		vel = vel.negate();

		return vel;
	}

	public void update() {
		getCrossheir();
		if (fireTimer < fireRate)
			fireTimer += CUtil.ElapsedTime;

		if (fireTimerSecondary < fireRate)
			fireTimerSecondary += CUtil.ElapsedTime;

		if (fireTimer >= fireRate && MouseManager.getLeftClicked()) {
			fireWeapon();
		}

		if (fireTimerSecondary >= fireRate && MouseManager.getRightClicked()) {
			fireWeaponSecondary();
		}

	}

	public void fireWeapon() {
		SoundManager.playSound(fireSound, 1, 0.3f);
		// SoundManager.playSound(SoundManager.heartgun);
		fireTimer = 0;
	}

	public void fireWeaponSecondary() {
		fireTimerSecondary = 0;
	}

}
