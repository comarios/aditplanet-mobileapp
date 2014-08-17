package com.aditplanet.database;

import com.aditplanet.database.DBSQLiteHelper.DbCouponsColumns;
import com.aditplanet.model.Coupons;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class is responsible for adding, editing and deleting a profile entry to the database.
 * 
 * @author Silouanos
 * 
 */

public class DBSource
{	
	/** Basic operations of database. */
	private SQLiteDatabase database;
	
	/** Database Helper. */
	private DBSQLiteHelper dbHelper;


	private String[] couponsColumns = { DbCouponsColumns.C_KEY, DbCouponsColumns.C_CODE,
			DbCouponsColumns.C_VALID_STATUS, DbCouponsColumns.C_MERGANT,
			DbCouponsColumns.C_DETAILS, DbCouponsColumns.C_VALID_START_DATE, DbCouponsColumns.C_VALID_END_DATE};
	
	/**
	 * Creates a new instance of DBSource.
	 * 
	 * @param context
	 *            global information about application's environment.
	 */
	public DBSource(Context context)
	{
		this.dbHelper = new DBSQLiteHelper(context);
	}
	
	/**
	 * Open the database or create it if does not exist.
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Close database.
	 */
	public void close()
	{
		if (dbHelper != null)
		{
			dbHelper.close();
		}
	}
	
	/**
	 * Adds new entry in the coupons table of the database.
	 * 
	 * @param code
	 *            the code of the coupon.
	 *            
	 * @param profile
	 *            the profile of the current location.
	 *            
	 * @param zValue
	 *            the z value that is calculated for algorithm's purposes.
	 *            
	 * @param dof
	 *            degrees of freedom.
	 *            
	 * @return an instance of the LocationData.
	 * 
	 * Remove tha later: int code, boolean validStatus, Date validDate, String mergant, String details, Date validStartDate, Date validEndDate
	 * 
	 */
	public Coupons addCoupon(Coupons coupon)
	{
		ContentValues values = DBSourceHelper.generateDBValuesForCoupon(coupon);
	    
		long insertId = database.insert(DbCouponsColumns.T_COUPONS, null, values);
		
		Cursor cursor = null;
		try
		{
			cursor = database.query(DbCouponsColumns.T_COUPONS, couponsColumns, DbCouponsColumns.C_KEY
					+ " = " + insertId, null, null, null, null);

		} catch (CursorIndexOutOfBoundsException ex)
		{

		}
		cursor.moveToFirst();


		// Check if the entry added successfully
		Coupons newCoupon = DBSourceHelper.cursorToCoupon(cursor);
		cursor.close();
		return newCoupon;
	}
	
}