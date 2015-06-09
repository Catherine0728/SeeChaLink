package com.home.mainactivity;

import java.util.ArrayList;

import com.home.adapter.AddSceneAdapter;
import com.home.constants.Configer;
import com.home.db.AllSceneDB;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.util.Utility;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author Catherine 添加设备，场景，遥控，根据已经拥有的设备，场景下面就进行添加
 * @see {应该是选择一个当前已经拥有的设备，然后进行}
 * */
public class AddSceneActivity extends Activity {
	public static String TAG = "AddSceneActivity";
	CommonTitleView commontitleview = null;
	// EditText edit_add_scene, edit_add_control, edit_add_device;
	ImageView image_scene;
	EditText edit_scene_name;
	Button btn_Add;
	ListView list_command;
	AddSceneAdapter addSceneAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene);
		initView();
	}

	ArrayList<String> mList = new ArrayList<String>();

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	public static int REQCODE = 0;

	public void initView() {

		Configer.PAGER = 3;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(AddSceneActivity.this, right_listener, "添加场景");
		image_scene = (ImageView) findViewById(R.id.image_scene);
		edit_scene_name = (EditText) findViewById(R.id.edit_scene_name);
		btn_Add = (Button) findViewById(R.id.btn_add);
		list_command = (ListView) findViewById(R.id.list_command);
		// edit_add_device = (EditText) findViewById(R.id.edit_add_device);
		// edit_add_control = (EditText) findViewById(R.id.edit_add_control);
		// edit_add_scene = (EditText) findViewById(R.id.edit_add_scene);
		btn_Add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AddSceneActivity.this, AddRemoteControll.class);
				intent.putExtra("fromwhere", "添加场景");
				startActivityForResult(intent, REQCODE);
			}
		});
		list_command.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String name = data.getStringExtra("name");
		Log.d(TAG, "选择的遥控为====>" + name);
		mList.add(name);
		SetAdapter(mList);
	}

	/**
	 * 得到点击得到的数据，然后填充数据
	 * */
	public void SetAdapter(ArrayList<String> mList) {

		if (null == addSceneAdapter) {
			addSceneAdapter = new AddSceneAdapter(this, mList);
		} else {
			addSceneAdapter.ReSetList(mList);
		}
		list_command.setAdapter(addSceneAdapter);
		Utility.setListViewHeightBasedOnChildren(list_command);
	}

	String sceneName = "快捷场景";
	AllSceneDB sceneDB = null;
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
				sceneName = edit_scene_name.getText().toString();
				if (InsertSceneDB(sceneName, mList)) {

					finish();
				}

			}
		}

	};
	StringBuffer sb = null;

	// 将已经完成的场景添加到数据库
	public boolean InsertSceneDB(String sceneName, ArrayList<String> mList) {
		sb = new StringBuffer();
		if (null == sceneDB) {
			sceneDB = new AllSceneDB(AddSceneActivity.this);

		}
		String name = sceneName;
		for (int i = 0; i < mList.size(); i++) {
			sb.append(mList.get(i).toString() + ",");
		}
		// Toast.makeText(AddSceneActivity.this,
		// "完成添加==" + name + "==遥控为==" + sb.toString(), Toast.LENGTH_SHORT)
		// .show();
		sceneDB.insert(name, sb.toString());
		return true;
	}
}
