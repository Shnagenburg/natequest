package NateQuest.Story;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.MouseManager;
import GameClasses.SoundManager;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.DrawableWithText;
import NateQuest.Drawables.LayerConstants;
import UserInterface.DanButton;

public class MainMenu extends GameScreen {

	DanButton startButton;
	ScrollingImage titlebg, titleText, fadeImage;
	boolean isTextFadingIn = false;
	boolean isTextDoneFading = false;
	boolean startedGame = false;
	Entity clickText;
	float clickTimer = 0;
	float MAX_CLICK_DELAY = 3000;

	public MainMenu() {
		entities = new ArrayList<Entity>();
		drawables = new ArrayList<Drawable>();
		titlebg = new ScrollingImage(this);
		titlebg.setImage("titlescreen");
		titlebg.setPosition(CUtil.Dimensions.width / 2, 600);
		titlebg.setDestination(CUtil.Dimensions.width / 2, 200);
		// SoundManager.playMusic(SoundManager.town, 0.3f);
		
	}

	public void update() {
		super.update();

		checkSkips();

		if (titlebg.isAtDestination() && !isTextFadingIn) {
			isTextFadingIn = true;
			makeTitleText();
		}
		if (titleText != null && titleText.fader != null
				&& !titleText.fader.isFading) {
			isTextDoneFading = true;
		}
		if (isTextDoneFading && MouseManager.getUpDownLeftMouseClick()) {
			if (!startedGame) {
				startGame();
			}
		}
		if (isTextDoneFading && clickText == null && !startedGame) {
			clickDelay();
		}
		if (startedGame) {
			waitForScreenFade();
		}
	}

	private void checkSkips() {
		if (MouseManager.getUpDownLeftMouseClick()) {
			if (!isTextFadingIn) {
				titlebg.jumpToDestination();
			} else if (!isTextDoneFading) {
				titleText.fadeIn(0);
			}
		}
	}

	private void makeTitleText() {
		titleText = new ScrollingImage(this);
		titleText.setImage("titletext2");
		titleText.setPositionAndDestination(750, 200);
		titleText.fadeIn(5000);
	}

	private void clickDelay() {
		clickTimer += CUtil.ElapsedTime;
		if (clickTimer > MAX_CLICK_DELAY) {
			makeClickText();
		}
	}

	private void makeClickText() {
		clickText = new Entity(this);
		DrawableWithText dwt = new DrawableWithText(clickText, null,
				LayerConstants.TILEMAP_OVERLAY, "Click to begin your legend");
		dwt.setTextColor(Color.white);
		clickText.drawable = dwt;
		clickText.setPosition(40, 660);
	}

	private void startGame() {
		startedGame = true;
		if (clickText != null) {
			clickText.markForDeletion();
		}

		fadeImage = new ScrollingImage(this);
		fadeImage.setImage("@vertfade");
		fadeImage.setPosition(CUtil.Dimensions.width / 2,
				4 * CUtil.Dimensions.height / 2);
		fadeImage.setDestination(CUtil.Dimensions.width / 2,
				CUtil.Dimensions.height / 2);
		fadeImage.scrollSpeed = 0.3f;

		GameScreen firstLevel = CUtil.LevelPool.getLevelByName("a-1");
		ScrollingImage other = new ScrollingImage(firstLevel);
		other.setImage("@vertfade");
		other.drawable.setIsCamerad(false);
		other.setPosition(CUtil.Dimensions.width / 2,
				CUtil.Dimensions.height / 2);
		other.setDestination(CUtil.Dimensions.width / 2,
				-CUtil.Dimensions.height);
		other.scrollSpeed = 1.0f;
	}

	private void waitForScreenFade() {
		if (fadeImage.isAtDestination()) {
			CUtil.GameHandle.startGame();
		}
	}
}
