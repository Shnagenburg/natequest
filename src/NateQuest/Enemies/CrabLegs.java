package NateQuest.Enemies;

import org.newdawn.slick.geom.Vector2f;

import Utility.UtilityOscillator;

public class CrabLegs extends CrabArmature {
	public CrabLegs(GiantCrab parentCrab, Vector2f offset, String imagename) {
		super(parentCrab, offset, imagename);
		setTimeAndOscillator();
	}

	public void setTimeAndOscillator() {
		oscillator = new UtilityOscillator(-15, 15, 1000);
		oscillator.setIsAbsoluteValued(true);
	}

	public void update() {
		super.update();
		oscillator.update();
		setRotation(oscillator.getValue());
	}
}
