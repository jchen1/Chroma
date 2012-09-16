package com.hivemind.chroma;

//Vision helper library
public class Vision {
    
	static{
		System.loadLibrary("native");
	}
	
	static public native void yuv2rgb(int[] rgb, byte[] yuv, int width, int height);

}