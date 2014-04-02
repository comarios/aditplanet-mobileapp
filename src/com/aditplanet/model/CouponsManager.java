package com.aditplanet.model;

import java.util.ArrayList;
import java.util.List;

public class CouponsManager {

	private static CouponsManager instance = null;
	private List<Coupons> coupons = null;
	private User user;

	protected CouponsManager() {
		// Exists only to defeat instantiation.
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

	public void setUser(User _user) {
		user = _user;
	}

	public User getUser() {
		return user;
	}
}
