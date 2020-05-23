import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Interpolation;
import dawnliker.PNG8;
import dawnliker.PaletteReducer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Tommy Ettinger on 1/4/2020.
 */
public class Scale2K extends ApplicationAdapter {
	 public static int lerp (int baseColor, int mixColor, float amount) {
		  if ((baseColor & 0x80) == 0)
				return mixColor;
		  if ((mixColor & 0x80) == 0)
				return baseColor;
//		  if(Math.abs((baseColor >>> 24) - (mixColor >>> 24))
//			  + Math.abs((baseColor >>> 16 & 0xFF) - (mixColor >>> 16 & 0xFF))
//			  + Math.abs((baseColor >>> 8 & 0xFF) - (mixColor >>> 8 & 0xFF)) > 225)
//		  	 return amount <= 0.5f ? baseColor : mixColor;
		  final float i = 1f - amount;
		  final int r = (int)((baseColor >>> 24) * i + (mixColor >>> 24) * amount), g = (int)((baseColor >>> 16 & 0xFF) * i
			  + (mixColor >>> 16 & 0xFF) * amount), b = (int)((baseColor >>> 8 & 0xFF) * i + (mixColor >>> 8 & 0xFF) * amount);
		  return r << 24 | g << 16 | b << 8 | 0xFF;
	 }

	 public static void scale2x (ByteBuffer dest, int A, int B, int C, int D, int E, int F, int G, int H, int I, int p0, int p1,
		 int p2, int p3) {
		  dest.putInt(p0, D != 0 && ((D == B && B != F && D != H) || (E == 0 && D != 0 && B != 0)) ? D : E);
		  dest.putInt(p1, B != 0 && ((B == F && B != D && F != H) || (E == 0 && B != 0 && F != 0)) ? B : E);
		  dest.putInt(p2, H != 0 && ((D == H && D != B && H != F) || (E == 0 && D != 0 && H != 0)) ? H : E);
		  dest.putInt(p3, F != 0 && ((H == F && D != H && B != F) || (E == 0 && H != 0 && F != 0)) ? F : E);
	 }
	 public static void scale2k (ByteBuffer dest, int A, int B, int C, int D, int E, int F, int G, int H, int I, int p0, int p1,
		 int p2, int p3) {
		  if (D == B && B != F && D != H) {
				if (B == C && D == G) {
					 if (A != E) {
						  dest.putInt(p0, lerp(D, dest.getInt(p0), 0.75f));
						  dest.putInt(p1, lerp(D, dest.getInt(p1), 0.25f));
						  dest.putInt(p2, lerp(D, dest.getInt(p2), 0.25f));
					 }
				} else if (B == C) {
					 dest.putInt(p0, lerp(D, dest.getInt(p0), 0.75f));
					 dest.putInt(p1, lerp(D, dest.getInt(p1), 0.25f));
				} else if (D == G) {
					 dest.putInt(p0, lerp(D, dest.getInt(p0), 0.75f));
					 dest.putInt(p2, lerp(D, dest.getInt(p2), 0.25f));
				} else {
					 dest.putInt(p0, lerp(D, dest.getInt(p0), 0.5f));
				}
		  }
	 }

