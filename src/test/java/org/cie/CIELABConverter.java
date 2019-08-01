//port of https://github.com/mcychan/nQuant.j2se
//Apache 2.0 license
package org.cie;

import com.badlogic.gdx.graphics.Color;
import squidpony.squidmath.NumberTools;

public class CIELABConverter {

	protected double a1Prime, a2Prime, CPrime1, CPrime2, barCPrime, barhPrime;
	public CIELABConverter()
	{
	}

	public static class Lab {
		public double alpha;
		public double A;
		public double B;
		public double L;
		public Lab()
		{
			alpha = 1.0;
			A = 0.0;
			B = 0.0;
			L = 0.0;
		}
		public Lab(double L, double A, double B, double alpha)
		{
			this.L = L;
			this.A = A;
			this.B = B;
			this.alpha = alpha;
		}
		public Lab(Color color)
		{
			double r = color.r, g = color.g, b = color.b;
			alpha = color.a;
			double x, y, z;

			r = ((r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92);
			g = ((g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92);
			b = ((b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92);

			x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.950489;
			y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.000000;
			z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.088840;

			x = (x > 0.008856) ? Math.cbrt(x) : (7.787037037037037 * x) + 0.13793103448275862;
			y = (y > 0.008856) ? Math.cbrt(y) : (7.787037037037037 * y) + 0.13793103448275862;
			z = (z > 0.008856) ? Math.cbrt(z) : (7.787037037037037 * z) + 0.13793103448275862;

			L = (116.0 * y) - 16.0;
			A = 500.0 * (x - y);
			B = 200.0 * (y - z);
		}
		
		public double lightnessLAB(Color color)
		{
			double r = color.r, g = color.g, b = color.b, y;
			r = ((r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92);
			g = ((g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92);
			b = ((b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92);
			y = (r * 0.2126 + g * 0.7152 + b * 0.0722);
			y = (y > 0.008856) ? Math.cbrt(y) : (7.787037037037037 * y) + 0.13793103448275862;
			return (116.0 * y) - 16.0;
		}
		public Lab fromRGBA(int rgba)
		{
			double r = (rgba >>> 24) / 255.0, g = (rgba >>> 16 & 0xFF) / 255.0, b = (rgba >>> 8 & 0xFF) / 255.0;
			alpha = (rgba & 0xFF) / 255.0;
			double x, y, z;

			r = ((r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92);
			g = ((g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92);
			b = ((b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92);

			x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.950489; // 0.96422;
			y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.000000; // 1.00000;
			z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.088840; // 0.82521;
	

			x = (x > 0.008856) ? Math.cbrt(x) : (7.787037037037037 * x) + 0.13793103448275862;
			y = (y > 0.008856) ? Math.cbrt(y) : (7.787037037037037 * y) + 0.13793103448275862;
			z = (z > 0.008856) ? Math.cbrt(z) : (7.787037037037037 * z) + 0.13793103448275862;

			L = (116.0 * y) - 16.0;
			A = 500.0 * (x - y);
			B = 200.0 * (y - z);
			return this;
		}
		public Color toColor(){
			double y = (L + 16.0) / 116.0;
			double x = A / 500.0 + y;
			double z = y - B / 200.0;
			double r, g, b;

			x = 0.95047 * ((x > 0.2068930344229638) ? x * x * x : (x - 16.0 / 116.0) / 7.787);
			y = 1.00000 * ((y > 0.2068930344229638) ? y * y * y : (y - 16.0 / 116.0) / 7.787);
			z = 1.08883 * ((z > 0.2068930344229638) ? z * z * z : (z - 16.0 / 116.0) / 7.787);

			r = x *  3.2406 + y * -1.5372 + z * -0.4986;
			g = x * -0.9689 + y *  1.8758 + z *  0.0415;
			b = x *  0.0557 + y * -0.2040 + z *  1.0570;

			r = (r > 0.0031308) ? (1.055 * Math.pow(r, 1.0 / 2.4) - 0.055) : 12.92 * r;
			g = (g > 0.0031308) ? (1.055 * Math.pow(g, 1.0 / 2.4) - 0.055) : 12.92 * g;
			b = (b > 0.0031308) ? (1.055 * Math.pow(b, 1.0 / 2.4) - 0.055) : 12.92 * b;

			return new Color((float)r, (float)g, (float)b, (float)alpha);
		}

		public int toRGBA(){
			double y = (L + 16.0) / 116.0;
			double x = A / 500.0 + y;
			double z = y - B / 200.0;
			double r, g, b;

			x = 0.95047 * ((x > 0.2068930344229638) ? x * x * x : (x - 16.0 / 116.0) / 7.787);
			y = 1.00000 * ((y > 0.2068930344229638) ? y * y * y : (y - 16.0 / 116.0) / 7.787);
			z = 1.08883 * ((z > 0.2068930344229638) ? z * z * z : (z - 16.0 / 116.0) / 7.787);

			r = x *  3.2406 + y * -1.5372 + z * -0.4986;
			g = x * -0.9689 + y *  1.8758 + z *  0.0415;
			b = x *  0.0557 + y * -0.2040 + z *  1.0570;

			r = ((r > 0.0031308) ? (1.055 * Math.pow(r, 1.0 / 2.4) - 0.055) : 12.92 * r) * 255.5;
			g = ((g > 0.0031308) ? (1.055 * Math.pow(g, 1.0 / 2.4) - 0.055) : 12.92 * g) * 255.5;
			b = ((b > 0.0031308) ? (1.055 * Math.pow(b, 1.0 / 2.4) - 0.055) : 12.92 * b) * 255.5;

			return  Math.max(0, Math.min(255, (int) r)) << 24 |
					Math.max(0, Math.min(255, (int) g)) << 16 | 
					Math.max(0, Math.min(255, (int) b)) << 8 | 
					Math.max(0, Math.min(255, (int) (alpha * 255.5)));
		}

	}
	
	public static Lab RGB2LAB(final double[] c)
	{
		double r = c[0], g = c[1], b = c[3];
		double x, y, z;

		r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
		g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
		b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

		x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.95047;
		y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.00000;
		z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.08883;

		x = (x > 0.008856) ? Math.cbrt(x) : (7.787 * x) + 16.0 / 116.0;
		y = (y > 0.008856) ? Math.cbrt(y) : (7.787 * y) + 16.0 / 116.0;
		z = (z > 0.008856) ? Math.cbrt(z) : (7.787 * z) + 16.0 / 116.0;

		Lab lab = new Lab();
		lab.alpha = c[3];
		lab.L = (116 * y) - 16;
		lab.A = 500 * (x - y);
		lab.B = 200 * (y - z);
		return lab;
	}

	public static double[] LAB2RGB(final Lab lab){
		double y = (lab.L + 16.0) / 116.0;
		double x = lab.A / 500.0 + y;
		double z = y - lab.B / 200.0;
		double r, g, b;

		x = 0.95047 * ((x > 0.2068930344229638) ? x * x * x : (x - 16.0 / 116.0) / 7.787);
		y = 1.00000 * ((y > 0.2068930344229638) ? y * y * y : (y - 16.0 / 116.0) / 7.787);
		z = 1.08883 * ((z > 0.2068930344229638) ? z * z * z : (z - 16.0 / 116.0) / 7.787);

		r = x *  3.2406 + y * -1.5372 + z * -0.4986;
		g = x * -0.9689 + y *  1.8758 + z *  0.0415;
		b = x *  0.0557 + y * -0.2040 + z *  1.0570;

		r = (r > 0.0031308) ? (1.055 * Math.pow(r, 1.0 / 2.4) - 0.055) : 12.92 * r;
		g = (g > 0.0031308) ? (1.055 * Math.pow(g, 1.0 / 2.4) - 0.055) : 12.92 * g;
		b = (b > 0.0031308) ? (1.055 * Math.pow(b, 1.0 / 2.4) - 0.055) : 12.92 * b;

		return new double[]{Math.max(0.0, Math.min(1.0, r)), Math.max(0.0, Math.min(1.0, g)), Math.max(0.0, Math.min(1.0, b)), Math.max(0.0, Math.min(1.0, lab.alpha))};
	}
	
	/*******************************************************************************
	* Conversions.
	******************************************************************************/
	private static final double deg2Rad30  = 30 ;//(0.5235987755982988);
	private static final double deg2Rad6   = 6  ;//(0.10471975511965978);
	private static final double deg2Rad25  = 25 ;//(0.4363323129985824);
	private static final double deg2Rad275 = 275;//(4.799655442984406);
	private static final double deg2Rad63  = 63 ;//(1.0995574287564276);

	static double L_prime_div_k_L_S_L(final Lab lab1, final Lab lab2)
	{
		final double k_L = 1.0;
		double deltaLPrime = lab2.L - lab1.L;	
		double barLPrime = (lab1.L + lab2.L) / 2.0;
		double S_L = 1 + ((0.015 * Math.pow(barLPrime - 50.0, 2.0)) / Math.sqrt(20 + Math.pow(barLPrime - 50.0, 2.0)));
		return deltaLPrime / (k_L * S_L);
	}

	protected double C_prime_div_k_C_S_C(final Lab lab1, final Lab lab2)
	{
		final double k_C = 1.0;
		final double pow25To7 = 6103515625.0; /* pow(25, 7) */
		double C1 = Math.sqrt((lab1.A * lab1.A) + (lab1.B * lab1.B));
		double C2 = Math.sqrt((lab2.A * lab2.A) + (lab2.B * lab2.B));
		double barC = (C1 + C2) * 0.5;
		final double barCTo7 = Math.pow(barC, 7);
		double G = 0.5 * (1 - Math.sqrt(barCTo7 / (barCTo7 + pow25To7)));
		a1Prime = ((1.0 + G) * lab1.A);
		a2Prime = ((1.0 + G) * lab2.A);

		CPrime1 = (Math.sqrt((a1Prime * a1Prime) + (lab1.B * lab1.B)));
		CPrime2 = (Math.sqrt((a2Prime * a2Prime) + (lab2.B * lab2.B)));
		double deltaCPrime = CPrime2 - CPrime1;
		double barCPrime =  (CPrime1 + CPrime2) * 0.5;
		
		double S_C = 1 + (0.045 * barCPrime);
		return deltaCPrime / (k_C * S_C);
	}

	protected double H_prime_div_k_H_S_H(final Lab lab1, final Lab lab2)
	{
		final double k_H = 1.0;
		final double deg360InRad = 360;//Math.PI * 2.0;
		final double deg180InRad = 180;//Math.PI;
		double CPrimeProduct = CPrime1 * CPrime2;
		double hPrime1;
		if (Math.abs(lab1.B) < 0x1p-32 && Math.abs(a1Prime) < 0x1p-32)
			hPrime1 = 0.0;
		else {
			hPrime1 = 360.0*NumberTools.atan2_(lab1.B, a1Prime);
		}
		double hPrime2;
		if (Math.abs(lab2.B) < 0x1p-32 && Math.abs(a2Prime) < 0x1p-32)
			hPrime2 = 0.0;
		else {
			hPrime2 = 360.0*NumberTools.atan2_(lab2.B, a2Prime);
		}
		double deltahPrime;
		if (Math.abs(CPrimeProduct) < 0x1p-32)
			deltahPrime = 0;
		else {
			deltahPrime = hPrime2 - hPrime1;
			if (deltahPrime < -deg180InRad)
				deltahPrime += deg360InRad;
			else if (deltahPrime > deg180InRad)
				deltahPrime -= deg360InRad;
		}

		double deltaHPrime = 2.0 * Math.sqrt(CPrimeProduct) * NumberTools.sin_(deltahPrime / 720.0);
		double hPrimeSum = hPrime1 + hPrime2;
		if (Math.abs(CPrimeProduct) < 0x1p-32) {
			barhPrime = hPrimeSum;
		}
		else {
			if (Math.abs(hPrime1 - hPrime2) <= deg180InRad)
				barhPrime = hPrimeSum * 0.5;
			else {
				if (hPrimeSum < deg360InRad)
					barhPrime = ((hPrimeSum + deg360InRad) * 0.5);
				else
					barhPrime = ((hPrimeSum - deg360InRad) * 0.5);
			}
		}

		barCPrime = ((CPrime1 + CPrime2) * 0.5);
		double T = 1.0 - 
				(0.17 * NumberTools.cos_((barhPrime - deg2Rad30) / 360.0)) + 
				(0.24 * NumberTools.cos_((2.0 * barhPrime)) / 360.0) + 
				(0.32 * NumberTools.cos_(((3.0 * barhPrime) + deg2Rad6)) / 360.0) - 
				(0.20 * NumberTools.cos_(((4.0 * barhPrime) - deg2Rad63)) / 360.0);
		double S_H = 1 + (0.015 * barCPrime * T);
		return deltaHPrime / (k_H * S_H);
	}

	protected double R_T()
	{
		final double pow25To7 = 6103515625.0; /* Math.pow(25, 7) */
		final double barCPrimeTo7 = Math.pow(barCPrime, 7.0);
		double deltaTheta = deg2Rad30 * Math.exp(-Math.pow((barhPrime - deg2Rad275) / deg2Rad25, 2.0));
		double R_C = -2.0 * Math.sqrt(barCPrimeTo7 / (barCPrimeTo7 + pow25To7));
		double R_T = NumberTools.sin_(deltaTheta / 180.0) * R_C;
		return R_T;
	}

	/* From the paper "The CIEDE2000 Color-Difference Formula: Implementation Notes, */
	/* Supplementary Test Data, and Mathematical Observations", by */
	/* Gaurav Sharma, Wencheng Wu and Edul N. Dalal, */
	/* Color Res. Appl., vol. 30, no. 1, pp. 21-30, Feb. 2005. */
	/* Return the CIEDE2000 Delta E color difference measure squared, for two Lab values */
	public double CIEDE2000(final Lab lab1, final Lab lab2)
	{
		double deltaL_prime_div_k_L_S_L = L_prime_div_k_L_S_L(lab1, lab2);
		double deltaC_prime_div_k_C_S_C = C_prime_div_k_C_S_C(lab1, lab2);
		double deltaH_prime_div_k_H_S_H = H_prime_div_k_H_S_H(lab1, lab2);
		double deltaR_T = R_T() * deltaC_prime_div_k_C_S_C * deltaH_prime_div_k_H_S_H;
		return
			deltaL_prime_div_k_L_S_L * deltaL_prime_div_k_L_S_L +
			deltaC_prime_div_k_C_S_C * deltaC_prime_div_k_C_S_C +
			deltaH_prime_div_k_H_S_H * deltaH_prime_div_k_H_S_H +
			deltaR_T;
	}
	
	public double cmc(final Lab lab1, final Lab lab2)
	{
		final double a1 = lab1.A, b1 = lab1.B, l1 = lab1.A;
		final double a2 = lab2.A, b2 = lab2.B, l2 = lab2.A;
		final double weightL = 1.0, weightC = 1.0;
		double xC1 = Math.sqrt(a1 * a1 + b1 * b1);
		double xC2 = Math.sqrt(a2 * a2 + b2 * b2);
		double xff = xC1 * xC1;
		xff *= xC2 * xC2;
		xff = Math.sqrt(xff / (xff + 1900));
		double xH1 = NumberTools.atan2_(b1, a1) * 360.0;
		double xTT, xSL;
		if ( xH1 < 164 || xH1 > 345 ) xTT = 0.36 + Math.abs( 0.4 * NumberTools.cos_(( 35 + xH1) / 360.0));
		else                          xTT = 0.56 + Math.abs( 0.2 * NumberTools.cos_((168 + xH1) / 360.0));

		if ( l1 < 16 ) xSL = 0.511;
		else           xSL = (0.040975 * l1) / (1.0 + (0.01765 * l1));
		double xSC = ((0.0638 * xC1) / ( 1 + (0.0131 * xC1))) + 0.638;
		double xSH = ((xff * xTT) + 1 - xff) * xSC;
		double xDH = Math.sqrt((a2 - a1) * (a2 - a1) + (b2 - b1) * (b2 - b1) - (xC2 - xC1) * (xC2 - xC1));
		xSL = (l2 - l1) / (weightL * xSL);
		xSC = (xC2 - xC1) / (weightC * xSC);
		xSH = xDH / xSH;
		
		return xSL * xSL + xSC * xSC + xSH * xSH;
		
		/* //formulas found on https://www.easyrgb.com/en/math.php
CIE-L*1, CIE-a*1, CIE-b*1          //Color #1 CIE-L*ab values
CIE-L*2, CIE-a*2, CIE-b*2          //Color #2 CIE-L*ab values
WHT-L, WHT-C                       //Weight factors

xC1 = sqrt( ( CIE-a*1 ^ 2 ) + ( CIE-b*1 ^ 2 ) )
xC2 = sqrt( ( CIE-a*2 ^ 2 ) + ( CIE-b*2 ^ 2 ) )
xff = sqrt( ( xC1 ^ 4 ) / ( ( xC1 ^ 4 ) + 1900 ) )
xH1 = CieLab2Hue( CIE-a*1, CIE-b*1 )

if ( xH1 < 164 || xH1 > 345 ) xTT = 0.36 + abs( 0.4 * cos( dtor(  35 + xH1 ) ) )
else                          xTT = 0.56 + abs( 0.2 * cos( dtor( 168 + xH1 ) ) )

if ( CIE-L*1 < 16 ) xSL = 0.511
else                xSL = ( 0.040975 * CIE-L*1 ) / ( 1 + ( 0.01765 * CIE-L*1 ) )

xSC = ( ( 0.0638 * xC1 ) / ( 1 + ( 0.0131 * xC1 ) ) ) + 0.638
xSH = ( ( xff * xTT ) + 1 - xff ) * xSC
xDH = sqrt( ( CIE-a*2 - CIE-a*1 ) ^ 2 + ( CIE-b*2 - CIE-b*1 ) ^ 2 - ( xC2 - xC1 ) ^ 2 )
xSL = ( CIE-L*2 - CIE-L*1 ) / ( WHT-L * xSL )
xSC = ( xC2 - xC1 ) / ( WHT-C * xSC )
xSH = xDH / xSH

Delta CMC = sqrt( xSL ^ 2 + xSC ^ 2 + xSH ^ 2 )
		 */
	}
	
	public double delta(final Lab lab1, final Lab lab2)
	{
		return (lab1.L - lab2.L) * (lab1.L - lab2.L) * 11.0 +
				(lab1.A - lab2.A) * (lab1.A - lab2.A) * 1.6 +
				(lab1.B - lab2.B) * (lab1.B - lab2.B);
	}
	
	public static double differenceLAB(final int rgba1, final int rgba2)
	{
		if(((rgba1 ^ rgba2) & 0x80) == 0x80) return Double.POSITIVE_INFINITY;
		double x, y, z, r, g, b;

		r = (rgba1 >>> 24) / 255.0;
		g = (rgba1 >>> 16 & 0xFF) / 255.0;
		b = (rgba1 >>> 8 & 0xFF) / 255.0;

		r = ((r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92);
		g = ((g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92);
		b = ((b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92);

		x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.950489; // 0.96422;
		y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.000000; // 1.00000;
		z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.088840; // 0.82521;
		
		x = (x > 0.008856) ? Math.cbrt(x) : (7.787037037037037 * x) + 0.13793103448275862;
		y = (y > 0.008856) ? Math.cbrt(y) : (7.787037037037037 * y) + 0.13793103448275862;
		z = (z > 0.008856) ? Math.cbrt(z) : (7.787037037037037 * z) + 0.13793103448275862;

		double L = (116.0 * y) - 16.0;
		double A = 500.0 * (x - y);
		double B = 200.0 * (y - z);

		r = (rgba2 >>> 24) / 255.0;
		g = (rgba2 >>> 16 & 0xFF) / 255.0;
		b = (rgba2 >>> 8 & 0xFF) / 255.0;

		r = ((r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92);
		g = ((g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92);
		b = ((b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92);

		x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.950489; // 0.96422;
		y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.000000; // 1.00000;
		z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.088840; // 0.82521;

		x = (x > 0.008856) ? Math.cbrt(x) : (7.787037037037037 * x) + 0.13793103448275862;
		y = (y > 0.008856) ? Math.cbrt(y) : (7.787037037037037 * y) + 0.13793103448275862;
		z = (z > 0.008856) ? Math.cbrt(z) : (7.787037037037037 * z) + 0.13793103448275862;

		L -= 116.0 * y - 16.0;
		A -= 500.0 * (x - y);
		B -= 200.0 * (y - z);
		
		return L * L * 11.0 + A * A * 1.6 + B * B;
	}
}