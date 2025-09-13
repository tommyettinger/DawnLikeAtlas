import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.github.tommyettinger.anim8.Dithered;
import com.github.tommyettinger.anim8.PNG8;
import com.github.tommyettinger.anim8.PaletteReducer;
import com.github.tommyettinger.anim8.QualityPalette;
import dawnliker.Coloring;

import java.nio.ByteBuffer;

/**
 * Created by Tommy Ettinger on 1/4/2020.
 */
public class ScaleP extends ApplicationAdapter {
	public static void scale2p(ByteBuffer dest, int A, int B, int C, int D, int E, int F, int G, int H, int I, int p0, int p1,
							   int p2, int p3) {
		dest.putInt(p0, D != 0 && ((D == B && B != F && D != H) || (E == 0 && B != 0)) ? D : E);
		dest.putInt(p1, B != 0 && ((B == F && B != D && F != H) || (E == 0 && F != 0)) ? B : E);
		dest.putInt(p2, H != 0 && ((H == D && D != B && H != F) || (E == 0 && D != 0)) ? H : E);
		dest.putInt(p3, F != 0 && ((F == H && D != H && B != F) || (E == 0 && H != 0)) ? F : E);
	}

	public static void scale3p(ByteBuffer dest, int A, int B, int C, int D, int E, int F, int G, int H, int I, int p0, int p1,
							   int p2, int p3, int p4, int p5, int p6, int p7, int p8) {
		dest.putInt(p0, D != 0 && ((D == B && B != F && D != H) || (E == 0 && B != 0)) ? D : E);
		dest.putInt(p1, B != 0 && (((D == B && B != F && D != H && E != C) || (B == F && B != D && F != H && E != A)) || (E == 0 && (D != 0 || F != 0))) ? B : E);
		dest.putInt(p2, B != 0 && ((B == F && B != D && F != H) || (E == 0 && F != 0)) ? B : E);
		dest.putInt(p3, D != 0 && (((D == B && B != F && D != H && E != G) || (D == H && D != B && H != F && E != A)) || (E == 0 && (B != 0 || H != 0))) ? D : E);
		dest.putInt(p4, E);
		dest.putInt(p5, F != 0 && (((B == F && B != D && F != H && E != I) || (H == F && D != H && B != F && E != C)) || (E == 0 && (B != 0 || H != 0))) ? F : E);
		dest.putInt(p6, H != 0 && ((D == H && D != B && H != F) || (E == 0 && D != 0)) ? H : E);
		dest.putInt(p7, H != 0 && (((D == H && D != B && H != F && E != I) || (H == F && D != H && B != F && E != G)) || (E == 0 && (D != 0 || F != 0))) ? H : E);
		dest.putInt(p8, F != 0 && ((H == F && D != H && B != F) || (E == 0 && H != 0)) ? F : E);
	}

