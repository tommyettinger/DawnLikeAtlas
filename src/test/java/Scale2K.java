import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import dawnliker.Coloring;
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
		  png.palette = new PaletteReducer(Coloring.DB16);
		  try {
//				Gdx.files.local("alternateScaling/next").mkdirs();
				png.writePrecisely(Gdx.files.local("atlas/Dawnlike2.png"), dest, Coloring.DB16, false, 64);
				png.writePrecisely(Gdx.files.local("atlas/Dawnlike4.png"), dest4, Coloring.DB16, false, 64);
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
