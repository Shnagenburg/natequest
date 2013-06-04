package NateQuest.Projectiles.EnemyProjectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.Tiles.Tile;

public class SineWaveProjectile extends Projectile {

	float timer = 0;
	Vector2f perp;

	public SineWaveProjectile(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);

		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("fireball")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		perp = new Vector2f(vel.getPerpendicular());
	}

	protected void setUpWeaponStats() {
		baseDamage = 2;
		SPEED = 0.20f;
	}

	public void update() {
		super.update();
		handleHeroHit();
		oscillate();

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

	private void oscillate() {
		timer += CUtil.ElapsedTime;
		position.x += perp.x * CUtil.ElapsedTime * SPEED
				* Math.sin(timer / 150) * 2;
		position.y += perp.y * CUtil.ElapsedTime * SPEED
				* Math.sin(timer / 150) * 2;
	}

}
