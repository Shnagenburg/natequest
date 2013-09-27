package NateQuest.Enemies;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import NateQuest.GameScreen;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Projectiles.Projectile;
import NateQuest.Projectiles.EnemyProjectiles.SonicWave;
import NateQuest.VFX.DeathAnimation;
import Utility.MathUtility;

public class Bat extends Enemy {

	float fireTimer = 0;

	public Bat() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Bat(GameScreen gs) {
		super(gs);
		FIRE_TIME = 500;
		MOVE_SPEED = 0.28f;
		MOVE_TIME = 1200;
		health = 11;
		SheetedSprite sprite = new SheetedSprite(
				CUtil.QuickImage("bat4frames2"), 4, 125);
		drawable = new Drawable(this, sprite, LayerConstants.CHARACTERS);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.35f));
		findTargetHero();
		dedSound = SoundManager.batdead;
	}

	public void update() {
		drawable.update();
		move();
		updateGun();
	}

	@Override
	protected void move() {
		super.move();
		if (moveTimer > MOVE_TIME) {
			makeNewMove();
		}
	}

	@Override
	protected void makeNewMove() {
		float angle = MathUtility.randomFloat(0, 360);
		drawable.setRotation(angle);
		velocity = new Vector2f(angle);
	}

	private void updateGun() {
		fireTimer += CUtil.ElapsedTime;
		if (fireTimer > FIRE_TIME) {
			fireTimer = 0;
			fireGun();
		}
	}

	@Override
	protected void fireGun() {
		float angle = getAngleToHero() + 45;
		new SonicWave(parentScreen, position, new Vector2f(angle));
	}

	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(CUtil.QuickImage("bat4frames"),
					4, 200);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}
}
