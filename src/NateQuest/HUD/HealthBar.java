package NateQuest.HUD;

import java.io.IOException;

import org.newdawn.slick.geom.Vector2f;

import GameClasses.CUtil;
import GameClasses.StretchScaleSprite;
import NateQuest.Debugger;
import NateQuest.Entity;
import NateQuest.GameScreen;
import NateQuest.ImageCreator;
import NateQuest.ImageCreator.DanColor;
import NateQuest.Drawables.Drawable;
import NateQuest.Drawables.MultiSprited;
import NateQuest.Projectiles.Projectile;
import NateQuest.VFX.DamageNumber;

public class HealthBar extends Entity {

	StretchScaleSprite barSprite;
	StretchScaleSprite backSprite;
	int fullbarWidth;
	public int maxHealth;
	public int currentHealth;

	public HealthBar(GameScreen gs) {
		super(gs);

		StretchScaleSprite bar = new StretchScaleSprite(
				ImageCreator.createImage(2, 2, 255, 0, 0, 255), 200, 25);
		StretchScaleSprite bg = new StretchScaleSprite(
				ImageCreator.createImage(2, 2, 0, 0, 0, 255), 210, 35);
		bar.bIsCameraed = false;
		bg.bIsCameraed = false;
		barSprite = bar;
		fullbarWidth = barSprite.rectX;
		MultiSprited d = new MultiSprited(this, bg, 99);
		d.addSprite(bar);
		d.setPosition(CUtil.Dimensions.width - 200,
				CUtil.Dimensions.height - 50);
		drawable = d;

		currentHealth = 30;
		maxHealth = currentHealth;

		setHealthBar(currentHealth, maxHealth);
		saveThisEntity = false;
	}

	public void takeDamage(Projectile p) {
		currentHealth = currentHealth - p.baseDamage;
		// Debugger.print(""+currentHealth + " d: " + p.baseDamage);
		setHealthBar(currentHealth, maxHealth);
		new DamageNumber(parentScreen, parentScreen.hero.position, p.baseDamage);
		if (currentHealth <= 0)
			parentScreen.hero.killHero();
	}

	public void takeDamage(int damage) {
		currentHealth = currentHealth - damage;
		// Debugger.print(""+currentHealth + " d: " + p.baseDamage);
		setHealthBar(currentHealth, maxHealth);
		new DamageNumber(parentScreen, parentScreen.hero.position, damage);
		if (currentHealth <= 0)
			parentScreen.hero.killHero();
	}

	public void setHealthBar(int currentHealth, int maxHealth) {
		float percent = (float) currentHealth / (float) maxHealth;
		int newWidth = (int) (fullbarWidth * percent);
		if (newWidth < 0)
			newWidth = 0;
		// Debugger.print("ch: " + currentHealth + " mh: " + maxHealth +
		// " newwid: " + newWidth + " percent: " + percent);
		barSprite.setRectWidthHeight(newWidth, barSprite.rectY);

	}

	public void setHealthbar() {
		this.setHealthBar(currentHealth, maxHealth);
	}

	public void fullHeal() {
		currentHealth = maxHealth;
		setHealthBar(currentHealth, maxHealth);
	}

}
