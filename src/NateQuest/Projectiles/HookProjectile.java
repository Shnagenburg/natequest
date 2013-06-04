package NateQuest.Projectiles;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Weapons.Hookshot;

public class HookProjectile extends Projectile {

	float MAX_LIFE_TIMER = 3000;
	float lifeTimer = 0;
	Hookshot parentHookshot;
	boolean bIsHookLocked = false;

	public HookProjectile(GameScreen gs, Vector2f pos, Vector2f vel,
			Hookshot parenthookshot) {
		super(gs, pos, vel);
		this.parentHookshot = parenthookshot;
		drawable = new Drawable(this, new Sprite(
				CUtil.ResourcePool.getImageByName("hook")),
				LayerConstants.PROJECTILES);
		drawable.mSprite.setPositionRounded(position.x, position.y);
		SPEED = 1.0f;
	}

	public void update() {
		// updateLifeSpan();
		if (bIsHookLocked) {
			parentHookshot.pullHero();
			return;
		} else {
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
		}
	}

	private void updateLifeSpan() {
		lifeTimer += CUtil.ElapsedTime;
		if (lifeTimer > MAX_LIFE_TIMER) {
			this.markForDeletion();
		}
	}

	public void lockHook() {
		SoundManager.playSound(SoundManager.grapplechink);
		bIsHookLocked = true;
		this.velocity.x = 0;
		this.velocity.y = 0;

	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		parentHookshot.finishHooking();
	}

}
