package com.aditplanet.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CouponsManager {

	private static CouponsManager instance = null;
	private List<Coupons> coupons = null;
	private HashMap<String, List<Coupons>> filteredCoupons = null;
	private User user;
	private String KEY_VALIDATED = "VALIDATED";
	private String KEY_NONVALIDATED = "NONVALIDATED";
	private String KEY_ALL = "ALL";

	protected CouponsManager() {
		// Exists only to defeat instantiation.
		this.coupons = new ArrayList<Coupons>();
	}

	public static CouponsManager getInstance() {
		if (instance == null) {
			instance = new CouponsManager();
		}
		return instance;
	}

	public void setCoupons(List<Coupons> _coupons) {
		coupons = _coupons;
		configureCouponsFiltering();
	}

	public List<Coupons> getCoupons() {
		return coupons;
	}

	public Coupons getCouponsByIndex(int idx) {
		return coupons.get(idx);
	}

	public Coupons getCouponsByCode(String code) {
		for (Coupons c : coupons) {
			if (c.getCode().equals(code)) {

				return c;
			}
		}
		return null;
	}

	public void setUser(User _user) {
		user = _user;
	}

	public User getUser() {
		return user;
	}

	public boolean isCouponsListEmpty() {
		return coupons.size() == 0 ? true : false;
	}

	public void clearData() {
		coupons.clear();
		coupons = null;
	}

	/*
	 * Validates a coupon. Set valid status true.
	 */
	public void setValidStatusByCouponNumber(String couponCode) {

		for (int i = 0; i < coupons.size(); ++i) {
			if (coupons.get(i).getCode().equals(couponCode)) {

				coupons.get(i).setValid_status(true);
			}
		}
		
		configureCouponsFiltering();
	}

	public List<Coupons> getValidatedCouponsList() {
		return filteredCoupons.get(KEY_VALIDATED);
	}

	public List<Coupons> getNonValidatedCouponsList() {
		return filteredCoupons.get(KEY_NONVALIDATED);
	}

	private void configureCouponsFiltering() {

		filteredCoupons = new HashMap<String, List<Coupons>>();

		List<Coupons> validatedCoupons = new ArrayList<Coupons>();
		List<Coupons> nonValidatedCoupons = new ArrayList<Coupons>();

		for (Coupons c : coupons) {

			if (c.getValid_status()) {
				validatedCoupons.add(c);
			} else {
				nonValidatedCoupons.add(c);
			}
		}

		Collections.sort(validatedCoupons, new Comparator<Coupons>() {
			public int compare(Coupons o1, Coupons o2) {
				if (o1.getValid_date() == null || o2.getValid_date() == null)
					return 0;
				return o1.getValid_date().compareTo(o2.getValid_date());
			}
		});
		Collections.reverse(validatedCoupons);
		filteredCoupons.put(KEY_VALIDATED, validatedCoupons);
		filteredCoupons.put(KEY_NONVALIDATED, nonValidatedCoupons);
		filteredCoupons.put(KEY_ALL, coupons);

	}

}
