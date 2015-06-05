package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.android.service.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.service.BackgroundService;
import com.home.constants.Configer;
import com.home.util.MQTTClientUtil;
import com.home.util.Notify;
import com.home.view.CommonTitleView;
import com.home.view.SlideToggle;
import com.home.view.SlideToggle.OnChangedListener;
import com.home.view.SwitchButton;
import com.home.view.SwitchButton.SwitchChangedListner;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
 * 点击场景进入设置
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
	String[] str = { "灯泡", "电视", "空调" };
	int[] img = { R.drawable.icon_light, R.drawable.icon_tv,
			R.drawable.icon_electricity };
	String scene_Str = "客房场景";

	public void initView() {
		Configer.PAGER = 2;
		commanTitle = (CommonTitleView) findViewById(R.id.toplayout);

		id = getIntent().getIntExtra("id", 0);

		edit_scene_name = (EditText) findViewById(R.id.edit_name);
		scene_Str = getIntent().getStringExtra("name").toString();
		edit_scene_name.setText(scene_Str);

		commanTitle.initData(SceneActivity.this, right_listener, scene_Str);

		Switch_List = (ListView) findViewById(R.id.switch_list);
		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < str.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", str[i].toString());
			map.put("icon", img[i]);
			mList.add(map);
		}
		switch_Adapter = new Switch_ListAdapter(this, mList, id);
		Switch_List.setAdapter(switch_Adapter);

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
				for (int i = 0; i < str.length; i++) {
					switch_Adapter.chooseState(i, true);
				}
			}
			Toast.makeText(SceneActivity.this, "请对" + scene_Str + "进行编辑",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub
			if (isFinish) {
				Toast.makeText(SceneActivity.this, "完成编辑", Toast.LENGTH_SHORT)
						.show();
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

class Switch_ListAdapter extends BaseAdapter {
	Context mContext = null;
	ArrayList<Map<String, Object>> mList = null;
	int id = 1;

	// 定义一个布尔值，来设置edit的属性
	private boolean isEnable[];

	public Switch_ListAdapter(Context mContext,
			ArrayList<Map<String, Object>> mList, int id) {
		this.mContext = mContext;
		this.mList = mList;
		isEnable = new boolean[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			isEnable[i] = false;
		}
		this.id = id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (id == 2) {
			return 1;
		} else {
			return mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 定义一个方法，然后设置所有的edit可编辑
	 * */
	public void chooseState(int position, boolean flags) {
		isEnable[position] = isEnable[position] == true ? true : true;

	}

	/**
	 * 设置是否可以编辑
	 * 
	 * */
	public boolean SetEnableAll(int position) {
		if (isEnabled(position)) {
			return true;
		} else {
			return false;
		}

	}

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.scene_layout_item, null);
			new ViewHolder(convertView, position);
		}

		viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.text_Title.setText(mList.get(position).get("title")
				.toString());
		viewHolder.text_Title.setCompoundDrawablesWithIntrinsicBounds(
				(Integer) mList.get(position).get("icon"), 0, 0, 0);
		if (id == 0) {
			viewHolder.switchButton.setOnSwitchListner(new Changelisten_one());
		} else if (id == 1) {
			viewHolder.switchButton.setOnSwitchListner(new Changelisten_two());
		} else {
			;
			viewHolder.switchButton
					.setOnSwitchListner(new Changelisten_three());

		}
		if (SetEnableAll(position)) {
			viewHolder.text_Title.setEnabled(true);
		} else {
			viewHolder.text_Title.setEnabled(false);
		}

		return convertView;
	}

	class ViewHolder {
		EditText text_Title;
		SwitchButton switchButton;

		public ViewHolder(View view, int pos) {
			view.setTag(this);
			text_Title = (EditText) view.findViewById(R.id.toggle_text_one);
			switchButton = (SwitchButton) view.findViewById(R.id.toggle_one);
		}

	}

	private class Changelisten_one implements SwitchChangedListner {

		@Override
		public void switchChanged(Integer viewId, boolean isOn) {
			if (isOn) {
				myHandler.sendEmptyMessage(ONE_ON);
			} else {

				myHandler.sendEmptyMessage(ONE_OFF);
			}
		}

	}

	private class Changelisten_two implements SwitchChangedListner {

		@Override
		public void switchChanged(Integer viewId, boolean isOn) {
			if (isOn) {
				myHandler.sendEmptyMessage(TWO_ON);
			} else {

				myHandler.sendEmptyMessage(TWO_OFF);
			}
		}

	}

	private class Changelisten_three implements SwitchChangedListner {

		@Override
		public void switchChanged(Integer viewId, boolean isOn) {
			if (isOn) {
				myHandler.sendEmptyMessage(THREE_ON);
			} else {

				myHandler.sendEmptyMessage(THREE_OFF);
			}
		}

	}

	/**
	 * 启动一个消息机制，来传送数据
	 * 
	 * 
	 * */
	/**
	 * 启动一个消息机制，来传送数据
	 * 
	 * 
	 * */
	public final static int ONE_OFF = 0x000001;
	public final static int ONE_ON = 0x001001;

	public final static int TWO_OFF = 0x000010;
	public final static int TWO_ON = 0x001010;

	public final static int THREE_OFF = 0x000011;
	public final static int THREE_ON = 0x001011;
	String getJson = "";
	MQTTClientUtil mcu = null;
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (null == mcu) {
				mcu = MQTTClientUtil.getInstance(mContext);

			}

			switch (msg.what) {
			case ONE_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 0, "pressed",
				// false, 0);
				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(9);
				} else if (id == 1) {
					getJson = GetRf3o4MWirelessCtrlClickJson(12);

				} else {

					getJson = GetRf3o4MWirelessCtrlClickJson(15);
				}

				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case ONE_OFF:

				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 0,
				// "released",
				// false, 0);
				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(1);
				} else if (id == 1) {
					getJson = GetRf3o4MWirelessCtrlClickJson(4);
				} else {
					getJson = GetRf3o4MWirelessCtrlClickJson(7);

				}
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case TWO_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1, "pressed",
				// false, 0);
				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(10);
				} else {

					getJson = GetRf3o4MWirelessCtrlClickJson(13);
				}
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case TWO_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1,
				// "released",
				// false, 0);
				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(2);
				} else {

					getJson = GetRf3o4MWirelessCtrlClickJson(5);
				}
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case THREE_ON:
				// getJson = GetJson(false, 2, "pressed", false, 0);
				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(11);
				} else {

					getJson = GetRf3o4MWirelessCtrlClickJson(14);
				}
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case THREE_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 2,
				// "released",
				// false, 0);

				if (id == 0) {
					getJson = GetRf3o4MWirelessCtrlClickJson(3);
				} else {
					getJson = GetRf3o4MWirelessCtrlClickJson(6);
				}

				mcu.publish(Configer.topicName, getJson, 2);
				break;
			}
		}
	};

	public String GetRf3o4MWirelessCtrlClickJson(int id) {
		String one_TAG = "GetRf3o4MWirelessCtrlClickJson";
		JSONObject json = new JSONObject();
		try {
			json.put("api", "v1.0.0");
			json.put("service", "smarthome-mqtt");
			json.put("delay", 0.3);
			json.put("id", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(one_TAG, "转化后的json 是===》" + json.toString());
		return json.toString();
	}

}