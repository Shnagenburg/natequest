package GameClasses;

import javax.swing.text.Position;

import org.lwjgl.opengl.HPOcclusionTest;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.FastTrig;

import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.ActionItems.LevelTransition;
import NateQuest.Gameplay.Camera;
import NateQuest.HUD.HealthBar;
import NateQuest.Story.PersistantEntities;
import NateQuest.Tiling.Tiles.Tile;
import NateQuest.Weapons.Armaments;

public class ScreenShifter {

	final float FADE_SPEED = 3.0f;

	public enum ShiftType {
		MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN, FADE_LEFT, FADE_RIGHT, FADE_UP, FADE_DOWN

	}

	ShiftType style;

	int shiftDir;
	float offset = 0;
	float timer = 0;
	GameScreen oldScreen, newScreen;
	int totalShift;
	float shiftSoFar;
	LevelTransition src;
	LevelTransition trg;

	boolean movedPersistants = false;
	boolean cameraPannedToStart = false;
	boolean fadeHalfWay = false;
	Vector2f fadePosition;
	Sprite fadeSprite;

	public ScreenShifter(LevelTransition source, LevelTransition target) {
		src = source;
		trg = target;
		style = source.style;
		oldScreen = source.parentScreen;
		oldScreen.camera.bIgnoreMaxes = true;
		oldScreen.camera.sensitivity = 4.0f;

		newScreen = target.parentScreen;
		newScreen.camera.bIgnoreMaxes = true;
		newScreen.camera.sensitivity = 4.0f;

		shiftSoFar = 0;

		switch (style) {
		case FADE_DOWN:
		case FADE_UP:
		case FADE_RIGHT:
		case FADE_LEFT:
			setUpFade(source, target);
			break;
		case MOVE_DOWN:
		case MOVE_LEFT:
		case MOVE_RIGHT:
		case MOVE_UP:
			setUpShift(source, target);
			break;

		}

		oldScreen.setSleepingAll(true);
		newScreen.setSleepingAll(true);

		/*
		 * Debugger.print("src cam y: " + oldScreen.camera.yOffset +
		 * " src loc y: " + source.position.y + "targ cam y: " +
		 * newScreen.camera.yOffset + " targ loc y: " + target.position.y);
		 * Debugger.print("" + newScreen + "  " + oldScreen); Debugger.print(""
		 * + newScreen.camera + "  " + oldScreen.camera); Debugger.print("" +
		 * -(target.position.x + CUtil.Dimensions.width));
		 */
	}

	private void setUpShift(LevelTransition source, LevelTransition target) {

		switch (style) {
		case MOVE_UP:
		case MOVE_LEFT:
			shiftDir = -1;
			break;
		case MOVE_RIGHT:
		case MOVE_DOWN:
			shiftDir = 1;
			break;
		}

		switch (style) {
		case MOVE_LEFT:
		case MOVE_RIGHT:
			totalShift = CUtil.Dimensions.width;
			break;
		case MOVE_UP:
		case MOVE_DOWN:
			totalShift = CUtil.Dimensions.height;
			break;
		}

		float dist = 0;

		switch (style) {
		case MOVE_LEFT:
		case MOVE_RIGHT:
			dist = source.position.y - target.position.y;
			dist = dist + oldScreen.camera.yOffset;
			break;
		case MOVE_UP:
		case MOVE_DOWN:
			dist = source.position.x - target.position.x;
			dist = dist + oldScreen.camera.xOffset;
			break;
		}

		switch (style) {
		case MOVE_LEFT:
			oldScreen.camera.jumpToPosition((int) (oldScreen.camera.xMin),
					(int) oldScreen.camera.yOffset);
			newScreen.camera.jumpToPosition(
					-(int) (newScreen.camera.xMax + CUtil.Dimensions.width),
					(int) dist);
			break;
		case MOVE_RIGHT:
			oldScreen.camera.jumpToPosition(-(int) (oldScreen.camera.xMax),
					(int) oldScreen.camera.yOffset);
			newScreen.camera.jumpToPosition(
					(int) ((newScreen.camera.xMin) + CUtil.Dimensions.width),
					(int) dist);
			break;
		case MOVE_UP:
			oldScreen.camera.jumpToPosition((int) oldScreen.camera.xOffset,
					(int) (oldScreen.camera.yMin));
			newScreen.camera.jumpToPosition((int) dist,
					-(int) (newScreen.camera.yMax + CUtil.Dimensions.height));
			break;
		case MOVE_DOWN:
			oldScreen.camera.jumpToPosition((int) oldScreen.camera.xOffset,
					-(int) (oldScreen.camera.yMax));
			newScreen.camera.jumpToPosition((int) dist,
					(int) ((oldScreen.camera.yMin) + CUtil.Dimensions.height));
			break;
		}
	}

