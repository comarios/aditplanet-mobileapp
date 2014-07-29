package com.aditplanet.main;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditplanet.R;
import com.aditplanet.qrcode.CameraPreview;

public class ValidateByQRCode extends Fragment { // implements Observer {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
//	private FragmentServiceReceiver fragmentService;
//	private IntentFilter fragmentFilter;

	ImageView qrWrapper;
	TextView scanText;
	Button scanButton;
	
	ImageScanner scanner;

	FrameLayout preview;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_qrcode, container,
				false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		mCamera = getCameraInstance();

		// Instance barcode scanner
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(getActivity().getApplicationContext(),
				mCamera, previewCb, autoFocusCB);
		this.preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
		this.preview.addView(mPreview);

		qrWrapper = new ImageView(rootView.getContext());
		qrWrapper.setImageDrawable(rootView.getResources()
				.getDrawable(R.drawable.qr_wrapper));

		this.preview.addView(qrWrapper);
		
		scanText = (TextView) rootView.findViewById(R.id.scanText);

		scanButton = (Button) rootView.findViewById(R.id.ScanButton);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanText.setText("Please wait. We are scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}
			}
		});

		return rootView;
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

//	private void setUpCameraElements() {
//
//	}
//
//	private void releaseCameraElements() {
//
//	}

	// private void setUpNotifications()
	// {
	// System.out.println("HERE, setup notifications");
	// //NotificationService.getInstance().addObserver("obs", this);
	// }

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			preview.removeView(mPreview);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					scanText.setText("barcode result " + sym.getData());
					System.out.println("barcode result " + sym.getData());
					barcodeScanned = true;
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	// @Override
	// public void update(Observable observable, Object data) {
	// // TODO Auto-generated method stub
	// System.out.println("Camera tab selected from observer: "+observable +" "+
	// data);
	// }

	/**
	 * Inner Class BroadCastReceiver, used for communication between fragments
	 * 
	 */
//	public class FragmentServiceReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//
//			Boolean releaseCamera = intent.getBooleanExtra("releaseCamera",
//					false);
//			if(!releaseCamera){
//			
//			}
//			// if(releaseCamera){
//			// releaseCamera();
//			// }
//			System.out.println("on receive:" + releaseCamera);
//		}
//	}
}
