package com.home.mainactivity;

import java.util.Timer;

import com.home.constants.Configer;
import com.home.constants.LayoutValue;
import com.home.service.BackgroundService;
import com.home.util.Notify;
import com.home.utils.DeviceCheck;
import com.home.utils.Logger;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 配置页
 * 
 * @author catherine
 * 
 */
public class ConfigActivity extends Activity {
	String TAG = "ConfigActivity";

	// TextView no_text, err_text;

	// CircleProgress circleProgress;

	EditText ssid_value;
	String ssid;

	CommonTitleView commmonTitleView;
	Button btn_connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.config);
		initview();

	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		// TODO Auto-generated method stub
		super.onResume();
		// if (isConnect(getBaseContext())) {
		// Intent intent = new Intent(this, BackgroundService.class);
		// stopService(intent);
		// }
	}

	private void initview() {
		Configer.PAGER = -1;
		// if (PrefrenceUtils.getInstance(this).getSwitchState(
		// Constants.GESTUREPASS)
		// && !StringTools.isNullOrEmpty(PrefrenceUtils.getInstance(this)
		// .getPassValue(Constants.LOCK_KEY))) {
		// // Intent intent = new Intent(this, LockActivity.class);
		// // startActivity(intent);
		// System.out.println("配置不成功");
		// }

		commmonTitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commmonTitleView.initData(ConfigActivity.this, null, "添加设备");

		// no_text = (TextView) findViewById(R.id.no_text);
		// err_text = (TextView) findViewById(R.id.err_text);
		// no_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		// err_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

		ssid_value = (EditText) findViewById(R.id.ssid_value);

		ssid_value.setText(DeviceCheck.getWifiSSID(this));

		// timer = new Timer();

		// circleProgress = (CircleProgress) findViewById(R.id.submit);
		// circleProgress.setOnClickListener(new OnClickListener() {

		// @Override
		// public void onClick(View v) {
		// closeInputMethod();
		// ssid = ssid_value.getText().toString();
		//
		// timer.schedule(new Job(), 1 * 1000, 1 * 1000);
		// }
		// });

		// no_text.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent it = new Intent(ConfigActivity.this, MainActivity.class);
		// startActivity(it);
		// finish();
		// }
		// });
		findViewById(R.id.btn_connect).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startServer();

					}
				});

	}

	Thread myThread = new Thread() {
		public void run() {
			myHander.sendEmptyMessage(CONNECT);
		};
	};

	private static final int CONNECT = 0x000001;
	Handler myHander = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CONNECT:
				startServer();
				break;

			default:
				break;
			}
		};
	};

	public void startServer() {
		Log.d(TAG, "startServer");
		if (isConnect(getBaseContext())) {
			Intent intent = new Intent(this, BackgroundService.class);
			startService(intent);
			IntentHome();
		} else {
			Notify.toast(getBaseContext(), "请检查您的网络，无连接 或者 连接不正确！",
					Toast.LENGTH_LONG);
			// finish();
		}
	}

	private void IntentHome() {

		Intent it1 = new Intent(ConfigActivity.this, BaseActivity.class);
		startActivity(it1);
		finish();
	}

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
			Log.i("isconnect error", e.toString());
		}
		return false;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);

		// 获取状态栏高度
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		LayoutValue.statusBarHeight = frame.top;

		// LayoutValue.titleViewHeight = commmonTitleView.getHeight();
		Logger.log("statusBarHeight:" + LayoutValue.statusBarHeight
				+ ",titleViewHeight:" + LayoutValue.titleViewHeight);

	}

	public void closeInputMethod() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
