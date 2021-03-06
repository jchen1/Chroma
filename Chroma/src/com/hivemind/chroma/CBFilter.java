package com.hivemind.chroma;

//Provides static functions to shift the color spectrum, allowing colorblind
//people to differentiate colors they would be unable to otherwise tell apart
public class CBFilter {
	static{
		System.loadLibrary("native");
	}
  public static native void filterRedGreen(int[] data, int[] filtered, int width, int height);
  public static native void filterRedGreenTwo(int[] data, int[] filtered, int width, int height);
  
}