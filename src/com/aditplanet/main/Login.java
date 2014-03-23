package com.aditplanet.main;

import com.aditplanet.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		final EditText username = (EditText) findViewById(R.id.txtUsername);
		final EditText password = (EditText) findViewById(R.id.txtPassword);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Perform authentication check - use aditplanet auth API
				if (isAuthenticate(username.getText().toString(), password
						.getText().toString())) {	
					Intent intent = new Intent(getApplicationContext(), Main.class);
					startActivity(intent);
					
				}else{
					
					
				}
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean isAuthenticate(String username, String password) {

		return true;
	}
}
