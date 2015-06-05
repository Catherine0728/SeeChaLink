package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.service.BackgroundService;
import com.home.application.BaseApp;
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
public class MainActivity extends FragmentActivity {
	String TAG = "MainActivity";
	private SharePreferenceUtil preferens;
	SharedPreferences sp = null;

	MQTTClientUtil mcu = null;
	CommonTitleView commantitleView = null;

	GridView grid_Scene = null;
	SceneAdapter adapter = null;

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
		Log.i(TAG, "onResume");
	}

	private DrawerLayout mDrawerLayout;
	ArrayList<Map<String, Object>> SceneList = null;
	String[] Scene_array = { "客房场景", "卧室场景", "起床场景", "添加场景" };

	public void initView() {
		BaseApp.getInstance().addActivity(this);
		Configer.PAGER = 1;
		commantitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commantitleView.initData(MainActivity.this, RightListener, "智能家控");
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		SceneList = new ArrayList<Map<String, Object>>();
		grid_Scene = (GridView) findViewById(R.id.grid_scene);
		for (int i = 0; i < Scene_array.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", Scene_array[i].toString());
			map.put("image", R.drawable.home_addimg_bg);
			SceneList.add(map);
		}
		adapter = new SceneAdapter(this, SceneList);
		grid_Scene.setAdapter(adapter);
		// mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
		// Gravity.RIGHT);
		// mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
		// Gravity.LEFT);
		grid_Scene.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == SceneList.size() - 1) {
					startActivity(new Intent().setClass(MainActivity.this,
							AddSceneActivity.class));
				} else {
					TOINTETTN(position, Scene_array[position].toString());

				}

			}
		});
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

	Intent intent = null;

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

	public void TOINTETTN(int id, String name) {
		intent = new Intent();
		intent.setClass(MainActivity.this, SceneActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("name", name);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		// if (Configer.ISUNREGISTER) {
		// Log.i(TAG, "ISUNREGISTER=true");
		// } else {
		// Log.i(TAG, "ISUNREGISTER=false");
		if (isConnect(getBaseContext())) {
			// Configer.ISUNREGISTER = true;
			Intent intent = new Intent(this, BackgroundService.class);
			stopService(intent);
			// }
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
			// finish();
			BaseApp.getInstance().exit();
			return false;
		} else {

			return super.onKeyDown(keyCode, event);
		}
	}

}

/**
 * 
 * 建立一个内部类，然后对场景进行填充
 * 
 * */
class SceneAdapter extends BaseAdapter {
	Context mContext = null;
	ArrayList<Map<String, Object>> Scene_String = null;

	public SceneAdapter(Context mContext,
			ArrayList<Map<String, Object>> sceneString) {
		this.mContext = mContext;
		this.Scene_String = sceneString;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Scene_String.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Scene_String.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_layout_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.text_name_one.setText(Scene_String.get(position).get("name")
				.toString());
		if (position == Scene_String.size() - 1) {

		} else {

			viewHolder.image_one.setBackgroundResource((Integer) Scene_String
					.get(position).get("image"));

		}

		return convertView;
	}

	class ViewHolder {
		ImageView image_one;
		TextView text_name_one;
		FrameLayout framelayout_one;

		public ViewHolder(View view, int pos) {
			image_one = (ImageView) view.findViewById(R.id.image_one);
			text_name_one = (TextView) view.findViewById(R.id.text_name_one);
			framelayout_one = (FrameLayout) view
					.findViewById(R.id.framelayout_one);
			view.setTag(this);
		}
	}

}