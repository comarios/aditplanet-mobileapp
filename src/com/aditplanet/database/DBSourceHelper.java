package com.aditplanet.database;

import java.util.Date;

import com.aditplanet.database.DBSQLiteHelper.DbCouponsColumns;
import com.aditplanet.model.Coupons;

import android.content.ContentValues;
import android.database.Cursor;

public class DBSourceHelper 
{
	/**
	 * Method has as parameters all the coupons' attributes and returns ContentValues object
	 * that is used to insert a row into a database table.
	 * 
	 * @param code
	 * @param validStatus
	 * @param mergant
	 * @param details
	 * @param validStartDate
	 * @param validEndDate
	 * @return
	 */
	public static ContentValues generateDBValuesForCoupon(Coupons coupon)
	{
		ContentValues values = new ContentValues();

		// Add all the variables except key.
		values.put(DbCouponsColumns.C_CODE, coupon.getCode());
		values.put(DbCouponsColumns.C_VALID_STATUS, coupon.getValid_status());
		values.put(DbCouponsColumns.C_VALID_DATE, coupon.getValid_date().toString());
		values.put(DbCouponsColumns.C_MERGANT, coupon.getMerchant());
		values.put(DbCouponsColumns.C_DETAILS, coupon.getCoupon_details());
		values.put(DbCouponsColumns.C_VALID_START_DATE, coupon.getValid_start_date().toString());
		values.put(DbCouponsColumns.C_VALID_END_DATE, coupon.getValid_end_date().toString());
		
		return values;
	}
	
	public static Coupons cursorToCoupon(Cursor cursor)
	{
		Coupons coupon = new Coupons(cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

		return coupon;
	}

}
