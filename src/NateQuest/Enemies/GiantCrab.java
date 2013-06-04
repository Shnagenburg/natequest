package NateQuest.Enemies;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Projectiles.Projectile;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.VFX.DeathAnimation;
import Utility.MathUtility;
import Utility.UtilityTimer;

public class GiantCrab extends Enemy {

	CrabArmature rightArm, leftArm, rightLegs, leftLegs;
	CrabArmature[] armatures = new CrabArmature[4];
	float speedPenalty = 0.3f;
	float rotation;

	public GiantCrab() {
		this(CUtil.ScreenGettingLoaded);
	}

	public GiantCrab(GameScreen gs) {
		super(gs);
		drawable = new Drawable(this, new Sprite("crabcarp"),
				LayerConstants.PROJECTILES);
		rightLegs = new CrabLegs(this, new Vector2f(-100, -95), "legsone");
		leftLegs = new CrabLegs(this, new Vector2f(110, 115), "legstwo");
		rightArm = new CrabClaw(this, new Vector2f(-125, 30), "clawone");
		leftArm = new CrabClaw(this, new Vector2f(10, 140), "clawtwo");
		rotation = 0;
		armatures[0] = rightArm;
		armatures[1] = leftArm;
		armatures[2] = rightLegs;
		armatures[3] = leftLegs;
		// anchorClaws();
		int radius = Math.max(drawable.mSprite.getWidth() / 2,
				drawable.mSprite.getHeight() / 2);
		collidable = new CollidableCircle(this, (int) (radius * 0.75f));
		health = 50;
		dedSound = SoundManager.crabdead;
	}

	// TODO messes up the full spinning crab. Needs to be set ONLY when setting
	// the claws angle, and origin'd when setting full angle
	public void anchorClaws() {
		rightArm.drawable.mSprite.mTexture.setCenterOfRotation(125, 50);
		leftArm.drawable.mSprite.mTexture.setCenterOfRotation(100, 25);
		rightLegs.drawable.mSprite.mTexture.setCenterOfRotation(130, 130);
		leftLegs.drawable.mSprite.mTexture.setCenterOfRotation(10, 10);
	}

	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		for (CrabArmature arm : armatures) {
			arm.setPositionFromParent();
		}
	}

	public void setRotation(float angle) {
		rotation = angle;
		drawable.mSprite.setRotation(angle);
		for (CrabArmature arm : armatures) {
			arm.setRotation(arm.rotation);
		}
	}

	public void update() {
		super.update();
		move();
		if (targetHero == null) {
			findTargetHero();
		}
		setRotation(rotation);
		for (CrabArmature arm : armatures) {
			arm.update();
		}
		setAngleToHero();
		drawable.update();
	}

	@Override
	public void setAngleToHero() {
		if (targetHero != null) {
			rotation = super.getAngleToHero();
			rotation -= 90;
			drawable.setRotation(rotation);
			for (CrabArmature arm : armatures) {
				arm.setRotation(arm.rotation);
			}
		}
	}

	@Override
	public void makeNewMove() {
		float angle = rotation;
		if (MathUtility.randomBool()) {
			angle += 45;
		} else {
			angle -= 135;
		}
		velocity = new Vector2f(angle); // Always facing hero, so this will move
										// perpindicular to him
	}

	@Override
	protected boolean isColliding() {
		Tile t = parentScreen.map.getTileFromPoint(position.x, position.y);
		if (t == null) {
			return true;
		} else {
			if (t.isCollidable)
				return true;
		}
		return false;
	}

	@Override
	protected void move() {
		float oldX = position.x;
		float oldY = position.y;
		moveTimer += CUtil.ElapsedTime;
		if (moveTimer > MOVE_TIME) {
			moveTimer = 0;
			makeNewMove();
		}
		position.x += velocity.x * CUtil.ElapsedTime * MOVE_SPEED
				* speedPenalty;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		if (isColliding()) {
			position.x = oldX;
		}
		position.y += velocity.y * CUtil.ElapsedTime * MOVE_SPEED
				* speedPenalty;
		drawable.mSprite.setPositionRounded(position.x, position.y);
		if (isColliding()) {
			position.y = oldY;
		}
	}

	@Override
	public void takeDamage(Projectile p) {
		super.takeDamage(p);
		health -= p.baseDamage;
		if (health <= 0) {
			SheetedSprite s = new SheetedSprite(CUtil.QuickImage("crabcarp"),
					8, 300);
			new DeathAnimation(parentScreen, s, position);
			this.markForDeletion();
		}
	}

	@Override
	public void removeEntity() {
		super.removeEntity();
		for (CrabArmature arm : armatures) {
			arm.removeArmature();
		}
	}

}
