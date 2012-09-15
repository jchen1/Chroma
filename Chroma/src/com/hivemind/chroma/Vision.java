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

}