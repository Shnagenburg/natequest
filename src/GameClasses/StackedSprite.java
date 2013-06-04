package GameClasses;

import java.io.IOException;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import NateQuest.Debugger;
import NateQuest.ImageCreator;
import NateQuest.Gameplay.Camera;

public class StackedSprite extends Sprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6331235133028961755L;
	public Image mTopTexture;
	private String topSpriteName;

	public StackedSprite(Image texture, Image topTexture) {
		super(texture);
		topSpriteName = topTexture.getName();
		mTopTexture = topTexture;
		mTopTexture.setCenterOfRotation(iWidth / 2, iHeight / 2);
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

		// Culling
		// int xbound = (int)((iXpos - (iWidth * fScale) / 2) + (bIsCameraed ?
		// CUtil.CurrentGame.camera.xRounded : 0));
		// int ybound = (int)((iYpos - (iHeight * fScale) / 2) + (bIsCameraed ?
		// CUtil.CurrentGame.camera.yRounded : 0));
		if (shouldCull(x, y, scale)) {
			return;
		}

		mTexture.setRotation((int) fAngle);
		mTopTexture.setRotation((int) fAngle);
		mTexture.draw(x - (iWidth * scale) / 2, y - (iHeight * scale) / 2,
				scale);
		mTopTexture.draw(x - (iWidth * scale) / 2, y - (iHeight * scale) / 2,
				scale);
	}

	public void setRotation(float value) {
		fAngle = value;

		if (bIsCameraed) {
			// Camera camera = CUtil.CurrentGame.camera;
			// mTexture.setCenterOfRotation(iWidth * camera.getZoom()/ 2,
			// iHeight * camera.getZoom() / 2);
		}
		mTexture.setRotation(fAngle);
		mTopTexture.setRotation((int) fAngle);
	}

	public void setScale(float scale) {
		fScale = scale;
		mTexture.setCenterOfRotation(iWidth * fScale / 2, iHeight * fScale / 2);
		mTopTexture.setCenterOfRotation(iWidth * fScale / 2, iHeight * fScale
				/ 2);
	}

	private void readObject(java.io.ObjectInputStream in) {

		try {
			topSpriteName = (String) in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Null indicates it was a generated sprite, we need to remake it.
		if (topSpriteName.charAt(0) == '!') {
			try {
				int tx = in.readInt();
				int ty = in.readInt();
				int r = in.readInt();
				int g = in.readInt();
				int b = in.readInt();
				int a = in.readInt();
				mTopTexture = ImageCreator.createImage(tx, ty, r, g, b, a);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (topSpriteName.charAt(0) == '#') {
			try {
				int tx = in.readInt();
				int ty = in.readInt();
				int set = in.readInt();
				mTopTexture = ImageCreator.createButton(tx, ty, set);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mTopTexture = CUtil.ResourcePool.getImageByName(topSpriteName);
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) {
		try {
			out.writeObject(topSpriteName);

			// this means it was a generated sprite
			if (topSpriteName.charAt(0) == '!') {
				out.writeInt(mTexture.getWidth());
				out.writeInt(mTexture.getHeight());
				out.writeInt(mTexture.getColor(0, 0).getRed());
				out.writeInt(mTexture.getColor(0, 0).getBlue());
				out.writeInt(mTexture.getColor(0, 0).getGreen());
				out.writeInt(mTexture.getColor(0, 0).getAlpha());
			}
			// this means it was a generated button
			if (topSpriteName.charAt(0) == '#') {
				out.writeInt(mTexture.getWidth());
				out.writeInt(mTexture.getHeight());
				String s = mTexture.getName();
				int set = Integer.parseInt("" + s.charAt(s.length() - 1));
				out.writeInt(set);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
