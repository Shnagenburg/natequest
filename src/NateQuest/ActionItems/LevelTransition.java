package NateQuest.ActionItems;

import GameClasses.CUtil;
import GameClasses.SheetedSprite;
import GameClasses.Sprite;
import GameClasses.ScreenShifter.ShiftType;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Enemies.Trooper;
import NateQuest.Gameplay.Hero;
import NateQuest.HUD.HealthBar;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Weapons.Armaments;

public class LevelTransition extends Actionable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -264062070574605299L;
	final String[] OPS = { "unlock", "lock" };
	public int sourceID = -1;
	public int targetID = -1;
	public String targetName = "needs target";
	boolean bTouchUnlocked; // this is just for not hitting a door over and over
	public boolean isLocked = false;
	int counter = 0;
	boolean runningTransition;
	public ShiftType style = ShiftType.MOVE_UP;
	Hero hero;

	public LevelTransition() {
		this(CUtil.ScreenGettingLoaded);
	}

	public LevelTransition(GameScreen gs) {
		super(gs);
		addOps(OPS);
		bTouchUnlocked = false;
		Sprite s = new SheetedSprite(CUtil.QuickImage("door4wip"), 2, 300);
		drawable = new Drawable(this, s, LayerConstants.CHARACTERS);
		parentScreen.levelTransitions.add(this);
		runningTransition = false;
		collidable = new Collidable(this);
		collidable.matchSprite(drawable.mSprite);
		collidable.mask = CollidableMasks.EVENT;
		updateLockState();
	}

	public void update() {
		updateLockState();
		if (hero == null) {
			hero = (Hero) parentScreen.findEntity(Hero.class);
			if (hero == null) {
				Debugger.print("couldnt find hero!");
			}
		}
		if (hero.collidable.isColliding(collidable) && bTouchUnlocked
				&& !isLocked) {
			transitionToLevel();
			bTouchUnlocked = false;
		} else if (!hero.collidable.isColliding(collidable)) {
			bTouchUnlocked = true;
		}
	}

	public void updateLockState() {
		if (isLocked) {
			drawable.mSprite.lock(0);
		} else {
			drawable.mSprite.lock(1);
		}
	}

	public void transitionToLevel() {
		GameScreen target = CUtil.LevelPool.getLevelByName(targetName);
		CUtil.CurrentGame = target;
		CUtil.GameHandle.currentLevel = target;
		LevelTransition newLoc = target.findTransition(targetID);
		// Debugger.print("src trans: " + this + "  trg trans " + newLoc);
		// Debugger.print("New hero loc: " + newLoc.position + " old loc " +
		// this.position);
		target.hero.setPosition(newLoc.position.x, newLoc.position.y);
		// parentScreen.hero.setPosition(newLoc.position.x, newLoc.position.y);
		newLoc.bTouchUnlocked = false;
		this.bTouchUnlocked = false;

		runningTransition = true;
		CUtil.GameHandle.CreateScreenShiter(this, newLoc);
		newLoc.parentScreen.reblendMaps();
		// Debugger.print(" transitioned " + this + " " + sourceID + " " +
		// targetID + " " + targetName );

		target.hero.copyStatusFromOtherHero(parentScreen.hero);
		Armaments trgArms = (Armaments) target.findEntity(Armaments.class);
		Armaments srcArms = (Armaments) parentScreen
				.findEntity(Armaments.class);

		try {
			trgArms.copyStatusFromOtherArms(srcArms);
		} catch (Exception e) {
			// TODO: handle exception
		}
		trgArms.resetSprites();
		HealthBar hp = (HealthBar) target.findEntity(HealthBar.class);
		hp.setHealthbar();

	}

	@Override
	public void setPosition(float x, float y) {
		int tsize = Tile.TILE_SIZE;
		x = x - (x % tsize);
		y = y - y % tsize;

		super.setPosition(x, y);
		collidable.matchSprite(drawable.mSprite);
	}

	@Override
	public void action(int actionID, Entity caller) {
		if (actionID == 0) {
			drawable.mSprite.lock(1);
			isLocked = false;
		} else if (actionID == 1) {
			drawable.mSprite.lock(0);
			isLocked = true;
		}
	}

	private void setSleepingAll(boolean isSleeping) {
		for (Entity e : parentScreen.entities) {
			if (e != this) {
				e.bIsSleeping = isSleeping;
			}
		}
	}

	public void setPosition(int x, int y) {
		position.x = x;
		position.y = y;
		drawable.mSprite.setPosition(x, y);
		collidable.matchSprite(drawable.mSprite);
	}

	public String toString() {
		return "Trans: Src: " + sourceID + " Src Lvl: "
				+ parentScreen.levelName + " Trg: " + targetID + " TrgLvl: "
				+ targetName;
	}

	@Override
	public void removeEntity() {
		super.removeEntity();
		parentScreen.levelTransitions.remove(this);
	}

}
