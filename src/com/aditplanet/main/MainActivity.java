package com.aditplanet.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aditplanet.R;
import com.aditplanet.adapters.TabsPagerAdapter;
import com.aditplanet.login.LoginActivity;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.utils.AutoLogin;
import com.aditplanet.utils.Dialogs;
import com.aditplanet.utils.Messages;
import com.aditplanet.utils.NetworkConnection;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private static int lastVisitedPage;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText couponCode;
	private Messages messages;
	private NetworkConnection network = new NetworkConnection(MainActivity.this);
	// Tab titles
	private String[] tabs = {"COUPON CODE","QR CODE","ALL COUPONS"};// {"COUPON CODE","QR CODE SCANNER","ALL COUPONS"}{ "VALIDATE COUPONS", "ALL COUPONS" };//
																	// {
	// "COUPON CODE",
	// "QR CODE SCANNER",
	// "ALL COUPONS"
	// };
	private final int VALIDATE_BY_QRCODE = 1;
	public static final String FRAGMENT_UPDATE = "com.aditplanet.main.MainActivity.FRAGMENT_UPDATE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);

		System.out.println("MainActivity : onCreate lastvisited: "
				+ lastVisitedPage);

		if (network.haveNetworkConnection()) {

			actionBar = getActionBar();
			mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
			messages = new Messages(this);

			viewPager.setAdapter(mAdapter);

			actionBar.setHomeButtonEnabled(false);
			// Hide Actionbar Title
			actionBar.setDisplayShowTitleEnabled(true);

			int titleId = getResources().getIdentifier("action_bar_title",
					"id", "android");

			TextView txtTitle = (TextView) findViewById(titleId);

			txtTitle.setTextColor(Color.BLACK);

			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			if (CouponsManager.getInstance().isCouponsListEmpty()) {
				getCouponsFromAPI();
			}

			// Adding Tabs
			for (String tab_name : tabs) {
				actionBar.addTab(actionBar.newTab().setText(tab_name)
						.setTabListener(this));
			}

			/*
			 * Configure first page to display xml for validateByCouponCode
			 */
//			RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutValidation);
//			View loadCouponCodeXML;
//		
//			LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
//			loadCouponCodeXML = inflater.inflate(R.layout.fragment_coupon_number, null);
//
//			
//			//if (layout.getChildCount() == 0) {
//				layout.addView(loadCouponCodeXML);
//			//}
			/**
			 * on swiping the viewpager make respective tab selected
			 * */
			viewPager
					.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

						@Override
						public void onPageSelected(int position) {
							// on changing the page
							// make respected tab selected

							System.out
									.println("MainActivity : onPageSelected position "
											+ position);
							lastVisitedPage = position;

							actionBar.setSelectedNavigationItem(position);
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}

					});

			// We are doing that in order to navigate to the third view
			// when we navigate to a current coupon.
			viewPager.setCurrentItem(lastVisitedPage);

		} else {

			new Dialogs().createDialogINTERNET(MainActivity.this,
					getApplicationContext());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		// return true;

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Take appropriate action for each action item click
		switch (item.getItemId()) {
		case R.id.action_logout:
			logout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onPause() {
		System.out.println("Main Activity : onPause");

		super.onPause();

	}

	public void onResume() {
		super.onResume();

		System.out.println("Main Activity : onResume");

	}

	public void logout() {
		AutoLogin.saveToFileLoggedIN(this, getFilesDir(),
				AutoLogin.IS_LOGGED_IN_NO);
		CouponsManager.getInstance().clearData();
		super.finish();
		this.finish();
		Intent i = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(i);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		// configureCameraElements((tab.getPosition() == VALIDATE_BY_QRCODE));

		updateMyCouponsFragmentIfNeeded();

		viewPager.setCurrentItem(tab.getPosition());

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void btnValidateByCouponCode(View view) {
		couponCode = (EditText) findViewById(R.id.txtCouponCode);
		validationByCouponCode(couponCode.getText().toString());

		// Toast.makeText(getApplicationContext(), "Send request to server!",
		// Toast.LENGTH_LONG).show();
	}

	// private void configureCameraElements(boolean cameraTabSelected)
	// {
	// Intent intent = new Intent(FRAGMENT_UPDATE);
	//
	// if(!cameraTabSelected)
	// {
	// //Send notification to ValidateByQRCode in order to release camera
	// objects.
	// intent.putExtra("releaseCamera", true);
	// }
	// else
	// {
	// System.out.println("Send notification.");
	// intent.putExtra("releaseCamera", false);
	// }
	// sendBroadcast(intent);
	// }

	private void validationByCouponCode(final String couponCode) {
		RequestParams params = new RequestParams();

		User user = CouponsManager.getInstance().getUser();
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
							System.out.println("couponCode: " + couponCode);
							CouponsManager.getInstance()
									.setValidStatusByCouponNumber(couponCode);

							Toast.makeText(getApplicationContext(),
									"json: " + "valid", Toast.LENGTH_LONG)
									.show();
							System.out.println("json: " + json);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
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

	private void getCouponsFromAPI() {

		SharedPreferences settings = getSharedPreferences(
				LoginActivity.LOGIN_CREDENTIALS, 0);
		final String username = settings.getString("username", null);
		final String password = settings.getString("password", null);

		RequestParams params = new RequestParams();
		params.put("m_name", username);
		params.put("m_pass", password);

		// params.put("m_name", "S1");
		// params.put("m_pass", "S1");
		WebClient.get("merchants_api.php", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String coupons) {
						// Pull out the first event on the public timeline
						try {

							JSONObject json = new JSONObject(coupons);

							if (RemoteParser.isAuth(json)) {
								System.out
										.println("User Authenticated successful: "
												+ username);
								List<Coupons> couponsList = RemoteParser
										.getCoupons(json);
								CouponsManager.getInstance().setCoupons(
										couponsList);

								// Store User details
								User user = new User(username, password);
								CouponsManager.getInstance().setUser(user);

								// for (Coupons cp : couponsList){
								// System.out.println(cp);
								// }

							} else {
								// Show authentication error message
								messages.showAuthError();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						System.out.println(error.getMessage());
						// TODO: Add message for network failure
						messages.showNetworkError();
					}
				});
	}

	/**
	 * 
	 * UI updates.
	 */

	private void updateMyCouponsFragmentIfNeeded() {
		MyCoupons fragment = (MyCoupons) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.pager + ":2");
		if (fragment != null) // could be null if not instantiated yet
		{
			if (fragment.getView() != null) {
				// no need to call if fragment's onDestroyView()
				// has since been called.
				fragment.reloadDataRemotely(); // do what updates are required
			}
		}
	}
}
