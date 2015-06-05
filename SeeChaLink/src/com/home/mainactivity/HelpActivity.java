package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Catherine 这是帮助的页面
 * 
 * */
public class HelpActivity extends Activity {
	CommonTitleView commantitle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		initView();
	}

	public void initView() {
		Configer.PAGER =6;
		commantitle = (CommonTitleView) findViewById(R.id.toplayout);
		commantitle.initData(HelpActivity.this, null, "帮助手册");
	}

}
