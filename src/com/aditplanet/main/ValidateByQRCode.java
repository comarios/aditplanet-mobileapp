package com.aditplanet.main;

import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	private CameraPreview mPreview = null;
	private Handler autoFocusHandler;
	// private FragmentServiceReceiver fragmentService;
	// private IntentFilter fragmentFilter;
	private static final String COUPON_NOT_FOUND = "Error:Coupon not found";
	private static final String COUPON_ALREADY_VALIDATED = "Error:Coupon already validated";
	private static final String COUPON_SUCCESS = "success";
	private static final String COUPON_NOT_AUTHORISED = "Error:Coupon not for merchant";
	private static final String COUPON_NOT_AUTHORISED_DIALOG = "Error: The coupon code is not valid for this merchant.";
	private static final String COUPON_NOT_FOUND_DIALOG = "Error: The coupon code is invalid.";
	private static final String COUPON_ALREADY_VALIDATED_DIALOG = "Error: The coupon has been already validated.";
	private static final String COUPON_SUCCESS_DIALOG = "The coupon has successfully validated.";
	private static final String COUPON_NETWORK_ERROR = "Network error has occurred. Please try again.";

	private Boolean comesFromResume;
	
	ImageView qrWrapper;
	TextView scanText;
	Button scanButton;

	ImageScanner scanner;

//	FrameLayout preview;
    RelativeLayout preview;
    private Handler startCameraHandler = new Handler();
    private Handler stopCameraHandler = new Handler();

	
	private View rootView;
	
	

	private boolean barcodeScanned = false;
	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getActivity().setProgressBarIndeterminateVisibility(false);

		this.rootView = inflater.inflate(R.layout.fragment_qrcode, container,
				false);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		this.preview = (RelativeLayout)rootView.findViewById(R.id.fragment_qrcode);

		
//		this.getOnResumeState();
//		
//		System.out.println("Comes from Camera: " + comesFromResume);
//		
//		if(comesFromResume)
//		{
//			startCamera();
//			
//			comesFromResume = false;
//			System.out.println("On create fragment " + comesFromResume);
////			this.saveOnResumeState();
//		}
//		else
//		{
//			comesFromResume = false;
//		}
		
//		autoFocusHandler = new Handler();
//		mCamera = getCameraInstance();
//
//		// Instance barcode scanner
//		scanner = new ImageScanner();
//		scanner.setConfig(0, Config.X_DENSITY, 3);
//		scanner.setConfig(0, Config.Y_DENSITY, 3);
//
//		mPreview = new CameraPreview(getActivity().getApplicationContext(),
//				mCamera, previewCb, autoFocusCB);
//		this.preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
//		this.preview.addView(mPreview);
//
//		qrWrapper = new ImageView(rootView.getContext());
//		qrWrapper.setImageDrawable(rootView.getResources().getDrawable(
//				R.drawable.qr_wrapper));
//
//		this.preview.addView(qrWrapper);
				
		scanText = (TextView) rootView.findViewById(R.id.scanText);

//		scanButton = (Button) rootView.findViewById(R.id.ScanButton);
		
		
		scanButton = new Button(getActivity());

		scanButton.setOnClickListener(new OnClickListener() {
			@Override
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
	
	@Override
	public void onPause() {
		
		super.onPause();
		
		this.stopAsynchCamera();
		
		
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(0);
		} catch (Exception e) {
			System.out.println("Can't open camera: " + e.getMessage());
		}
		return c;
	}

	public void releaseCamera() {
		if (mCamera != null) {
			System.out.println("Camera released");
//			mPreview = null;
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
		@Override
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		@Override
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
		@Override
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
		
		final String couponCode = RemoteParser.parseCouponAfterScan(data);

		params.put("m_name", user.getUsername());
		params.put("m_pass", user.getPassword());
		params.put("c_key", couponCode);


		WebClient.get("merchants_api.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// Pull out the first event on the public timeline
						try {
							JSONObject json = new JSONObject(response);

							System.out.println("JSON COUPON CODE: " + response);
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
							} else if (json.getString("res").equals(COUPON_NOT_AUTHORISED)){
								
										CouponsManager.getInstance()
										.setValidStatusByCouponNumber(
												couponCode);
								new Dialogs().createDialogQRValidation(
										getActivity(), getActivity()
												.getApplicationContext(),
												COUPON_NOT_AUTHORISED_DIALOG,
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

	private void continueCamera()
	{
		if(mCamera == null)
		{
			return;
		}
		barcodeScanned = false;
		scanText.setText("Please wait. We are scanning...");
		mCamera.setPreviewCallback(previewCb);
		mCamera.startPreview();
		previewing = true;
		mCamera.autoFocus(autoFocusCB);
	}
	
	private void pauseCamera()
	{
		if(mCamera == null)
		{
			return;
		}
		
		previewing = false;
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
	}
	
    private final Runnable mLoadCamera = new Runnable()
    {
        public void run()
        {
            startAsynchCamera();
        }
    };
    
    private final Runnable mUnloadCamera = new Runnable() {
		
		@Override
		public void run() {

			stopAsynchCamera();
		}
	};
	
	/**
	 * Camera managers methods.
	 */
	
	public void startCamera()
	{
		startCameraHandler.postDelayed(mLoadCamera, 1000);
	}
	
	private void startAsynchCamera()
	{
		System.out.println("startAsynchCamera mPreview: " + mPreview);

		if(mPreview != null)
		{			
			continueCamera();
		}
		else
		{
			autoFocusHandler = new Handler();
			System.out.println("Camera" + mCamera);

			mCamera = getCameraInstance();

			// Instance barcode scanner
			scanner = new ImageScanner();
			scanner.setConfig(0, Config.X_DENSITY, 3);
			scanner.setConfig(0, Config.Y_DENSITY, 3);
			
			mPreview = new CameraPreview(getActivity().getApplicationContext(),
					mCamera, previewCb, autoFocusCB);

//			this.preview = (FrameLayout) rootView.findViewById(R.id.cameraPreview);
			this.preview.addView(mPreview);
			
//			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
//
//			qrWrapper = new ImageView(mPreview.getContext());
////			qrWrapper.getLayoutParams().height = this.preview.getLayoutParams().height;
//			qrWrapper.setLayoutParams(layoutParams);
			
			this.configureQrFrameImageView();
			this.configureScanButton();
			

			this.preview.addView(scanButton);
			
			this.preview.addView(qrWrapper);
		}
	}
	
	private void stopAsynchCamera()
	{
		System.out.println("stopAsynchCamera mPreview: " + mPreview);
		
		if(mPreview == null)
		{
			this.releaseCamera();
		}
		else
		{

			pauseCamera();
		}
	}
	
	public void stopCamera()
	{
		stopCameraHandler.postDelayed(mUnloadCamera, 1000);
		
	}
	
	/**
	 * UI Configuration.
	 */
	
	private void configureQrFrameImageView()
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.preview.getLayoutParams().width, this.preview.getLayoutParams().height);

		qrWrapper = new ImageView(mPreview.getContext());
//		qrWrapper.getLayoutParams().height = this.preview.getLayoutParams().height;
		qrWrapper.setLayoutParams(layoutParams);
		qrWrapper.setImageDrawable(rootView.getResources().getDrawable(
				R.drawable.qr_wrapper));
	}
	
	private void configureScanButton()
	{
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.preview.getLayoutParams().width, 100);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		scanButton.setLayoutParams(layoutParams);
		scanButton.setText("Scan");
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
