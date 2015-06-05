package com.home.mainactivity;

import java.util.Timer;
import java.util.TimerTask;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * 启动界面
 * 
 * @author catherine
 * 
 */
public class PreStartActivity extends Activity {

	// 设置倒计时器
	Timer timer = new Timer();
	int number = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pre_start);

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

				// if (StringTools.isNullOrEmpty(utils
				// .getPassValue(Constants.LOCK_KEY))) {
				Intent it = new Intent(PreStartActivity.this, ConfigActivity.class);
				startActivity(it);
				finish();
				// } else {
				// Intent it = new Intent(StartActivity.this,
				// LockActivity.class);
				// startActivity(it);
				// finish();
				// }

				break;

			case 1:
				Intent it1 = new Intent(PreStartActivity.this, ConfigActivity.class);
				startActivity(it1);
				finish();
				break;
			}

		};

	};
}
