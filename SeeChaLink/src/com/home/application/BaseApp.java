package com.home.application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.home.constants.LayoutValue;
import com.home.utils.Logger;
import com.lhl.crash.CrashHandler;

import android.app.Activity;
import android.app.Application;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 继承application
 * 
 * 把所有的Activity集中起来，然后，在特定的时候，一起销毁
 * 
 * @author Catherine
 * 
 * */
public class BaseApp extends Application {
	public static String TAG = "BaseApp";

	// private static BaseApp instance;

	// 运用list来保存们每一个activity是关键
	private List<Activity> mList = new LinkedList<Activity>();

	// 构造方法
	public BaseApp() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		// instance = this;
		//
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
		// ImageLoader.getInstance(instance, 0).init();

		// initSound();

		// 屏幕宽高
		DisplayMetrics dm = getResources().getDisplayMetrics();
		LayoutValue.SCREEN_WIDTH = dm.widthPixels;
		LayoutValue.SCREEN_HEIGHT = dm.heightPixels;
		// 密度
		LayoutValue.SCREEN_DENSITY = dm.density;
		// SQLiteDatabase.loadLibs(this);
		//
		// if (dbOperator == null) {
		// dbOperator = new DbOperator(instance);
		// }

	}

	// // 实例化一次
	// public synchronized static BaseApp getInstance() {
	// if (null == instance) {
	// instance = new BaseApp();
	// }
	// return instance;
	// }

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	// 杀进程
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	private static HashMap<String, Integer> soundMap;
	private static SoundPool soundPool;

	private static boolean loaded = false;

	private void initSound() {

		soundMap = new HashMap<String, Integer>();
		soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				loaded = true;
			}
		});

		// soundMap.put(Constants.BACK, soundPool.load(this, R.raw.back, 1));
		// soundMap.put(Constants.BACK_TO_TOP,
		// soundPool.load(this, R.raw.back_to_top, 1));
		// soundMap.put(Constants.COMFIRE, soundPool.load(this, R.raw.comfire,
		// 1));
		// soundMap.put(Constants.MOVE_DOWN,
		// soundPool.load(this, R.raw.move_bottom, 1));
		// soundMap.put(Constants.MOVE_LEFT,
		// soundPool.load(this, R.raw.move_left, 1));
		// soundMap.put(Constants.MOVE_RIGHT,
		// soundPool.load(this, R.raw.move_right, 1));
		// soundMap.put(Constants.NET_CONNECTED,
		// soundPool.load(this, R.raw.net_connected, 1));
		// soundMap.put(Constants.NET_FOUND,
		// soundPool.load(this, R.raw.net_found, 1));
		// soundMap.put(Constants.TOP_FLOAT_DISABLED,
		// soundPool.load(this, R.raw.top_float_disabled, 1));
		// soundMap.put(Constants.TOP_FLOAT,
		// soundPool.load(this, R.raw.top_float, 1));
		// soundMap.put(Constants.PAGE_CHANGE,
		// soundPool.load(this, R.raw.page_change, 1));
		// soundMap.put(Constants.LOCK, soundPool.load(this, R.raw.lock, 1));
		// soundMap.put(Constants.EFFECT_TICK,
		// soundPool.load(this, R.raw.effect_tick, 1));
	}
	//
	// public static void playSound(String action) {
	// AudioManager amgr = (AudioManager) getSystemService(AUDIO_SERVICE);
	// float volumeCurrent = amgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	// float volumeMax = amgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	// float volumn = volumeCurrent / volumeMax;
	// volumn = volumn / 2;
	// Logger.log("volumn:" + volumn);
	// if (loaded) {
	// soundPool.play(soundMap.get(action), volumn, volumn, 0, 0, 1f);
	// }
	//
	// }

}
