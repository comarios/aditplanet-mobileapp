package com.aditplanet.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.aditplanet.web.client.DateParser;

public class Coupons {

	private String code;
	private Boolean valid_status;
	private Date valid_date;
	private String valid_dateAsString;
	private String merchant;
	private String coupon_details;
	private Date valid_start_date;
	private Date valid_end_date;

	public Coupons(JSONObject coupons) {
		try {
			this.code = coupons.getString("code");
			this.valid_status = (coupons.getString("valid_status").equals("1") ? true
					: false);
			this.valid_date = DateParser.parseDate(coupons
					.getString("valid_date"));
			this.valid_dateAsString = coupons.getString("valid_date");
			this.merchant = coupons.getString("merchant");
			this.coupon_details = coupons.getString("coupon_details");
			this.valid_start_date = DateParser.parseDate(coupons
					.getString("valid_start_date"));
			this.valid_end_date = DateParser.parseDate(coupons
					.getString("valid_end_date"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Coupons(String code, int validStatus, String validDate,
			String merchant, String couponDetails, String validStartDate,
			String validEndDate) {
		this.code = code;
		this.valid_status = (validStatus == 0) ? false : true;
		this.valid_date = DateParser.parseDate(validDate);
		this.merchant = merchant;
		this.coupon_details = couponDetails;
		this.valid_start_date = DateParser.parseDate(validStartDate);
		this.valid_end_date = DateParser.parseDate(validEndDate);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getValid_status() {
		return valid_status;
	}

	public void setValid_status(Boolean valid_status) {
		this.valid_status = valid_status;
	}

	public Date getValid_date() {
		return valid_date;
	}

	public void setValid_date(Date valid_date) {
		this.valid_date = valid_date;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public String getCoupon_details() {
		return coupon_details;
	}

	public void setCoupon_details(String coupon_details) {
		this.coupon_details = coupon_details;
	}

	public Date getValid_start_date() {
		return valid_start_date;
	}

	public void setValid_start_date(Date valid_start_date) {
		this.valid_start_date = valid_start_date;
	}

	public Date getValid_end_date() {
		return valid_end_date;
	}

	public void setValid_end_date(Date valid_end_date) {
		this.valid_end_date = valid_end_date;
	}

	public String getValid_dateAsString() {
		return valid_dateAsString;
	}

	public void setValid_dateAsString(String valid_dateAsString) {
		this.valid_dateAsString = valid_dateAsString;
	}

	@Override
	public String toString() {

		return "code: " + this.code + ", valid_status: " + this.valid_status
				+ "valid_date:" + this.valid_date + "merchant: "
				+ this.merchant + ", coupon_details: " + this.coupon_details
				+ "valid_start_date:" + this.valid_start_date
				+ "valid_end_date:" + this.valid_end_date;
	}
}
