package com.aditplanet.login;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.aditplanet.utils.AutoLogin;
import com.aditplanet.utils.Dialogs;
import com.aditplanet.utils.Messages;
import com.aditplanet.utils.NetworkConnection;
import com.aditplanet.web.client.RemoteParser;
import com.aditplanet.web.client.WebClient;
import com.loopj.android.http.*;

public class LoginActivity extends Activity {

	private Messages messages;
	public static final String LOGIN_CREDENTIALS = "LoginCredentials";
	private NetworkConnection network = new NetworkConnection(
			LoginActivity.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		if (network.haveNetworkConnection()) {
			this.autoLoginConfiguration();

			if (AutoLogin.getAnswer(getFilesDir())) {
				this.getGredentialsFromMemoryAndSetUser();
				this.navigateToMainActivity();
			} else {

				this.intialiseObjects();

				this.constructElementsInUI();
			}
		} else {

			new Dialogs().createDialogINTERNET(LoginActivity.this,
					getApplicationContext());
		}

	}

	private void navigateToMainActivity() {
		Intent i = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(i);
	}

	private void constructElementsInUI() {
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		final EditText username = (EditText) findViewById(R.id.txtUsername);
		final EditText password = (EditText) findViewById(R.id.txtPassword);

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

	private void intialiseObjects() {
		messages = new Messages(this);

	}

	private void autoLoginConfiguration() {
		File myFileLoggedIN = new File(getFilesDir() + AutoLogin.confFile);

		if (!myFileLoggedIN.exists()) {
			AutoLogin.saveToFileLoggedIN(this, getFilesDir(),
					AutoLogin.IS_LOGGED_IN_NO);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void authentication(final String username, final String password) {

		System.out.println("authentication");
		
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

								authSuccess(username, password);
							} else {
								// Show authentication error message
								Messages.showAuthError();
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
						Messages.showNetworkError();
					}
				});
	}

	private void authSuccess(final String username, final String password) {
		AutoLogin.saveToFileLoggedIN(this, getFilesDir(),
				AutoLogin.IS_LOGGED_IN_YES);
		SharedPreferences settings = getSharedPreferences(LOGIN_CREDENTIALS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", username);
		editor.putString("password", password);

		editor.commit();

		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
	/**
	 * This method is called only when user is appeared to be logged in.
	 * That is, the autologin file (in AutoLogin class) contains YES.
	 */
	private void getGredentialsFromMemoryAndSetUser()
	{
	       SharedPreferences settings = getSharedPreferences(LOGIN_CREDENTIALS, 0);
	       String username = settings.getString("username", null);
	       String password = settings.getString("password", null);
	       
	       //Set credentials to CouponsManager.
	       CouponsManager.getInstance().setUser(new User(username, password));
	       
	}
}
