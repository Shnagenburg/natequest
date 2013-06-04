package NateQuest.Projectiles;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;

public class BouncingProjectile extends Projectile {

	final int MAX_BOUNCES = 1;
	int bounces = 0;

	public BouncingProjectile(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		// TODO Auto-generated constructor stub
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("heart")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		SPEED = 0.4f;
	}

	public void bounce(Tile t) {
		if (bounces == MAX_BOUNCES) {
			this.markForDeletion();
			return;
		}

		if (t == null) {
			velocity = velocity.negate();
		} else {
			if (Math.abs(t.tileSprite.getX() - position.x) < Math
					.abs(t.tileSprite.getY() - position.y)) {
				velocity.y = -velocity.y;
			} else {
				velocity.x = -velocity.x;
			}
		}
		bounces++;
	}

	public void update() {
		super.update();
		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t != null) {
			if (t.isCollidable) {
				bounce(t);
			}
		}
		if (t == null) {
			bounce(t);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {

	}

	private void readObject(java.io.ObjectInputStream in) {

	}

}
