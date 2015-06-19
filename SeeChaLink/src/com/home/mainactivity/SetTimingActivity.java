package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.view.CommonTitleView;

import android.app.Activity;

/**
 * @author Catherine 这是设置定时启动的页面
 * 
 * 
 * */
public class SetTimingActivity extends Activity {
	public static String TAG = "SetTiming";
	CommonTitleView commtitle = null;

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		setContentView(R.layout.set_timing);
		initView();
	}

	/**
	 * 
	 * 初始化控件
	 * */
	public void initView() {
		Configer.PAGER = 9;
		commtitle = (CommonTitleView) findViewById(R.id.top_layout);
		commtitle.initData(SetTimingActivity.this, right_Listener, "设置定时");

	}

	CommanTitle_Right_Listener right_Listener = new CommanTitle_Right_Listener() {

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub

		}

		@Override
		public void DotRightEdit(boolean isEdit) {
			// TODO Auto-generated method stub

		}

		@Override
		public void DotRight(boolean isDot) {
			// TODO Auto-generated method stub

		}

		@Override
		public void DotLeft(boolean isDot) {
			// TODO Auto-generated method stub

		}
	};
}
