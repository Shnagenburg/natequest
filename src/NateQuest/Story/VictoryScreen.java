package NateQuest.Story;

import java.util.ArrayList;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.Drawables.Drawable;
import NateQuest.VFX.DamageNumber;
import UserInterface.DanButton;
import VisualFX.DanText;
import VisualFX.SimpleText;

public class VictoryScreen extends GameScreen {

	DanButton button;

	public VictoryScreen() {
		entities = new ArrayList<Entity>();
		drawables = new ArrayList<Drawable>();

		button = new DanButton(this, new Vector2f(CUtil.Dimensions.width / 2,
				CUtil.Dimensions.height / 2), 400, 150,
				"A winner is you!!!!! Restart?");
	}

	@Override
	public void update() {
		super.update();
		CUtil.SlickGameContainer.setMouseGrabbed(false);

		if (button.isHit) {
			CUtil.Harness.restartGame();
		}
	}
}
