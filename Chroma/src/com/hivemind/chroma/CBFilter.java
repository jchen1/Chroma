package com.hivemind.chroma;

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
  public static native void filterRedGreen(int[] data, int[] filtered, int width, int height);
}