package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;

/**
 * 这是一个数字控制的界面
 * */
public class Number_Controll_Activity extends Activity {
	CommonTitleView commtitleView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.number_layout);
		initView();
	}

	public void initView() {
		Configer.PAGER = 9;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(this, null, "学习空调");
	}
}
