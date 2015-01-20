package com.iclickcy.main;

import java.io.File;

import com.iclickcy.R;
import com.iclickcy.login.LoginActivity;
import com.iclickcy.model.CouponsManager;
import com.iclickcy.model.User;
import com.iclickcy.utils.AutoLogin;
import com.iclickcy.utils.Dialogs;
import com.iclickcy.utils.NetworkConnection;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;
	private static boolean isUserActive = false;
	public static final String LOGIN_CREDENTIALS = "LoginCredentials";
	private NetworkConnection network = new NetworkConnection(
			WelcomeScreen.this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (network.haveNetworkConnection()) {
			this.autoLoginConfiguration();

			if (AutoLogin.getAnswer(getFilesDir())) {
				this.getGredentialsFromMemoryAndSetUser();
				this.navigateToMainActivity();
			} else {

				setContentView(R.layout.welcome_screen);

				new Handler().postDelayed(new Runnable() {

					/*
					 * Showing splash screen with a timer. This will be useful when
					 * you want to show case your app logo / company
					 */

					@Override
					public void run() {
						// This method will be executed once the timer is over
						// Start your app main activity
						Intent i = new Intent(WelcomeScreen.this,
								LoginActivity.class);
						startActivity(i);

						// close this activity
						finish();
					}
				}, SPLASH_TIME_OUT);

			}
		} else {

			new Dialogs().createDialogINTERNET(WelcomeScreen.this,
					getApplicationContext());
		}
		
//		if (isUserActive) {
//			Intent i = new Intent(this, MainActivity.class);
//			startActivity(i);
//		} else {
//			isUserActive = true;
//			setContentView(R.layout.welcome_screen);
//
//			new Handler().postDelayed(new Runnable() {
//
//				/*
//				 * Showing splash screen with a timer. This will be useful when
//				 * you want to show case your app logo / company
//				 */
//
//				@Override
//				public void run() {
//					// This method will be executed once the timer is over
//					// Start your app main activity
//					Intent i = new Intent(WelcomeScreen.this,
//							LoginActivity.class);
//					startActivity(i);
//
//					// close this activity
//					finish();
//				}
//			}, SPLASH_TIME_OUT);
//
//		}
	}

	private void autoLoginConfiguration() {
		File myFileLoggedIN = new File(getFilesDir() + AutoLogin.confFile);

		if (!myFileLoggedIN.exists()) {
			AutoLogin.saveToFileLoggedIN(this, getFilesDir(),
					AutoLogin.IS_LOGGED_IN_NO);
		}
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
	
	private void navigateToMainActivity() {
		Intent i = new Intent(WelcomeScreen.this, MainActivity.class);
		startActivity(i);
	}
}
