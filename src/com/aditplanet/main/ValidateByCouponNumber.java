package com.aditplanet.main;

import com.aditplanet.R;

import android.os.Bundle;
//import android.provider.Telephony.TextBasedSmsColumns;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ValidateByCouponNumber extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_coupon_number, container, false);
		
//		final TextView txtCouponCode = (TextView) rootView.findViewById(R.id.txtCouponCode);
//		Button btnValidateByCouponCode = (Button) rootView.findViewById(R.id.btnValidateByCouponCode);
//		
//		btnValidateByCouponCode.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				System.out.println("reached here");
//				// Perform authentication check - use aditplanet auth API
//				Toast.makeText(getActivity(), txtCouponCode.getText().toString(), Toast.LENGTH_LONG).show();
//			}
//
//		});
		
		return rootView;
	}
	

}