	 public static void scale (Pixmap src, Pixmap dest) {

		  final int width = src.getWidth() - 1, height = src.getHeight() - 1, dw = dest.getWidth(), dh = dest.getHeight();
		  ByteBuffer pixels = dest.getPixels();

		  for (int y = 1; y < height; ++y) {
				for (int x = 1; x < width; ++x) {
					 int p0, p1, p2, p3;
					 int A, B, C, D, E, F, G, H, I;

					 A = src.getPixel(x - 1, y - 1);
					 B = src.getPixel(x, y - 1);
					 C = src.getPixel(x + 1, y - 1);
					 D = src.getPixel(x - 1, y);
					 E = src.getPixel(x, y);
					 F = src.getPixel(x + 1, y);
					 G = src.getPixel(x - 1, y + 1);
					 H = src.getPixel(x, y + 1);
					 I = src.getPixel(x + 1, y + 1);

//					 dest.setColor(E);
//					 dest.fillRectangle(x << 1, y << 1, 2, 2);
					 p0 = (y * dw + x << 1) << 2;
					 p1 = (y * dw + x << 1 | 1) << 2;
					 p2 = ((y * dw + x << 1) + dw) << 2;
					 p3 = ((y * dw + x << 1 | 1) + dw) << 2;
					 
					 scale2x(pixels, A, B, C, D, E, F, G, H, I, p0, p1, p2, p3);

//					 scale2x(pixels, A, B, C, D, E, F, G, H, I, p0, p1, p2, p3);
//					 scale2x(pixels, G, D, A, H, E, B, I, F, C, p2, p0, p3, p1);
//					 scale2x(pixels, I, H, G, F, E, D, C, B, A, p3, p2, p1, p0);
//					 scale2x(pixels, C, F, I, B, E, H, A, D, G, p1, p3, p0, p2);


//					 scale2k(pixels, A, B, C, D, E, F, G, H, I, p0, p1, p2, p3);
//					 scale2k(pixels, G, D, A, H, E, B, I, F, C, p2, p0, p3, p1);
//					 scale2k(pixels, I, H, G, F, E, D, C, B, A, p3, p2, p1, p0);
//					 scale2k(pixels, C, F, I, B, E, H, A, D, G, p1, p3, p0, p2);
				}
		  }
	 }

	 public void create () {
		 Pixmap source = new Pixmap(Gdx.files.local("atlas/Dawnlike.png"));
		 Pixmap dest = new Pixmap(source.getWidth() * 2, source.getHeight() * 2, Pixmap.Format.RGBA8888);
		 Pixmap dest4 = new Pixmap(source.getWidth() * 4, source.getHeight() * 4, Pixmap.Format.RGBA8888);
		 scale(source, dest);
		 scale(dest, dest4);
		 PNG8 png = new PNG8();
		 png.setFlipY(false);
		 int[] palette = new int[8];
//				 new int[]{0x00000000, 0x101313FF, 0x373730FF, 0x505B5BFF,
//				 0x808070FF, 0xA4A490FF, 0xB0C9C9FF, 0xEDEDD0FF,
//				 0x346836FF, 0x76C070FF, 0xD8F8D0FF,
//							  0x702028FF, 0xD05030FF, 0xFFC080FF,
//				 0x6E5A54FF, };
		 System.out.print("0x00000000, ");
		 for (int i = 1; i < 8; i++) {
			 int r = (int) Interpolation.smoother.apply(0f, 255f, (float)Math.sqrt(i / 10f));
			 //(int)(Math.pow(i / 6.25, 0.625) * 222);
			 palette[i] = r * 0x01010100 | 0xFF;
			 System.out.printf("0x%08X, ", palette[i]);
		 }
		 System.out.println();
		 png.palette = new PaletteReducer(palette);
		 try {
			 String dir = "otherColors/curved";
			 Gdx.files.local(dir).mkdirs();
//							  0x081820FF, 0x346856FF, 0x88C070FF, 0xE0F8D0FF,
//							  0x400810FF, 0x802438FF, 0xE08058FF, 0xFFB090FF
//							  {0x00000000, 0x101010FF, 0x323232FF, 0x494949FF, 0x5B5B5BFF, 0x6E6E6EFF,
//					  0x808080FF, 0x929292FF, 0xA4A4A4FF, 0xB6B6B6FF, 0xC9C9C9FF, 0xDBDBDBFF, 0xEDEDEDFF,
//					  0xEE1100FF,0xCC1908FF,0x9F2010FF,}

			 png.write(Gdx.files.local(dir + "/Dawnlike.png"), source, false, true, 64);
			 png.write(Gdx.files.local(dir + "/Dawnlike2.png"), dest, false, true, 64);
			 png.write(Gdx.files.local(dir + "/Dawnlike4.png"), dest4, false, true, 64);
		 } catch (IOException e) {
			 e.printStackTrace();
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
		  final Scale2K app = new Scale2K();
		  new Lwjgl3Application(app, config);
	 }

}
