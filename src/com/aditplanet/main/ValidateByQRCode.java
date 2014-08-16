package com.aditplanet.main;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.aditplanet.R;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.qrcode.CameraPreview;
import com.aditplanet.utils.Dialogs;
import com.aditplanet.utils.Messages;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ValidateByQRCode extends Fragment { // implements Observer {

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	// private FragmentServiceReceiver fragmentService;
	// private IntentFilter fragmentFilter;
	private static final String COUPON_NOT_FOUND = "Error:Coupon not found";
	private static final String COUPON_ALREADY_VALIDATED = "Error:Coupon already validated";
	private static final String COUPON_SUCCESS = "success";
	private static final String COUPON_NOT_FOUND_DIALOG = "An error occurred. The coupon code is invalid.";
	private static final String COUPON_ALREADY_VALIDATED_DIALOG = "An error occurred. The coupon has been already validated.";
	private static final String COUPON_SUCCESS_DIALOG = "The coupon has successfully validated.";
	private static final String COUPON_NETWORK_ERROR = "Network error has occurred. Please try again.";

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

		//System.out.println("camera is up");
		
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
		qrWrapper.setImageDrawable(rootView.getResources().getDrawable(
				R.drawable.qr_wrapper));

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
		releaseCamera();
		super.onPause();
	}

	// private void setUpCameraElements() {
	//
	// }
	//
	// private void releaseCameraElements() {
	//
	// }

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

	public void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			preview.removeView(mPreview);
			mCamera.release();
			mCamera = null;
		}
	}

	public void unblockCamera() {

		scanText.setText("Please wait. We are scanning...");
		mCamera.setPreviewCallback(previewCb);
		mCamera.startPreview();
		previewing = true;
		mCamera.autoFocus(autoFocusCB);

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
				String couponData = null;
				for (Symbol sym : syms) {
					// scanText.setText("barcode result " + sym.getData());
					couponData = sym.getData();
					// System.out.println("barcode result " + sym.getData());
					barcodeScanned = true;
				}

				// Take data of the qr-code and send them to the server in order
				// to validate the coupon.
				validateCouponWithData(couponData);
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * Client methods.
	 */

	private void validateCouponWithData(String data) {
		RequestParams params = new RequestParams();

		User user = CouponsManager.getInstance().getUser();

		System.out.println("Username: " + user.getUsername() + " Password: "
				+ user.getPassword());

		params.put("m_name", user.getUsername());
		params.put("m_pass", user.getPassword());
		params.put("c_key", RemoteParser.parseCouponAfterScan(data));

		final String couponCode = RemoteParser.parseCouponAfterScan(data);

		WebClient.get("merchants_api.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// Pull out the first event on the public timeline
						try {
							JSONObject json = new JSONObject(response);

							if (json.getString("res").equals(COUPON_NOT_FOUND)) {
								new Dialogs().createDialogQRValidation(
										getActivity(), getActivity()
												.getApplicationContext(),
										COUPON_NOT_FOUND_DIALOG,
										ValidateByQRCode.this);

							} else if (json.getString("res").equals(
									COUPON_ALREADY_VALIDATED)) {
								new Dialogs().createDialogQRValidation(
										getActivity(), getActivity()
												.getApplicationContext(),
										COUPON_ALREADY_VALIDATED_DIALOG,
										ValidateByQRCode.this);

							} else if (json.getString("res").equals(
									COUPON_SUCCESS)) {

								CouponsManager.getInstance()
										.setValidStatusByCouponNumber(
												couponCode);
								new Dialogs().createDialogQRValidation(
										getActivity(), getActivity()
												.getApplicationContext(),
										COUPON_SUCCESS_DIALOG,
										ValidateByQRCode.this);
							}

							//
							// Messages.showToash("valid, JSON: "+json);
							// System.out.println("jsonQRCODE: " + json);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						System.out.println(error.getMessage());
						// TODO: Add message for network failure
						Messages.showNetworkError();
					}
				});

	}

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
	// public class FragmentServiceReceiver extends BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // TODO Auto-generated method stub
	//
	// Boolean releaseCamera = intent.getBooleanExtra("releaseCamera",
	// false);
	// if(!releaseCamera){
	//
	// }
	// // if(releaseCamera){
	// // releaseCamera();
	// // }
	// System.out.println("on receive:" + releaseCamera);
	// }
	// }
}
