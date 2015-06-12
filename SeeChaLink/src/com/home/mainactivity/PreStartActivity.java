package com.home.mainactivity;

import java.util.Timer;
import java.util.TimerTask;

import com.home.constants.Configer;
import com.wujay.fund.GestureVerifyActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

/**
 * 启动界面
 * 
 * @author catherine
 * 
 */
public class PreStartActivity extends Activity {
	public static String TAG = "PreStartActivity";

	// 设置倒计时器
	Timer timer = new Timer();
	int number = 1;

	// 查询是否需要密码
	SharedPreferences sharePre = null, shareHost = null;
	String inputCode = "111111";
	String hostName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_start);
		initView();

	}

	public void initView() {
		if (null == sharePre) {
			sharePre = getSharedPreferences(Configer.FUNDGESTURE,
					Context.MODE_PRIVATE);
		}
		if (null == shareHost) {
			shareHost = getSharedPreferences(Configer.ISBOKER,
					Context.MODE_PRIVATE);
		}

		hostName = shareHost.getString("host_name", "hostname");
		inputCode = sharePre.getString("input_code", "11111");
		Log.d(TAG, "得到的密码为===》" + inputCode + "===得到的服务器的uri===》" + hostName);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		timer.schedule(new Job(), 1 * 1000, 1 * 1000);
		// JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		// JPushInterface.onPause(this);
	}

	class Job extends TimerTask {

		@Override
		public void run() {
			number--;
			if (number == 0) {
				timer.cancel();
				handler.sendEmptyMessage(0);
			}
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (inputCode.equals("11111") || inputCode.equals("111111")) {
					if (hostName.equals("hostname") || "".equals(hostName)
							|| null == hostName) {
						Intent it = new Intent(PreStartActivity.this,
								ConfigActivity.class);
						startActivity(it);
						finish();
					} else {
						Intent it = new Intent(PreStartActivity.this,
								MainActivity.class);
						it.putExtra("isshow", true);
						startActivity(it);
						finish();
					}

				} else {

					Intent it = new Intent(PreStartActivity.this,
							GestureVerifyActivity.class);
					it.putExtra("input_code", inputCode);
					startActivity(it);
					finish();
				}

				break;

			case 1:
				Intent it1 = new Intent(PreStartActivity.this,
						ConfigActivity.class);
				startActivity(it1);
				finish();
				break;
			}

		};

	};
}
