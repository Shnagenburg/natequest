package NateQuest;

import java.nio.ByteBuffer;

import javax.print.attribute.standard.MediaSize.Other;

import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.opengl.ImageDataFactory;

import GameClasses.CUtil;

public class ImageCreator {

	public enum DanColor {
		RED, BLUE, GREEN
	}

	public static Image createImage(int x, int y, DanColor color) {
		int r = 0;
		int g = 0;
		int b = 0;
		switch (color) {
		case RED:
			r = 255;
			break;
		case BLUE:
			b = 255;
			break;
		case GREEN:
			g = 255;
			break;
		}

		ImageBuffer data = new ImageBuffer(x, y);
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				data.setRGBA(i, j, r, g, b, 255); // alpha of 0 is transparent
		Image image = new Image(data);
		image.setName("!" + r + " " + g + " " + b);
		return image;
	}

	public static Image createImage(int x, int y, int r, int g, int b, int a) {
		ImageBuffer data = new ImageBuffer(x, y);
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++)
				data.setRGBA(i, j, r, g, b, a); // alpha of 0 is transparent
		Image image = new Image(data);
		image.setName("!" + r + " " + g + " " + b + " " + a);
		return image;
	}

	public static Image createFadeAnimation(String name, int frames) {
		Image image = CUtil.QuickImage(name);

		ImageBuffer data = new ImageBuffer(image.getWidth() * frames,
				image.getHeight());

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {

				int r = image.getColor(i, j).getRed();
				int b = image.getColor(i, j).getBlue();
				int g = image.getColor(i, j).getGreen();
				int a = image.getColor(i, j).getAlpha();
				data.setRGBA(i, j, r, g, b, (int) (a * 0.5f));
			}
		}

		Image ret = new Image(data);
		ret.setName("$" + name + "$" + frames);
		return image;
	}

	public static Image createHorizFadedScreenTransition() {
		int x = CUtil.Dimensions.width;
		int y = CUtil.Dimensions.height;
		ImageBuffer data = new ImageBuffer(x * 2, y);
		for (int i = x / 2; i < 3 * x / 2; i++) {
			for (int j = 0; j < y; j++) {
				data.setRGBA(i, j, 0, 0, 0, 255);
			}
		}// alpha of 0 is transparent
		for (int i = 0; i < x * 0.5f; i++)
			for (int j = 0; j < y; j++)
				data.setRGBA(i, j, 0, 0, 0, (int) ((i / (x * 0.5f)) * 255));

		for (int i = (int) (x * 1.5f); i < x * 2; i++) {
			for (int j = 0; j < y; j++) {
				float percent = (i - (x * 1.5f)) / (x * 0.5f);
				data.setRGBA(i, j, 0, 0, 0,
						(int) (255 - (float) (percent * 255)));
			}
		}

		Image image = new Image(data);
		image.setName("@" + "horzfade");
		return image;
	}

	public static Image createVerticalFadedScreenTransition() {
		int x = CUtil.Dimensions.width;
		int y = CUtil.Dimensions.height;
		ImageBuffer data = new ImageBuffer(x, y * 2);
		for (int i = 0; i < x; i++) {
			for (int j = y / 2; j < 3 * y / 2; j++) {
				data.setRGBA(i, j, 0, 0, 0, 255);
			}
		}// alpha of 0 is transparent
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y * 0.5f; j++)
				data.setRGBA(i, j, 0, 0, 0, (int) ((j / (y * 0.5f)) * 255));

		for (int i = 0; i < x; i++) {
			for (int j = (int) (y * 1.5f); j < y * 2; j++) {
				float percent = (j - (y * 1.5f)) / (y * 0.5f);
				data.setRGBA(i, j, 0, 0, 0,
						(int) (255 - (float) (percent * 255)));
			}
		}

		Image image = new Image(data);
		image.setName("@" + "vertfade");
		return image;
	}

	// the set is whether its the normal, hover, or clicked button.. 1, 2, 3
	public static Image createButton(int width, int height, int set) {
		ImageBuffer data = new ImageBuffer(width, height);

		int ratio = width / height * 7;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data.setRGBA(i, j, 225 / set, 225 / set, 225 / set, 255);
			}
		}// alpha of 0 is transparent

		for (int i = 0; i < width / ratio; i++) {
			for (int j = 0; j < height; j++) {
				if (j >= i && j < height - i) {
					data.setRGBA(i, j, 160 / set, 160 / set, 160 / set, 255);
				}
			}
		}
		for (int i = (ratio - 1) * width / ratio; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (j > width - i && j < height - (width - i)) {
					data.setRGBA(i, j, 160 / set, 160 / set, 160 / set, 255);
				}
			}
		}

		ratio = ((height / width) + 1) * 5;

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height / ratio; j++) {
				if (j <= i && i <= width - j) {
					data.setRGBA(i, j, 200 / set, 200 / set, 200 / set, 255);
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = (ratio - 1) * height / ratio; j < height; j++) {
				if (i >= height - j && i <= width - (height - j)) {
					data.setRGBA(i, j, 200 / set, 200 / set, 200 / set, 255);
				}
			}
		}

		Image image = new Image(data);
		image.setCenterOfRotation(width / 2, height / 2);
		image.setName("#" + width + " " + height + " " + set);
		return image;
	}

	public static Image createBlurTop(Image base) {
		int x = base.getWidth();
		int y = base.getHeight();
		ImageBuffer toReturn = new ImageBuffer(x, y);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				int alpha = 255;
				if (j < y / 2) {
					float percent = ((float) j / (float) (y / 2));
					percent = modPercent(percent);
					alpha = (int) (percent * 255);
					if (alpha < 0)
						alpha = 0;
				}
				toReturn.setRGBA(i, j, base.getColor(i, j).getRed(), base
						.getColor(i, j).getGreen(), base.getColor(i, j)
						.getBlue(), alpha); // alpha of 0 is transparent
			}
		}

		Image image = new Image(toReturn);
		image.setCenterOfRotation(x / 2, y / 2);
		image.setName(base.getName() + "fadeupBLEND");
		return image;
	}

	public static Image createBlurCorner(Image base) {
		// Debugger.print("Making blue corner for: " + base);
		int x = base.getWidth();
		int y = base.getHeight();
		ImageBuffer toReturn = new ImageBuffer(x, y);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				int alpha = 255;
				float percentone = 1;
				float percenttwo = 1;
				if (j < y / 2) {
					percentone = ((float) j / (float) (y / 2));
					percentone = modPercent(percentone);
				}
				if (i > x / 2) {
					percenttwo = 1.0f - ((float) (i - (x / 2)) / (float) (x / 2));
					percenttwo = modPercent(percenttwo);
				}
				alpha = (int) (alpha * percentone * percenttwo);
				if (alpha > 255)
					alpha = 255;
				if (alpha < 0)
					alpha = 0;
				toReturn.setRGBA(i, j, base.getColor(i, j).getRed(), base
						.getColor(i, j).getGreen(), base.getColor(i, j)
						.getBlue(), alpha); // alpha of 0 is transparent
			}
		}

		Image image = new Image(toReturn);
		image.setCenterOfRotation(x / 2, y / 2);
		image.setName(base.getName() + "fadecornerBLEND");
		return image;

		/*
		 * int alpha = 255; if (j < y / 2) { float percent = ((float)j /
		 * (float)(y/2)); percent *= percent; alpha = (int)( percent * 255); if
		 * (alpha < 0) alpha = 0; } if (i > x / 2) { float otherpercent =
		 * ((float)(i - (x/2)) / (float)(x/2)); otherpercent *= otherpercent;
		 * int newalpha = 255 - (int)(otherpercent * 255);
		 * 
		 * if (newalpha < 0) newalpha = 0;
		 * 
		 * alpha = (newalpha + alpha);
		 * 
		 * if (alpha < 0) alpha = 0;
		 * 
		 * if (i - (x/2) >= j) alpha = 0; } toReturn.setRGBA(i, j,
		 * base.getColor(i, j).getRed(), base.getColor(i, j).getGreen(),
		 * base.getColor(i, j).getBlue(), alpha); // alpha of 0 is transparent
		 */
	}

	public static Image createBlurCornerInverted(Image base) {

		int x = base.getWidth();
		int y = base.getHeight();
		ImageBuffer toReturn = new ImageBuffer(x, y);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				int alpha = 255;
				if (j < y / 2 && i < x / 2) {
					float percent = ((float) j / (float) (y / 2));
					percent = modPercent(percent);
					alpha = (int) (percent * 255);

					float otherpercent = ((float) i / (float) (x / 2));
					otherpercent = modPercent(otherpercent);
					int otheralpha = (int) (otherpercent * 255);
					alpha = (alpha + otheralpha);
					if (alpha < 0)
						alpha = 0;
					if (alpha > 255)
						alpha = 255;
				}

				toReturn.setRGBA(i, j, base.getColor(i, j).getRed(), base
						.getColor(i, j).getGreen(), base.getColor(i, j)
						.getBlue(), alpha); // alpha of 0 is transparent
			}
		}

		Image image = new Image(toReturn);
		image.setCenterOfRotation(x / 2, y / 2);
		image.setName(base.getName() + "fadeinvertedBLEND");
		return image;
	}

	public static float modPercent(float base) {
		return base * base * base;
	}
}