	public static int brightness(int rgba) {
		int r = rgba >>> 23 & 0x1FE;
		int g = rgba >>> 14 & 0x3FC;
		int b = rgba >>> 8 & 0xFF;
		return (r + g + b) * (rgba & 0xFF);
	}
	public static void scale2(Pixmap src, Pixmap dest) {

		final int width = src.getWidth() - 1, height = src.getHeight() - 1, dw = dest.getWidth(), dh = dest.getHeight();
		ByteBuffer pixels = dest.getPixels();

		for (int y = 0; y <= height; ++y) {
			for (int x = 0; x <= width; ++x) {
				int p0, p1, p2, p3;
				int A, B, C, D, E, F, G, H, I;

				A = (x & y) == 0 ? 0 : src.getPixel(x - 1, y - 1);
				B = y == 0 ? 0 : src.getPixel(x, y - 1);
				C = y == 0 || x == width ? 0 : src.getPixel(x + 1, y - 1);
				D = x == 0 ? 0 : src.getPixel(x - 1, y);
				E = src.getPixel(x, y);
				F = x == width ? 0 : src.getPixel(x + 1, y);
				G = x == 0 || y == height ? 0 : src.getPixel(x - 1, y + 1);
				H = y == height ? 0 : src.getPixel(x, y + 1);
				I = x == width || y == height ? 0 : src.getPixel(x + 1, y + 1);

				p0 = (y * dw + x << 1) << 2;
				p1 = (y * dw + x << 1 | 1) << 2;
				p2 = ((y * dw + x << 1) + dw) << 2;
				p3 = ((y * dw + x << 1 | 1) + dw) << 2;

				scale2p(pixels, A, B, C, D, E, F, G, H, I, p0, p1, p2, p3);
			}
		}

		for (int y = 1; y < dh; y++) {
			for (int x = 1; x < dw; x++) {
				int p0, p1, p2, p3, c0, c1, c2, c3;
				c0 = pixels.getInt(p0 = (y * dw + x - 1 - dw) << 2);
				c1 = pixels.getInt(p1 = (y * dw + x - dw) << 2);
				c2 = pixels.getInt(p2 = ((y * dw + x - 1)) << 2);
				c3 = pixels.getInt(p3 = ((y * dw + x)) << 2);
				if (c0 == c3 && c1 == c2 && c0 != c1)
				{
					c3 = (brightness(c0) > brightness(c1) ? c0 : c1);
					pixels.putInt(p0, c3);
					pixels.putInt(p1, c3);
					pixels.putInt(p2, c3);
					pixels.putInt(p3, c3);
				}
				else if (c0 == c3 && c0 != c1 && c0 != c2)
				{
					pixels.putInt(p1, c0);
					pixels.putInt(p2, c0);
				}
				else if (c1 == c2 && c1 != c0 && c1 != c3)
				{
					pixels.putInt(p0, c1);
					pixels.putInt(p3, c1);
				}
			}
		}
	}
	public static void scale3(Pixmap src, Pixmap dest) {

		final int width = src.getWidth() - 1, height = src.getHeight() - 1, dw = dest.getWidth(), dh = dest.getHeight();
		ByteBuffer pixels = dest.getPixels();

		for (int y = 0; y <= height; ++y) {
			for (int x = 0; x <= width; ++x) {
				int p0, p1, p2, p3, p4, p5, p6, p7, p8;
				int A, B, C, D, E, F, G, H, I;

				A = (x & y) == 0 ? 0 : src.getPixel(x - 1, y - 1);
				B = y == 0 ? 0 : src.getPixel(x, y - 1);
				C = y == 0 || x == width ? 0 : src.getPixel(x + 1, y - 1);
				D = x == 0 ? 0 : src.getPixel(x - 1, y);
				E = src.getPixel(x, y);
				F = x == width ? 0 : src.getPixel(x + 1, y);
				G = x == 0 || y == height ? 0 : src.getPixel(x - 1, y + 1);
				H = y == height ? 0 : src.getPixel(x, y + 1);
				I = x == width || y == height ? 0 : src.getPixel(x + 1, y + 1);

				p0 = (y * dw + x) * 3 << 2;
				p1 = (y * dw + x) * 3 + 1 << 2;
				p2 = (y * dw + x) * 3 + 2 << 2;
				p3 = (y * dw + x) * 3 + dw << 2;
				p4 = (y * dw + x) * 3 + 1 + dw << 2;
				p5 = (y * dw + x) * 3 + 2 + dw << 2;
				p6 = (y * dw + x) * 3 + dw + dw << 2;
				p7 = (y * dw + x) * 3 + 1 + dw + dw << 2;
				p8 = (y * dw + x) * 3 + 2 + dw + dw << 2;

				scale3p(pixels, A, B, C, D, E, F, G, H, I, p0, p1, p2, p3, p4, p5, p6, p7, p8);
			}
		}

		for (int y = 1; y < dh; y++) {
			for (int x = 1; x < dw; x++) {
				int p0, p1, p2, p3, c0, c1, c2, c3;
				c0 = pixels.getInt(p0 = (y * dw + x - 1 - dw) << 2);
				c1 = pixels.getInt(p1 = (y * dw + x - dw) << 2);
				c2 = pixels.getInt(p2 = ((y * dw + x - 1)) << 2);
				c3 = pixels.getInt(p3 = ((y * dw + x)) << 2);
				if (c0 == c3 && c1 == c2 && c0 != c1)
				{
					c3 = (brightness(c0) > brightness(c1) ? c0 : c1);
					pixels.putInt(p0, c3);
					pixels.putInt(p1, c3);
					pixels.putInt(p2, c3);
					pixels.putInt(p3, c3);
				}
				else if (c0 == c3 && c0 != c1 && c0 != c2)
				{
					pixels.putInt(p1, c0);
					pixels.putInt(p2, c0);
				}
				else if (c1 == c2 && c1 != c0 && c1 != c3)
				{
					pixels.putInt(p0, c1);
					pixels.putInt(p3, c1);
				}
			}
		}
	}

