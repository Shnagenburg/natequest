package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Enemies.Enemy;
import NateQuest.Tiling.Tiles.Tile;

public class StarMagnetProjectile extends Projectile {

	public boolean isTagged = false;
	public Enemy target;

	public StarMagnetProjectile(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("magnet")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		SPEED = 0.5f;
		baseDamage = 999;

	}

	public void update() {
		if (isTagged) {
			trackAndWait();
		} else {
			super.update();
			handleEnemyHit();

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
	}

	public void trackAndWait() {
		this.position.x = target.position.x;
		this.position.y = target.position.y;
		drawable.setPositionRounded(position.x, position.y);
	}

	public void handleEnemyHit() {
		for (Enemy e : parentScreen.enemies) {
			if (e.collidable.isColliding(drawable.mSprite)) {
				target = e;
				isTagged = true;
				;
			}
		}
	}

}
