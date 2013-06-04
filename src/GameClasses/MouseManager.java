package GameClasses;

import java.awt.event.MouseEvent;
import org.newdawn.slick.Input;

import NateQuest.Debugger;
import NateQuest.Gameplay.Camera;

public class MouseManager {
	public static int iX;
	public static int iY;
	static boolean bLeftClicked;
	static boolean bRightClicked;
	static boolean bPrevLeftClicked;
	static boolean bPrevRightClicked;

	static void initialize() {
		iX = 0;
		iY = 0;
		bLeftClicked = false;
	}

	public static int getCameredX() {
		Camera camera = CUtil.CurrentGame.camera;
		int x = (int) ((iX) - (camera.getZoom() * camera.xRounded));
		int result = (int) (x / camera.getZoom());
		return result;
	}

	public static int getCameredY() {
		Camera camera = CUtil.CurrentGame.camera;
		int y = (int) ((iY) - (camera.getZoom() * camera.yRounded));
		int result = (int) (y / camera.getZoom());
		return result;
	}

	static void HandleSlickInput(Input input) {
		bPrevLeftClicked = bLeftClicked;
		bPrevRightClicked = bRightClicked;
		iX = input.getMouseX();
		iY = input.getMouseY();

		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			bLeftClicked = true;
		} else if (!input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			bLeftClicked = false;
		}

		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)) {
			bRightClicked = true;
		} else {
			bRightClicked = false;
		}

	}

	public static void HandleMouseClickDown(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			bLeftClicked = true;
		} else {
			bRightClicked = true;
		}

		iX = e.getX();
		iY = e.getY();
	}

	public static void HandleMouseClickRelease(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			bLeftClicked = false;
		} else {
			bRightClicked = false;
		}

		iX = e.getX();
		iY = e.getY();
	}

	public static boolean getLeftClicked() {
		return bLeftClicked;
	}

	public static void setLeftClicked(boolean value) {
		bLeftClicked = value;
	}

	public static boolean getRightClicked() {
		return bRightClicked;
	}

	public static boolean getPrevLeftClicked() {
		return bPrevLeftClicked;
	}

	public static boolean getPrevRightClicked() {
		return bPrevRightClicked;
	}

	public static boolean getUpDownLeftMouseClick() {
		return !bPrevLeftClicked && bLeftClicked ? true : false;
	}

	public static boolean getUpDownRightMouseClick() {
		return !bPrevRightClicked && bRightClicked ? true : false;
	}

	public static boolean getDownUpLeftMouseClick() {
		return bPrevLeftClicked && !bLeftClicked;
	}

	public static boolean getDownUpRightMouseClick() {
		return bPrevRightClicked && !bRightClicked;
	}

}
