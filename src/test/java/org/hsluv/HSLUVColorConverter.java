//Copyright (c) 2016 Alexei Boronine
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.

package org.hsluv;

import squidpony.StringKit;

public class HSLUVColorConverter {
    private static final double[][] m = new double[][]
            {
                    new double[]{3.240969941904521, -1.537383177570093, -0.498610760293},
                    new double[]{-0.96924363628087, 1.87596750150772, 0.041555057407175},
                    new double[]{0.055630079696993, -0.20397695888897, 1.056971514242878},
            };

    private static final double[][] minv = new double[][]
            {
                    new double[]{0.41239079926595, 0.35758433938387, 0.18048078840183},
                    new double[]{0.21263900587151, 0.71516867876775, 0.072192315360733},
                    new double[]{0.019330818715591, 0.11919477979462, 0.95053215224966},
            };

    private static double refY = 1.0;

    private static double refU = 0.19783000664283;
    private static double refV = 0.46831999493879;

    private static double kappa = 903.2962962;
    private static double epsilon = 0.0088564516;

    private static final double[][] bounds = new double[6][2];
    
    private static void getBounds(double L) {
        //double[][] result = new double[6][];

        double sub1 = Math.pow(L + 16, 3) / 1560896;
        double sub2 = sub1 > epsilon ? sub1 : L / kappa;

        for (int c = 0; c < 3; ++c) {
            double m1 = m[c][0];
            double m2 = m[c][1];
            double m3 = m[c][2];

            for (int t = 0; t < 2; ++t) {
                double top1 = (284517 * m1 - 94839 * m3) * sub2;
                double top2 = (838422 * m3 + 769860 * m2 + 731718 * m1) * L * sub2 - 769860 * t * L;
                double bottom = (632260 * m3 - 126452 * m2) * sub2 + 126452 * t;

                bounds[c << 1 | t][0] = top1 / bottom;
                bounds[c << 1 | t][1] = top2 / bottom;
            }
        }
    }

    private static double intersectLineLine(double ax, double ay, double bx, double by) {
        return (ay - by) / (bx - ax);
    }

    private static double distanceFromPole(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    private static double lengthOfRayUntilIntersect(double theta, double[] line) {
        return line[1] / (Math.sin(theta) - line[0] * Math.cos(theta));
    }

    private static double maxSafeChromaForL(double L) {
        getBounds(L);
        double min = Double.MAX_VALUE;

        for (int i = 0; i < 2; ++i) {
            double m1 = bounds[i][0];
            double b1 = bounds[i][1];
            double x = intersectLineLine(m1, b1, -1 / m1, 0);
            double length = distanceFromPole(x, b1 + x * m1);

            min = Math.min(min, length);
        }

        return min;
    }

    private static double maxChromaForLH(double L, double H) {
        double hrad = H / 360 * Math.PI * 2;
        getBounds(L);
        double min = Double.MAX_VALUE;

        for (double[] bound : bounds) {
            double length = lengthOfRayUntilIntersect(hrad, bound);
            if (length >= 0.0) {
                min = Math.min(min, length);
            }
        }

        return min;
    }

    private static double dotProduct(double[] a, double[] b) {
        double sum = 0;

        for (int i = 0; i < a.length; ++i) {
            sum += a[i] * b[i];
        }

        return sum;
    }

    private static double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }

    private static double fromLinear(double c) {
        if (c <= 0.0031308) {
            return 12.92 * c;
        } else {
            return 1.055 * Math.pow(c, 1 / 2.4) - 0.055;
        }
    }

    private static double toLinear(double c) {
        if (c > 0.04045) {
            return Math.pow((c + 0.055) / (1 + 0.055), 2.4);
        } else {
            return c / 12.92;
        }
    }

    public static void xyzToRgb(double[] tuple, double[] result) {
        result[0] = fromLinear(dotProduct(m[0], tuple));
        result[1] = fromLinear(dotProduct(m[1], tuple));
        result[2] = fromLinear(dotProduct(m[2], tuple));
    }

