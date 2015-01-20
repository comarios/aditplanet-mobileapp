package com.iclickcy.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SubTabsPagerAdapter extends FragmentPagerAdapter {

	public SubTabsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:

			// Top Rated fragment activity
			return null;
		case 1:
			// Games fragment activity
			return null;
		case 2:
			// Movies fragment activity
			return null;
		}

		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		System.out.println("restoreState");
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}