	private void setUpFade(LevelTransition source, LevelTransition target) {
		switch (style) {
		case FADE_LEFT:
		case FADE_UP:
			shiftDir = -1;
			break;
		case FADE_DOWN:
		case FADE_RIGHT:
			shiftDir = 1;
			break;
		}
		float dist = 0;
		switch (style) {
		case FADE_LEFT:
		case FADE_RIGHT:
			dist = source.position.y - target.position.y;
			dist = dist + oldScreen.camera.yOffset;
			break;
		case FADE_DOWN:
		case FADE_UP:
			dist = source.position.x - target.position.x;
			dist = dist + oldScreen.camera.xOffset;
			break;
		}

		switch (style) {
		case FADE_RIGHT:
			// oldScreen.camera.jumpToPosition(-(int)(oldScreen.camera.xMax),
			// (int)oldScreen.camera.yOffset);
			newScreen.camera.jumpToPosition((int) ((newScreen.camera.xMin)),
					(int) dist);
			fadeSprite = new Sprite(CUtil.QuickImage("@horzfade"));
			break;
		case FADE_LEFT:
			// oldScreen.camera.jumpToPosition((int)(oldScreen.camera.xMin),
			// (int)oldScreen.camera.yOffset);
			newScreen.camera.jumpToPosition(-(int) (newScreen.camera.xMax),
					(int) dist);
			fadeSprite = new Sprite(CUtil.QuickImage("@horzfade"));
			break;
		case FADE_DOWN:
			// oldScreen.camera.jumpToPosition((int)oldScreen.camera.xOffset,
			// -(int)(oldScreen.camera.yMax));
			newScreen.camera.jumpToPosition((int) dist,
					(int) ((oldScreen.camera.yMin)));
			fadeSprite = new Sprite(CUtil.QuickImage("@vertfade"));
			break;
		case FADE_UP:
			// oldScreen.camera.jumpToPosition((int)oldScreen.camera.xOffset,
			// (int)(oldScreen.camera.yMin) );
			newScreen.camera.jumpToPosition((int) dist,
					-(int) (newScreen.camera.yMax));
			fadeSprite = new Sprite(CUtil.QuickImage("@vertfade"));
			break;
		}
		fadeSprite.bIsCameraed = false;
		switch (style) {
		case FADE_LEFT:
			fadePosition = new Vector2f(CUtil.Dimensions.width
					+ (fadeSprite.getWidth() / 2), CUtil.Dimensions.height / 2);
			fadeSprite.setPositionRounded(fadePosition.x, fadePosition.y);
			break;
		case FADE_RIGHT:
			fadePosition = new Vector2f(-(fadeSprite.getWidth() / 2),
					CUtil.Dimensions.height / 2);
			fadeSprite.setPositionRounded(fadePosition.x, fadePosition.y);
			break;
		case FADE_UP:
			fadePosition = new Vector2f(CUtil.Dimensions.width / 2,
					CUtil.Dimensions.height + (fadeSprite.getHeight() / 2));
			fadeSprite.setPositionRounded(fadePosition.x, fadePosition.y);
			break;
		case FADE_DOWN:
			fadePosition = new Vector2f(CUtil.Dimensions.width / 2,
					-(fadeSprite.getHeight() / 2));
			fadeSprite.setPositionRounded(fadePosition.x, fadePosition.y);
			break;
		}

		switch (style) {
		case FADE_LEFT:
		case FADE_RIGHT:
			totalShift = fadeSprite.getWidth() * 2;
			break;
		case FADE_DOWN:
		case FADE_UP:
			totalShift = fadeSprite.getHeight() * 2;
			break;
		}
	}