    public static void rgbToXyz(double[] tuple, double[] result) {
        result[0] = toLinear(tuple[0]);
        result[1] = toLinear(tuple[1]);
        result[2] = toLinear(tuple[2]);
        
        result[0] = dotProduct(minv[0], result);         
        result[1] = dotProduct(minv[1], result);
        result[2] = dotProduct(minv[2], result);
    }

    private static double yToL(double Y) {
        if (Y <= epsilon) {
            return (Y / refY) * kappa;
        } else {
            return 116 * Math.pow(Y / refY, 1.0 / 3.0) - 16;
        }
    }

    private static double lToY(double L) {
        if (L <= 8) {
            return refY * L / kappa;
        } else {
            return refY * Math.pow((L + 16) / 116, 3);
        }
    }

    public static void xyzToLuv(double[] tuple, double[] result) {
        double X = tuple[0];
        double Y = tuple[1];
        double Z = tuple[2];

        double varU = (4 * X) / (X + (15 * Y) + (3 * Z));
        double varV = (9 * Y) / (X + (15 * Y) + (3 * Z));

        double L = yToL(Y);

        if (L == 0) {
            result[0] = result[1] = result[2] = 0;
            return;
        }
        result[0] = L;
        result[1] = 13 * L * (varU - refU);
        result[2] = 13 * L * (varV - refV);
    }

    public static void luvToXyz(double[] tuple, double[] result) {
        double L = tuple[0];
        double U = tuple[1];
        double V = tuple[2];

        if (L == 0) {
            result[0] = result[1] = result[2] = 0;
            return;
        }

        double varU = U / (13 * L) + refU;
        double varV = V / (13 * L) + refV;

        double Y = lToY(L);
        double X = 0 - (9 * Y * varU) / ((varU - 4) * varV - varU * varV);
        double Z = (9 * Y - (15 * varV * Y) - (varV * X)) / (3 * varV);
        
        result[0] = X;
        result[1] = Y;
        result[2] = Z;
    }

    public static void luvToLch(double[] tuple, double[] result) {
        double L = tuple[0];
        double U = tuple[1];
        double V = tuple[2];

        double C = Math.sqrt(U * U + V * V);
        double H;

        if (C < 0.00000001) {
            H = 0;
        } else {
            double Hrad = Math.atan2(V, U);

            // pi to more digits than they provide it in the stdlib
            H = (Hrad * 180.0) / 3.1415926535897932;

            if (H < 0) {
                H = 360 + H;
            }
        }
        result[0] = L;
        result[1] = C;
        result[2] = H;
    }

    public static void lchToLuv(double[] tuple, double[] result) {
        double L = tuple[0];
        double C = tuple[1];
        double H = tuple[2];

        double Hrad = (H / 360.0) * 6.283185307179586;
        result[0] = L;
        result[1] = Math.cos(Hrad) * C;
        result[2] = Math.sin(Hrad) * C;
    }

    public static void hsluvToLch(double[] tuple, double[] result) {
        double H = tuple[0];
        double S = tuple[1];
        double L = tuple[2];
        
        result[2] = H;
        
        if (L > 99.9999999) {
            result[0] = 100.0;
            result[1] = 0.0;
            return;
        }

        if (L < 0.00000001) {
            result[0] = 0.0;
            result[1] = 0.0;
            return;
        }

        double max = maxChromaForLH(L, H);
        result[0] = L;
        result[1] = max / 100 * S;
    }

    public static void lchToHsluv(double[] tuple, double[] result) {
        double L = tuple[0];
        double C = tuple[1];
        double H = tuple[2];
        
        result[0] = H;
        
        if (L > 99.9999999) {
            result[1] = 0.0;
            result[2] = 100.0;
            return;
        }

        if (L < 0.00000001) {
            result[1] = 0.0;
            result[2] = 0.0;
            return;
        }

        double max = maxChromaForLH(L, H);
        result[1] = C / max * 100;
        result[2] = L;
    }

