package NateQuest.Drawables;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import NateQuest.Entity;
import NateQuest.Gameplay.Camera;

public class DrawableLine extends Drawable {

	int x1, y1, x2, y2;

	public DrawableLine(Entity parentEntity, int priority) {
		super(parentEntity);
		iPriority = priority;
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;

	}

	@Override
	public void draw(Graphics g) {
		Camera cam = parentEntity.parentScreen.camera;
		g.setLineWidth(2);
		g.setColor(Color.red);
		g.drawLine(x1 + cam.xOffset, y1 + cam.yOffset, x2 + cam.xOffset, y2
				+ cam.yOffset);
	}

	public void setTargets(Vector2f src, Vector2f trg) {
		x1 = Math.round(src.x);
		y1 = Math.round(src.y);
		x2 = Math.round(trg.x);
		y2 = Math.round(trg.y);
	}

}
