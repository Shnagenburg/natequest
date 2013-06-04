package NateQuest.Drawables;

import java.io.Serializable;

import org.newdawn.slick.Graphics;

import GameClasses.Sprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.Gameplay.Camera;

public class SpriteMapped extends Drawable implements Serializable {

	public Sprite[][] sprites;

	public SpriteMapped(Entity parentEntity) {
		super(parentEntity);
		setSaveThisProperty(false);
		// TODO Auto-generated constructor stub
	}

	public SpriteMapped(Entity parentEntity, int priority) {
		super(parentEntity, null, priority);
		setSaveThisProperty(false);
	}

	public void draw(Graphics g) {
		if (bIgnore)
			return;
		// REVIST fix quickcull, breaks screen transitions
		// if (quickCull())
		// return;
		// Debugger.print("parent " + parentEntity);
		// Debugger.print("spz " + sprites + " " + this.toString());
		for (int i = 0; i < sprites.length; i++) {
			for (int j = 0; j < sprites[0].length; j++) {
				if (sprites[i][j] != null) {
					sprites[i][j].draw(g);
				}
			}
		}
	}

	// checks to see if we need to draw this thing at all
	// true if so, false otherwise,
	public boolean quickCull() {
		int maxI = sprites.length - 1;
		int maxJ = sprites[0].length - 1;
		return ((sprites[0][0] != null && sprites[0][0].willBeCulled())
				&& (sprites[0][maxJ] != null && sprites[0][maxJ].willBeCulled())
				&& (sprites[maxI][0] != null && sprites[maxI][0].willBeCulled()) && (sprites[maxI][maxJ] != null && sprites[maxI][maxJ]
				.willBeCulled()));
	}

	public void createDoubleArray(int x, int y) {
		sprites = new Sprite[x][y];
	}

	public void setCamera(Camera cam) {
		for (int i = 0; i < sprites.length; i++) {
			for (int j = 0; j < sprites[0].length; j++) {
				if (sprites[i][j] != null) {
					sprites[i][j].camera = cam;
				}
			}
		}
	}

}
