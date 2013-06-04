package GameClasses;

import java.applet.*;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.event.*;
import java.sql.Struct;

import org.newdawn.slick.Input;

import NateQuest.Debugger;

// This class will tell us what is going on with the keyboard
public class KeyManager {

	public static boolean isWdown;
	public static boolean isSdown;
	public static boolean isAdown;
	public static boolean isDdown;
	public static boolean isSpaceDown;
	public static Input currentState, prevState;
	private static boolean[] curStats = new boolean[255];
	private static boolean[] prevStats = new boolean[255];

	private static boolean hitW, hitS, hitA, hitD, hitSpace, hitEsc, hit1,
			hitEnter;

	public KeyManager() {
	}

	static public void initialize() {
		isWdown = false;
		isSdown = false;
		isAdown = false;
		isDdown = false;

		hitW = false;
		hitS = false;
		hitA = false;
		hitD = false;
		hitSpace = false;
		hitEsc = false;
		hit1 = false;
		hitEnter = false;

	}

	static void CopyKeys(Input curr, boolean[] loc) {
		loc[Input.KEY_0] = curr.isKeyDown(Input.KEY_0);
		loc[Input.KEY_1] = curr.isKeyDown(Input.KEY_1);
		loc[Input.KEY_2] = curr.isKeyDown(Input.KEY_2);
		loc[Input.KEY_3] = curr.isKeyDown(Input.KEY_3);
		loc[Input.KEY_4] = curr.isKeyDown(Input.KEY_4);
		loc[Input.KEY_5] = curr.isKeyDown(Input.KEY_5);
		loc[Input.KEY_6] = curr.isKeyDown(Input.KEY_6);
		loc[Input.KEY_7] = curr.isKeyDown(Input.KEY_7);
		loc[Input.KEY_8] = curr.isKeyDown(Input.KEY_8);
		loc[Input.KEY_9] = curr.isKeyDown(Input.KEY_9);
		loc[Input.KEY_MINUS] = curr.isKeyDown(Input.KEY_MINUS);
		loc[Input.KEY_EQUALS] = curr.isKeyDown(Input.KEY_EQUALS);
		loc[Input.KEY_W] = curr.isKeyDown(Input.KEY_W);
		loc[Input.KEY_A] = curr.isKeyDown(Input.KEY_A);
		loc[Input.KEY_B] = curr.isKeyDown(Input.KEY_B);
		loc[Input.KEY_E] = curr.isKeyDown(Input.KEY_E);
		loc[Input.KEY_F] = curr.isKeyDown(Input.KEY_F);
		loc[Input.KEY_S] = curr.isKeyDown(Input.KEY_S);
		loc[Input.KEY_R] = curr.isKeyDown(Input.KEY_R);
		loc[Input.KEY_U] = curr.isKeyDown(Input.KEY_U);
		loc[Input.KEY_T] = curr.isKeyDown(Input.KEY_T);
		loc[Input.KEY_I] = curr.isKeyDown(Input.KEY_I);
		loc[Input.KEY_X] = curr.isKeyDown(Input.KEY_X);
		loc[Input.KEY_Y] = curr.isKeyDown(Input.KEY_Y);
		loc[Input.KEY_D] = curr.isKeyDown(Input.KEY_D);
		loc[Input.KEY_ESCAPE] = curr.isKeyDown(Input.KEY_ESCAPE);
		loc[Input.KEY_SPACE] = curr.isKeyDown(Input.KEY_SPACE);
		loc[Input.KEY_ENTER] = curr.isKeyDown(Input.KEY_ENTER);
	}

	static void CopyKeys() {
		/*
		 * if (old == null) { Debugger.print("asdasd");
		 * 
		 * curStats = new boolean[255]; prevStats = new boolean[255];
		 * 
		 * } if (prev == null) { Debugger.print("asdasdxxxxxxxxx");
		 * 
		 * curStats = new boolean[255]; prevStats = new boolean[255]; }
		 */

		prevStats[Input.KEY_0] = curStats[Input.KEY_0];
		prevStats[Input.KEY_1] = curStats[Input.KEY_1];
		prevStats[Input.KEY_2] = curStats[Input.KEY_2];
		prevStats[Input.KEY_3] = curStats[Input.KEY_3];
		prevStats[Input.KEY_4] = curStats[Input.KEY_4];
		prevStats[Input.KEY_5] = curStats[Input.KEY_5];
		prevStats[Input.KEY_6] = curStats[Input.KEY_6];
		prevStats[Input.KEY_7] = curStats[Input.KEY_7];
		prevStats[Input.KEY_8] = curStats[Input.KEY_8];
		prevStats[Input.KEY_9] = curStats[Input.KEY_9];
		prevStats[Input.KEY_MINUS] = curStats[Input.KEY_MINUS];
		prevStats[Input.KEY_EQUALS] = curStats[Input.KEY_EQUALS];
		prevStats[Input.KEY_W] = curStats[Input.KEY_W];
		prevStats[Input.KEY_A] = curStats[Input.KEY_A];
		prevStats[Input.KEY_B] = curStats[Input.KEY_B];
		prevStats[Input.KEY_E] = curStats[Input.KEY_E];
		prevStats[Input.KEY_F] = curStats[Input.KEY_F];
		prevStats[Input.KEY_U] = curStats[Input.KEY_U];
		prevStats[Input.KEY_S] = curStats[Input.KEY_S];
		prevStats[Input.KEY_D] = curStats[Input.KEY_D];
		prevStats[Input.KEY_R] = curStats[Input.KEY_R];
		prevStats[Input.KEY_T] = curStats[Input.KEY_T];
		prevStats[Input.KEY_I] = curStats[Input.KEY_I];
		prevStats[Input.KEY_X] = curStats[Input.KEY_X];
		prevStats[Input.KEY_Y] = curStats[Input.KEY_Y];
		prevStats[Input.KEY_ESCAPE] = curStats[Input.KEY_ESCAPE];
		prevStats[Input.KEY_SPACE] = curStats[Input.KEY_SPACE];
		prevStats[Input.KEY_ENTER] = curStats[Input.KEY_ENTER];
	}

