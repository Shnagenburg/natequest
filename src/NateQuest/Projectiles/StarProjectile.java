package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;

public class StarProjectile extends Projectile {

	float MAX_LIFE_TIMER = 3000;
	float lifeTimer = 0;
	StarMagnetProjectile lead;
	float lastAngle;
	int lastDir = 0;
	final float TURNING_POWER = 0.0015f;

	public StarProjectile(GameScreen gs, Vector2f pos, Vector2f vel,
			StarMagnetProjectile lead) {
		super(gs, pos, vel);
		this.lead = lead;
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("redstar")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	@Override
	protected void setUpWeaponStats() {
		SPEED = 0.5f;
		baseDamage = 1;
	}

	public void update() {
		super.update();
		updateLifespan();
		handleEnemyHit();
		track();
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

	private void updateLifespan() {
		lifeTimer += CUtil.ElapsedTime;
		if (lifeTimer > MAX_LIFE_TIMER) {
			this.markForDeletion();
		}
	}

	public void track() {
		if (lead == null)
			return;
		Vector2f pos = new Vector2f(position.x, position.y);
		Vector2f target = lead.position;
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

}
