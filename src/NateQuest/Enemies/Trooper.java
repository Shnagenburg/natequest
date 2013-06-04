package NateQuest.Enemies;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.EnergyShot;
import NateQuest.Projectiles.HeartProjectile;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.VFX.DeathAnimation;
import NateQuest.VFX.Smoke;

public class Trooper extends Enemy {

	public Trooper() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Trooper(GameScreen gs) {
		super(gs);
		init();

		timer = 0;
		moveTimer = 0;
		collidable = new Collidable(this);
		collidable.matchSprite(drawable.mSprite);
		collidable.mask = CollidableMasks.ENEMY;
		dedSound = SoundManager.orcdead1;
	}

	protected void init() {
		Sprite headSprite = new Sprite(CUtil.QuickImage("orchead"));
		Sprite sprite = new Sprite(CUtil.ResourcePool.getImageByName("orcarms"));
		Sprite gunSprite = new Sprite(CUtil.QuickImage("orcgun1"));
		drawable = new MultiSprited(this, sprite, LayerConstants.CHARACTERS);
		((MultiSprited) drawable).addSprite(gunSprite);
		((MultiSprited) drawable).addSprite(headSprite);
		health = 5;
	}

	public void update() {
		if (targetHero == null)
			findTargetHero();

		super.update();
		checkAggro();
		if (isAggroed) {
			attack();
			move();
			setAngleToHero();
		}

		// if (targetHero != null)
		// Debugger.print("" + targetHero.position.distanceSquared(position));

		drawable.setPositionRounded(position.x, position.y);
		collidable.matchSprite(drawable.mSprite);
		drawable.update();
	}

	protected void fireGun() {
		if (targetHero == null) {
			Debugger.print("no target!");
			return;
		}
		Vector2f pos = new Vector2f(position.x, position.y);
		Vector2f target = targetHero.position;

		Vector2f vel = CUtil.getNormaledVector(pos, target);

		new EnergyShot(parentScreen, pos, vel);
	}

	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		knockBack(p);
		moveTimer = 0;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(CUtil.QuickImage("orc"), 4, 200);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}

	@Override
	public void makeNewMove() {
		if (targetHero == null)
			findTargetHero();
		if (targetHero == null) {
			Debugger.print("no target!");
			return;
		}
		Vector2f pos = new Vector2f(position.x, position.y);
		Vector2f target = targetHero.position;

		Vector2f vel = new Vector2f(pos.x - target.x, pos.y - target.y);
		vel = vel.normalise();
		int variance = 0;
		for (int tries = 3; tries > 0; tries--) {
			float a = (float) Math.toDegrees(Math.atan((pos.y - target.y)
					/ (pos.x - target.x)));
			if (pos.x < target.x) {
				a += 180;
			}
			// At this point the angle is pointing away from the hero
			float dist = pos.distanceSquared(target);
			if (dist > COMFORT_DIST) {
				a += 180;
			}
			a = a + (variance / 2 - rand.nextInt(variance + 1));
			velocity = new Vector2f(a);
			if (!willHitWallSoon()) {
				break;
			}
			variance += 180;
		}
		// Debugger.print("dist " + dist);
	}

}
