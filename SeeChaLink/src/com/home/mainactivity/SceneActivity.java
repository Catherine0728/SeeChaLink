package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.android.service.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.service.BackgroundService;
import com.home.adapter.Switch_ListAdapter;
import com.home.constants.Configer;
import com.home.db.AllSceneDB;
import com.home.util.MQTTClientUtil;
import com.home.util.Notify;
import com.home.utils.Logger;
import com.home.view.CommonTitleView;
import com.home.view.SlideToggle;
import com.home.view.SlideToggle.OnChangedListener;
import com.home.view.SwitchButton;
import com.home.view.SwitchButton.SwitchChangedListner;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 点击场景
 * 
 * 
 * @author Catherine 进入场景后，根据传过来的场景名字进行数据库查询...
 * 
 * */
public class SceneActivity extends Activity {
	String TAG = "SceneActivity";
	CommonTitleView commanTitle = null;
	EditText edit_scene_name = null;
	ListView Switch_List = null;
	Switch_ListAdapter switch_Adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_layout);

		initView();
	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		if (Configer.ISCONNECT) {
		} else {

			startServer();
		}

	};

	public void startServer() {
		Log.d(TAG, "startServer");
		if (isConnect(getBaseContext())) {
			Intent intent = new Intent(this, BackgroundService.class);
			startService(intent);
		} else {
			Notify.toast(getBaseContext(), "请检查您的网络，无连接 或者 连接不正确！",
					Toast.LENGTH_LONG);
		}
	}

	int id = 0;
	ArrayList<Map<String, Object>> mList = null;
	// String[] str = { "灯泡", "电视", "空调" };
	int[] img = { R.drawable.icon_light, R.drawable.icon_tv,
			R.drawable.icon_electricity };
	String scene_Str = "";
	String Command_Info = "";
	ArrayList<String> controlName = null;

	public void initView() {
		Configer.PAGER = 2;
		commanTitle = (CommonTitleView) findViewById(R.id.toplayout);
		edit_scene_name = (EditText) findViewById(R.id.edit_name);
		id = getIntent().getIntExtra("id", 0);
		scene_Str = getIntent().getStringExtra("name").toString();

		edit_scene_name.setText(scene_Str);
		Command_Info = CheckDB(scene_Str);
		if (null == Command_Info) {

		} else {
			String[] str = Command_Info.split(",");
			for (int i = 0; i < str.length; i++) {
				controlName.add(str[i]);
			}

		}
		commanTitle.initData(SceneActivity.this, right_listener, scene_Str);

		Switch_List = (ListView) findViewById(R.id.switch_list);
		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < controlName.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", controlName.get(i).toString());
			map.put("icon", img[i % img.length]);
			mList.add(map);
		}
		switch_Adapter = new Switch_ListAdapter(this, mList, id);
		Switch_List.setAdapter(switch_Adapter);

	}

	/**
	 * 查询数据库
	 * 
	 * */
	AllSceneDB sceneDB = null;

	public String CheckDB(String sceneName) {
		controlName = new ArrayList<String>();
		String command = "";
		if (null == sceneDB) {
			sceneDB = new AllSceneDB(this);
		}
		if (sceneDB.select(sceneName) == 0) {
			// 没有数据
		} else {

			// 有数据，然后把相关的需要执行的命令给查询出来
			Cursor cursor = sceneDB.selectName(sceneName);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				command = cursor.getString(cursor
						.getColumnIndex(sceneDB.s_Command));

			}
			Log.d(TAG, "要执行的命令有===》" + command);
		}
		return command;
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
			if (isEdit) {
				edit_scene_name.setEnabled(true);
				for (int i = 0; i < controlName.size(); i++) {
					switch_Adapter.chooseState(i, true);
				}
			}
			Toast.makeText(SceneActivity.this, "请对" + scene_Str + "进行编辑",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void DotRightFinish(boolean isFinish) {

			if (isFinish) {
				StringBuffer sb = new StringBuffer();
				ArrayList<Map<String, Object>> newList = switch_Adapter
						.ReturnList();
				String newName = edit_scene_name.getText().toString();
				for (int i = 0; i < newList.size(); i++) {
					String name = newList.get(i).get("title").toString();
					sb.append(name + ",");
				}
				Toast.makeText(SceneActivity.this,
						"完成编辑==" + newName + "==其遥控为==" + sb,
						Toast.LENGTH_SHORT).show();
				// 存入数据库
				if (null == sceneDB) {
					sceneDB = new AllSceneDB(SceneActivity.this);
				}

				sceneDB.update(scene_Str, newName, sb.toString());
				// Intent intent = new Intent();
				// intent.setClass(SceneActivity.this, MainActivity.class);
				// intent.putExtra("name", newName);
				// startActivity(intent);
				finish();
			}
		}

	};

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");

		// if (isConnect(getBaseContext())) {
		// Intent intent = new Intent(this, BackgroundService.class);
		// stopService(intent);
		// }
		super.onDestroy();
	}

	/**
	 * 判断网络是否连接
	 * */
	public boolean isConnect(Context context) {
		Log.d(TAG, "isConnect");
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("isconnect error", e.toString());
		}
		return false;
	}

}
