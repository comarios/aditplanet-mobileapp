package com.aditplanet.main;

import java.util.HashMap;
import java.util.List;

import com.aditplanet.R;
import com.aditplanet.adapters.LazyAdapter;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MyCoupons extends Fragment {

	private ListView listView;
	private LazyAdapter lAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_coupons, container, false);
		lAdapter = new LazyAdapter(getActivity());
		
		listView = (ListView) rootView.findViewById(R.id.list);
		setDataToAdapter();
		return rootView;
	}

	private void setDataToAdapter(){
		
		lAdapter.clearContentList();
		
		List<Coupons> coupons = CouponsManager.getInstance().getCoupons();
		
		HashMap<String, String> map = new HashMap<String, String>();



		//Add elements to map.
		for (Coupons cp : coupons)
		{
//			map.put(LazyAdapter.KEY_IMAGE_ID, p.getProfileImage(ld.getProfile()));
			map.put(LazyAdapter.KEY_COUPON_CODE, cp.getCode());
			map.put(LazyAdapter.KEY_COUPON_DETAILS, cp.getCoupon_details());
			
			//Adding HashList to ArrayList
			this.lAdapter.addToContentList(map);
			map.clear();
		}

		listView.setAdapter(lAdapter);
		
	}

}
