package com.hivemind.chroma;

//Provides a number of static functions that take an RGB array and simulate
//a certain type of color-blindness
public class CBSimulator {
    static public void simAchromatomaly(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.618 * r + 0.320 * g + 0.062 * b);
            int cb_g = (int)(0.163 * r + 0.775 * g + 0.062 * b);
            int cb_b = (int)(0.163 * r + 0.320 * g + 0.516 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simAchromatopsia(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.299 * r + 0.587 * g + 0.114 * b);
            int cb_g = (int)(0.299 * r + 0.587 * g + 0.114 * b);
            int cb_b = (int)(0.299 * r + 0.587 * g + 0.114 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simDeuteranomaly(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.800 * r + 0.200 * g + 0.000 * b);
            int cb_g = (int)(0.258 * r + 0.742 * g + 0.000 * b);
            int cb_b = (int)(0.142 * r + 0.858 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simDeuteranopia(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.625 * r + 0.375 * g + 0.000 * b);
            int cb_g = (int)(0.700 * r + 0.300 * g + 0.000 * b);
            int cb_b = (int)(0.300 * r + 0.700 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simProtanomaly(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.817 * r + 0.183 * g + 0.000 * b);
            int cb_g = (int)(0.333 * r + 0.667 * g + 0.000 * b);
            int cb_b = (int)(0.125 * r + 0.875 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simProtanopia(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.567 * r + 0.433 * g + 0.000 * b);
            int cb_g = (int)(0.558 * r + 0.442 * g + 0.000 * b);
            int cb_b = (int)(0.242 * r + 0.758 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simTritanomaly(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.967 * r + 0.033 * g + 0.000 * b);
            int cb_g = (int)(0.733 * r + 0.267 * g + 0.000 * b);
            int cb_b = (int)(0.183 * r + 0.817 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }

    static public void simTritanopia(int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            int r = (rgb[i] >> 16) & 0xFF;
            int g = (rgb[i] >> 8)  & 0xFF;
            int b = (rgb[i])       & 0xFF;

            int cb_r = (int)(0.950 * r + 0.050 * g + 0.000 * b);
            int cb_g = (int)(0.433 * r + 0.567 * g + 0.000 * b);
            int cb_b = (int)(0.475 * r + 0.525 * g + 0.000 * b);

            rgb[i] = 0xFF000000 | cb_r << 16 | cb_g << 8 | b;
        }
    }
}

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
    static public void filterRedGreen(int[] rgb, int filtered[], int width, int height) {

        int[] hsl = new int[width * height];

        Vision.rgb2hsl(rgb, hsl, width, height);

        for (int i = 0; i < width * height; i++) {
            int h = (rgb[i] >> 16) & 0xFF;
            int s = (rgb[i] >> 8)  & 0xFF;
            int l = (rgb[i])       & 0xFF;

            if (h >= 120 && h < 140 || h <= 215 && h > 190)
            {
                s *= (0.000000410256 * Math.pow(h, 4) - 0.000274872 * Math.pow(h, 3) + 0.0685359 * Math.pow(h, 2) - 7.53578 * h + 308.285);
                if (s < 0) s = 0;    
            }
            else if (h >= 140 && h <= 190) s = 0;

            //Shift most green up to where blue used to be
            if (h > 42 && h < 120) h += 70; 

            hsl[i] = 0xFF000000 | (h << 16) | (s << 16) | (l);
        }

        Vision.hsl2rgb(hsl, filtered, width, height);
    }
}

//Vision helper library
public class Vision {

    //Converts an array from YUV420 into RGB and stores it in rgb[]
	static public void yuv4202rgb(byte[] yuv420sp, int[] rgb, int width, int height) {
        final int frameSize = width * height;
        
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                
                if (r < 0) r = 0; else if (r > 262143) r = 262143;
                if (g < 0) g = 0; else if (g > 262143) g = 262143;
                if (b < 0) b = 0; else if (b > 262143) b = 262143;
                
                rgb[yp] = 0xFF000000 | ((r << 6) & 0xFF0000) | ((g >> 2) & 0xFF00) | ((b >> 10) & 0xFF);
            }
        }
    }

    //Converts a byte array from RGB to HSL and stores it in hsl[]
    static public void rgb2hsl(int[] rgb, int[] hsl, int width, int height) {
        for (int i = 0; i < width * height; i++)
        {
            double r = ((rgb[i] >> 16) & 0xFF) / 255.0f;
            double g = ((rgb[i] >> 8)  & 0xFF) / 255.0f;
            double b = ((rgb[i])       & 0xFF) / 255.0f;

            double max = Math.max(r, Math.max(g, b));
            double min = Math.min(r, Math.min(g, b));

            double c = max - min;

            double h = 0.0f, s = 0.0f, l = 0.0f;

            if (c == 0);
            else if (max == r) {
                h = (double)(g - b) / c;
                if (h < 0) h += 6.0f;
            }
            else if (max == g) {
                h = (double)(b - r) / c + 2.0f;
            }
            else if (max == b) {
                h = (double)(r - g) / c + 4.0f;
            }
            h /= 6.0f;
            l = (max + min) >> 1;
            if (c != 0) s = c / (1 - Math.abs(2.0f * l - 1.0f));

            hsl[i] = 0xFF000000 | (char)(h * 255.0f) << 16 | (char)(s * 255.0f) << 8 | (char)(l * 255.0f);
        }
    }

    //Converts a byte array from HSL to RGB and stores it in rgb[]
    static public void hsl2rgb(int[] hsl, int[] rgb, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            double h = ((rgb[i] >> 16) & 0xFF) / 255.0f;
            double s = ((rgb[i] >> 8)  & 0xFF) / 255.0f;
            double l = ((rgb[i])       & 0xFF) / 255.0f;

            double c = (1 - Math.abs(2.0f * l - 1.0f)) * s;
            double h_ = h * 6.0f;
            double h_mod2 = h_;

            if (h_mod2 >= 4.0f) h_mod2 -= 4.0f;
            else if (h_mod2 >= 2.0f) h_mod2 -= 2.0f;

            double x = c * (1 - Math.abs(h_mod2 - 1));
            double r = 0, g = 0, b = 0;

            if (h_ < 1)         {r = c; g = x; b = 0;}
            else if (h_ < 2)    {r = x; g = c; b = 0;}
            else if (h_ < 3)    {r = 0; g = c; b = x;}
            else if (h_ < 4)    {r = 0; g = x; b = c;}
            else if (h_ < 5)    {r = x; g = 0; b = c;}
            else                {r = c; g = 0; b = x;}

            double m = l - (c >> 1);

            r = (int)((r + m) * 255.0f + 0.5);
            g = (int)((g + m) * 255.0f + 0.5);
            b = (int)((b + m) * 255.0f + 0.5);

            rgb[i] = 0xFF000000 | (char)(r << 16) | (char)(g << 8) | (char)b;
        }
    }

}