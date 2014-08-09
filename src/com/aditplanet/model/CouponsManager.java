package com.aditplanet.model;

import java.util.ArrayList;
import java.util.List;

public class CouponsManager {

	private static CouponsManager instance = null;
	private List<Coupons> coupons = null;
	private User user;

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
	}
}
