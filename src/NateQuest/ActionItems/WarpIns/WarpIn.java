package NateQuest.ActionItems.WarpIns;

import GameClasses.CUtil;
import GameClasses.SoundManager;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableCircle;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Enemies.Trooper;
import NateQuest.VFX.VisualFX;
import NateQuest.VFX.WarpEffect;

public class WarpIn extends Entity {

	private float delay = 0;
	private float timer = 0;
	private boolean markedToWarp = false;
	private boolean isWarpingIn = false;
	public int warpGroup = 0;
	WarpEffect warpEffects = null;
	Class entityType = Trooper.class;

	public WarpIn() {
		this(CUtil.ScreenGettingLoaded);
	}

	public WarpIn(GameScreen gs) {
		super(gs);
		setEntityType();
		init();
		collidable = new CollidableCircle(this, 16);
		collidable.mask = CollidableMasks.EDITOR;
	}

	protected void setEntityType() {
	}

	protected void init() {
		this.drawable = new DrawableWithText(this, new Sprite("warpicon"),
				LayerConstants.TILEMAP_OVERLAY, "\n"
						+ entityType.getSimpleName());
		drawable.setIsCamerad(true);
	}

	public void update() {
		if (isWarpingIn) {
			if (warpEffects == null) {
				createWarpEffect();
			}
			if (warpEffects.isDoneAnimating) {
				spawnEntity();

				warpEffects.markForDeletion();
				this.markForDeletion();
			}
		}
		if (markedToWarp) {
			timer += CUtil.ElapsedTime;
			if (timer > delay) {
				startWarping();
				markedToWarp = false;
			}
		}
	}

	public void startWarping() {
		SoundManager.playSound(SoundManager.warpin);
		isWarpingIn = true;
	}

	public void setWarpingAfterTime(float delay) {
		this.delay = delay;
		markedToWarp = true;
	}

	private void createWarpEffect() {
		Sprite sprite = new Sprite("spawntwist");
		warpEffects = new WarpEffect(parentScreen, sprite, position);
	}

	private void spawnEntity() {
		Entity e = null;
		CUtil.ScreenGettingLoaded = parentScreen;
		try {
			e = (Entity) entityType.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (e != null) {
			e.setPosition(position.x, position.y);
		}
		Debugger.peek(e);
	}

}