	static void HandleSlickInput(Input input) {
		CopyKeys();
		CopyKeys(input, curStats);
		currentState = input;

		// Key pressed = one time hit,
		// key down = being held down

		if (currentState.isKeyPressed(Input.KEY_ESCAPE)) {
			hitEsc = true;
		} else if (!currentState.isKeyPressed(Input.KEY_ESCAPE)) {
			hitEsc = false;
		}
		if (currentState.isKeyPressed(Input.KEY_ENTER)) {
			hitEnter = true;
		} else if (!currentState.isKeyPressed(Input.KEY_ENTER)) {
			hitEnter = false;
		}

		if (currentState.isKeyPressed(Input.KEY_W)) {
			hitW = true;
		} else if (!currentState.isKeyPressed(Input.KEY_W)) {
			hitW = false;
		}

		if (currentState.isKeyPressed(Input.KEY_S)) {
			hitS = true;
		} else if (!currentState.isKeyPressed(Input.KEY_S)) {
			hitS = false;
		}

		if (currentState.isKeyPressed(Input.KEY_A)) {
			hitA = true;
		} else if (!currentState.isKeyPressed(Input.KEY_A)) {
			hitA = false;
		}

		if (currentState.isKeyPressed(Input.KEY_D)) {
			hitD = true;
		} else if (!currentState.isKeyPressed(Input.KEY_D)) {
			hitD = false;
		}

		if (currentState.isKeyPressed(Input.KEY_SPACE)) {
			hitSpace = true;
		} else if (!currentState.isKeyPressed(Input.KEY_SPACE)) {
			hitSpace = false;
		}

		if (currentState.isKeyPressed(Input.KEY_1)) {
			hit1 = true;
		} else if (!currentState.isKeyPressed(Input.KEY_1)) {
			hit1 = false;
		}

		if (input.isKeyPressed(Input.KEY_SPACE)) {
			isSpaceDown = true;
		} else if (!input.isKeyPressed(Input.KEY_SPACE)) {
			isSpaceDown = false;
		}

		if (input.isKeyDown(Input.KEY_W))
			isWdown = true;
		else if (!input.isKeyDown(Input.KEY_W))
			isWdown = false;

		if (input.isKeyDown(Input.KEY_S))
			isSdown = true;
		else if (!input.isKeyDown(Input.KEY_S))
			isSdown = false;

		if (input.isKeyDown(Input.KEY_A))
			isAdown = true;
		else if (!input.isKeyDown(Input.KEY_A))
			isAdown = false;

		if (input.isKeyDown(Input.KEY_D))
			isDdown = true;
		else if (!input.isKeyDown(Input.KEY_D))
			isDdown = false;

	}

	static public boolean isKeyHit(int keycode) {
		return curStats[keycode] && !prevStats[keycode];
	}

	static public boolean isKeyDown(int keycode) {
		return curStats[keycode];
	}

	static public boolean HitEsc() {
		return hitEsc;
	}

	static public boolean HitEnter() {
		return hitEnter;
	}

	static public boolean HitSpace() {
		return hitSpace;
	}

	static public boolean Hit1() {
		return hit1;
	}

	static public boolean HitW() {
		return hitW;
	}

	static public boolean HitS() {
		return hitS;
	}

	static public boolean HitA() {
		return hitA;
	}

	static public boolean HitD() {
		return hitD;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * static public void HandleKeyDown(KeyEvent e) { if (e.getKeyCode() ==
	 * KeyEvent.VK_W) { isWdown = true; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_S) { isSdown = true; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_A) { isAdown = true; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_D) { System.out.println("sdfdsf"); isDdown = true; } else if
	 * (e.getKeyCode() == KeyEvent.VK_SPACE) { System.out.println("sdfdsf");
	 * isSpaceDown = true; } }
	 * 
	 * static public void HandleKeyUp(KeyEvent e) { if (e.getKeyCode() ==
	 * KeyEvent.VK_W) { isWdown = false; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_S) { isSdown = false; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_A) { isAdown = false; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_D) { isDdown = false; } else if (e.getKeyCode() ==
	 * KeyEvent.VK_SPACE) { isSpaceDown = false; } }
	 */

}
