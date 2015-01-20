package com.iclickcy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DBSQLiteHelper executes the low level transactions directly into the database.
 * 
 */


public class DBSQLiteHelper extends SQLiteOpenHelper
{

	/**
	 * Inner class is created to group the attributes of the coupons table.
	 * 
	 */
	public static class DbCouponsColumns
	{
		static final String T_COUPONS = "coupons";
		static final String C_KEY = "_key";
		static final String C_CODE = "code";
		static final String C_VALID_STATUS = "valid_status";
		static final String C_VALID_DATE = "valid_date";
		static final String C_MERGANT = "mergant";
		static final String C_DETAILS = "details";
		static final String C_VALID_START_DATE ="valid_start_date";
		static final String C_VALID_END_DATE = "valid_end_date";
	}

	/**
	 * Inner class is created to group the attributes of the user's data table.
	 * 
	 * TODO: This attributes are going to be used in the future.
	 * 
	 */
	public class DbUserColumns
	{
		static final String T_USER = "user";
		static final String C_KEY = "_key";
		static final String C_NAME = "name";
	}
	
	
	
	private static final String DATABASE_NAME = "aditplanet_contents.db";
	private static final int DATABASE_VERSION = 1;

	//Database creation. Sql statements.
	
	/**
	 * Command TABLE_COUPONS_CREATE: <i>create table coupons(_key integer primary key autoincrement, code integer not null, valid_status integer not null, valid_date text not null, 
	 * 									mergant text not null, details text not null, valid_start_date text, valid_end_date text);</i>
	 */
	private static final String TABLE_COUPONS_CREATE = "create table " + DbCouponsColumns.T_COUPONS + "(" + DbCouponsColumns.C_KEY + " integer primary key autoincrement, "
			+ DbCouponsColumns.C_CODE + " integer not null, "
			+ DbCouponsColumns.C_VALID_STATUS + " integer not null, "
			+ DbCouponsColumns.C_VALID_DATE + " text not null, "
			+ DbCouponsColumns.C_MERGANT + " text not null, "
			+ DbCouponsColumns.C_DETAILS + " text not null,"
			+ DbCouponsColumns.C_VALID_START_DATE + " text not null,"
			+ DbCouponsColumns.C_VALID_END_DATE +" text not null"+");";

	
	/**
	 * Command TABLE_USER_CREATE: <i>create table user(_key integer primary key autoincrement, mac_address text not null, variation real not null,
	 * 									FOREIGN KEY(location_id) REFERENCES location(_id));</i>
	 * Relationship 1 to Many.
	 */
	private static final String TABLE_USER_CREATE = "create table " + DbUserColumns.T_USER + "(" + DbUserColumns.C_KEY + " integer primary key, "
			+ DbUserColumns.C_NAME + " text not null"+");";
	
	/**
	 * Defines the database's name and version.
	 * 
	 * @param context
	 * 					variable contains information about the current environment.
	 */
	public DBSQLiteHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Creates the two tables.
	 */
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		database.execSQL(TABLE_COUPONS_CREATE);
//		database.execSQL(TABLE_USER_CREATE);
	}
	
	/**
	 * Executes when there is a need to delete both tables.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(DBSQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DbCouponsColumns.T_COUPONS);
//		db.execSQL("DROP TABLE IF EXISTS " + DbUserColumns.T_USER);
		onCreate(db);
	}
}
