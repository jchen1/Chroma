package com.hivemind.chroma;

import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class MainPane extends Activity {
	private SurfaceView surface = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera cam = null;
	private boolean isCameraConfigured = false, showingVideo = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pane);
        
        surface = (SurfaceView)findViewById(R.id.surface);
        surfaceHolder = surface.getHolder();
        surfaceHolder.addCallback(surfaceCallback);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
    		Camera.CameraInfo info = new Camera.CameraInfo();
    		
    		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
    			Camera.getCameraInfo(i, info);
    			if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
    				cam = Camera.open(i);
    		}
    	}
    	
    	if (cam == null)
    		cam = Camera.open();
    	
    	startCapture();
    }
    
    @Override
    public void onPause() {
    	if (showingVideo)
    		cam.stopPreview();
    	
    	cam.setPreviewCallback(null);
    	cam.release();
    	cam = null;
    	showingVideo = false;
    	
    	super.onPause();
    }
    
    private void initCapture(final int width, final int height) {
    	if (cam != null && surfaceHolder.getSurface() != null) {
    		try {
    			cam.setPreviewDisplay(surfaceHolder);
    		} catch (Throwable t) {
    			Log.e("Could not set camera preview display.", "Error", t);
    			Toast.makeText(MainPane.this, t.getMessage(), Toast.LENGTH_LONG).show();
    		}
    		
    		if (!isCameraConfigured) {
    			Camera.Parameters param = cam.getParameters();
    			Camera.Size maxSize = Collections.max(param.getSupportedPreviewSizes(), new Comparator<Camera.Size>() {
    				public int compare(Camera.Size s1, Camera.Size s2) {
    					if (s1.width > width || s1.height > height || s2.width > width || s2.height > height)
    						return -1;
    					return s1.width * s1.height - s2.width * s2.height;
    				}
    			});
    			Camera.Size minSize = Collections.min(param.getSupportedPictureSizes(), new Comparator<Camera.Size>() {
    				public int compare(Camera.Size s1, Camera.Size s2) {
    					return s1.width * s1.height - s2.width * s2.height;
    				}
    			});
    			
    			if (maxSize != null && minSize != null) {
    				param.setPreviewSize(maxSize.width, maxSize.height);
    				param.setPictureSize(minSize.width, minSize.height);
    				param.setPictureFormat(ImageFormat.JPEG);
    				cam.setParameters(param);
    				isCameraConfigured = true;
    			}
    		}
    	}
    }
    
    private void startCapture() {
    	if (cam != null && isCameraConfigured) {
			cam.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera camera) {
                    int width = param.getPreviewSize().width;
                    int height = param.getPreviewSize().height;
					int[] rgbData = new int[width * height];
                    int[] filteredData = new int[width * height];

                    Vision.yuv4202rgb(rgbData, data, width, height);
                    CBFilter.filterRedGreen(rgbData, filteredData, width, height);

                    //the updated data is now in filteredData in RGB... not sure what to do with it
				}
			});
    		cam.startPreview();
    		showingVideo = true;
    	}
    }


    
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
		
		public void surfaceDestroyed(SurfaceHolder holder) {
			
		}
		
		public void surfaceCreated(SurfaceHolder holder) {
			
		}
		
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			initCapture(width, height);
			startCapture();
		}
	};
}
