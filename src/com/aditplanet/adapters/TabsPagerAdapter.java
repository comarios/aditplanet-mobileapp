package com.aditplanet.adapters;

import com.aditplanet.main.ValidateByQRCode;
import com.aditplanet.main.MyCoupons;
import com.aditplanet.main.ValidateByCouponNumber;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		
		
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new ValidateByCouponNumber();
		case 1:
			// Games fragment activity
			return new ValidateByQRCode();
		case 2:
			// Movies fragment activity
			return new MyCoupons();
		}

		return null;
	}

	
	
	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	
 

}