    public static void hpluvToLch(double[] tuple, double[] result) {
        double H = tuple[0];
        double S = tuple[1];
        double L = tuple[2];

        result[2] = H;

        if (L > 99.9999999) {
            result[0] = 100.0;
            result[1] = 0.0;
            return;
        }

        if (L < 0.00000001) {
            result[0] = 0.0;
            result[1] = 0.0;
            return;
        }

        double max = maxSafeChromaForL(L);
        result[0] = L;
        result[1] = max / 100 * S;
    }

    public static void lchToHpluv(double[] tuple, double[] result) {
        double L = tuple[0];
        double C = tuple[1];
        double H = tuple[2];

        result[0] = H;

        if (L > 99.9999999) {
            result[1] = 0.0;
            result[2] = 100.0;
            return;
        }

        if (L < 0.00000001) {
            result[1] = 0.0;
            result[2] = 0.0;
            return;
        }

        double max = maxSafeChromaForL(L);
        result[0] = H;
        result[1] = C / max * 100;
    }

    public static String rgbToHex(double[] tuple) {
        final char[] cs = {'#', '0', '0', '0', '0', '0', '0'};
        for (int i = 0, c = 1; i < tuple.length; ++i) {
            double chan = tuple[i];
            double rounded = round(chan);

            if (rounded < -0.0001 || rounded > 1.0001) {
                throw new IllegalArgumentException("Illegal rgb value: " + rounded);
            }
            int rdd = (int) (rounded * 255 + 0.5);
            cs[c++] = StringKit.hexDigits[rdd >>> 4];
            cs[c++] = StringKit.hexDigits[rdd & 15];
        }
        return String.valueOf(cs);
    }

    public static double[] hexToRgb(String hex) {
        return new double[]
                {
                        Integer.parseInt(hex.substring(1, 3), 16) / 255.0,
                        Integer.parseInt(hex.substring(3, 5), 16) / 255.0,
                        Integer.parseInt(hex.substring(5, 7), 16) / 255.0,
                };
    }

    public static double[] lchToRgb(double[] tuple) {
        lchToLuv(tuple, tuple);
        luvToXyz(tuple, tuple);
        xyzToRgb(tuple, tuple);
        return tuple;
    }

    public static double[] rgbToLch(double[] tuple) {
        rgbToXyz(tuple, tuple);
        xyzToLuv(tuple, tuple);
        luvToLch(tuple, tuple);
        return tuple;
    }


    public static double[] luvToRgb(double[] tuple) {
        luvToXyz(tuple, tuple);
        xyzToRgb(tuple, tuple);
        return tuple;
    }

    public static double[] rgbToLuv(double[] tuple) {
        rgbToXyz(tuple, tuple);
        xyzToLuv(tuple, tuple);
        return tuple;
    }


    // RGB <--> HUSL(p)

    public static double[] hsluvToRgb(double[] tuple) {
        hsluvToLch(tuple, tuple);
        return lchToRgb(tuple);
    }

    public static double[] rgbToHsluv(double[] tuple) {
        rgbToLch(tuple);
        lchToHsluv(tuple, tuple);
        return tuple;
    }

    public static double[] hpluvToRgb(double[] tuple) {
        hpluvToLch(tuple, tuple);
        return lchToRgb(tuple);
    }

    public static double[] rgbToHpluv(double[] tuple) {
        rgbToLch(tuple);
        lchToHpluv(tuple, tuple);
        return tuple;
    }

    // Hex

    public static String hsluvToHex(double[] tuple) {
        return rgbToHex(hsluvToRgb(tuple));
    }

    public static String hpluvToHex(double[] tuple) {
        return rgbToHex(hpluvToRgb(tuple));
    }

    public static double[] hexToHsluv(String s) {
        return rgbToHsluv(hexToRgb(s));
    }

    public static double[] hexToHpluv(String s) {
        return rgbToHpluv(hexToRgb(s));
    }

}