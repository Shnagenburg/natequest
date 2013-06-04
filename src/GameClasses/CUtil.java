package GameClasses;

import java.awt.Dimension;
import java.util.Scanner;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;

import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Story.Checkpoint;

// Core game utilities
public class CUtil {
	static public GameHarness Harness;
	static public GameContainer SlickGameContainer;
	static public GameScreen ScreenGettingLoaded;
	static public Game GameHandle;
	static public NateQuest.GameScreen CurrentGame;
	static public ResourcePool ResourcePool;
	static public LevelPool LevelPool;
	static public Dimension Dimensions;
	// static public Font GameFont;
	// static public Font AnalyticFont;
	static public Graphics GameGraphics;
	// static public Camera Camera;
	static public float ElapsedTime;
	static public UnicodeFont GameFont;

	static public Image QuickImage(String name) {
		return ResourcePool.getImageByName(name);
	}

	static public Vector2f getNormaledVector(Vector2f source, Vector2f target) {
		Vector2f vel = new Vector2f(source.x - target.x, source.y - target.y);
		vel = vel.normalise();
		vel = vel.negate();
		return vel;
	}

	static public String getConsoleInput(String prompt) {
		Debugger.print(prompt);
		Scanner user_input = new Scanner(System.in);
		return user_input.next();
	}

}
