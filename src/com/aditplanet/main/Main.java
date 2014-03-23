package com.aditplanet.main;

import tabs.FragmentTab1;
import tabs.FragmentTab2;
import tabs.FragmentTab3;

import com.aditplanet.R;

import android.app.ActionBar;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Main extends Activity {

	// Declare Tab Variable
    ActionBar.Tab Tab1, Tab2, Tab3;
    Fragment fragmentTab1 = new FragmentTab1();
    Fragment fragmentTab2 = new FragmentTab2();
    Fragment fragmentTab3 = new FragmentTab3();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		ActionBar actionBar = getActionBar();
		 
        // Hide Actionbar Icon
        actionBar.setDisplayShowHomeEnabled(false);
 
        // Hide Actionbar Title
        actionBar.setDisplayShowTitleEnabled(false);
 
        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
 
        // Set Tab Icon and Titles
        Tab1 = actionBar.newTab().setIcon(R.drawable.qr_icon);
        Tab2 = actionBar.newTab().setText("Coupon Number");
        Tab3 = actionBar.newTab().setText("Coupon List");
 
        // Set Tab Listeners        
        Tab1.setTabListener(new com.aditplanet.main.TabListener(fragmentTab1));
        Tab2.setTabListener(new com.aditplanet.main.TabListener(fragmentTab2));
        Tab3.setTabListener(new com.aditplanet.main.TabListener(fragmentTab3));
 
        
        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 getMenuInflater().inflate(R.menu.main, menu);
		 return true;

		//MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.activity_main_actions, menu);

		//return super.onCreateOptionsMenu(menu);
	}
	
//	 @Override
//	    public boolean onOptionsItemSelected(MenuItem item) {
//	        // Take appropriate action for each action item click
//	        switch (item.getItemId()) {
//	        case R.id.action_search:
//	            // search action
//	            return true;
//	        case R.id.action_refresh:
//	            // refresh
//	        	syncProcess();
//	            return true;
//	        case R.id.action_help:
//	            // help action
//	            return true;
//	        default:
//	            return super.onOptionsItemSelected(item);
//	        }
//	    }
//	 
//	 /**
//	     * Launching new activity
//	     * */
//	    private void syncProcess() {
//	        Toast.makeText(getApplicationContext(), "Start syncing", Toast.LENGTH_LONG).show();
//	    }

}
