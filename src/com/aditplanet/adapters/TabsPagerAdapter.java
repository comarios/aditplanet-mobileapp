package com.aditplanet.adapters;

import java.util.HashMap;
import java.util.Map;

import com.aditplanet.main.ValidateByQRCode;
import com.aditplanet.main.MyCoupons;
import com.aditplanet.main.ValidateByCouponNumber;
import com.aditplanet.main.Validation;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
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

			// ValidateByCouponNumber fragment activity
			return new ValidateByCouponNumber();
		case 1:
			// ValidateByQRCode fragment activity
			return new ValidateByQRCode();
		case 2:
			// MyCoupons activity
			return new MyCoupons();
		}

		return null;
	}
	
	@Override
	public void restoreState (Parcelable state, ClassLoader loader)
	{
		System.out.println("restoreState");
	}

	
	
	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
	
 

}
