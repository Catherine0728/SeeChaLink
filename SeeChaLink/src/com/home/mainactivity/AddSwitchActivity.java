package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.db.AllSwitchDB;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 添加开关
 * 
 * @author Catherine
 * 
 * */
public class AddSwitchActivity extends Activity {
	CommonTitleView commtitleView = null;
	EditText edit_Name;

	// init the db
	AllSwitchDB switchDB = null;
	TextView text_error;

	// public static int SWITCHCODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_define);
		initView();
	}

	public void initView() {
		Configer.PAGER = 2;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(AddSwitchActivity.this, right_Listener, "添加开关");
		edit_Name = (EditText) findViewById(R.id.edit_name);
		text_error = (TextView) findViewById(R.id.text_error);
		text_error.setVisibility(View.INVISIBLE);
		edit_Name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s != null && !"".equals(s.toString())) {
					text_error.setVisibility(View.INVISIBLE);
				}

			}
		});
	}

	CommanTitle_Right_Listener right_Listener = new CommanTitle_Right_Listener() {

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub
			String name = edit_Name.getText().toString();
			// String nameInfo = edit_Name_info.getText().toString();
			if (null == name) {
				text_error.setText("请输入开关名！");
				text_error.setVisibility(View.VISIBLE);
				edit_Name.setFocusable(true);
			} else {
				CheckDB(name);

			}
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

	public void CheckDB(String name) {
		if (null == switchDB) {
			switchDB = new AllSwitchDB(AddSwitchActivity.this);

		}
		if (switchDB.select(name) == 0) {
			switchDB.insert(name);

			finish();
		} else {
			text_error.setText("此开关名已经存在!");
			text_error.setVisibility(View.VISIBLE);
			edit_Name.setText("");

		}
	}
}
