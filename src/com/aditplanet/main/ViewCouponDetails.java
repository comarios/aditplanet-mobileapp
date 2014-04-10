package com.aditplanet.main;

import com.aditplanet.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewCouponDetails extends Activity {

	private TextView couponCode;
	private TextView couponDetails;
	private TextView couponMerchant;
	private TextView couponValidStatus;
	private TextView couponValidDate;
	private TextView couponValidStartDate;
	private TextView couponValidEndDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coupon_details);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

		}

		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Set-up text views.
		couponCode = (TextView) findViewById(R.id.details_couponCode);
		couponDetails = (TextView) findViewById(R.id.details_couponDetails);
		couponMerchant = (TextView) findViewById(R.id.details_couponMerchant);
		couponValidStatus = (TextView) findViewById(R.id.details_couponValidStatus);
		couponValidDate = (TextView) findViewById(R.id.details_couponValidDate);
		couponValidStartDate = (TextView) findViewById(R.id.details_couponValidStartDate);
		couponValidEndDate = (TextView) findViewById(R.id.details_couponValidEndDate);

		// Show information.
		couponCode.setText(extras.getString("couponCode"));
		couponDetails.setText(extras.getString("couponDetails"));
		couponMerchant.setText(extras.getString("couponMerchant"));
		couponValidStatus.setText(extras.getString("couponValidStatus"));
		couponValidDate.setText(extras.getString("couponValidDate"));
		couponValidStartDate.setText(extras.getString("couponValidStartDate"));
		couponValidEndDate.setText(extras.getString("couponValidEndDate"));
		
		System.out.println("sent: couponCode" + extras.getString("couponCode"));
		System.out.println("sent: couponDetails" + extras.getString("couponDetails"));
		System.out.println("sent: couponMerchant" + extras.getString("couponMerchant"));
		System.out.println("sent: couponValidStatus" + extras.getString("couponValidStatus"));
		System.out.println("sent: couponValidDate" + extras.getString("couponValidDate"));
		System.out.println("sent: couponValidStartDate" + extras.getString("couponValidStartDate"));
		System.out.println("sent: couponValidEndDate" + extras.getString("couponValidEndDate"));

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
