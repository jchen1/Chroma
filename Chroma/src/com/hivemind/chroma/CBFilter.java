package com.hivemind.chroma;

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
  static public void filterRedGreen(int[] rgb, int[] filtered, int width, int height) {

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

          hsl[i] = 0xFF000000 | (h << 16) | (s << 8) | (l);
      }

      Vision.hsl2rgb(hsl, filtered, width, height);
  }
}