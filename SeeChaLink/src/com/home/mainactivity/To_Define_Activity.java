package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.db.AllCommandDB;
import com.home.view.CommonTitleView;

import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 点击自定义里面的添加以及点击已经添加的遥控进行编辑
 * 
 * @author Catherine
 * 
 * */
public class To_Define_Activity extends Activity {
	public static String TAG = "To_Define_Activity";
	CommonTitleView commtitleView = null;
	ImageView image_control = null;
	EditText edit_Name, edit_Name_info;
	Button btn_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_define);

	}

	@Override
	protected void onResume() {
		super.onResume();
		initDB();
		initView();
	};

	AllCommandDB commandDB = null;

	public void initDB() {
		name = getIntent().getStringExtra("name");
		Log.d(TAG, "name is-===>" + name);
		FromWhere = getIntent().getStringExtra("fromwhere");
		if (null == commandDB) {
			commandDB = new AllCommandDB(To_Define_Activity.this);

		}
		if (FromWhere.equals("编辑")) {

			Cursor cursor = commandDB.selectName(name);
			if (cursor.moveToFirst()) {
				nameInfo = cursor.getString(cursor
						.getColumnIndex(commandDB.c_Command_Info));
			}

		} else {

		}

	}

	String editName = "";
	public static final int REQUESTQUDE = 1;
	String FromWhere = "";
	String name = "自定义";
	String nameInfo = "遥控描述";

	public void initView() {
		Configer.PAGER = 9;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(To_Define_Activity.this, null, "添加遥控");

		edit_Name = (EditText) findViewById(R.id.edit_name);
		edit_Name_info = (EditText) findViewById(R.id.edit_name_info);
		edit_Name.setText(name);
		edit_Name_info.setText(nameInfo);
		image_control = (ImageView) findViewById(R.id.image_control);
		image_control.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = edit_Name.getText().toString();
				String nameInfo = edit_Name_info.getText().toString();
				if (null == name) {
					name = "自定义";
				} else {
				}
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.putExtra("nameInfo", nameInfo);
				setResult(REQUESTQUDE, intent);
				CheckDB(name, nameInfo);
				finish();

			}
		});

	}

	public void CheckDB(String newname, String nameInfo) {
		Log.d(TAG, "CheckDB");
		if (null == commandDB) {
			commandDB = new AllCommandDB(To_Define_Activity.this);

		}
		if (FromWhere.equals("编辑")) {
			// 应该是update
			commandDB.update(name, newname, nameInfo);
		} else {

			// a当fromwhere为添加的时候应该自己去insert
			commandDB.insert(newname, nameInfo);
		}
	}
}
