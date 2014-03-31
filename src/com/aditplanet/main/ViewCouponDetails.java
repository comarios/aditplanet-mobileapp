package com.aditplanet.main;

import com.aditplanet.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewCouponDetails extends Activity{

	/** Text view for showing profile name */
	private TextView profileName;
	
	/** Text view for showing profile description */
	private TextView profile;
	
	/** Text view for showing number of access points */
	private TextView numberOfAP;
	
	/** Text view for showing number of samples*/
	private TextView numberOfSamples;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_coupon_details);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {

		}

		//Set-up text views.
		profileName = (TextView) findViewById(R.id.show_profile_name);
		profile = (TextView) findViewById(R.id.show_profile_details);
		numberOfAP = (TextView) findViewById(R.id.show_number_of_access_points);
		numberOfSamples = (TextView) findViewById(R.id.show_number_of_samples);
		
		
		//Show information.
		profileName.setText(extras.getString("profile_name"));
		profile.setText(extras.getString("profile_desc"));
		numberOfAP.setText(extras.getString("no_of_ap"));
		numberOfSamples.setText(extras.getString("no_of_samples"));
		
	}
}
