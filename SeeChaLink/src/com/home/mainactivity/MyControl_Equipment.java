package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/**
 * 
 * 我的设备
 * 
 * @author Catherine
 * */
public class MyControl_Equipment extends Activity {
	ListView list_equipment;
	CommonTitleView commontitleview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_control_equipment);
		initView();
	}

	public void initView() {
		Configer.PAGER = 5;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(MyControl_Equipment.this, null, "我的设备");
		list_equipment = (ListView) findViewById(R.id.list_equipment);
	}

}
