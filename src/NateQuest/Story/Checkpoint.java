package NateQuest.Story;

import GameClasses.CUtil;
import GameClasses.Sprite;
import GameClasses.ScreenShifter.ShiftType;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameProgression;
import NateQuest.GameScreen;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;
import NateQuest.HUD.HealthBar;
import NateQuest.VFX.RisingText;

public class Checkpoint extends Entity {

	Hero targetHero;

	public Checkpoint() {
		this(CUtil.ScreenGettingLoaded);
	}

	public Checkpoint(GameScreen gs) {
		super(gs);
		drawable = new Drawable(this, new Sprite("checkpoint"),
				LayerConstants.SUB_CHARACTERS);
		collidable = new CollidableCircle(this,
				(int) (drawable.mSprite.getHeight() * 0.75f));
		collidable.mask = CollidableMasks.EVENT;
	}

	public void update() {
		getHero();
		fullHeal();
	}

	private void getHero() {
		if (targetHero == null) {
			targetHero = (Hero) parentScreen.findEntity(Hero.class);
		}
	}

	private void fullHeal() {
		if (targetHero != null && targetHero.collidable.isColliding(collidable)) {
			HealthBar hp = (HealthBar) parentScreen.findEntity(HealthBar.class);
			hp.fullHeal();
			GameProgression.LastCheckpoint = this;
		}
	}

	public void revertToCheckpoint(Hero deadHero) {
		LevelTransition source = new LevelTransition(deadHero.parentScreen);
		source.sourceID = 999;
		source.targetID = 999;
		source.targetName = parentScreen.levelName;
		source.style = ShiftType.FADE_UP;
		source.setPosition(deadHero.position.x, deadHero.position.y);

		LevelTransition target = new LevelTransition(parentScreen);
		target.sourceID = 999;
		target.targetID = 999;
		target.isLocked = true;
		target.targetName = "no target";
		target.setPosition(position.x, position.y);
		source.transitionToLevel();
		source.markForDeletion();
		target.markForDeletion();

		fullHeal();
		RisingText text = new RisingText(parentScreen, position,
				"Never say die!");
	}

}
