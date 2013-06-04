package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Crossheir;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.VFX.Smoke;

public class TrackingRocket extends Projectile {

	final float TURNING_POWER = 0.0015f;
	final float SMOKE_TIME = 50;
	Crossheir crossheir;
	float lastAngle;
	int lastDir = 0;
	float timer = 0;

	public TrackingRocket(GameScreen gs, Vector2f pos, Vector2f vel,
			Crossheir xheir) {
		super(gs, pos, vel);
		crossheir = xheir;
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("rocket")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		drawable.mSprite.setRotation((float) vel.getTheta());
		lastAngle = (float) vel.getTheta();
	}

	@Override
	protected void setUpWeaponStats() {
		SPEED = 0.6f;
		baseDamage = 2;
	}

	public void update() {
		super.update();

		handleEnemyHit();
		track();
		generateSmoke();
		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t != null) {
			if (t.isCollidable) {
				this.markForDeletion();
			}
		}
		if (t == null) {
			this.markForDeletion();
		}
	}

	public void generateSmoke() {
		timer += CUtil.ElapsedTime;
		if (timer > SMOKE_TIME) {
			timer = 0;
			new Smoke(parentScreen, position);
		}
	}

	public void track() {
		Vector2f pos = new Vector2f(position.x, position.y);
		Vector2f target = crossheir.position;
		float curAngle = (float) velocity.getTheta();

		Vector2f vel = new Vector2f(pos.x - target.x, pos.y - target.y);
		vel = vel.normalise();
		vel = vel.negate();
		float targetAngle = (float) vel.getTheta();
		float dist;

		if (Math.abs(curAngle - targetAngle) > 90) {
			dist = curAngle - targetAngle;
			if (dist > 0 && lastDir == -1) {
				dist *= -1;
			} else if (dist < 0 && lastDir == 1) {
				dist *= -1;
			}
		} else {
			dist = curAngle - targetAngle;
		}
		curAngle -= dist * TURNING_POWER * CUtil.ElapsedTime;

		// Debugger.print("target: " + targetAngle + " current: " + curAngle +
		// " last target: " + lastAngle + " adjustment: " + (dist *
		// TURNING_POWER * CUtil.ElapsedTime) );
		velocity = new Vector2f(curAngle);
		drawable.mSprite.setRotation(curAngle + 45);
		lastAngle = targetAngle;

		if (dist > 0) {
			lastDir = 1;
		} else {
			lastDir = -1;
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {

	}

	private void readObject(java.io.ObjectInputStream in) {

	}
}
