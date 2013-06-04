package VisualFX;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import GameClasses.CUtil;
import GameClasses.GameHarness;
import NateQuest.Entity;

public class DanText {
	final float TIMER_MAX = 1500.0f;
	float fPosX, fPosY, fVecX, fVecY, fTimer, fTimerMax;
	String sText;

	public DanText(String text, int x, int y) {
		fPosX = x;
		fPosY = y;

		fVecX = 0;
		fVecY = 0;

		sText = text;

		fTimer = 0;
		fTimerMax = TIMER_MAX;
	}

	public void setPosition(int x, int y) {
		fPosX = x;
		fPosY = y;
	}

	public void update() {
		fTimer += GameHarness.GameTime;

		fPosX = fPosX + (GameHarness.GameTime * fVecX);
		fPosY = fPosY + (GameHarness.GameTime * fVecY);
	}

	public void draw(Graphics g) {
		// g.setFont(CUtil.GameFont);
		g.setColor(new Color(255, 0, 0));
		g.drawString(sText, fPosX, fPosY);
	}
}
