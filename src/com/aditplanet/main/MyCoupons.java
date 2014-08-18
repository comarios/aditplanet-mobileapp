package com.aditplanet.main;

import java.util.HashMap;
import java.util.List;

import com.aditplanet.R;
import com.aditplanet.adapters.LazyAdapter;
import com.aditplanet.adapters.SubTabsPagerAdapter;
import com.aditplanet.callbacks.RemoteDataCallback;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;
import com.aditplanet.utils.CouponsFilters;

import android.R.color;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MyCoupons extends Fragment implements RemoteDataCallback {

	private ListView listView;
	private LazyAdapter lAdapter;
	private String[] tabs = { "COUPON CODE", "QR CODE SCANNER", "ALL COUPONS" };
	private ViewPager subViewPager;
	private SubTabsPagerAdapter submAdapter;
	private ActionBar subActionBar;
	private Button validatedCoupons;
	private Button nonValidatedCoupons;
	private Button allCoupons;
//	private HashMap<String, List<Coupons>> filteredCoupons = null;
//	private String KEY_VALIDATED = "VALIDATED";
//	private String KEY_NONVALIDATED = "NONVALIDATED";
//	private String KEY_ALL = "ALL";
	private CouponsFilters filters;
	private Boolean fragmentCreated = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		

		View rootView = inflater.inflate(R.layout.fragment_my_coupons,
				container, false);
		lAdapter = new LazyAdapter(getActivity());

		listView = (ListView) rootView.findViewById(R.id.list);
		setDataToAdapter(CouponsManager.getInstance().getCoupons());
		filters = CouponsFilters.ALL;


		
		
		//configureFiltering();

		/*
		 * Buttons for the sub-menu of ViewCouponDetails
		 */

		validatedCoupons = (Button) rootView
				.findViewById(R.id.btnValidatedCoupons);
		nonValidatedCoupons = (Button) rootView
				.findViewById(R.id.btnNotValidatedCoupons);
		allCoupons = (Button) rootView.findViewById(R.id.btnAllCoupons);

		this.reloadButtonsDesign();
		
		validatedCoupons.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//setDataToAdapter(filteredCoupons.get(KEY_VALIDATED));
				setDataToAdapter(CouponsManager.getInstance().getValidatedCouponsList());
				filters = CouponsFilters.VALIDATED;
				reloadButtonsDesign();
			}
		});

		nonValidatedCoupons.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//setDataToAdapter(filteredCoupons.get(KEY_NONVALIDATED));
				setDataToAdapter(CouponsManager.getInstance().getNonValidatedCouponsList());
				filters = CouponsFilters.NONVALIDATED;
				reloadButtonsDesign();
			}
		});

		allCoupons.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				//setDataToAdapter(filteredCoupons.get(KEY_ALL));
				setDataToAdapter(CouponsManager.getInstance().getCoupons());
				filters = CouponsFilters.ALL;
				reloadButtonsDesign();
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
							CouponsManager.getInstance().getValidatedCouponsList().get(arg2)
									.getCode());
				} else if (filters == CouponsFilters.NONVALIDATED) {
					viewCoupon.putExtra("couponCode",
							CouponsManager.getInstance().getNonValidatedCouponsList().get(arg2)
									.getCode());
				} else if (filters == CouponsFilters.ALL) {
					viewCoupon.putExtra("couponCode",
							CouponsManager.getInstance().getCoupons().get(arg2).getCode());
				}

				startActivity(viewCoupon);
			}
		});
		
		getActivity().setProgressBarIndeterminateVisibility(true);
		
		this.reloadDataRemotely();

		fragmentCreated = true;
		
		return rootView;
	}

