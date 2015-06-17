package com.home.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 记录命令的DB
 * 
 * 一个数据库，然后三个字段：id,command_Name,command_info
 * 
 * */
public class AllCommandDB extends SQLiteOpenHelper {
	public String TAG = "AllCommandDB";

	public final static String DATABASE_NAME = "ALLCOMMAND.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "COMMAND";
	public final static String c_Id = "_id";// 自增长ID

	public final static String c_command = "_command";// 命令的名字
	public final static String c_Command_Info = "_Command_Info";// 命令对应要执行的信息
	public final static String c_Command_Image = "_Command_Image";// 命令对应的要显示的图片

	public AllCommandDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + c_Id
				+ " INTEGER primary key autoincrement, " + c_command
				+ " text, " + c_Command_Image + " text, " + c_Command_Info
				+ " text);";

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
		String str = "select * from " + TABLE_NAME + " where _command='" + name
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getColumnIndex(c_Command_Info);
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
		String str = "select * from " + TABLE_NAME + " where _command='" + Name
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;

	}

	public long insert(String name, String command, String image_url)

	{
		Log.d(TAG, "insert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(c_command, name);
		cv.put(c_Command_Info, command);
		cv.put(c_Command_Image, image_url);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String name)

	{
		Log.d(TAG, "delete");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = c_command + " = ?";

		String[] whereValue = { name };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String name, String newName, String command,
			String image_uri)

	{
		Log.d(TAG, "update");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = c_command + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(c_command, newName);
		cv.put(c_Command_Info, command);
		cv.put(c_Command_Image, image_uri);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
