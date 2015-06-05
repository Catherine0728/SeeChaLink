package com.home.mainactivity;

import com.home.constants.Configer;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Catherine 添加设备，场景，遥控，根据已经拥有的设备，场景下面就进行添加
 * @see {应该是选择一个当前已经拥有的设备，然后进行}
 * */
public class AddSceneActivity extends Activity {
	CommonTitleView commontitleview = null;
	EditText edit_add_scene, edit_add_control, edit_add_device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene);
		initView();
	}

	public void initView() {

		Configer.PAGER = 3;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(AddSceneActivity.this, right_listener, "添加场景");
		edit_add_device = (EditText) findViewById(R.id.edit_add_device);
		edit_add_control = (EditText) findViewById(R.id.edit_add_control);
		edit_add_scene = (EditText) findViewById(R.id.edit_add_scene);
		edit_add_device.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShowDialog();
			}
		});

	}

	public void ShowDialog() {
		Toast.makeText(this, "选择设备", Toast.LENGTH_SHORT).show();
	}

	CommanTitle_Right_Listener right_listener = new CommanTitle_Right_Listener() {

		@Override
		public void DotRight(boolean isDot) {

		}

		@Override
		public void DotLeft(boolean isDot) {
			if (isDot) {

			}

		}

		@Override
		public void DotRightEdit(boolean isEdit) {
			// TODO Auto-generated method stub
		}

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub
			if (isFinish) {
				Toast.makeText(AddSceneActivity.this, "完成添加",
						Toast.LENGTH_SHORT).show();
			}
		}

	};
}
