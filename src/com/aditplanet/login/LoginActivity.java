package com.aditplanet.login;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.aditplanet.R;
import com.aditplanet.main.MainActivity;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.model.User;
import com.aditplanet.utils.Messages;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.*;

public class LoginActivity extends Activity {

	private Messages messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		final EditText username = (EditText) findViewById(R.id.txtUsername);
		final EditText password = (EditText) findViewById(R.id.txtPassword);
		
		messages = new Messages(this);
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				System.out.println("reached here");
				// Perform authentication check - use aditplanet auth API
				authentication(username.getText().toString(), password
						.getText().toString());
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void authentication(final String username, final String password) {

		RequestParams params = new RequestParams();
		params.put("m_name", username);
		params.put("m_pass", password);

//		params.put("m_name", "S1");
//		params.put("m_pass", "S1");
		WebClient.get("merchants_api.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String coupons) {
				// Pull out the first event on the public timeline
				 try {
					JSONObject json = new JSONObject(coupons);
					
					if(RemoteParser.isAuth(json)){
						List<Coupons> couponsList = RemoteParser.getCoupons(json);		
						CouponsManager.getInstance().setCoupons(couponsList);
						
						
						//Store User details 
						User user = new User(username, password);
						CouponsManager.getInstance().setUser(user);
						
//						for (Coupons cp : couponsList){
//							System.out.println(cp);
//						}
						authSuccess();
					}else{
						//Show authentication error message
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
		        //TODO: Add message for network failure
		        messages.showNetworkError();
		    }
		});
	}

	private void authSuccess() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
