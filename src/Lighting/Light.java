package Lighting;

import org.newdawn.slick.Color;

public class Light {
	/** The position of the light */
	public float x, y;
	/** The RGB tint of the light, alpha is ignored */
	public Color tint;
	/** The alpha value of the light, default 1.0 (100%) */
	float alpha;
	/** The amount to scale the light (use 1.0 for default size). */
	public float scale;
	// original scale
	private float scaleOrig;

	public Light(float x, float y, float scale, Color tint) {
		this.x = x;
		this.y = y;
		this.scale = scaleOrig = scale;
		this.alpha = 1f;
		this.tint = tint;
	}

	public Light(float x, float y, float scale) {
		this(x, y, scale, Color.white);
	}

	public void update(float time) {
		// effect: scale the light slowly using a sin func
		scale = scaleOrig + 1f + .5f * (float) Math.sin(time);
	}
}
