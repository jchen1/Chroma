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