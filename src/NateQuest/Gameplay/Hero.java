package NateQuest.Gameplay;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.text.DefaultEditorKit.CutAction;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.GameHarness;
import GameClasses.KeyManager;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameProgression;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Enemies.Knight;
import NateQuest.Enemies.Wizard;
import NateQuest.HUD.HealthBar;
import NateQuest.ImageCreator.DanColor;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Tiling.Tiles.Tile.BasicType;
import NateQuest.VFX.DeathAnimation;
import NateQuest.Weapons.Gun;

public class Hero extends Entity {

	final float SPEED = 0.25f;
	final float DEATH_TIMER_MAX = 0;
	float stunDuration = 0;
	float stunTimer = 0;
	float deathTimer = 0;
	boolean isStunned = false;

	public Vector2f velocity = new Vector2f();
	Vector2f acceleration = new Vector2f();
	public boolean isGettingKnockedBack = false;
	HealthBar hpBar;
	float knockBackTimer = 0;
	private boolean isDying = false;
	private DeathAnimation deathAnimation;

	public Hero(GameScreen gs) {
		super(gs);
		// doesnt work currently due to saving / loading
		// Sprite sprite = new Sprite(ImageCreator.createImage(64, 64,
		// Color.BLUE));
		Sprite headSprite = new Sprite(CUtil.QuickImage("natehead"));
		SheetedSprite sprite = new SheetedSprite(
				CUtil.ResourcePool.getImageByName("nate4frames"), 4, 200);
		Sprite gunSprite = new Sprite(CUtil.QuickImage("noimage"));
		drawable = new MultiSprited(this, sprite, LayerConstants.CHARACTERS);
		((MultiSprited) drawable).addSprite(gunSprite);
		((MultiSprited) drawable).addSprite(headSprite);

		collidable = new CollidableCircle(this, headSprite.getHeight() / 2);
		collidable.mask = CollidableMasks.HERO;

		position = new Vector2f(200, 200);
		velocity = new Vector2f(0, 0);
		acceleration = new Vector2f(0, 0);

		hpBar = new HealthBar(parentScreen);
		gs.hero = this;
		// new Sword(parentScreen, this);
		saveThisEntity = false;
	}

	public void update() {
		super.update();
		parentScreen.map.setActiveChunk(
				(position.x + parentScreen.crossheir.position.x) / 2,
				(position.y + parentScreen.crossheir.position.y) / 2);
		if (isGettingKnockedBack) {
			knockBackTimer -= CUtil.ElapsedTime;
			if (knockBackTimer < 0) {
				isGettingKnockedBack = false;
			}
		} else {
			updateVelocity();
		}
		updatePosition();
		if (velocity.x != 0 || velocity.y != 0)
			drawable.update();
		else
			drawable.mSprite.setFrame(0);
		collidable.matchSprite(drawable.mSprite);
		// parentScreen.map.getTileFromPoint(position.x, position.y);
		parentScreen.camera.update(drawable.mSprite);
		updateStun();
		updateDeath();
	}

	public void takeDamage(Projectile p) {
		SoundManager.playSound(SoundManager.natedamage);
		drawable.flashSprite(Color.red, 300);
		if (hpBar != null)
			hpBar.takeDamage(p);
	}

	public void takeDamage(int damage) {
		SoundManager.playSound(SoundManager.natedamage);
		drawable.flashSprite(Color.red, 300);
		if (hpBar != null)
			hpBar.takeDamage(damage);
	}

	public void updateVelocity() {

		int xDir = 0;

		if (KeyManager.isAdown)
			xDir += -1;
		if (KeyManager.isDdown)
			xDir += 1;

		velocity.x = xDir * SPEED;

		int yDir = 0;

		if (KeyManager.isWdown)
			yDir += -1;
		if (KeyManager.isSdown)
			yDir += 1;

		velocity.y = yDir * SPEED;

		if (KeyManager.HitSpace()) {
			velocity.y = -0.5f;
		}
	}