//	private void configureFiltering() {
//
//		filteredCoupons = new HashMap<String, List<Coupons>>();
//
//		List<Coupons> coupons = CouponsManager.getInstance().getCoupons();
//		List<Coupons> validatedCoupons = new ArrayList<Coupons>();
//		List<Coupons> nonValidatedCoupons = new ArrayList<Coupons>();
//
//		for (Coupons c : coupons) {
//
//			if (c.getValid_status()) {
//				validatedCoupons.add(c);
//			} else {
//				nonValidatedCoupons.add(c);
//			}
//		}
//
//		
//		Collections.sort(validatedCoupons, new Comparator<Coupons>() {
//		    public int compare(Coupons o1, Coupons o2) {
//		    	if (o1.getValid_date() == null || o2.getValid_date() == null)
//		            return 0;
//		          return o1.getValid_date().compareTo(o2.getValid_date());
//		    }
//		});
//		Collections.reverse(validatedCoupons);
//		filteredCoupons.put(KEY_VALIDATED, validatedCoupons);
//		filteredCoupons.put(KEY_NONVALIDATED, nonValidatedCoupons);
//		filteredCoupons.put(KEY_ALL, coupons);
//
//	}

	private void setDataToAdapter(List<Coupons> coupons) {

		lAdapter.clearContentList();

		HashMap<String, String> map = new HashMap<String, String>();

		// System.out.prinln(coupons);

		// Add elements to map.
		for (Coupons cp : coupons) {
			// map.put(LazyAdapter.KEY_IMAGE_ID,
			// p.getProfileImage(ld.getProfile()));
			map.put(LazyAdapter.KEY_COUPON_CODE, cp.getCode());
			map.put(LazyAdapter.KEY_COUPON_DETAILS, cp.getValid_dateAsString());
			map.put(LazyAdapter.KEY_COUPON_VALIDATION, cp.getValid_status()
					.toString());
			// Adding HashList to ArrayList
			this.lAdapter.addToContentList(map);
//			System.out.println("TEST1: " + cp.getCode() + ", "
//					+ cp.getValid_status());
			map.clear();
		}

		listView.setAdapter(lAdapter);

	}
	
	public void reloadDataRemotely()
	{		
		System.out.println("reloadDataRemotely f created: " + this.filters);


		
		this.reloadData();
		
		CouponsManager.getInstance().remoteReloadCoupons(this);
		
//		switch (this.filters) {
//		case VALIDATED:
//			setDataToAdapter(CouponsManager.getInstance().getValidatedCouponsList());
//			break;
//			
//		case NONVALIDATED:
//			setDataToAdapter(CouponsManager.getInstance().getNonValidatedCouponsList());
//			break;
//
//		default:
//			setDataToAdapter(CouponsManager.getInstance().getCoupons());
//			break;
//		}
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("IS HERE ON RESUME");
		setDataToAdapter(CouponsManager.getInstance().getCoupons());
		lAdapter.notifyDataSetChanged();
	}

	@Override
	public void remoteDataReady() {

		System.out.println("coupons from callback");
		getActivity().setProgressBarIndeterminateVisibility(false);

		this.reloadData();
	}
	
	public void reloadData()
	{
		switch (this.filters) {
		case VALIDATED:
			setDataToAdapter(CouponsManager.getInstance().getValidatedCouponsList());
			break;
			
		case NONVALIDATED:
			setDataToAdapter(CouponsManager.getInstance().getNonValidatedCouponsList());
			break;

		default:
			setDataToAdapter(CouponsManager.getInstance().getCoupons());
			break;
		}
	}
	
	/**
	 * UI methods.
	 */

	private void reloadButtonsDesign()
	{
		makeButtonsTextWhite();
		
		if(filters == CouponsFilters.ALL)
		{
			allCoupons.setTextColor(greenColour());

		}
		else if(filters == CouponsFilters.VALIDATED)
		{
			validatedCoupons.setTextColor(greenColour());

		}
		else if(filters == CouponsFilters.NONVALIDATED)
		{
			nonValidatedCoupons.setTextColor(greenColour());

		}
	}
	
	private void makeButtonsTextWhite()
	{

		validatedCoupons.setTextColor(whiteColour());
		nonValidatedCoupons.setTextColor(whiteColour());
		allCoupons.setTextColor(whiteColour());
	}
	
	
	private int whiteColour()
	{
		return getActivity().getApplication().getResources().getColor(R.color.white);
	}
	
	private int greenColour()
	{
		return getActivity().getApplication().getResources().getColor(R.color.green);

	}
}
