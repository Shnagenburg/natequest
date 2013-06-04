package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.Tiling.Tiles.Tile;

public class CrabShot extends Projectile {

	private static final long serialVersionUID = 770539583729220978L;

	public CrabShot(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		this.position = new Vector2f(pos);
		drawable = new Drawable(this, new Sprite("bubble"),
				LayerConstants.PROJECTILES);
		drawable.setPositionRounded(pos.x, pos.y);
	}

	@Override
	protected void setUpWeaponStats() {
		baseDamage = 2;
	}

	public void update() {
		super.update();
		checkForWallHit();
		handleHeroHit();
	}

	public void checkForWallHit() {
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

	@Override
	public void handleHeroHit() {
		Hero hero = (Hero) parentScreen.findEntity(Hero.class);
		if (hero != null) {
			if (hero.collidable.isColliding(position)) {
				hero.takeDamage(this);
				this.markForDeletion();
			}
		}
	}

}
