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
import NateQuest.Projectiles.EnemyProjectiles.KnightSwordHit;
import NateQuest.VFX.DeathAnimation;

public class Knight extends Enemy {

	public KnightSwordHit swordAttack;

	public Knight() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Knight(GameScreen gs) {
		super(gs);
		FIRE_TIME = 500;
		MOVE_SPEED = 0.2f;
		MOVE_TIME = 200;
		health = 25;
		SheetedSprite sprite = new SheetedSprite(
				CUtil.QuickImage("knight2frames"), 2, 250);
		drawable = new Drawable(this, sprite, LayerConstants.CHARACTERS);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.35f));
		findTargetHero();
		swordAttack = new KnightSwordHit(parentScreen, position, new Vector2f());
		swordAttack.removeEntity();
		dedSound = SoundManager.knightdead;
	}

	public void update() {
		drawable.update();
		move();
		setAngleToHero();
	}

	@Override
	public void setAngleToHero() {
		float angle = getAngleToHero();
		drawable.setRotation(angle - 45);
	}

	protected void makeNewMove() {
		velocity = new Vector2f(getAngleToHero() + 45);
	}

	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(
					CUtil.QuickImage("knight2frames"), 4, 200);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}
}
