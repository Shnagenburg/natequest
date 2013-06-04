package Utility;

import java.util.Random;

import org.newdawn.slick.geom.Vector2f;

public class MathUtility {
	public static Random rand = new Random();

	public static float randomFloat(float min, float max) {
		float range = max - min;
		return min + (rand.nextFloat() * range);
	}

	public static int randomSign() {
		return rand.nextBoolean() ? 1 : -1;
	}

	public static boolean randomBool() {
		return rand.nextBoolean();
	}

	public static float rangify(float min, float max, int steps, int currentStep) {
		float stepValue = (max - min) / steps;
		return min + (stepValue * currentStep);
	}

	public static float distance(float x1, float x2, float y1, float y2) {
		float distanceSquared = ((x1 - x2) * (x1 - x2))
				+ ((y1 - y2) * (y1 - y2));
		return (float) Math.sqrt(distanceSquared);
	}

	public static float distance(Vector2f source, Vector2f target) {
		return distance(source.x, target.x, source.y, target.y);
	}

	public static float percent(float current, float max) {
		return current / max;
	}
}
