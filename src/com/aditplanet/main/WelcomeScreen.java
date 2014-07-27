package com.aditplanet.main;

import com.aditplanet.R;
import com.aditplanet.login.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeScreen extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 2000;
	private static boolean isUserActive = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isUserActive) {
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
		} else {
			isUserActive = true;
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
	}

}
