package com.aditplanet.main;

import java.util.HashMap;
import java.util.List;

import com.aditplanet.R;
import com.aditplanet.adapters.LazyAdapter;
import com.aditplanet.adapters.SubTabsPagerAdapter;
import com.aditplanet.adapters.TabsPagerAdapter;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MyCoupons extends Fragment {

	private ListView listView;
	private LazyAdapter lAdapter;
	private String[] tabs = { "COUPON CODE", "QR CODE SCANNER", "ALL COUPONS" };
	private ViewPager subViewPager;
	private SubTabsPagerAdapter submAdapter;
	private ActionBar subActionBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_coupons,
				container, false);
		lAdapter = new LazyAdapter(getActivity());

		listView = (ListView) rootView.findViewById(R.id.list);
		setDataToAdapter();

		// subViewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		// subActionBar = getActivity().getActionBar();
		// submAdapter = new SubTabsPagerAdapter(getActivity()
		// .getSupportFragmentManager());
		//
		// System.out.println("subViewPager: " + subViewPager);
		// if(subViewPager != null)
		// subViewPager.setAdapter(submAdapter);
		//
		// subActionBar.setHomeButtonEnabled(false);
		//
		// // Hide Actionbar Title
		// subActionBar.setDisplayShowTitleEnabled(false);
		// subActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//
		// // Adding Tabs
		// for (String tab_name : tabs) {
		// subActionBar.addTab(subActionBar.newTab().setText(tab_name)
		// .setTabListener(this));
		// }

		/**
		 * Inner class responsible to listens possible touch on list view.
		 */
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO: Fix the view_coupons_details.xml and the code below
				// Coupons coupons = CouponsManager.getInstance()
				// .getCouponsByIndex(arg2);

				Intent viewCoupon = new Intent(getActivity(),
						ViewCouponDetails.class);
				viewCoupon.putExtra("couponIdx", arg2);
				// viewCoupon.putExtra("couponCode", coupons.getCode());
				// viewCoupon.putExtra("couponDetails",
				// coupons.getCoupon_details());
				// viewCoupon.putExtra("couponMerchant", coupons.getMerchant());
				// viewCoupon.putExtra("couponValidStatus",
				// coupons.getValid_status());
				// viewCoupon.putExtra("couponValidDate",
				// coupons.getValid_date());
				// viewCoupon.putExtra("couponValidStartDate",
				// coupons.getValid_start_date());
				// viewCoupon.putExtra("couponValidEndDate",
				// coupons.getValid_end_date());
				// System.out.println("before sent: " + coupons);

				startActivity(viewCoupon);
			}
		});

		return rootView;
	}

	private void setDataToAdapter() {

		lAdapter.clearContentList();

		List<Coupons> coupons = CouponsManager.getInstance().getCoupons();

		HashMap<String, String> map = new HashMap<String, String>();

		// System.out.prinln(coupons);

		// Add elements to map.
		for (Coupons cp : coupons) {
			// map.put(LazyAdapter.KEY_IMAGE_ID,
			// p.getProfileImage(ld.getProfile()));
			map.put(LazyAdapter.KEY_COUPON_CODE, cp.getCode());
			map.put(LazyAdapter.KEY_COUPON_DETAILS, cp.getCoupon_details());
			map.put(LazyAdapter.KEY_COUPON_VALIDATION, cp.getValid_status()
					.toString());
			// Adding HashList to ArrayList
			this.lAdapter.addToContentList(map);
			System.out.println("TEST1: " + cp.getCode() + ", "
					+ cp.getValid_status());
			map.clear();
		}

		listView.setAdapter(lAdapter);

	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("IS HERE ON RESUME");
		setDataToAdapter();
		lAdapter.notifyDataSetChanged();
	}
	
	//NOT WORKING!!!

//	/**
//	 * Sub-tabs buttons. On click methods
//	 * */
//
//	public void btnFilterValidated(View view) {
//
//		Toast.makeText(getActivity().getApplicationContext(), "this", Toast.LENGTH_LONG).show();
//	}
//
//	public void btnFilterNotValidated(View view) {
//
//	}
//
//	public void btnFilterAll(View view) {
//
//	}

}
