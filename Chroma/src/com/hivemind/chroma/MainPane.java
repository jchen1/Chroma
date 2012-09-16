package com.hivemind.chroma;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainPane extends Activity {
	private SurfaceView surface = null;
	private SurfaceHolder surfaceHolder = null;
	private Camera cam = null;
	private boolean isCameraConfigured = false, showingVideo = false;
	public Bitmap frame = null;
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main_pane, menu);
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pane);

		surface = (SurfaceView)findViewById(R.id.surface);
		surfaceHolder = surface.getHolder();
		surfaceHolder.addCallback(surfaceCallback);

		Toast.makeText(MainPane.this, "Loading...", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_help:
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast_layout,
					(ViewGroup) findViewById(R.id.toast_layout_root));
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText("TAP SCREEN to toggle from filter mode to normal view.");
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER|Gravity.FILL_HORIZONTAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
			break;
		case R.id.menu_about:
			Dialog dialog = new Dialog(MainPane.this);

			dialog.setContentView(R.layout.custom_dialog);
			dialog.setTitle("About Chroma");

			TextView text2 = (TextView) dialog.findViewById(R.id.text2);
			text2.setText("Version 1.0 \n\nDesigned to aid color-blind persons differentiate between similarly-perceived colors.\n\nCreated by Jeff Chen, Rolando Schneiderman, Cary Yang, and Emily Yeh for PennApps 2012.");
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			break;
		default:
			return false;
		}
		return true;
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

	private Camera.Size getBestPreviewSize(List<Camera.Size> sizes, int width, int height) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double)width / height;

		Camera.Size bestSize = null;
		double minDiff = Double.MAX_VALUE;

		for (Camera.Size s : sizes) {
			double ratio = (double)s.width / s.height;
			if (Math.abs(targetRatio - ratio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(s.height - height) < minDiff) {
				bestSize = s;
				minDiff = Math.abs(s.height - height);
			}
		}

		if (bestSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Camera.Size s : sizes) {
				if (Math.abs(s.height - height) < minDiff) {
					bestSize = s;
					minDiff = Math.abs(s.height - height);
				}
			}
		}
		return bestSize;
	}
    
    private void initCapture(final int width, final int height) {
    	if (cam != null && surfaceHolder.getSurface() != null) {
    		try{
    			//cam.setPreviewDisplay(surfaceHolder);
    		} catch (Throwable t) {
    			Log.e("setPreviewDisplay failed", "Error", t);
    			Toast.makeText(MainPane.this, t.getMessage(), Toast.LENGTH_LONG).show();
    		}
    		
    		if (!isCameraConfigured) {
    			Camera.Parameters param = cam.getParameters();
    			Camera.Size maxSize = getBestPreviewSize(param.getSupportedPreviewSizes(), width, height);
    			
    			Camera.Size minSize = maxSize;
    			
    			if (maxSize != null && minSize != null) {
    				param.setPreviewSize(maxSize.width, maxSize.height);
    				cam.setParameters(param);
    				isCameraConfigured = true;
    			}
    		}
    	}
    }
    
    private void startCapture() {
    	if (cam != null && isCameraConfigured) {
    		frame = Bitmap.createBitmap(cam.getParameters().getPreviewSize().width,
    				cam.getParameters().getPreviewSize().height, Bitmap.Config.ARGB_8888);
    		surfaceHolder.setFormat(PixelFormat.RGBA_8888 | PixelFormat.OPAQUE);
			cam.setPreviewCallback(new PreviewCallback() {
				public void onPreviewFrame(byte[] data, Camera camera) {
					int width = camera.getParameters().getPreviewSize().width;
					int height = camera.getParameters().getPreviewSize().height;
					int[] rgbData = new int[width * height];
                    int[] filteredData = new int[width * height];

                    Vision.yuv2rgb(rgbData, data, width, height);
                    //CBFilter.filter(rgbData, filteredData, width, height);
                    //Toast.makeText(MainPane.this, "filtered", Toast.LENGTH_LONG).show();
                    CBFilter.filterRedGreen(rgbData, filteredData, width, height);
                    //CBSimulator.simDeuteranopia(filteredData, width, height);
                    frame.setPixels(filteredData, 0, width, 0, 0, width, height);
                    if (surfaceHolder.getSurface().isValid()) {
                    	Canvas c = surfaceHolder.lockCanvas();
                    	c.drawBitmap(frame, 0, 0, null);
                    	surfaceHolder.unlockCanvasAndPost(c);
                    }
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
