package NateQuest.Enemies;

import java.io.IOException;
import java.util.Random;

import javax.swing.text.Position;

import org.newdawn.slick.Color;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.VFX.DamageNumber;
import NateQuest.VFX.DebugMarker;
import NateQuest.VFX.VisualFX;
import Utility.MathUtility;

public class Enemy extends Entity {

	float FIRE_TIME = 1000;
	float MOVE_TIME = 3000;
	final float COMFORT_DIST = 150000;
	final float AGGRO_RANGE = 350000;
	final float KNOCKBACK_DUR = 100;
	float MOVE_SPEED = 0.22f;
	float timer;
	int pushX = 0;
	int pushY = 0;
	Random rand = new Random();

	float moveTimer;
	Vector2f velocity;
	Hero targetHero;
	boolean isAggroed = false;
	float knockTimer = 0;
	boolean isGettingKnockBacked = false;
	EnemySpawner parentSpawner;
	public int health = 9;
	Sound dedSound = SoundManager.crabdead;
	Sound damageSound1 = SoundManager.enemydamage1;
	Sound damageSound2 = SoundManager.enemydamage2;
	Sound damageSound3 = SoundManager.enemydamage3;

	public Enemy() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Enemy(GameScreen gs) {
		super(gs);
		parentScreen.enemies.add(this);
		velocity = new Vector2f();
		findTargetHero();
	}

	public void findTargetHero() {
		/*
		 * for (Entity e : parentScreen.entities) { if (e.getClass() ==
		 * Hero.class) { targetHero = (Hero) e; //Debugger.print("found hero");
		 * return; } } Debugger.print("couldn't find hero");
		 */
		targetHero = parentScreen.hero;
	}

	public void update() {
		if (isGettingKnockBacked) {
			knockTimer += CUtil.ElapsedTime;
			if (knockTimer > KNOCKBACK_DUR) {
				knockTimer = 0;
				isGettingKnockBacked = false;
				makeNewMove();
			}
		}
	}

	public void removeEntity() {
		parentScreen.enemies.remove(this);
		if (parentSpawner != null) // null implies this badguy wasnt from a
									// spawner
			parentSpawner.spawnedEnemies.remove(this);
		super.removeEntity();
	}

	public void takeDamage(Projectile p) {
		int sound = MathUtility.rand.nextInt(3);
		switch (sound) {
		case 0:
			// SoundManager.playSound(damageSound1);
			break;
		case 1:
			// SoundManager.playSound(damageSound2);
			break;
		case 3:
			// SoundManager.playSound(damageSound3);
			break;
		}
		new DamageNumber(parentScreen, new Vector2f(position.x, position.y),
				p.baseDamage);
		drawable.flashSprite(Color.red, 100);
	}

	public void knockBack(Projectile p) {
		isGettingKnockBacked = true;
		velocity.x += p.velocity.x * 3f;
		velocity.y += p.velocity.y * 3f;
	}

	public void setAngleToHero() {
		float angle = getAngleToHero();
		drawable.setRotation(angle);
	}

	public float getAngleToHero() {
		float angle = (float) Math.atan((position.y - targetHero.position.y)
				/ (position.x - targetHero.position.x));
		angle = (float) Math.toDegrees(angle)
				- (position.x < targetHero.position.x ? 45 : 225);
		return angle;
	}

	public void checkAggro() {
		if (targetHero != null) {
			if (position.distanceSquared(targetHero.position) < AGGRO_RANGE) {
				isAggroed = true;
			} else {
				isAggroed = false;
			}
		}
	}

	protected void move() {
		// Debugger.print(velocity.toString() + "" + isGettingKnockBacked);
		float oldX = position.x;
		float oldY = position.y;
		moveTimer += CUtil.ElapsedTime;
		if (moveTimer > MOVE_TIME) {
			moveTimer = 0;
			makeNewMove();
		}
		position.x += velocity.x * CUtil.ElapsedTime * MOVE_SPEED;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		if (collidable != null) {
			collidable.matchSprite(drawable.mSprite);
		}
		if (isColliding()) {
			position.x = oldX;
			// position.x += pushX;

		}
		position.y += velocity.y * CUtil.ElapsedTime * MOVE_SPEED;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		if (collidable != null) {
			collidable.matchSprite(drawable.mSprite);
		}
		if (isColliding()) {
			// Debugger.print("pre pos: " + position.y);
			position.y = oldY;
			// Debugger.print("post pos: " + position.y);
			// position.y += pushY;
		}
		pushX = 0;
		pushY = 0;
	}

	protected void attack() {
		timer += CUtil.ElapsedTime;
		if (timer > FIRE_TIME) {
			timer = 0;
			fireGun();
		}
	}

	protected boolean isColliding() {
		for (Entity e : parentScreen.collidables) {
			if (e.collidable.isColliding(drawable.mSprite) && e != this
					&& (parentSpawner == null || e != parentSpawner)
					&& e.collidable.mask != CollidableMasks.ENEMY
					&& e.collidable.mask != CollidableMasks.EDITOR) {
				if (e.collidable.boundingBox.getCenterX() > position.x)
					pushX = -1;
				else
					pushX = 1;
				if (e.collidable.boundingBox.getCenterY() > position.y)
					pushY = -1;
				else
					pushY = 1;
				return true;
			}
		}
		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t == null) {
			return true;
		} else {
			if (t.isCollidable)
				return true;
		}
		return false;
	}

	protected void fireGun() {
	}

	protected void makeNewMove() {
	}

	// Returns true if the baddie will hit a wall, used to determine new moves
	public boolean willHitWallSoon() {
		Vector2f v = new Vector2f(velocity).scale(30);
		Vector2f newPos = new Vector2f(position.x, position.y).add(v);
		// new DebugMarker(parentScreen, newPos);

		Tile t = parentScreen.map.getTileFromPoint(newPos.x, newPos.y);
		if (t == null || t.isCollidable) {
			return true;
		}
		for (Entity c : parentScreen.collidables) {
			if (c.collidable.isColliding(newPos)
					&& c.collidable.mask == CollidableMasks.NEUTRAL) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		SoundManager.playSound(dedSound);
	}
}
