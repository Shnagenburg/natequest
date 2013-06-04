package NateQuest.ActionItems;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.KeyManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Enemies.Trooper;
import NateQuest.Gameplay.Hero;
import NateQuest.Tiling.Tiles.Tile;

public class PushBlock extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6730476840287653748L;
	final float SPEED = 0.12f;
	final float PHYS_BOX_COLLIDER_SCALE = 0.75f;
	int xDir, yDir;
	Hero hero;
	Vector2f target;
	boolean isAnimating = false;
	Collidable pushCollider;

	public PushBlock(GameScreen gs) {
		super(gs);
		drawable = new Drawable(this, new Sprite("crate"), LayerConstants.WALLS);
		collidable = new Collidable(this);
		collidable.matchSprite(drawable.mSprite, PHYS_BOX_COLLIDER_SCALE);
		position = new Vector2f();
		pushCollider = new Collidable(this);
		pushCollider.removeProperty();
		pushCollider.matchSprite(drawable.mSprite);
	}

	public void update() {
		if (hero == null)
			hero = (Hero) parentScreen.findEntity(Hero.class);

		if (KeyManager.isKeyHit(Input.KEY_E)
				&& pushCollider.isColliding(hero.collidable)) {
			playerAction();
		}
	}

	@Override
	public void setPosition(float x, float y) {
		int tsize = Tile.TILE_SIZE;
		x = x - (x % tsize);
		y = y - y % tsize;

		super.setPosition(x, y);
		collidable.matchSprite(drawable.mSprite, PHYS_BOX_COLLIDER_SCALE);
		pushCollider.matchSprite(drawable.mSprite);
	}

	public void playerAction() {
		int displacement = Tile.TILE_SIZE;
		float xdist = position.x - hero.position.x;
		float ydist = position.y - hero.position.y;
		xDir = 0;
		yDir = 0;
		target = new Vector2f(position.x, position.y);

		if (Math.abs(ydist) > Math.abs(xdist)) {
			// y displacement
			if (ydist > 0) {
				target.y += displacement;
				yDir = 1;
			} else {
				target.y -= displacement;
				yDir = -1;
			}
		} else {
			if (xdist > 0) {
				target.x += displacement;
				xDir = 1;
			} else {
				target.x -= displacement;
				xDir = -1;
			}
		}

		isAnimating = true;

		this.setPosition(position.x, position.y);
	}

	public void animate() {
		if (isAnimating) {
			if (position.x < target.x) {
				position.x += SPEED * CUtil.ElapsedTime;
			} else {
				position.x -= SPEED * CUtil.ElapsedTime;
			}
		}
	}

}
