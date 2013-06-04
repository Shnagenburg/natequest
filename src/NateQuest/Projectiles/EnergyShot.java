package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.Tiling.Tiles.Tile;

public class EnergyShot extends Projectile {

	public EnergyShot(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("blueball")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
	}

	@Override
	protected void setUpWeaponStats() {
		SPEED = 0.1f;
		baseDamage = 2;
	}

	public void update() {
		super.update();
		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t != null) {
			if (t.isCollidable) {
				this.markForDeletion();
			}
		}
		if (t == null) {
			this.markForDeletion();
		}
		handleHeroHit();
	}

	private void writeObject(java.io.ObjectOutputStream out) {

	}

	private void readObject(java.io.ObjectInputStream in) {

	}

}
