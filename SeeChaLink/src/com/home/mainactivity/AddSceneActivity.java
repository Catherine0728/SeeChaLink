package com.home.mainactivity;

import java.util.ArrayList;

import com.home.adapter.AddSceneAdapter;
import com.home.adapter.ControlListAdapter;
import com.home.constants.Configer;
import com.home.db.AllSceneDB;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.listener.SelectAll_Listener;
import com.home.util.Utility;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author Catherine 添加设备，场景，遥控，根据已经拥有的设备，场景下面就进行添加
 * @see {应该是选择一个当前已经拥有的设备，然后进行}
 * 
 *      {修改：将选择直接显示在本页面，在点击完成后直接跳转到场景显示的页面==SceneActivity.class}
 * */
public class AddSceneActivity extends Activity implements OnClickListener {
	public static String TAG = "AddSceneActivity";
	CommonTitleView commontitleview = null;
	// EditText edit_add_scene, edit_add_control, edit_add_device;
	ImageView image_scene;
	EditText edit_scene_name;
	// Button btn_Add;
	ListView list_command;
	Button btn_select_all, btn_add;
	AddSceneAdapter addSceneAdapter = null;
	// 填充设备列表的adapter
	ControlListAdapter controlAdapter = null;
	TextView text_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene);
		initView();
	}

	// 这是定义用来存储填充遥控列表的
	ArrayList<String> mList = new ArrayList<String>();

	// 定义一个来得到需要添加的遥控列表
	ArrayList<String> AddList = new ArrayList<String>();

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	public static int REQCODE = 0;

	public void initView() {

		Configer.PAGER = 3;
		// Configer.PAGER = -1;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(AddSceneActivity.this, right_listener, "添加场景");
		image_scene = (ImageView) findViewById(R.id.image_scene);
		edit_scene_name = (EditText) findViewById(R.id.edit_scene_name);
		btn_select_all = (Button) findViewById(R.id.btn_select_all);
		btn_add = (Button) findViewById(R.id.btn_add);
		text_error = (TextView) findViewById(R.id.text_error);
		btn_add.setOnClickListener(this);
		btn_select_all.setOnClickListener(this);
		list_command = (ListView) findViewById(R.id.list_command);
		mList = new ArrayList<String>();
		for (int i = 0; i < Configer.strList.length; i++) {
			mList.add(Configer.strList[i]);

		}

		controlAdapter = new ControlListAdapter(this, mList, true, IsSelectAll);
		list_command.setAdapter(controlAdapter);
		Utility.setListViewHeightBasedOnChildren(list_command);
		edit_scene_name.addTextChangedListener(textW);

	}

	SelectAll_Listener IsSelectAll = new SelectAll_Listener() {

		@Override
		public void IsSelectAll(boolean isSelectAll) {
			if (isSelectAll) {
				btn_select_all.setText("取消全选");
			} else {

				btn_select_all.setText("全选");
			}
		}
	};
	TextWatcher textW = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Log.d(TAG, "onTextChanged===>" + s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Log.d(TAG, "beforeTextChanged===>" + s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.d(TAG, "afterTextChanged===" + s.toString());
			if (s != null && !"".equals(s.toString())) {
				text_error.setVisibility(View.INVISIBLE);
			}
		}
	};
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// String name = data.getStringExtra("name");
	// Log.d(TAG, "选择的遥控为====>" + name);
	// mList.add(name);
	// SetAdapter(mList);
	// }

	// /**
	// * 得到点击得到的数据，然后填充数据
	// * */
	// public void SetAdapter(ArrayList<String> mList) {
	//
	// if (null == addSceneAdapter) {
	// addSceneAdapter = new AddSceneAdapter(this, mList);
	// } else {
	// addSceneAdapter.ReSetList(mList);
	// }
	// list_command.setAdapter(addSceneAdapter);
	// Utility.setListViewHeightBasedOnChildren(list_command);
	// }

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
			if (isEdit) {
				// 清空所有操作
				edit_scene_name.setText("");
				edit_scene_name.setHint("设置场景");
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, false, true);
				}
				finish();

			}
		}

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub

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

	private boolean is_select_all = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_select_all:
			if (is_select_all) {
				btn_select_all.setText("全选");
				is_select_all = false;
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, false, true);
				}

			} else {
				btn_select_all.setText("取消全选");
				is_select_all = true;
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, true, true);
				}
			}
			break;
		case R.id.btn_add:
			String sceneName = edit_scene_name.getText().toString();
			if (null == sceneName || sceneName.equals("")) {
				text_error.setVisibility(View.VISIBLE);
			} else {

				// 判断当前的是否有选中
				if (controlAdapter.Add_Check()) {
					ShowToast("确定添加这些操作数据", 1);
				} else {
					ShowToast("请先选择要添加的数据", 0);
				}
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 显示提醒添加
	 * 
	 * @see{这里的is_go如果为0，代表没有数据选中，所以点击确定无操作，否则进行添加
	 * 
	 * */

	public void ShowToast(String Title, final int is_go) {
		final AlertDialog dialog = new AlertDialog.Builder(
				AddSceneActivity.this).create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.show_toast);
		// 设置宽高
		window.setLayout(LayoutParams.FILL_PARENT, 400);
		window.setGravity(Gravity.CENTER);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);
		TextView text_title = (TextView) window.findViewById(R.id.text_title);
		text_title.setText(Title);
		Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
		Button btn_sure = (Button) window.findViewById(R.id.btn_sure);
		if (is_go == 0) {
			btn_cancel.setVisibility(View.GONE);
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (is_go == 0) {

				} else {

					// 获取到数据，然后添加到数据库，且跳转到场景查看的页面==SceneActivity.class
					AddList = controlAdapter.CheckSelected();
					sceneName = edit_scene_name.getText().toString();

					if (InsertSceneDB(sceneName, AddList)) {

						finish();
					}

				}
				dialog.cancel();
			}
		});
	}
}
