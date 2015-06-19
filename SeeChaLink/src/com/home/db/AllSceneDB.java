package com.home.db;

import java.sql.Array;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 记录场景的DB
 * 
 * 一个数据库，然后三个字段：id,scene_Name,scene_Command(这里考虑是一个数组);
 * 
 * @see{添加一个字段用于存储图片的uri -s_Image_Uri
 * 
 * */
public class AllSceneDB extends SQLiteOpenHelper {
	public String TAG = "AllSceneDB";

	public final static String DATABASE_NAME = "ALLSCENE.db";

	public final static int DATABASE_VERSION = 1;

	public final static String TABLE_NAME = "SCENE";
	public final static String s_Id = "_id";// 自增长ID

	public final static String s_NAME = "_NAME";// 场景的名字
	public final static String s_Command = "_Command";// 场景对应里面要执行的命令
	public final static String s_Image_Uri = "_Image_Uri";// 场景的图片的路径

	public AllSceneDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + s_Id

		+ " INTEGER primary key autoincrement, " + s_NAME + " text, "
				+ s_Image_Uri + " text, " + s_Command + " text);";

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
		Log.d(TAG, "select name==1");
		long time = 0;
		String str = "select * from " + TABLE_NAME + " where _NAME='" + name
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				time = cursor.getColumnIndex(s_Command);
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
		Log.d(TAG, "select name===2");
		String str = "select * from " + TABLE_NAME + " where _NAME='" + Name
				+ "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(str, null);
		return cursor;

	}

	public long insert(String name, String command, String image_uri)

	{
		Log.d(TAG, "insert");
		SQLiteDatabase db = this.getWritableDatabase();

		/* ContentValues */

		ContentValues cv = new ContentValues();
		cv.put(s_NAME, name);
		cv.put(s_Command, command);
		cv.put(s_Image_Uri, image_uri);
		long row = db.insert(TABLE_NAME, null, cv);

		return row;

	}

	public void delete(String name)

	{
		Log.d(TAG, "delete");

		SQLiteDatabase db = this.getWritableDatabase();

		String where = s_NAME + " = ?";

		String[] whereValue = { name };

		db.delete(TABLE_NAME, where, whereValue);

	}

	public void update(String name, String newName, String command,
			String image_uri)

	{
		Log.d(TAG, "update");
		SQLiteDatabase db = this.getWritableDatabase();

		String where = s_NAME + " = ?";

		String[] whereValue = { name + "" };

		ContentValues cv = new ContentValues();

		cv.put(s_NAME, newName);
		cv.put(s_Command, command);
		cv.put(s_Image_Uri, image_uri);
		db.update(TABLE_NAME, cv, where, whereValue);

	}

}
