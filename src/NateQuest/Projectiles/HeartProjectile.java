package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;

public class HeartProjectile extends Projectile {

	public HeartProjectile(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		// TODO Auto-generated constructor stub
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("heart")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	@Override
	protected void setUpWeaponStats() {
		SPEED = 0.5f;
		baseDamage = 2;
	}

	public void update() {
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

	private void writeObject(java.io.ObjectOutputStream out) {

	}

	private void readObject(java.io.ObjectInputStream in) {

	}

}
