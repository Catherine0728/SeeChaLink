package com.home.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 记录设备列表的数据库
 * 
 * 一个数据库，然后三个字段：id,equip_name,equipment_status
 * 
 * */
public class AllEquipmentDB extends SQLiteOpenHelper {
	public String TAG = "AllEquipmentDB";

	public final static String DATABASE_NAME = "ALLEQUIPMENT.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "EQUIPMENT";
	public final static String e_Id = "_id";// 自增长ID

	public final static String e_Equipment = "_equipment";// 设备的名字
	public final static String e_Equipment_Status = "_equipment_status";// 设备的状态

	public AllEquipmentDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + e_Id

		+ " INTEGER primary key autoincrement, " + e_Equipment + " text, "
				+ e_Equipment_Status + " text);";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		// to update the database
		db.execSQL(sql);

		onCreate(db);
	}

	public Cursor select() {
		Log.d(TAG, "select");
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db
				.query(TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	/**
	 * 根据场景查询对应的数据条数
	 * */
	public long select(String name) {
		Log.d(TAG, "select name===1");
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _equipment='"
				+ name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getColumnIndex(e_Equipment_Status);
			}
			return time;
		} else {
			return 0;
		}

	}

	/**
	 * 根据场景查询对应要执行的所有命令数据
	 * 
	 * */
	public Cursor selectName(String Name) {
		Log.d(TAG, "selectName==>2");
		String str = "select * from " + TABLE_NAME + " where _equipment='"
				+ Name + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;

	}

	public long insert(String name, String equipment_status)

	{
		Log.d(TAG, "insert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(e_Equipment, name);
		cv.put(e_Equipment_Status, equipment_status);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String name)

	{
		Log.d(TAG, "delete");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = e_Equipment + " = ?";

		String[] whereValue = { name };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String name, String newName, String equipment_status)

	{
		Log.d(TAG, "update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = e_Equipment + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(e_Equipment, newName);
		cv.put(e_Equipment_Status, equipment_status);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
