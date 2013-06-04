package GameClasses;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;

import NateQuest.Debugger;
import NateQuest.ImageCreator;
import NateQuest.Gameplay.Camera;

public class StretchScaleSprite extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -97386563933027069L;
	public int rectX = 1;
	public int rectY = 1;

	// Stretch scaled sprites are drawn from the top left
	public StretchScaleSprite(Image texture, int x, int y) {
		super(texture);
		rectX = x;
		rectY = y;
	}

	public void setRectWidthHeight(int x, int y) {
		rectX = x;
		rectY = y;
	}

	public void draw(Graphics g) {
		int x = iXpos;
		int y = iYpos;
		float scale = fScale;
		if (bIsCameraed) {
			x *= camera.getZoom();
			y *= camera.getZoom();
			x += camera.xRoundedWithZoom;
			y += camera.yRoundedWithZoom;
			scale *= camera.getZoom();
		}

		if (x + ((iWidth * scale)) < 0
				|| x - ((iWidth * scale)) > CUtil.Dimensions.width
				|| y + ((iHeight * scale)) < 0
				|| y - ((iHeight * scale)) > CUtil.Dimensions.height) {
			return;
		}

		mTexture.setRotation((int) fAngle);

		mTexture.draw(x, y, x + rectX, y + rectY, 0, 0,
				mTexture.getWidth() - 1, mTexture.getHeight() - 1);

	}

}
