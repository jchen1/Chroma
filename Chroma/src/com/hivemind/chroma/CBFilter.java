package com.hivemind.chroma;

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
  static public void filterRedGreen(int[] data, int[] filtered, int width, int height) {
      for (int i = 0; i < width * height; i++) {
          int r = (data[i] >> 16) & 0xFF;
          int g = (data[i] >> 8)  & 0xFF;
          int b = (data[i])       & 0xFF;

          int hsl[] = {0, 0, 0};
          Vision.f_rgb2hsl(r, g, b, hsl);

          int h = hsl[0], s = hsl[1], l = hsl[2];

          if (h >= 120 && h < 140 || h <= 215 && h > 190)
          {
              s *= (0.000000410256 * h * h * h * h - 0.000274872 * h * h * h + 0.0685359 * h * h - 7.53578 * h + 308.285);
              if (s < 0) s = 0;    
          }
          else if (h >= 140 && h <= 190) s = 0;

          //Shift most green up to where blue used to be
          if (h > 42 && h < 120) h += 70; 

          int rgb[] = {0, 0, 0};
          Vision.f_hsl2rgb(h, s, l, rgb);

          filtered[i] = (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);
      }
  }
}