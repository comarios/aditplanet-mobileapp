package com.aditplanet.web.client;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.aditplanet.model.Coupons;

public class RemoteParser {

	private static final String API_ERROR_RES = "Error:Incorrect user name and\\/or password!";

	public static Boolean isAuth(JSONObject response) {

		String res = null;
		try {
			res = response.getString("res");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		if (res.equals("")) {
			return true;
		} else if (res.equals(API_ERROR_RES)) {
			return false;
		}
		return false;
	}

	public static List<Coupons> getCoupons(JSONObject response) {

		List<Coupons> couponsList = new ArrayList<Coupons>();
		
		// Remove res from the response.
		response.remove("res");

		for (int i = 0; i < response.length(); ++i) {
			JSONObject coupon;
			try {
				coupon = response.getJSONObject(i + "");
				
				Coupons cp = new Coupons(coupon);
				couponsList.add(cp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				couponsList = null;
			}
		}

		return couponsList;

	}
	
	/**
	 * Methods used before request.
	 */
	
	public static String parseCouponAfterScan(String couponData)
	{
		System.out.println("couponData: " + couponData);
		String [] tokens = couponData.split("_");
		
		return tokens[0] + tokens[tokens.length-1];
	}
}
