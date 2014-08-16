package com.aditplanet.main;

import com.aditplanet.R;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCouponDetails extends Activity {

	// private TextView couponCode;
	// private TextView couponDetails;
	// private TextView couponMerchant;
	// private TextView couponValidStatus;
	// private TextView couponValidDate;
	// private TextView couponValidStartDate;
	// private TextView couponValidEndDate;

	private WebView webView;
	private String couponURI;
	private final String couponViewAPI = "http://aditplanet.com/coupon_view.php?id=";
	private String REDEEM_OFFLINE = "&mobile=1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coupon_details);

		Bundle extras = getIntent().getExtras();
		String couponCode = null;

		if (extras != null) {
			couponCode = extras.getString("couponCode");
		}

		//getActionBar().setDisplayHomeAsUpEnabled(true);

		int titleId = getResources().getIdentifier("action_bar_title", "id",
				"android");

		TextView txtTitle = (TextView) findViewById(titleId);

		txtTitle.setTextColor(Color.BLACK);

		int upId = getResources().getIdentifier("up", "id", "android");

		ImageView backImageView = (ImageView) findViewById(upId);

		backImageView.setImageResource(R.drawable.ic_up_button);

		// Set-up text views.
		// couponCode = (TextView) findViewById(R.id.details_couponCode);
		// couponDetails = (TextView) findViewById(R.id.details_couponDetails);
		// couponMerchant = (TextView)
		// findViewById(R.id.details_couponMerchant);
		// couponValidStatus = (TextView)
		// findViewById(R.id.details_couponValidStatus);
		// couponValidDate = (TextView)
		// findViewById(R.id.details_couponValidDate);
		// couponValidStartDate = (TextView)
		// findViewById(R.id.details_couponValidStartDate);
		// couponValidEndDate = (TextView)
		// findViewById(R.id.details_couponValidEndDate);

		// Coupons coupons = CouponsManager.getInstance().getCouponsByIndex(
		// extras.getInt("couponIdx"));

		Coupons coupons = CouponsManager.getInstance().getCouponsByCode(couponCode);
		System.out.println("after sent: coupon: " + coupons);

		// Show information.
		webView = (WebView) findViewById(R.id.couponWebView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		int IntCouponCode = Integer.parseInt(couponCode);
		int removeFirstTwoDigits = Integer.parseInt(Integer
				.toString(IntCouponCode).substring(2));

		couponURI = couponViewAPI + "00_" + removeFirstTwoDigits + "_00"
				+ REDEEM_OFFLINE;
		webView.loadUrl(couponURI);
		// couponCode.setText("Coupon Code: " + coupons.getCode());
		// couponDetails.setText("Details: " +coupons.getCoupon_details());
		// couponMerchant.setText("Merchant: " +coupons.getMerchant());
		// couponValidStatus.setText("Valid status: "
		// +coupons.getValid_status()+"");
		// couponValidDate.setText("Valid Date: " +coupons.getValid_date()+"");
		// couponValidStartDate.setText("Valid Start Date: "
		// +coupons.getValid_start_date()+"");
		// couponValidEndDate.setText("Valid End Date: "
		// +coupons.getValid_end_date()+"");

		// System.out.println("sent: couponCode" +
		// extras.getString("couponCode"));
		// System.out.println("sent: couponDetails" +
		// extras.getString("couponDetails"));
		// System.out.println("sent: couponMerchant" +
		// extras.getString("couponMerchant"));
		// System.out.println("sent: couponValidStatus" +
		// extras.getString("couponValidStatus"));
		// System.out.println("sent: couponValidDate" +
		// extras.getString("couponValidDate"));
		// System.out.println("sent: couponValidStartDate" +
		// extras.getString("couponValidStartDate"));
		// System.out.println("sent: couponValidEndDate" +
		// extras.getString("couponValidEndDate"));

	}

	@Override
	public void onBackPressed() {

		// super.onBackPressed();

		NavUtils.navigateUpFromSameTask(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		System.out.println("ViewCouponDetails : onOptionsItemSelected");

		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			System.out.println("default");

			return super.onOptionsItemSelected(item);
		}
	}
}
