package com.aditplanet.main;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
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
import android.widget.TextView;

import com.aditplanet.R;
import com.aditplanet.qrcode.CameraPreview;

public class ValidateByQRCode extends Fragment { // implements Observer {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private FragmentServiceReceiver fragmentService;
	private IntentFilter fragmentFilter;

	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

	FrameLayout preview;

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	// Called once the parent Activity and the Fragment's UI have
	// been created.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Complete the Fragment initialization particularly anything
		// that requires the parent Activity to be initialized or the
		// Fragment's view to be fully inflated.
		System.out.println("Here onActivityCreated");
		fragmentService = new FragmentServiceReceiver();
		fragmentFilter = new IntentFilter(MainActivity.FRAGMENT_UPDATE);

		getActivity().registerReceiver(fragmentService, fragmentFilter);
	}

	@Override
	public void onStart() {
		super.onStart();
		System.out.println("Here onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("Here onResume");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);
		System.out.println("Here onSaveInstanceState");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("Here onStop");
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		System.out.println("Here onDestroyView");
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		System.out.println("Here onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		System.out.println("Here onDetach");
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

		scanText = (TextView) rootView.findViewById(R.id.scanText);

		scanButton = (Button) rootView.findViewById(R.id.ScanButton);

		scanButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (barcodeScanned) {
					barcodeScanned = false;
					scanText.setText("Scanning...");
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

	private void setUpCameraElements() {

	}

	private void releaseCameraElements() {

	}

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
	public class FragmentServiceReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			Boolean releaseCamera = intent.getBooleanExtra("releaseCamera",
					false);
//			if(releaseCamera){
//				releaseCamera();
//			}
			System.out.println("on receive:" + releaseCamera);
		}
	}
}
