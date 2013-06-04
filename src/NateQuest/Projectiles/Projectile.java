package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Enemies.Enemy;
import NateQuest.Gameplay.Hero;

public class Projectile extends Entity {

	public Vector2f velocity;
	protected float SPEED = 0.2f;
	public int baseDamage = 0;
	float timer = 0;
	protected Hero targetHero;

	public Projectile(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs);
		position = new Vector2f(pos);
		velocity = new Vector2f(vel);
		saveThisEntity = false;
		setUpWeaponStats();
	}

	protected void setUpWeaponStats() {
	}

	public void update() {
		position.x += velocity.x * CUtil.ElapsedTime * SPEED;
		position.y += velocity.y * CUtil.ElapsedTime * SPEED;

		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	public void handleEnemyHit() {
		for (Enemy e : parentScreen.enemies) {
			if (e.collidable != null
					&& e.collidable.isColliding(drawable.mSprite)) {
				e.takeDamage(this);
				this.markForDeletion();
			}
		}
	}

	public void handleHeroHit() {
		for (Entity e : parentScreen.collidables) {
			if (e.collidable.isColliding(drawable.mSprite)
					&& e.collidable.mask != CollidableMasks.ENEMY
					&& e.collidable.mask != CollidableMasks.SPAWNER) {
				if (e.getClass() == Hero.class) {
					((Hero) e).takeDamage(this);
				}
				this.markForDeletion();
			}
		}
	}

	public void removeEntity() {
		super.removeEntity();
	}

	protected void getHero() {
		if (targetHero == null) {
			targetHero = (Hero) parentScreen.findEntity(Hero.class);
		}
	}

}