	public void updatePosition() {
		int oldX = (int) position.x;
		int oldY = (int) position.y;
		position.x += getVelocityX() * CUtil.ElapsedTime;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		for (Entity c : parentScreen.collidables) {
			if (c.collidable.isColliding(drawable.mSprite)
					&& c.collidable.mask != CollidableMasks.HERO
					&& c.collidable.mask != CollidableMasks.EVENT
					&& c.collidable.mask != CollidableMasks.EDITOR) {
				pushX(c, oldX);
				break;
			}
		}

		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t == null || t.isCollidable) {
			position.x = oldX;
		}

		position.y += getVelocityY() * CUtil.ElapsedTime;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		for (Entity c : parentScreen.collidables) {
			if (c.collidable.isColliding(drawable.mSprite)
					&& c.collidable.mask != CollidableMasks.HERO
					&& c.collidable.mask != CollidableMasks.EVENT
					&& c.collidable.mask != CollidableMasks.EDITOR) {
				pushY(c, oldY);
				break;
			}
		}

		t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t == null || t.isCollidable) {
			position.y = oldY;
		}
		drawable.setPositionRounded(position.x, position.y);
	}

	private void pushX(Entity pusher, int oldX) {
		checkIfPushDamages(pusher);
		if (pusher.collidable.boundingBox.getCenterX() > oldX) {
			position.x = -1 + pusher.collidable.boundingBox.getMinX()
					- drawable.mSprite.getScaledWidth() / 2;
		} else {
			position.x = 1 + pusher.collidable.boundingBox.getMaxX()
					+ drawable.mSprite.getScaledWidth() / 2;
		}
		velocity.x = 0;
	}

	private void pushY(Entity pusher, int oldY) {
		checkIfPushDamages(pusher);
		if (pusher.collidable.boundingBox.getCenterY() > oldY) {
			position.y = -1 + pusher.collidable.boundingBox.getMinY()
					- drawable.mSprite.getScaledHeight() / 2;
		} else {
			position.y = pusher.collidable.boundingBox.getMaxY()
					+ drawable.mSprite.getScaledHeight() / 2;
		}
		velocity.y = 0;
	}

	private void checkIfPushDamages(Entity pusher) {
		if (pusher.getClass() == Knight.class) {
			Knight knight = (Knight) pusher;
			takeDamage(knight.swordAttack);
		}
	}

	private float getVelocityX() {
		return velocity.x * (isStunned ? 0.5f : 1);
	}

	private float getVelocityY() {
		return velocity.y * (isStunned ? 0.5f : 1);
	}

	public void killHero() {
		if (!isDying) {
			SoundManager.playSound(SoundManager.natedead);
			// SheetedSprite s = new
			// SheetedSprite(CUtil.QuickImage("nate4frames"), 6, 200);
			// deathAnimation = new DeathAnimation(parentScreen, s, position);
			isDying = true;
		}
	}

	public void setPosition(float x, float y) {
		drawable.setPositionRounded(x, y);
		position.x = x;
		position.y = y;
	}

	@Override
	public void attach(GameScreen parentScreen) {
		super.attach(parentScreen);
		parentScreen.hero = this;
		drawable.resetCamera();
	}

	public void knockBack(Vector2f vec, float duration) {
		isGettingKnockedBack = true;
		velocity = vec;
		knockBackTimer = duration;
	}

	public void getStunned(float duration) {
		this.stunDuration = duration;
		isStunned = true;
	}

	private void updateStun() {
		stunTimer += CUtil.ElapsedTime;
		if (stunTimer > stunDuration) {
			isStunned = false;
			stunTimer = 0;
		}
	}

	private void updateDeath() {
		if (isDying) {
			deathTimer += CUtil.ElapsedTime;
			if (deathTimer > DEATH_TIMER_MAX) {
				isDying = false;
				GameProgression.LastCheckpoint.revertToCheckpoint(this);
				// deathAnimation.markForDeletion();
			}
		}
	}

	public void copyStatusFromOtherHero(Hero other) {
		Debugger.print("hp: " + other.hpBar.currentHealth + " "
				+ other.hpBar.maxHealth);
		this.hpBar.currentHealth = other.hpBar.currentHealth;
		this.hpBar.maxHealth = other.hpBar.maxHealth;
		this.hpBar.setHealthbar();
	}

}
