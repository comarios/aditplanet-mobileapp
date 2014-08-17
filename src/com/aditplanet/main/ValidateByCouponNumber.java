package com.aditplanet.main;

import org.json.JSONException;
import org.json.JSONObject;

import com.aditplanet.R;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.utils.Dialogs;
import com.aditplanet.utils.Messages;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
//import android.provider.Telephony.TextBasedSmsColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class ValidateByCouponNumber extends Fragment implements View.OnClickListener  {

	private EditText couponCode;
	private Button btnValidateByCouponCode;
	private ProgressBar spinner;
	
	private static final String COUPON_NOT_FOUND = "Error:Coupon not found";
	private static final String COUPON_ALREADY_VALIDATED = "Error:Coupon already validated";
	private static final String COUPON_SUCCESS = "success";
	private static final String COUPON_NOT_FOUND_DIALOG = "An error occurred. The coupon code is invalid.";
	private static final String COUPON_ALREADY_VALIDATED_DIALOG = "An error occurred. The coupon has been already validated.";
	private static final String COUPON_SUCCESS_DIALOG = "The coupon has successfully validated.";
	private static final String COUPON_NETWORK_ERROR = "Network error has occurred. Please try again.";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		final TextView txtCouponCode = (TextView) rootView.findViewById(R.id.txtCouponCode);

		
		
		View rootView = inflater.inflate(R.layout.fragment_coupon_number, container, false);
		couponCode = (EditText) rootView.findViewById(R.id.txtCouponCode);
		spinner = (ProgressBar) rootView.findViewById(R.id.progressBarValidationByCouponCode);
		btnValidateByCouponCode = (Button) rootView.findViewById(R.id.btnValidateByCouponCode);
		btnValidateByCouponCode.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btnValidateByCouponCode:
			this.validatedButtonClicked();
			break;

		default:
			break;
		}
		
	}
	
	private void validatedButtonClicked()
	{
		String code = couponCode.getText().toString();
		spinner.setVisibility(View.VISIBLE);
		validationByCouponCode(code);
	}
	
	private void validationByCouponCode(final String couponCode) {
		RequestParams params = new RequestParams();

		User user = CouponsManager.getInstance().getUser();
		params.put("m_name", user.getUsername());
		params.put("m_pass", user.getPassword());
		params.put("c_key", couponCode);

		// final ProgressDialog validationProgress = new ProgressDialog(this);
		// validationProgress.setTitle("Please Wait");
		// validationProgress.setMessage("We are validating the coupon...");
		// validationProgress.show();
	
		WebClient.get("merchants_api.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String response) {
						// Pull out the first event on the public timeline
						try {

							JSONObject json = new JSONObject(response);
							spinner.setVisibility(View.GONE);
							if (json.getString("res").equals(COUPON_NOT_FOUND)) {
								new Dialogs().createDialogValidation(
										getActivity(),
										getActivity().getApplicationContext(),
										COUPON_NOT_FOUND_DIALOG);

							} else if (json.getString("res").equals(
									COUPON_ALREADY_VALIDATED)) {
								new Dialogs().createDialogValidation(
										getActivity(),
										getActivity().getApplicationContext(),
										COUPON_ALREADY_VALIDATED_DIALOG);

							} else if (json.getString("res").equals(
									COUPON_SUCCESS)) {

								CouponsManager.getInstance()
										.setValidStatusByCouponNumber(
												couponCode);
								new Dialogs()
										.createDialogValidation(
												getActivity(),
												getActivity().getApplicationContext(),
												COUPON_SUCCESS_DIALOG);
							}

							System.out.println("couponCode: " + couponCode);

							System.out.println("json: " + json.getString("res"));
							// To dismiss the dialog
							// validationProgress.dismiss();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							spinner.setVisibility(View.GONE);
							new Dialogs()
							.createDialogValidation(
									getActivity(),
									getActivity().getApplicationContext(),
									COUPON_NETWORK_ERROR);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						System.out.println(error.getMessage());
						// TODO: Add message for network failure
						spinner.setVisibility(View.GONE);
						Messages.showNetworkError();
					}
				});

	}
}
