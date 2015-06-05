package com.lhl.unuse;

import java.util.TimerTask;

import org.eclipse.paho.android.service.Constants;

import org.eclipse.paho.android.service.SharePreferenceUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.mainactivity.R;
import com.home.mainactivity.SceneActivity;
import com.home.service.BackgroundService;
import com.home.constants.Configer;
import com.home.util.MQTTClientUtil;
import com.home.util.Notify;
import com.home.view.CommonTitleView;
import com.nineoldandroids.view.ViewHelper;

/**
 * 
 * MQTT连接到中控（中转控制器），然后通过pub topic来控制设备的状态
 * 
 * @see IOT
 * 
 *      {@code 中控的ID：192.168.1.1
 *      first:connect the device
 *      Sec:pub topic to control the device}
 * 
 * 
 * */
public class MainActivity_Unuse extends FragmentActivity implements
		OnClickListener {
	Button btn_get_up, btn_lay_up;
	private SharePreferenceUtil preferens;
	// this is the pub topic
	EditText edit_ip;
	SharedPreferences sp = null;

	Button btn_Connect;
	java.util.Timer timer;
	MQTTClientUtil mcu = null;
	FrameLayout framelayout_one, framelayout_two;
	CommonTitleView commantitleView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);

		preferens = new SharePreferenceUtil(this);
		preferens.setDeviceId();

		initView();
		initEvents();

	}

	@Override
	protected void onResume() {
		// 获取数据
		super.onResume();
		Connect();
	}

	private DrawerLayout mDrawerLayout;

	public void initView() {
		Configer.PAGER = 1;
		commantitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commantitleView
				.initData(MainActivity_Unuse.this, RightListener, "智能家控");
		framelayout_one = (FrameLayout) findViewById(R.id.framelayout_one);
		framelayout_two = (FrameLayout) findViewById(R.id.framelayout_two);
		framelayout_one.setOnClickListener(this);
		framelayout_two.setOnClickListener(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		// mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
		// Gravity.RIGHT);
		// mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
		// Gravity.LEFT);
	}

	CommanTitle_Right_Listener RightListener = new CommanTitle_Right_Listener() {

		@Override
		public void DotRight(boolean isDot) {
			if (isDot) {
				OpenRightMenu();
			}
		}

		@Override
		public void DotLeft(boolean isDot) {
			if (isDot) {
				OpenLeftMenu();
			}
		}

		@Override
		public void DotRightEdit(boolean isEdit) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub
			
		}
	};

	private void initEvents() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
				System.out.println("the newstate is===>" + newState);
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT")) {

					float leftScale = 1 - 0.3f * scale;

					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				} else {
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}

			}

			@Override
			public void onDrawerOpened(View drawerView) {

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerLayout.setDrawerLockMode(
						DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
	}

	public void OpenRightMenu() {
		mDrawerLayout.openDrawer(Gravity.RIGHT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.RIGHT);
	}

	public void OpenLeftMenu() {
		mDrawerLayout.openDrawer(Gravity.LEFT);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
				Gravity.LEFT);
	}

	public void ChangeCheck(int pos) {
		switch (pos) {
		// case R.id.botton_one_on:
		//
		// button_one_on.setTextColor(0XFFFFFFFF);
		// button_one_off.setTextColor(0XFF000000);
		// myHandler.sendEmptyMessage(ONE_ON);
		//
		// break;
		// case R.id.button_one_off:
		// myHandler.sendEmptyMessage(ONE_OFF);
		// button_one_off.setTextColor(0XFFFFFFFF);
		// button_one_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_two_on:
		// myHandler.sendEmptyMessage(TWO_ON);
		// button_two_on.setTextColor(0XFFFFFFFF);
		// button_two_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_two_off:
		// myHandler.sendEmptyMessage(TWO_OFF);
		// button_two_off.setTextColor(0XFFFFFFFF);
		// button_two_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_three_on:
		//
		// myHandler.sendEmptyMessage(THREE_ON);
		// button_three_on.setTextColor(0XFFFFFFFF);
		// button_three_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_three_off:
		// myHandler.sendEmptyMessage(THREE_OFF);
		// button_three_off.setTextColor(0XFFFFFFFF);
		// button_three_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_four_on:
		//
		// myHandler.sendEmptyMessage(FOUR_ON);
		// button_four_on.setTextColor(0XFFFFFFFF);
		// button_four_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_four_off:
		// myHandler.sendEmptyMessage(FOUR_OFF);
		// button_four_off.setTextColor(0XFFFFFFFF);
		// button_four_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_five_on:
		//
		// myHandler.sendEmptyMessage(FIVE_ON);
		// button_five_on.setTextColor(0XFFFFFFFF);
		// button_five_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_five_off:
		// myHandler.sendEmptyMessage(FIVE_OFF);
		// button_five_off.setTextColor(0XFFFFFFFF);
		// button_five_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_six_on:
		//
		// myHandler.sendEmptyMessage(SIX_ON);
		// button_six_on.setTextColor(0XFFFFFFFF);
		// button_six_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_six_off:
		// myHandler.sendEmptyMessage(SIX_OFF);
		// button_six_off.setTextColor(0XFFFFFFFF);
		// button_six_on.setTextColor(0XFF000000);
		// break;
		// case R.id.botton_seven_on:
		//
		// myHandler.sendEmptyMessage(SEVEN_ON);
		// button_seven_on.setTextColor(0XFFFFFFFF);
		// button_seven_off.setTextColor(0XFF000000);
		// break;
		// case R.id.button_seven_off:
		// myHandler.sendEmptyMessage(SEVEN_OFF);
		// button_seven_off.setTextColor(0XFFFFFFFF);
		// button_seven_on.setTextColor(0XFF000000);
		// break;
		//
		// default:
		// break;
		}
	}

	class Job extends TimerTask {

		@Override
		public void run() {
		}

	}

	Intent intent = null;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_connect:
			Connect();
			break;

		case R.id.btn_get_up:
			myHandler.sendEmptyMessage(ALL_DONE);
			break;
		case R.id.btn_lay_up:
			myHandler.sendEmptyMessage(DELAY);

			break;
		case R.id.framelayout_one:
			myHandler.sendEmptyMessage(1);

			break;
		case R.id.framelayout_two:
			myHandler.sendEmptyMessage(2);

			break;
		}

	}

	public void Connect() {

		if (Constants.MQTT_SERVER.length() == 0
				|| null == Constants.MQTT_SERVER
				|| Constants.MQTT_SERVER.equals("127.0.0.1")) {
			Toast.makeText(MainActivity_Unuse.this, "请填写Ip地址",
					Toast.LENGTH_LONG).show();
		} else {
			// // 添加数据，一定要使用edit函数
			// Editor editor = sp.edit();
			// editor.putString("ip", Constants.MQTT_SERVER);
			// // 保存数据 ，类是于事务
			// editor.commit();
			// Constants.MQTT_SERVER =Constants.MQTT_SERVER;
			// System.out.println("the server ip is===" +
			// Constants.MQTT_SERVER);
			startServer();

		}
	}

	public void startServer() {
		System.out.println("startServer");
		if (isConnect(getBaseContext())) {
			Intent intent = new Intent(this, BackgroundService.class);
			startService(intent);
		} else {
			Notify.toast(getBaseContext(), "请检查您的网络，无连接 或者 连接不正确！",
					Toast.LENGTH_LONG);
		}
	}

	public void StartJob() {
		timer = new java.util.Timer();
		timer.schedule(new Job(), 3000, 2000);
		// isDot = true;

	}

	public boolean isConnect(Context context) {
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

	public final static int FOUR_OFF = 0x000100;
	public final static int FOUR_ON = 0x001100;

	public final static int FIVE_OFF = 0x000101;
	public final static int FIVE_ON = 0x001101;

	public final static int SIX_OFF = 0x000110;
	public final static int SIX_ON = 0x001110;

	public final static int SEVEN_OFF = 0x000111;
	public final static int SEVEN_ON = 0x001111;

	public final static int ALL_DONE = 0x000077;
	public final static int DELAY = 0x000088;
	public final static int CHECK = 0x000000;
	public final static int ISBACK = 0x00001;
	public final static int INTENT_SCENE = 0x009999;
	String getJson = "";
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (null == mcu) {
				mcu = MQTTClientUtil.getInstance(getBaseContext());

			}

			switch (msg.what) {
			// case ONE_ON:
			// // getJson = GetRf3o4MWirelessMultiCtrlJson(false, 0, "pressed",
			// // false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(9);
			// mcu.publish(Constants.topicName, getJson, 2);
			//
			// break;
			// case ONE_OFF:
			//
			// // getJson = GetRf3o4MWirelessMultiCtrlJson(false, 0,
			// // "released",
			// // false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(1);
			// mcu.publish(Constants.topicName, getJson, 2);
			//
			// break;
			// case TWO_ON:
			// // getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1, "pressed",
			// // false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(10);
			// mcu.publish(Constants.topicName, getJson, 2);
			// break;
			// case TWO_OFF:
			// // getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1,
			// // "released",
			// // false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(2);
			// mcu.publish(Constants.topicName, getJson, 2);
			//
			// break;
			// case THREE_ON:
			// // getJson = GetJson(false, 2, "pressed", false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(11);
			// mcu.publish(Constants.topicName, getJson, 2);
			// break;
			// case THREE_OFF:
			// // getJson = GetRf3o4MWirelessMultiCtrlJson(false, 2,
			// // "released",
			// // false, 0);
			// getJson = GetRf3o4MWirelessCtrlClickJson(3);
			// mcu.publish(Constants.topicName, getJson, 2);
			//
			// break;
			case FOUR_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 3, "pressed",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(12);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case FOUR_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false,3,
				// "released",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(4);
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case FIVE_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 3, "pressed",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(13);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case FIVE_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false,3,
				// "released",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(5);
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case SIX_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 3, "pressed",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(14);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case SIX_OFF:

				// getJson = GetRf3o4MWirelessMultiCtrlJson(false,3,
				// "released",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(6);
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case SEVEN_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 3, "pressed",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(15);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case SEVEN_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false,3,
				// "released",
				// false, 0);
				getJson = GetRf3o4MWirelessCtrlClickJson(7);
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case DELAY:
				getJson = GetRf3o4MWirelessMultiCtrlJson(false, 3, "released",
						true, 10);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case ALL_DONE:
				getJson = GetRf3o4MWirelessMultiCtrlJson(true, 3, "released",
						false, 0);
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			default:
				break;
			case INTENT_SCENE:

				break;
			case 1:
				TOINTETTN(1);
				break;
			case 2:
				TOINTETTN(2);
				break;
			}

		}
	};

	public void TOINTETTN(int id) {
		intent = new Intent();
		intent.setClass(MainActivity_Unuse.this, SceneActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	/**
	 * 
	 * 将pub的信息转化为json
	 * 
	 * @see 2015-05-25 {"api": "v1.0.0", "service": "smarthome-mqtt", "id": 1}
	 */
	public String GetRf3o4MWirelessMultiCtrlJson(Boolean isallDone, int id,
			String status, boolean isDelay, int delay_Time) {
		String one_TAG = "GetRf3o4MWirelessMultiCtrlJson";
		JSONObject json = new JSONObject();
		try {
			json.put("api", "v1.0.0");
			json.put("service", "smarthome-mqtt");
			if (isallDone) {
				json.put("commands", GetSecJson());
			} else {
				json.put("id", id);
				if (isDelay) {
					if (delay_Time <= 600 && delay_Time >= 0) {
						json.put("delay", delay_Time);
					} else {

						Toast.makeText(this, "延时时间超出操作范围，请检查！",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					json.put("mode", status);

				}

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(one_TAG, "转化后的json 是===》" + json.toString());
		return json.toString();
	}

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

	public String GetRf3o4MWirelessCtrlPressedJson() {
		String one_TAG = "GetRf3o4MWirelessCtrlPressedJson";
		JSONObject json = new JSONObject();
		try {
			json.put("api", "v1.0.0");
			json.put("service", "smarthome-mqtt");
			json.put("id", 3);
			json.put("delay", 0);// json.put("delay","-");
			json.put("mode", "pressed");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(one_TAG, "转化后的json 是===》" + json.toString());
		return json.toString();
	}

	public String GetRf3o4MWirelessCtrlReleasedJson() {
		String one_TAG = "GetRf3o4MWirelessCtrlReleasedJson";
		JSONObject json = new JSONObject();
		try {
			json.put("api", "v1.0.0");
			json.put("service", "smarthome-mqtt");
			json.put("id", 3);
			json.put("delay", 0);// json.put("delay","-");
			json.put("mode", "released");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(one_TAG, "转化后的json 是===》" + json.toString());
		return json.toString();
	}

	/**
	 * 如果是联控,那么我们就添加命令
	 * 
	 * "commands": [ { "id": 0, "wait": 1 }, { "id": 1, "delay": 1, "wait": 1 },
	 * { "id": 2, "wait": 1 }, { "id": 3, "delay": 2, "wait": 1 }, { "id": 1,
	 * "mode": "pressed", "delay": 3, "wait": 1 }, { "id": 1, "mode": "released"
	 * } ]
	 * 
	 * 定义了一些数组来表示无规则的命令
	 * 
	 * @see 0，代表没有这个字段，-1.代表“pressed”,1代表“released”
	 * 
	 * */
	int[] ID = { 0, 1, 2, 3, 1, 1 };
	int[] WAIT = { 1, 1, 1, 1, 1, 0 };
	int[] DELAY_ID = { 0, 1, 0, 2, 3, 0 };
	int[] MODE = { 0, 0, 0, 0, 1, -1 };

	private JSONArray GetSecJson() {
		String sec_TAG = "GetSecJson";
		JSONArray jsonArray = new JSONArray();

		try {
			for (int i = 0; i < ID.length; i++) {

				jsonArray.put(i,
						getThirdJson(ID[i], WAIT[i], DELAY_ID[i], MODE[i]));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(sec_TAG, "转化后的json 是===》" + jsonArray.toString());
		return jsonArray;
	}

	// 针对commands下面的json进行封装
	private JSONObject getThirdJson(int id, int wait, int delay, int mode) {
		String three_TAG = "getThirdJson";
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			if (wait == 0) {

			} else {
				json.put("wait", wait);

			}

			if (delay == 0) {

			} else {
				json.put("delay", delay);
			}

			if (mode == 0) {

			} else if (mode == -1) {
				json.put("mode", "released");
			} else {
				json.put("mode", "pressed");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(three_TAG, "转化后的json 是===》" + json.toString());
		return json;
	}

	@Override
	protected void onDestroy() {
		if (isConnect(getBaseContext())) {
			Intent intent = new Intent(this, BackgroundService.class);
			stopService(intent);
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			if (isConnect(getBaseContext())) {
				Intent intent = new Intent(this, BackgroundService.class);
				stopService(intent);
			}
			finish();
			return false;
		} else {

			return super.onKeyDown(keyCode, event);
		}
	}

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	// myHandler.sendEmptyMessage(THREE_ON);
	// System.out.println("this is pressed");
	// } else if (event.getAction() == MotionEvent.ACTION_UP) {
	// myHandler.sendEmptyMessage(THREE_OFF);
	// System.out.println("this is released");
	// }
	//
	// return false;
	// }

}
