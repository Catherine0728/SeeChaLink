package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Catherine 这是反馈的页面
 * 
 * */
public class FeedBackActivity extends Activity {
	CommonTitleView commantitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		initView();
	}

	public void initView() {
		Configer.PAGER = 7;
		commantitle = (CommonTitleView) findViewById(R.id.toplayout);
		commantitle.initData(FeedBackActivity.this, null, "意见反馈");
	}

}
