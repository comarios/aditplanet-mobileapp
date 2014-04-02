package com.aditplanet.main;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.aditplanet.adapters.TabsPagerAdapter;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.utils.Messages;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;

import com.aditplanet.R;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText couponCode;
	private Messages messages;
	// Tab titles
	private String[] tabs = { "Coupon Number", "QR Code", "My Coupons" };

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
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
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
		        messages.showNetworkError();
		    }
		});
		
	}
}