	 public void create () {
//		 Pixmap source = new Pixmap(Gdx.files.local("thirteen/Dawnlike.png"));
//		 Pixmap dest = new Pixmap(source.getWidth() * 2, source.getHeight() * 2, Pixmap.Format.RGBA8888);
//		 Pixmap dest3 = new Pixmap(source.getWidth() * 3, source.getHeight() * 3, Pixmap.Format.RGBA8888);
//		 Pixmap dest4 = new Pixmap(source.getWidth() * 4, source.getHeight() * 4, Pixmap.Format.RGBA8888);
//		 scale2(source, dest);
//		 scale3(source, dest3);
//		 scale2(dest, dest4);

		 Pixmap source = new Pixmap(Gdx.files.local("thirteen/Dawnlike.png"));
		 Pixmap dest = new Pixmap(Gdx.files.local("thirteen/Dawnlike2.png"));
		 Pixmap dest3 = new Pixmap(Gdx.files.local("thirteen/Dawnlike3.png"));
		 Pixmap dest4 = new Pixmap(Gdx.files.local("thirteen/Dawnlike4.png"));

		 PNG8 png = new PNG8();
		 png.setFlipY(false);
		 int[] palette;
//         if("db16 version? yes".endsWith("yes")) {
//			 palette = Coloring.DB16;
//			 png.palette = new QualityPalette(palette);
//			 png.setDitherAlgorithm(Dithered.DitherAlgorithm.NONE);
//
//		 String dir = "otherColorsNew/db16/";
//			 Gdx.files.local(dir).mkdirs();
//
//			 png.write(Gdx.files.local(dir + "Dawnlike.png"), source, false, true, 100);
//			 png.write(Gdx.files.local(dir + "Dawnlike2.png"), dest, false, true, 100);
//			 png.write(Gdx.files.local(dir + "Dawnlike3.png"), dest3, false, true, 100);
//			 png.write(Gdx.files.local(dir + "Dawnlike4.png"), dest4, false, true, 100);
//			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
//			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
//			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
//			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
//		 }
		 if("japanese woodblock version? yes".endsWith("no")) {
			 palette = Coloring.JAPANESE_WOODBLOCK_12;
			 png.palette = new QualityPalette(palette);
			 png.setDitherAlgorithm(Dithered.DitherAlgorithm.GOURD);
             png.setDitherStrength(0.25f);

			 String dir = "otherColorsNew/jw12/";
			 Gdx.files.local(dir).mkdirs();

			 png.write(Gdx.files.local(dir + "Dawnlike.png"), source, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike2.png"), dest, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike3.png"), dest3, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike4.png"), dest4, false, true, 100);
			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
		 }
		 if("super-spark version? yes".endsWith("yes")) {
			 palette = Coloring.SUPER_SPARK;
			 png.palette = new QualityPalette(palette);
			 png.setDitherAlgorithm(Dithered.DitherAlgorithm.GOURD);
             png.setDitherStrength(0.25f);

			 String dir = "otherColorsNew/superspark12/";
			 Gdx.files.local(dir).mkdirs();

			 png.write(Gdx.files.local(dir + "Dawnlike.png"), source, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike2.png"), dest, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike3.png"), dest3, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike4.png"), dest4, false, true, 100);
			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
		 }
		 if("prospecal version? yes".endsWith("no")) {
			 palette = Coloring.PROSPECAL;
			 png.palette = new QualityPalette(palette);
			 png.setDitherAlgorithm(Dithered.DitherAlgorithm.GOURD);
             png.setDitherStrength(0.25f);

			 String dir = "otherColorsNew/prospecal8/";
			 Gdx.files.local(dir).mkdirs();

			 png.write(Gdx.files.local(dir + "Dawnlike.png"), source, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike2.png"), dest, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike3.png"), dest3, false, true, 100);
			 png.write(Gdx.files.local(dir + "Dawnlike4.png"), dest4, false, true, 100);
			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
		 }
		 if("desaturated version? yes".endsWith("no")) {
			 String dir = "otherColorsNew/desat16/";
			 Gdx.files.local(dir).mkdirs();

			 int[] palette2 = new int[Coloring.DB16.length];
			 System.out.print("0x00000000, ");
			 final float[][] oklab = PaletteReducer.OKLAB;
			 for (int i = 1; i < palette2.length; i++) {
				 int s = PaletteReducer.shrink(Coloring.DB16[i]);
				 palette2[i] = PaletteReducer.oklabToRGB(oklab[0][s], oklab[1][s] * 0.5f, oklab[2][s] * 0.5f, 1f);
//				 System.out.printf("0x%08X, ", palette2[i]);
			 }

			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike.png"), Gdx.files.local(dir + "/Dawnlike.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike2.png"), Gdx.files.local(dir + "/Dawnlike2.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike3.png"), Gdx.files.local(dir + "/Dawnlike3.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike4.png"), Gdx.files.local(dir + "/Dawnlike4.png"), palette2);

			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
		 }
		 if("high-saturated version? yes".endsWith("no")) {
			 String dir = "otherColorsNew/hisat16/";
			 Gdx.files.local(dir).mkdirs();

			 int[] palette2 = new int[Coloring.DB16.length];
			 System.out.print("0x00000000, ");
			 final float[][] oklab = PaletteReducer.OKLAB;
			 for (int i = 1; i < palette2.length - 1; i++) {
				 int s = PaletteReducer.shrink(Coloring.DB16[i]);
				 palette2[i] = PaletteReducer.oklabToRGB(oklab[0][s], oklab[1][s] * 1.75f, oklab[2][s] * 1.75f, 1f);
//				 System.out.printf("0x%08X, ", palette2[i]);
			 }
			 palette2[palette2.length - 1] = 0xFFFFFFFF;
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike.png"), Gdx.files.local(dir + "/Dawnlike.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike2.png"), Gdx.files.local(dir + "/Dawnlike2.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike3.png"), Gdx.files.local(dir + "/Dawnlike3.png"), palette2);
			 PNG8.swapPalette(Gdx.files.local("otherColorsNew/db16/Dawnlike4.png"), Gdx.files.local(dir + "/Dawnlike4.png"), palette2);

			 Gdx.files.local("thirteen/Dawnlike.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike2.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike3.atlas").copyTo(Gdx.files.local(dir));
			 Gdx.files.local("thirteen/Dawnlike4.atlas").copyTo(Gdx.files.local(dir));
		 }
		 Gdx.app.exit();
	 }

	 public static void main(String[] arg) {
		  Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		  config.setTitle("ENLARGE");
		  config.setWindowedMode(640, 480);
		  config.setIdleFPS(10);
		  config.useVsync(true);
		  config.setResizable(false);
		  final ScaleP app = new ScaleP();
		  new Lwjgl3Application(app, config);
	 }

}
