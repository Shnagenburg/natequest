package NateQuest.Enemies;

import java.util.Random;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;
import javax.swing.text.Position;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.Fader;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Projectiles.EnergyShot;
import NateQuest.Projectiles.Projectile;
import NateQuest.Projectiles.EnemyProjectiles.SineWaveProjectile;
import NateQuest.VFX.DeathAnimation;
import NateQuest.VFX.VisualFX;

public class Wizard extends Enemy {

	final float MOVE_SPEED = 1.12f;
	boolean isNextMoveStop = false;
	final float SHADOW_MAX_TIME = 100;
	float shadowTimer = 0;
	boolean isShadowStepping = false;

	public Wizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Wizard(GameScreen gs) {
		super(gs);
		// TODO Auto-generated constructor stub
		Sprite sprite = new Sprite(CUtil.ResourcePool.getImageByName("wizard"));
		drawable = new Drawable(this, sprite, LayerConstants.CHARACTERS);

		collidable = new CollidableCircle(this, sprite.getWidth() / 2);
		collidable.matchSprite(drawable.mSprite);
		collidable.mask = CollidableMasks.ENEMY;
		health = 10;
		rand = new Random();
		dedSound = SoundManager.wizarddead;
	}

	public void update() {
		if (targetHero == null)
			findTargetHero();

		super.update();
		shadowStep();
		checkAggro();
		if (isAggroed) {
			attack();
			move();
			if (velocity.x == 0)
				setAngleToHero();
		}

		// if (targetHero != null)
		// Debugger.print("" + targetHero.position.distanceSquared(position));

		drawable.setPositionRounded(position.x, position.y);
		collidable.matchSprite(drawable.mSprite);
		drawable.update();
	}

	private void shadowStep() {
		if (isShadowStepping) {
			shadowTimer += CUtil.ElapsedTime;
			if (shadowTimer > SHADOW_MAX_TIME) {
				VisualFX shadow = new VisualFX(this.parentScreen, new Vector2f(
						position.x, position.y));
				shadow.drawable = new Drawable(shadow, new Sprite("wizard"),
						LayerConstants.SUB_CHARACTERS);
				shadow.setPosition(position.x, position.y);
				shadow.drawable
						.setRotation(this.drawable.mSprite.getRotation());
				Fader fader = new Fader(shadow.drawable.mSprite);
				fader.fadeOut(1000);
				shadow.addFader(fader);
				shadowTimer = 0;
			}
		}
	}

	protected void fireGun() {
		if (targetHero == null) {
			Debugger.print("no target!");
			return;
		}
		Vector2f pos = new Vector2f(position.x, position.y);
		Vector2f target = targetHero.position;

		Vector2f vel = CUtil.getNormaledVector(pos, target);

		new SineWaveProjectile(parentScreen, pos, vel);
	}

	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		knockBack(p);
		moveTimer = 0;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(
					CUtil.QuickImage("wizarddeath"), 4, 200);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}

	@Override
	public void makeNewMove() {
		if (rand == null) {
			rand = new Random();
		}
		if (isNextMoveStop) {
			velocity.x = 0;
			velocity.y = 0;
			isNextMoveStop = false;
			isShadowStepping = false;
			return;
		}

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
			a = a + (135 - rand.nextInt(270));
			velocity = new Vector2f(a);
			if (!willHitWallSoon()) {
				break;
			}
		}
		velocity.scale(MOVE_SPEED);
		isNextMoveStop = true;
		isShadowStepping = true;
		// Debugger.print("dist " + dist);
	}
}
