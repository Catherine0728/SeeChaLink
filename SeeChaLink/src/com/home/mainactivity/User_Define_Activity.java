package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.home.adapter.AddControlAdapter;
import com.home.constants.Configer;
import com.home.db.AllCommandDB;
import com.home.util.Utility;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 这是自定义的页面
 * 
 * @author Catherine
 * 
 * */
public class User_Define_Activity extends Activity {
	public static final String TAG = "User_Define_Activity";
	CommonTitleView commtitleView = null;
	GridView grid_control = null;
	Button image_control = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_define);

	}

	AddControlAdapter controlAdapter = null;

	AllCommandDB commanDB = null;

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		initDB();
		initView();
	};

	ArrayList<String> strName = new ArrayList<String>();
	ArrayList<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();

	public void initDB() {

		strName.clear();
		mList.clear();
		CheckDB();

		if (null == controlAdapter) {
			controlAdapter = new AddControlAdapter(User_Define_Activity.this,
					mList);
		}
	}

	public void CheckDB() {

		if (null == commanDB) {
			commanDB = new AllCommandDB(User_Define_Activity.this);
		}
		Cursor cursor = commanDB.select();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(commanDB.c_command));
			strName.add(name);
		}
		for (int i = 0; i < strName.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			;
			map.put("name", strName.get(i).toString());
			map.put("image", R.drawable.btn_tv_press);
			mList.add(map);
		}
	}

	Intent intent = null;
	public static final int REQUESTQUDE = 1;

	public void initView() {
		Configer.PAGER = 9;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(User_Define_Activity.this, null, "添加设备");
		grid_control = (GridView) findViewById(R.id.grid_control);
		image_control = (Button) findViewById(R.id.btn_control_add);
		grid_control.setAdapter(controlAdapter);
		Utility.setGridViewHeightBasedOnChildren(grid_control, 3);
		grid_control.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent();
				intent.setClass(User_Define_Activity.this,
						To_Define_Activity.class);
				intent.putExtra("name", mList.get(position).get("name")
						.toString());
				intent.putExtra("fromwhere", "编辑");
				startActivityForResult(intent, REQUESTQUDE);
			}
		});
		image_control.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(User_Define_Activity.this,
						To_Define_Activity.class);
				intent.putExtra("name", "自定义");
				intent.putExtra("fromwhere", "添加");
				startActivityForResult(intent, REQUESTQUDE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String name = data.getStringExtra("name").toString();
		Log.d(TAG, "修改的name is===>" + name);
		CheckDB();
		controlAdapter.ReSetList(mList);

	}
}
