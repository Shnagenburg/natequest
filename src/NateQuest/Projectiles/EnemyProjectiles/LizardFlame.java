package NateQuest.Projectiles.EnemyProjectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.Projectile;

public class LizardFlame extends Projectile {

	final float TIMER_MAX = 1200;
	float timer = 0;

	public LizardFlame(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		drawable = new Drawable(this, new Sprite("lizardfireball"),
				LayerConstants.PROJECTILES);
		drawable.setPositionRounded(pos.x, pos.y);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getWidth() * 0.80f));
		collidable.removeProperty();
		baseDamage = 1;
	}

	@Override
	protected void setUpWeaponStats() {
		baseDamage = 1;
	}

	@Override
	public void update() {
		super.update();
		collidable.setPosition(position.x, position.y);
		handleTimer();
		handleHeroHit();
	}

	private void handleTimer() {
		timer += CUtil.ElapsedTime;
		if (timer > TIMER_MAX) {
			this.markForDeletion();
		}
	}

	public void handleHeroHit() {
		getHero();
		if (targetHero == null || targetHero.collidable == null) {
			return;
		}

		if (collidable.isColliding(targetHero.collidable)) {
			targetHero.takeDamage(this);
			this.markForDeletion();
		}
	}

}
