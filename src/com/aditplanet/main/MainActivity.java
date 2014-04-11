package com.aditplanet.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.aditplanet.R;
import com.aditplanet.adapters.TabsPagerAdapter;
import com.aditplanet.login.LoginActivity;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.utils.AutoLogin;
import com.aditplanet.utils.Messages;
import com.aditplanet.utils.NotificationService;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText couponCode;
	private Messages messages;
	// Tab titles
	private String[] tabs = { "Coupon Number", "QR Code", "My Coupons" };
	private final int VALIDATE_BY_QRCODE = 1;
	public static final String FRAGMENT_UPDATE = "com.aditplanet.main.MainActivity.FRAGMENT_UPDATE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
	    messages = new Messages(this);
	    
		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

		
		
	   if(CouponsManager.getInstance().isCouponsListEmpty()){
		   getCouponsFromAPI();
	   }
		
		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				
				
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
			
			
		});
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
	public void onPause()
	{
		System.out.println("Main Activity : onPause");
		
		super.onPause();
		
	}
	
	public void onResume()
	{
		super.onResume();
		
		System.out.println("Main Activity : onResume");

	}

	public void logout() {
		AutoLogin.saveToFileLoggedIN(this, getFilesDir(), AutoLogin.IS_LOGGED_IN_NO);
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
		//configureCameraElements((tab.getPosition() == VALIDATE_BY_QRCODE));
		
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void btnValidateByCouponCode(View view) {
		couponCode = (EditText) findViewById(R.id.txtCouponCode);
		validationByCouponCode(couponCode.getText().toString());
		
		//Toast.makeText(getApplicationContext(), "Send request to server!", Toast.LENGTH_LONG).show();
	}
	
//	private void configureCameraElements(boolean cameraTabSelected)
//	{
//		Intent intent = new Intent(FRAGMENT_UPDATE);
//		
//		if(!cameraTabSelected)
//		{
//			//Send notification to ValidateByQRCode in order to release camera objects.
//			intent.putExtra("releaseCamera", true);
//		}
//		else
//		{
//			System.out.println("Send notification.");	
//			intent.putExtra("releaseCamera", false);		
//		}
//		sendBroadcast(intent);
//	}
	
	private void validationByCouponCode(String couponCode){
		RequestParams params = new RequestParams();
		
		User user = CouponsManager.getInstance().getUser();
		params.put("m_name", user.getUsername());
		params.put("m_pass", user.getPassword());
		params.put("c_key", couponCode);
		
		
		WebClient.get("merchants_api.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// Pull out the first event on the public timeline
				 try {
					JSONObject json = new JSONObject(response);
					
					System.out.println("json: " + json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
		    public void onFailure(Throwable error, String content) {
		        System.out.println(error.getMessage());
		        //TODO: Add message for network failure
		        Messages.showNetworkError();
		    }
		});
		
	}

	private void getCouponsFromAPI() {

		SharedPreferences settings = getSharedPreferences(LoginActivity.LOGIN_CREDENTIALS, 0);
	    final String username = settings.getString("username",null);
	    final String password = settings.getString("password",null);
	    
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
}
