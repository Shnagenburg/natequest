package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;

public class ShotgunPellet extends Projectile {

	final float SPIN_SPEED = 1.0f;
	float lifeSpan = 300.0f;

	public ShotgunPellet(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		// TODO Auto-generated constructor stub
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("pellet")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	@Override
	protected void setUpWeaponStats() {
		SPEED = 0.8f;
		baseDamage = 2;
	}

	public void update() {
		super.update();
		handleEnemyHit();

		timer += CUtil.ElapsedTime;
		if (timer > lifeSpan)
			this.markForDeletion();

		float angle = drawable.mSprite.getRotation();
		angle += SPIN_SPEED * CUtil.ElapsedTime;
		drawable.mSprite.setRotation(angle);

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
