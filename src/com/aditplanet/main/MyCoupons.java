package com.aditplanet.main;

import java.util.HashMap;
import java.util.List;

import com.aditplanet.R;
import com.aditplanet.adapters.LazyAdapter;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyCoupons extends Fragment {

	private ListView listView;
	private LazyAdapter lAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_coupons,
				container, false);
		lAdapter = new LazyAdapter(getActivity());

		listView = (ListView) rootView.findViewById(R.id.list);
		setDataToAdapter();

		/**
		 * Inner class responsible to listens possible touch on list view.
		 */
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// TODO: Fix the view_coupons_details.xml and the code below
				Coupons coupons = CouponsManager.getInstance()
						.getCouponsByIndex(arg2);

				Intent viewCoupon = new Intent(getActivity(),
						ViewCouponDetails.class);
				viewCoupon.putExtra("couponCode", coupons.getCode());
				viewCoupon.putExtra("couponDetails",
						coupons.getCoupon_details());
				viewCoupon.putExtra("couponMerchant", coupons.getMerchant());
				viewCoupon.putExtra("couponValidStatus",
						coupons.getValid_status());
				viewCoupon.putExtra("couponValidDate", coupons.getValid_date());
				viewCoupon.putExtra("couponValidStartDate",
						coupons.getValid_start_date());
				viewCoupon.putExtra("couponValidEndDate",
						coupons.getValid_end_date());
				System.out.println("before sent: " + coupons);

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

			// Adding HashList to ArrayList
			this.lAdapter.addToContentList(map);
			map.clear();
		}

		listView.setAdapter(lAdapter);

	}

}
