package NateQuest.Enemies;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Projectiles.Projectile;
import NateQuest.Projectiles.EnemyProjectiles.LizardFlame;
import NateQuest.VFX.DeathAnimation;
import Utility.MathUtility;
import Utility.UtilityOscillator;

public class Lizard extends Enemy {

	final float FLAME_RANGE = 150;
	final float FLAME_CYCLE_TIME = 3000;
	final float FLAME_CYCLE_ANGLE = 45;
	final float SPRITE_ROTATE_ADJUST = 135;
	final float GUN_ROTATE_ADJUST = -90;
	float fireTimer = 0, initialFlameAngle = 0;
	UtilityOscillator oscillator;

	private LizardState state;

	enum LizardState {
		MOVING, FLAMING
	}

	public Lizard() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Lizard(GameScreen gs) {
		super(gs);
		init();
		dedSound = SoundManager.lizarddead;
	}

	protected void init() {
		FIRE_TIME = 130;
		MOVE_SPEED = 0.28f;
		MOVE_TIME = 1200;
		SheetedSprite sprite = new SheetedSprite(
				CUtil.QuickImage("lizard4frames"), 4, 150);
		drawable = new Drawable(this, sprite, LayerConstants.CHARACTERS);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.5f));
		findTargetHero();
		startMoving();
		health = 15;
	}

	public void update() {
		findTargetHero();
		switch (state) {
		case MOVING:
			updateMovement();
			break;
		case FLAMING:
			updateFlaming();
			break;
		default:
			break;
		}
		drawable.update();

	}

	private void updateMovement() {
		move();
		handleNewMove();

		if (isWithinRangeOfHero()) {
			startFlaming();
		}
	}

	private void startFlaming() {
		state = LizardState.FLAMING;
		drawable.mSprite.lock(0);
		oscillator = new UtilityOscillator(-FLAME_CYCLE_ANGLE,
				FLAME_CYCLE_ANGLE, FLAME_CYCLE_TIME);
		initialFlameAngle = drawable.getRotation();
	}

	protected void startMoving() {
		state = LizardState.MOVING;
		drawable.mSprite.unlock();
		moveTimer = MOVE_TIME;
	}

	private void handleNewMove() {
		moveTimer += CUtil.ElapsedTime;
		if (moveTimer > MOVE_TIME) {
			moveTimer = 0;
			makeNewMove();
		}
	}

	protected void makeNewMove() {
		velocity = new Vector2f(getAngleToHero() + 45);
		float angle = getAngleToHero();
		drawable.setRotation(angle + SPRITE_ROTATE_ADJUST);
	}

	private boolean isWithinRangeOfHero() {
		float distance = MathUtility.distance(position, targetHero.position);
		if (distance < FLAME_RANGE) {
			return true;
		}
		return false;
	}

	private void updateFlaming() {
		oscillator.update();
		drawable.mSprite.setRotation(initialFlameAngle + oscillator.getValue());
		updateGun();
		if (oscillator.getHasDonePass()) {
			startMoving();
		}
	}

	private void updateGun() {
		fireTimer += CUtil.ElapsedTime;
		if (fireTimer > FIRE_TIME) {
			fireTimer = 0;
			fireGun();
		}
	}

	protected void fireGun() {
		float angle = drawable.getRotation() + GUN_ROTATE_ADJUST;
		new LizardFlame(parentScreen, position, new Vector2f(angle));
	}

	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(drawable.mSprite.mTexture, 4,
					200);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}

}
