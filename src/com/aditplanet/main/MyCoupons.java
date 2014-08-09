package com.aditplanet.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aditplanet.R;
import com.aditplanet.adapters.LazyAdapter;
import com.aditplanet.adapters.SubTabsPagerAdapter;
import com.aditplanet.adapters.TabsPagerAdapter;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.utils.CouponsFilters;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
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
	private Button validatedCoupons;
	private Button nonValidatedCoupons;
	private Button allCoupons;
	private HashMap<String, List<Coupons>> filteredCoupons = null;
	private String KEY_VALIDATED = "VALIDATED";
	private String KEY_NONVALIDATED = "NONVALIDATED";
	private String KEY_ALL = "ALL";
	private CouponsFilters filters;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_coupons,
				container, false);
		lAdapter = new LazyAdapter(getActivity());

		listView = (ListView) rootView.findViewById(R.id.list);
		setDataToAdapter(CouponsManager.getInstance().getCoupons());
		filters = CouponsFilters.ALL;

		configureFiltering();

		/*
		 * Buttons for the sub-menu of ViewCouponDetails
		 */

		validatedCoupons = (Button) rootView
				.findViewById(R.id.btnValidatedCoupons);
		nonValidatedCoupons = (Button) rootView
				.findViewById(R.id.btnNotValidatedCoupons);
		allCoupons = (Button) rootView.findViewById(R.id.btnAllCoupons);

		validatedCoupons.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				setDataToAdapter(filteredCoupons.get(KEY_VALIDATED));
				filters = CouponsFilters.VALIDATED;
			}
		});

		nonValidatedCoupons.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				setDataToAdapter(filteredCoupons.get(KEY_NONVALIDATED));
				filters = CouponsFilters.NONVALIDATED;
			}
		});

		allCoupons.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				setDataToAdapter(filteredCoupons.get(KEY_ALL));
				filters = CouponsFilters.ALL;
			}
		});

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

				if (filters == CouponsFilters.VALIDATED) {
					viewCoupon.putExtra("couponCode",
							filteredCoupons.get(KEY_VALIDATED).get(arg2)
									.getCode());
				} else if (filters == CouponsFilters.NONVALIDATED) {
					viewCoupon.putExtra("couponCode",
							filteredCoupons.get(KEY_NONVALIDATED).get(arg2)
									.getCode());
				} else if (filters == CouponsFilters.ALL) {
					viewCoupon.putExtra("couponCode",
							filteredCoupons.get(KEY_ALL).get(arg2).getCode());
				}

				startActivity(viewCoupon);
			}
		});

		return rootView;
	}

	private void configureFiltering() {

		filteredCoupons = new HashMap<String, List<Coupons>>();

		List<Coupons> coupons = CouponsManager.getInstance().getCoupons();
		List<Coupons> validatedCoupons = new ArrayList<Coupons>();
		List<Coupons> nonValidatedCoupons = new ArrayList<Coupons>();

		for (Coupons c : coupons) {

			if (c.getValid_status()) {
				validatedCoupons.add(c);
			} else {
				nonValidatedCoupons.add(c);
			}
		}

		filteredCoupons.put(KEY_VALIDATED, validatedCoupons);
		filteredCoupons.put(KEY_NONVALIDATED, nonValidatedCoupons);
		filteredCoupons.put(KEY_ALL, coupons);

	}

	private void setDataToAdapter(List<Coupons> coupons) {

		lAdapter.clearContentList();

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
		setDataToAdapter(CouponsManager.getInstance().getCoupons());
		lAdapter.notifyDataSetChanged();
	}

}
