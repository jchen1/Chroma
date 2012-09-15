package com.hivemind.chroma;

//Vision helper library
public class Vision {

    //Converts an array from YUV420 into RGB and stores it in rgb[]
	static public void yuv4202rgb(int[] rgb, byte[] yuv420sp, int width, int height) {
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
	
	static public void rgb2yuv(int[] rgb_in, byte[] yuv_out, int width, int height) {
		int cnt = 0;
		for (int i = 0; i < width * height; i++) {
			int rgb = rgb_in[i];
			int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
			int y, u, v;
			
			y = (int) (r *  .299000 + g *  .587000 + b *  .114000);
	        u = (int) (r * -.168736 + g * -.331264 + b *  .500000 + 128);
	        v = (int) (r *  .500000 + g * -.418688 + b * -.081312 + 128);
	        
	        if (y < 16)  y = 16;
	        if (u < 0)   u = 0;
	        if (v < 0)   v = 0;
	        if (y > 255) y = 255;
	        if (u > 255) u = 255;
	        if (v > 255) v = 255;
	        
	        yuv_out[cnt++] = (byte) y;
	        if ((i & 1) == 0) {
	        	yuv_out[cnt++] = (byte) v;
	        	yuv_out[cnt++] = (byte) u;
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
            l = (max + min) / 2;
            if (c != 0) s = c / (1 - Math.abs(2.0f * l - 1.0f));

            hsl[i] = 0xFF000000 | ((int)(h * 255.0f) << 16) | ((int)(s * 255.0f) << 8) | (int)(l * 255.0f);
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
            int r = 0, g = 0, b = 0;

            if (h_ < 1)         {r = (int) c; g = (int) x; b = 0;}
            else if (h_ < 2)    {r = (int) x; g = (int) c; b = 0;}
            else if (h_ < 3)    {r = 0; g = (int) c; b = (int) x;}
            else if (h_ < 4)    {r = 0; g = (int) x; b = (int) c;}
            else if (h_ < 5)    {r = (int) x; g = 0; b = (int) c;}
            else                {r = (int) c; g = 0; b = (int) x;}

            double m = l - (c / 2);

            r = (int)((r + m) * 255.0f + 0.5);
            g = (int)((g + m) * 255.0f + 0.5);
            b = (int)((b + m) * 255.0f + 0.5);

            rgb[i] = 0xFF000000 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
        }
    }

}