	public void update() {
		if (!cameraPannedToStart) {
			oldScreen.camera.sensitivity = 1;
			cameraPannedToStart = oldScreen.camera.updatePanToPoint(
					src.position.x, src.position.y);
			// cameraPannedToStart = true;
			return;
		}
		oldScreen.camera.sensitivity = 4;
		switch (style) {
		case MOVE_DOWN:
		case MOVE_UP:
		case MOVE_LEFT:
		case MOVE_RIGHT:
			updateMove();
			break;
		case FADE_LEFT:
		case FADE_RIGHT:
		case FADE_DOWN:
		case FADE_UP:
			updateFade();
			break;

		}
	}

	public void updateMove() {

		if (shiftSoFar < totalShift) {
			float s1, s2;
			switch (style) {
			case MOVE_RIGHT:
			case MOVE_LEFT:
				s1 = oldScreen.camera.xOffset;
				oldScreen.camera.shiftX(50 * shiftDir);
				s2 = oldScreen.camera.xOffset;
				shiftSoFar += Math.abs(s1 - s2);
				newScreen.camera.shiftX(50 * shiftDir);
				break;
			case MOVE_UP:
			case MOVE_DOWN:
				s1 = oldScreen.camera.yOffset;
				oldScreen.camera.shiftY(50 * shiftDir);
				s2 = oldScreen.camera.yOffset;
				shiftSoFar += Math.abs(s1 - s2);
				newScreen.camera.shiftY(50 * shiftDir);
				break;
			}

		} else {
			deactivate();
		}
		// oldScreen.update();
		// newScreen.update();
	}

	public void updateFade() {
		if (shiftSoFar < totalShift) {
			// Debugger.print(shiftSoFar + " " + totalShift + " " + totalShift /
			// 2.66f + " " + fadePosition.x);
			shiftSoFar += FADE_SPEED * CUtil.ElapsedTime;

			switch (style) {
			case FADE_RIGHT:
			case FADE_LEFT:
				fadePosition.x += FADE_SPEED * CUtil.ElapsedTime * shiftDir;
				break;
			case FADE_UP:
			case FADE_DOWN:
				fadePosition.y += FADE_SPEED * CUtil.ElapsedTime * shiftDir;
				break;
			}

			if (shiftSoFar * 2.666f > totalShift) {
				halfwayFadePoint();
			}
		} else {
			Debugger.print("done with transition");
			deactivate();
		}
		fadeSprite.setPositionRounded(fadePosition.x, fadePosition.y);

	}

	private void halfwayFadePoint() {
		fadeHalfWay = true;
	}

	public void draw(Graphics g) {
		switch (style) {
		case FADE_RIGHT:
		case FADE_LEFT:
		case FADE_DOWN:
		case FADE_UP:
			if (fadeHalfWay)
				newScreen.draw(g);
			else
				oldScreen.draw(g);
			fadeSprite.draw(g);
			break;
		case MOVE_DOWN:
		case MOVE_UP:
		case MOVE_LEFT:
		case MOVE_RIGHT:
			newScreen.draw(g);
			oldScreen.draw(g);
			break;
		}
	}

	public void deactivate() {
		CUtil.GameHandle.currentLevel = newScreen;
		oldScreen.camera.bIgnoreMaxes = false;
		newScreen.camera.bIgnoreMaxes = false;
		oldScreen.camera.sensitivity = 1.0f;
		newScreen.camera.sensitivity = 1.0f;
		oldScreen.setSleepingAll(false);
		newScreen.setSleepingAll(false);

		CUtil.GameHandle.shifter = null;
		if (oldScreen.editorController != null
				&& oldScreen.editorController.editor.isPlayingLevel) {
			if (newScreen.editorController != null) {
				newScreen.editorController.editor.playLevel(false);
			}
		}
		Armaments arms = (Armaments) newScreen.findEntity(Armaments.class);
		arms.resetSprites();
		HealthBar bar = (HealthBar) newScreen.findEntity(HealthBar.class);
		bar.setHealthbar();
	}

}
