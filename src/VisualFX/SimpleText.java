package VisualFX;

import java.io.IOException;
import java.io.Serializable;

import javax.print.attribute.standard.Media;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.GameHarness;
import NateQuest.Debugger;
import NateQuest.GameScreen;
import NateQuest.Collidables.Collidable;
import NateQuest.Drawables.Drawable;
import NateQuest.Gameplay.Camera;

public class SimpleText implements Serializable {

	int iXPos, iYPos;
	String sText;
	UnicodeFont ufont;
	Color color;
	public boolean bIsCamered = true;
	public Camera camera;

	public SimpleText(String text, int x, int y, boolean isCamed) {
		this(text, x, y);
		bIsCamered = isCamed;
	}

	public SimpleText(String text, int x, int y) {
		iXPos = x;
		iYPos = y;
		sText = text;
		// Debugger.print("tryin to load font " + CUtil.GameFont);
		ufont = CUtil.GameFont;
		color = Color.black;
	}

	public void setPosition(int x, int y) {
		iXPos = x;
		iYPos = y;
	}

	public void setPositionRounded(float x, float y) {
		iXPos = Math.round(x);
		iYPos = Math.round(y);
	}

	public void draw(Graphics g) {

		int x = iXPos;
		int y = iYPos;
		if (bIsCamered) {
			x *= camera.getZoom();
			y *= camera.getZoom();
			x += camera.xRoundedWithZoom;
			y += camera.yRoundedWithZoom;
		}

		g.setFont(CUtil.GameFont);
		// CUtil.GameFont.drawString(500, 500,
		// "oadfasdfdsfdsfdfdsafasdfadfadfllol", Color.black);
		g.setColor(color);
		g.drawString(sText, x, y);
	}

	public String getText() {
		return sText;
	}

	public void setText(String message) {
		sText = message;
	}

	public void setColor(Color c) {
		color = c;
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeInt(iXPos);
			out.writeInt(iYPos);
			out.writeObject(sText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readObject(java.io.ObjectInputStream in) {

		try {
			iXPos = in.readInt();
			iYPos = in.readInt();
			sText = (String) in.readObject();
			ufont = CUtil.GameFont;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		color = Color.black;

	}

}
