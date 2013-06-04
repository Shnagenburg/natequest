package NateQuest.Projectiles.EnemyProjectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.ChunkMap;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.VFX.RisingText;

public class SonicWave extends Projectile {

	final float TIMER_MAX = 10000;
	float timer = 0;

	public SonicWave(GameScreen gs, Vector2f pos, Vector2f vel) {
		super(gs, pos, vel);
		SheetedSprite sprite = new SheetedSprite(
				CUtil.QuickImage("sonic2frames"), 2, 200);
		drawable = new Drawable(this, sprite, LayerConstants.PROJECTILES);
		drawable.setPositionRounded(pos.x, pos.y);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.50f));
		collidable.removeProperty();
		drawable.setRotation((float) vel.getTheta() + 90);
	}

	@Override
	protected void setUpWeaponStats() {
		baseDamage = 2;
	}

	@Override
	public void update() {
		super.update();
		drawable.update();
		collidable.setPosition(position.x, position.y);
		handleTimer();
		handleHeroHit();
		handleEnvironmentHit();
	}

	private void handleTimer() {
		timer += CUtil.ElapsedTime;
		if (timer > TIMER_MAX) {
			this.markForDeletion();
		}
	}

	public void handleHeroHit() {
		getHero();
		if (targetHero == null || targetHero.collidable == null)
			return;

		if (targetHero.collidable.isColliding(collidable)) {
			targetHero.takeDamage(this);
			targetHero.getStunned(1000);
			new RisingText(parentScreen, position, "Slowed!");
			this.markForDeletion();
		}
	}

	private void handleEnvironmentHit() {
		ChunkMap map = parentScreen.map;
		Tile t = map.getTileFromPoint(position.x, position.y);
		if (t == null || t.isCollidable) {
			this.markForDeletion();
		}
	}

}
