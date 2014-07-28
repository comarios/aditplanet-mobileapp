package com.aditplanet.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aditplanet.R;
import com.aditplanet.model.Coupons;
import com.aditplanet.model.CouponsManager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Responsible for preserving list view data.
 * 
 * @author Silouanos
 *
 */
public class LazyAdapter extends BaseAdapter
{
	
	/**
	 * Hashmap's keys contains all the contents of the list.
	 */
	
	public static final String KEY_COUPON_CODE = "coupon_code";
	public static final String KEY_COUPON_DETAILS = "coupon_details";
	public static final String KEY_IMAGE = "thumb_url";
	public static final String KEY_COUPON_VALIDATION = "coupon_validation";

	/** Main activity. */
	//private Activity activity;
	
	/** Layout inflater. */
	private static LayoutInflater inflater = null;
	
	/** List that contains map.
	 *	Each map contains data for each element of the list view.
	 */
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	/**
	 * Creates a new instance of LazyAdapter.
	 * 
	 * @param activity
	 * 					main activity.
	 */
	public LazyAdapter(Activity activity)
	{
		//this.activity = activity;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount()
	{
		return data.size();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}
	
	/**
	 * Adds new map to the data list and notifies that something is changed.
	 * 
	 * @param mapInc
	 * 				the new map.
	 */
	public void addToContentList(Map<String, String> mapInc)
	{
		Map<String, String> map = new HashMap<String, String>(mapInc);

		this.data.add(map);
		this.notifyDataSetChanged();
	}
	
	/**
	 * Clears content list.
	 */
	public void clearContentList()
	{
		this.data.clear();
	}
	
	/**
	 * Adds new data in the list.
	 * 
	 * @param newLocationData
	 * 						new data in the list.
	 */
	public void addToContentList(Coupons coupons)
	{
		/** Map that is used for store all data from database */
		HashMap<String, String> map = new HashMap<String, String>();

		
		//map.put(LazyAdapter.KEY_IMAGE_ID, p.getProfileImage(newLocationData.getProfile()));
		//map.put(LazyAdapter.KEY_SUBTITLE, p.getProfileMessage(newLocationData.getProfile()));
		//map.put(LazyAdapter.KEY_PROFILE_NAME, newLocationData.getLocation());
		map.put(LazyAdapter.KEY_COUPON_CODE, coupons.getCode());
		map.put(LazyAdapter.KEY_COUPON_DETAILS, coupons.getCoupon_details());
		System.out.println("HERE333: " + coupons.getValid_status());
		map.put(LazyAdapter.KEY_COUPON_VALIDATION, coupons.getValid_status().toString());
		//map.put(LazyAdapter.KEY_IMAGE, coupons.getCode());
		
		//Adding HashList to ArrayList
		this.addToContentList(map);
	}
	
	/**
	 * Gets map from the data list with the appropriate index.
	 * 
	 * @param index
	 * 				the location of the element.
	 * 					
	 * @return
	 * 			the appropriate map.
	 */
	public Map<String, String> getMapFromList(int index)
	{
		Map<String,String> temp = new HashMap<String, String>();
		temp = this.data.get(index);
		return this.data.get(index);
	}
	
	/**
	 * Removes an element from the list.
	 * 
	 * @param location
	 * 					of the element.
	 */
	public void removeMapFromList(int location)
	{
		this.data.remove(location);
		this.notifyDataSetChanged();
	}

	/**
	 * Sets characteristics into a particular element into the list view.
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);

		TextView couponCode = (TextView) vi.findViewById(R.id.coupon_code);
		TextView couponDetails = (TextView) vi.findViewById(R.id.coupon_details);
													
		ImageView thumb_image = (ImageView) vi.findViewById(R.id.list_image);

		Map<String, String> coupons = new HashMap<String, String>();
		coupons = data.get(position);

		//Setting all values in listview
		couponCode.setText(coupons.get(KEY_COUPON_CODE));
		couponDetails.setText(coupons.get(KEY_COUPON_DETAILS));
		
		if(Boolean.parseBoolean(coupons.get(KEY_COUPON_VALIDATION))){
			thumb_image.setImageResource(R.drawable.ic_validated);	
		}else{
			thumb_image.setImageResource(R.drawable.ic_not_validated);
		}
		
		
		return vi;
	}
}