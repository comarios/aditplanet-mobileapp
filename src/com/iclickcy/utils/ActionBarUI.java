package com.iclickcy.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class ActionBarUI extends Activity {

//	private static final String newTitle = "iclickcy.com - The merchant's app";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		configureTitle();
	}

	private void configureTitle() {

		int titleId = getResources().getIdentifier("action_bar_title",
				"id", "android");

		TextView txtTitle = (TextView) findViewById(titleId);

		txtTitle.setTextColor(Color.BLACK);
	}
}
