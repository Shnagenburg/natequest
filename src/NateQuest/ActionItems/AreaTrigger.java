package NateQuest.ActionItems;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.Collidables.Collidable;
import NateQuest.Collidables.CollidableMasks;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.LayerConstants;
import NateQuest.Gameplay.Hero;

public class AreaTrigger extends Actionable {

	final String[] OPS = { "isOn", "enter", "exit" };
	AreaTriggerCorner topLeft, bottomRight;
	AreaTriggerCorner[] corners;
	Rectangle rectangle = new Rectangle(0, 0, 0, 0);
	Hero hero;
	public float tlOffX = 0;
	public float tlOffY = 0;
	public float brOffX = 0;
	public float brOffY = 0;
	boolean firstSetup = false;

	public AreaTrigger() {
		this(CUtil.ScreenGettingLoaded);
	}

	public AreaTrigger(GameScreen gs) {
		super(gs);

		addOps(OPS);
		topLeft = new AreaTriggerCorner(this);
		bottomRight = new AreaTriggerCorner(this);
		corners = new AreaTriggerCorner[2];
		corners[0] = topLeft;
		corners[1] = bottomRight;
		topLeft.position = new Vector2f(-50, -50);
		bottomRight.position = new Vector2f(50, 50);
		collidable = new Collidable(this);
		collidable.removeProperty();
	}

	public void update() {
		getHero();
		runFirstSetup();
		if (hero.collidable.isColliding(collidable)) {
			playActions(0);
		}
	}

	private void runFirstSetup() {
		if (!firstSetup) {
			firstSetup = true;
			// Debugger.print("first setup : " + tlOffX + " " + tlOffY);
			topLeft.setPosition(position.x - tlOffX, position.y - tlOffY);
			bottomRight.setPosition(position.x - brOffX, position.y - brOffY);
		}
	}

	public void setPosition(float x, float y) {
		float[] xOffsets = new float[2];
		float[] yOffsets = new float[2];
		for (int i = 0; i < corners.length; i++) {
			xOffsets[i] = corners[i].getXOffset();
			yOffsets[i] = corners[i].getYOffset();
		}
		position.x = x;
		position.y = y;
		rectangle.setCenterX(x);
		rectangle.setCenterY(y);
		for (int i = 0; i < corners.length; i++) {
			corners[i].shiftViaParent(x - xOffsets[i], y - yOffsets[i]);
		}
		anchorViaCorners();
		reMakeSprite();
		collidable.matchSprite(drawable.mSprite);
	}

	public void anchorTrigger() {
		anchorViaCorners();
		reMakeSprite();
	}

	private void anchorViaCorners() {
		int width = (int) (bottomRight.position.x - topLeft.position.x);
		int height = (int) (bottomRight.position.y - topLeft.position.y);
		rectangle.setWidth(width);
		rectangle.setHeight(height);
		rectangle.setCenterX((bottomRight.position.x + topLeft.position.x) / 2);
		rectangle.setCenterY((bottomRight.position.y + topLeft.position.y) / 2);
		tlOffX = position.x - topLeft.position.x;
		tlOffY = position.y - topLeft.position.y;
		brOffX = position.x - bottomRight.position.x;
		brOffY = position.y - bottomRight.position.y;

	}

	private void reMakeSprite() {
		if (drawable != null) {
			drawable.removeProperty();
		}
		int width = (int) rectangle.getWidth();
		int height = (int) rectangle.getHeight();
		boolean isInvis = true;
		if (drawable != null) {
			isInvis = drawable.bIgnore;
		}
		Sprite block = new Sprite(ImageCreator.createImage(width, height, 122,
				122, 122, 222));
		drawable = new Drawable(this, block, LayerConstants.CHARACTERS);
		drawable.setPositionRounded(rectangle.getCenterX(),
				rectangle.getCenterY());
		collidable.matchSprite(drawable.mSprite);
		drawable.bIgnore = isInvis;
	}

	public void getHero() {
		if (hero == null) {
			hero = (Hero) parentScreen.findEntity(Hero.class);
		}
	}

	@Override
	public void markForDeletion() {
		super.markForDeletion();
		if (corners != null) {
			for (AreaTriggerCorner corner : corners) {
				if (corner != null) {
					corner.markForDeletion();
				}
			}
		}
	}

	@Override
	public void removeEntity() {
		super.removeEntity();
		if (corners != null) {
			for (AreaTriggerCorner corner : corners) {
				if (corner != null) {
					corner.removeEntity();
				}
			}
		}
	}

}
