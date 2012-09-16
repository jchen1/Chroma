package com.hivemind.chroma;

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
	static{
		System.loadLibrary("native");
	}
  public static native void filterRedGreen(int[] data, int[] filtered, int width, int height);
  
  	public static void filter(int[] data, int[] filtered, int width, int height)
  	{
  		for (int i = 0; i < height*width; i++)
  		{
  			//filtered[i] = 0xFFFFFFFF;
  			
  			int r = (data[i] >> 16) & 0xFF;
  			int g = (data[i] >> 8) & 0xFF;
  			int b = (data[i]) & 0xFF;
		  
  			int h = 0, s = 0, l = 0;
		  
  			int[] hsl = {0, 0, 0};
  			int[] rgb = {0, 0, 0};
  			
  			rgb2hsl(r, g, b, hsl);
  			
  			
  			h = hsl[0]; s = hsl[1]; l = hsl[2];
  			
  			if (h >= 120 && h < 140 || h <= 215 && h > 190)
  	        {
  	            s *= (0.000000410256 * h * h * h * h - 0.000274872 * h * h * h + 0.0685359 * h * h - 7.53578 * h + 308.285);
  	            if (s < 0) s = 0;    
  	        }
  	        else if (h >= 140 && h <= 190) s = 0;

  	        //Shift most green up to where blue used to be
  	        if (h > 42 && h < 120) h += 70; 

  	        hsl2rgb(h, s, l, rgb);

  	        filtered[i] = (0xFF000000) | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);
  	        //filtered[i] = 0xFFFFFFFF;
  			
  		}
	}
  	
  	private static void rgb2hsl(int r, int g, int b, int[] hsl)
  	{
  		int max = Math.max(r, Math.max(g, b));
  	    int min = Math.min(r, Math.min(g, b));
  	    
  	    int c = max - min;
  	    
  	    int h_prime = 0;
  	    if (c == 0);
  	    else if (max == r)
  	    {
  	        h_prime = ((g - b) << 8) / (c);
  	        if (h_prime < 0) h_prime += (6 << 8);
  	    }
  	    else if (max == g)
  	    {
  	        h_prime = ((b - r) << 8) / (c);
  	        h_prime += (2 << 8);
  	    }
  	    else if (max == b)
  	    {
  	        h_prime = ((r - g) << 8) / (c);
  	        h_prime += (4 << 8);
  	    }
  	    hsl[0] = (h_prime / 6) - (1 << 7);
  	    hsl[2] = (max + min) >> 1;
  	    hsl[1] = 0;
  	    if (c != 0)
  	    {
  	        int divisor = 1 - Math.abs(2 * hsl[2] - 1);
  	        hsl[1] = (divisor == 0 ? 255 : (c << 8) / (divisor));
  	    }
  	    
  	    hsl[0] = clamp(hsl[0], 0, 255);
  	    hsl[1] = clamp(hsl[1], 0, 255);
  	    hsl[2] = clamp(hsl[2], 0, 255);
  	}
  	
  	private static void hsl2rgb(int h, int s, int l, int[] rgb)
  	{
  		int v = (l < 128) ? (l * (256 + s)) >> 8 : (((l + s) << 8) - l * s) >> 8;
  	    if (v <= 0)
  	    {
  	        rgb[0] = rgb[1] = rgb[2] = 0;
  	    }
  	    else
  	    {
  	        int m, sextant, fract, vsf, mid1, mid2;
  	        
  	        m = l + l - v;
  	        h *= 6;
  	        sextant = h >> 8;
  	        fract = h - (sextant << 8);
  	        vsf = v * fract * (v - m) / v >> 8;
  	        mid1 = m + vsf;
  	        mid2 = v - vsf;
  	        switch (sextant)
  	        {
  	            case 0: rgb[0] = v; rgb[1] = mid1; rgb[2] = m; break;
  	            case 1: rgb[0] = mid2; rgb[1] = v; rgb[2] = m; break;
  	            case 2: rgb[0] = m; rgb[1] = v; rgb[2] = mid1; break;
  	            case 3: rgb[0] = m; rgb[1] = mid2; rgb[2] = v; break;
  	            case 4: rgb[0] = mid1; rgb[1] = m; rgb[2] = v; break;
  	            case 5: rgb[0] = v; rgb[1] = m; rgb[2] = mid2; break;
  	        }
  	    }
  	    
  	    rgb[0] = clamp(rgb[0], 0, 255);
  	    rgb[1] = clamp(rgb[1], 0, 255);
  	    rgb[2] = clamp(rgb[2], 0, 255);
  	}
  	
  	private static int clamp(int x, int min, int max)
  	{
  		return Math.max(min, Math.min(x, max));
  	}
}