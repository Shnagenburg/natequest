package NateQuest.Projectiles.EnemyProjectiles;

import org.newdawn.slick.geom.Vector2f;

import NateQuest.GameScreen;
import NateQuest.Projectiles.Projectile;

public class KnightSwordHit extends Projectile {

	public KnightSwordHit(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
	}

	@Override
	protected void setUpWeaponStats() {
		baseDamage = 5;
	}

	public void update() {
	}

}
