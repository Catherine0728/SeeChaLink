package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * 先建立一个空调遥控器、
 * 
 * @author Catherine
 * */

public class Controll_AirConditioner extends Activity {
	CommonTitleView commtitleView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controll_airconditioner);
		initView();
	}

	public void initView() {
		Configer.PAGER = 9;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(this, null, "学习空调");
	}

}
