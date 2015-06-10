package com.home.mainactivity;

import java.util.ArrayList;

import com.home.adapter.EquipmentAdapter;
import com.home.constants.Configer;
import com.home.db.AllEquipmentDB;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * 我的设备
 * 
 * @author Catherine
 * */
public class MyControl_Equipment extends Activity {
	public static String TAG = "MyControl_Equipment";
	ListView list_equipment;
	CommonTitleView commontitleview = null;
	EquipmentAdapter equipmentAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_equipment);
		initView();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	ArrayList<String> mList = null;

	public void initView() {
		Configer.PAGER = 5;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(MyControl_Equipment.this, null, "我的设备");
		list_equipment = (ListView) findViewById(R.id.list_equipment);
		CheckDB();
		equipmentAdapter = new EquipmentAdapter(this, mList);
		list_equipment.setAdapter(equipmentAdapter);
		list_equipment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(MyControl_Equipment.this,
						mList.get(position).toString(), Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	AllEquipmentDB equipmentDB = null;

	public void CheckDB() {
		mList = new ArrayList<String>();
		if (null == equipmentDB) {
			equipmentDB = new AllEquipmentDB(this);
		}
		Cursor cursor = equipmentDB.select();
		if (cursor.moveToFirst()) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				String name = cursor.getString(cursor
						.getColumnIndex(equipmentDB.e_Equipment));
				Log.d(TAG, "设备的名字有====》" + name);
				mList.add(name);
			}
		}
	}
}
