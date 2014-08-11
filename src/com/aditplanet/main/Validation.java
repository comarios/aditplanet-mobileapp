package com.aditplanet.main;

import com.aditplanet.R;
import com.aditplanet.utils.CouponsFilters;

import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
//import android.provider.Telephony.TextBasedSmsColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Validation extends Fragment {

	private Button btnValidateByCouponCode;
	private Button btnValidateByQRCode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			final Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_validation,
				container, false);

		btnValidateByCouponCode = (Button) rootView
				.findViewById(R.id.btnValidateByCouponCode);
		btnValidateByQRCode = (Button) rootView
				.findViewById(R.id.btnValidateByQRCode);

		btnValidateByCouponCode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				RelativeLayout layout = (RelativeLayout) rootView
						.findViewById(R.id.layoutValidation);

				View loadCouponCodeXML = getLayoutInflater(savedInstanceState)
						.inflate(R.layout.fragment_coupon_number, null);
				if (layout.getChildCount() == 0) {
					layout.addView(loadCouponCodeXML);
				}

			}
		});

		btnValidateByQRCode.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		return rootView;
	}

}
