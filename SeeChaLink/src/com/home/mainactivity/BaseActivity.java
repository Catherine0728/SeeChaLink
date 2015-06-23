package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.paho.android.service.SharePreferenceUtil;

import com.cn.daming.deskclock.SetAlarm;
import com.home.adapter.AddControlAdapter;
import com.home.adapter.AddSwitchAdapter;
import com.home.application.BaseApp;
import com.home.db.AllCommandDB;
import com.home.db.AllSceneDB;
import com.home.db.AllSwitchDB;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.listener.ContextListener;
import com.home.util.MQTTClientUtil;
import com.home.view.Dialog_Loding;
import com.nineoldandroids.view.ViewHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author Catherine
 * @see{这里分别为遥控，开关，场景
 * 
 * 
 * @category 基类
 * 
 * */
public class BaseActivity extends FragmentActivity implements OnClickListener {
	public static String TAG = "BaseActivity";
	private SharePreferenceUtil preferens;
	SharedPreferences sp = null;
	Fragment mContext;
	Fragment main = null;
	static Context con;
	public FragmentManager mFragmentManager = null;
	Button btn_Control, btn_Scene, btn_Switch;
	Button btn_left_info, btn_right_add;
	TextView text_title;
	BaseApp baseApp = null;
	// 设置倒计时器
	Timer timer = new Timer();
	int number = 2;
	// mqtt
	MQTTClientUtil mcu = null;
	// 侧滑栏需要
	private DrawerLayout mDrawerLayout;
	ContextListener contextListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basemain);
		con = this.getApplicationContext();
		main = new ControllFragment();
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		if (savedInstanceState != null) {
			mContext = mFragmentManager.getFragment(savedInstanceState,
					"mContext");

		} else {
			mFragmentManager.beginTransaction().add(R.id.body_layout, main)
					.commitAllowingStateLoss();
		}

		if (mContext == null) {

			mContext = main;
		}
		init();
		initView();
		initEvents();
	}

	public void init() {
		if (null == baseApp) {
			baseApp = new BaseApp();
		}
		preferens = new SharePreferenceUtil(BaseActivity.this);
		preferens.setDeviceId();

		/**
		 * there is a fault that start the schedule in onresume
		 * */
		ISSHOW = getIntent().getBooleanExtra("isshow", false);
		if (ISSHOW) {
			myHandler.sendEmptyMessage(SHOW);

			timer.schedule(new Job(), 1 * 1000, 1 * 1000);
		}
	}

	/**
	 * 初始化
	 * */
	public void initView() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
		btn_Control = (Button) findViewById(R.id.btn_control);
		btn_Scene = (Button) findViewById(R.id.btn_scene);
		btn_Switch = (Button) findViewById(R.id.btn_switch);
		btn_left_info = (Button) findViewById(R.id.btn_left_info);
		btn_right_add = (Button) findViewById(R.id.btn_right_info);
		btn_Control.setOnClickListener(this);
		btn_Scene.setOnClickListener(this);
		btn_Switch.setOnClickListener(this);
		btn_left_info.setOnClickListener(this);
		btn_right_add.setOnClickListener(this);
		text_title = (TextView) findViewById(R.id.text_nav_title);
		MyControll(btn_Control, btn_Scene, btn_Switch);
	}

	/**
	 * 
	 * 切换模块的内容
	 * 
	 * @param fragment
	 */

	public void switchContent(Fragment fragment, String FragmentTitle,
			Boolean isAdd) {
		text_title.setText(FragmentTitle);
		mContext = fragment;
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();

		fragmentTransaction

		.replace(R.id.body_layout, fragment).setTransition(
				FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		// if (!isAdd) {
		// } else {
		//
		// fragmentTransaction.addToBackStack(null);
		// }

		fragmentTransaction.commitAllowingStateLoss();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		super.onResume();
		baseApp.addActivity(this);
	}

	private Boolean ISSHOW = false;

	class Job extends TimerTask {

		@Override
		public void run() {
			number--;
			if (number == 0) {
				timer.cancel();
				myHandler.sendEmptyMessage(CANCELL);
			}
		}
	}

	Dialog_Loding dialog_Loading = null;
	public static final int SHOW = 0x000099;
	public static final int CANCELL = 0x000011;
	Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW:
				dialog_Loading = new Dialog_Loding(BaseActivity.this,
						"Loading...");

				if (null != dialog_Loading && !dialog_Loading.isShowing()) {
					dialog_Loading.show();
				}

				break;
			case CANCELL:
				dialog_Loading.dismiss();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			// if (isConnect(getBaseContext())) {
			// Intent intent = new Intent(this, BackgroundService.class);
			// stopService(intent);
			// }
			baseApp.exit();
			return false;
		} else {

			return super.onKeyDown(keyCode, event);
		}
	}

	public void MyControll(Button btn_one, Button btn_two, Button btn_three) {
		//
		// btn_Control.setCompoundDrawablesWithIntrinsicBounds(0,
		// R.drawable.icon_find_device_f, 0, 0);
		// btn_Scene.setCompoundDrawablesWithIntrinsicBounds(0,
		// R.drawable.icon_control_device_d, 0, 0);
		// btn_Switch.setCompoundDrawablesWithIntrinsicBounds(0,
		// R.drawable.icon_perference_device_d, 0, 0);
		/* 修改获得焦点的颜色 */
		btn_two.setTextColor(0xFF999999);
		btn_one.setTextColor(0xFFFFFFFF);
		btn_three.setTextColor(0xFF999999);
	}

	Fragment newContent = null;

	// to set the tab values,0==controll,1==scene,2==switch
	int flags = 0;
	/***
	 * to define a code to compare the resultcode which from to_define_activity
	 * 
	 * 
	 * */

	public static final int CONTROLCODE = 0;
	public static final int SCENECODE = 1;
	public static final int SWITCHCODE = 2;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_control:
			newContent = new ControllFragment();
			switchContent(newContent, "我的遥控", false);
			MyControll(btn_Control, btn_Scene, btn_Switch);
			flags = 0;
			break;
		case R.id.btn_scene:
			newContent = new SceneFragment();
			switchContent(newContent, "我的场景", false);
			MyControll(btn_Scene, btn_Control, btn_Switch);
			flags = 1;
			break;
		case R.id.btn_switch:
			newContent = new SwitchFragment();
			switchContent(newContent, "我的开关", false);
			MyControll(btn_Switch, btn_Scene, btn_Control);
			flags = 2;

			break;
		case R.id.btn_left_info:
			OpenLeftMenu();
			break;
		case R.id.btn_right_info:
			if (flags == 0) {
				// intent.setClass(BaseActivity.this, To_Define_Activity.class);
				// intent.putExtra("name", "");
				// intent.putExtra("fromwhere", "添加");
				// startActivityForResult(intent, CONTROLCODE);
				startActivity(new Intent().setClass(BaseActivity.this,
						To_Define_Activity.class));
			} else if (flags == 1) {
				startActivity(new Intent().setClass(BaseActivity.this,
						AddSceneActivity.class));
			} else {
				startActivity(new Intent().setClass(BaseActivity.this,
						AddSwitchActivity.class));
			}
			break;
		default:
			break;
		}
	}

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
					// ViewHelper.setTranslationX(mContent,
					// -mMenu.getMeasuredWidth() * slideOffset);
					// ViewHelper.setPivotX(mContent,
					// mContent.getMeasuredWidth());
					// ViewHelper.setPivotY(mContent,
					// mContent.getMeasuredHeight() / 2);
					// mContent.invalidate();
					// ViewHelper.setScaleX(mContent, rightScale);
					// ViewHelper.setScaleY(mContent, rightScale);
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

	AddControlAdapter controlAdapter = null;

	AllCommandDB commanDB = null;
	AllSwitchDB switchDB = null;

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// Log.d(TAG, "onActivityResult");
	// if (resultCode == CONTROLCODE) {
	// String name = data.getStringExtra("name").toString();
	// String uri = data.getStringExtra("image_uri").toString();
	// if (uri.equals("") || null == uri) {
	//
	// } else {
	//
	// }
	// Log.d(TAG, "修改的name is===>" + name + "===resultCode===>"
	// + resultCode);
	// CheckControlDB();
	// controlAdapter.ReSetList(mcontrolList);
	// } else if (resultCode == SWITCHCODE) {
	// CheckSwitchDB();
	//
	// } else {
	//
	// }
	//
	// }

	// define a list to get content the control
	ArrayList<String> mcontrolList = new ArrayList<String>();
	ArrayList<String> mswitchList = new ArrayList<String>();

	public void CheckControlDB() {

		if (null == commanDB) {
			commanDB = new AllCommandDB(BaseActivity.this);
		}
		Cursor cursor = commanDB.select();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(commanDB.c_command));
			// String uri = cursor.getString(cursor
			// .getColumnIndex(commanDB.c_Command_Image));
			// // strName.add(name);
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("name", name);
			// map.put("image_uri", uri);
			mcontrolList.add(name);

		}
	}

	public void CheckSwitchDB() {
		if (null == switchDB) {
			switchDB = new AllSwitchDB(BaseActivity.this);
		}
		Cursor cursor = switchDB.select();
		cursor.moveToFirst();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(switchDB.s_NAME));

			mswitchList.add(name);
			Log.d(TAG, name);
		}

	}

	/**
	 * 点击出现上下文
	 * 
	 * */

	int position = 0;
	String deleteName = "";

	// 显示对话框
	public void ToShowDialog(Context context, int pos, final String name,
			final ContextListener conListener) {
		this.contextListener = conListener;
		position = pos;
		deleteName = name;
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.content_view_opera);
		// 设置宽高
		window.setLayout(LayoutParams.FILL_PARENT, 350);
		window.setGravity(Gravity.BOTTOM);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);
		// 设置监听
		final Button btn_edit = (Button) window.findViewById(R.id.btn_edit);
		final Button btn_start_ontime = (Button) window
				.findViewById(R.id.btn_start_ontime);
		final Button btn_delete = (Button) window.findViewById(R.id.btn_delete);

		Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
		btn_edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_edit.setTextColor(0xFF14a1e3);
				btn_edit.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_edit_icon2, 0, 0);

				dialog.cancel();

			
				conListener.DotListener(0);

			}
		});
		btn_start_ontime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_start_ontime.setTextColor(0xFF14a1e3);
				btn_start_ontime.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_timer_icon2, 0, 0);
				dialog.cancel();
				conListener.DotListener(1);

			}
		});
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_delete.setTextColor(0xFF14a1e3);
				btn_delete.setCompoundDrawablesWithIntrinsicBounds(0,
						R.drawable.btn_delete_icon2, 0, 0);

				dialog.cancel();
				conListener.DotListener(2);

			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		// 因为我们用的是windows的方法，所以不管ok活cancel都要加上“dialog.cancel()”这句话，
		// 不然有程序崩溃的可能，仅仅是一种可能，但我们还是要排除这一点，对吧？
		// 用AlertDialog的两个Button，即使监听里什么也不写，点击后也是会吧dialog关掉的，不信的同学可以去试下

	}

	public void TOINTETTN(int id, String name) {
		Intent intent = new Intent();
		intent.setClass(BaseActivity.this, SceneActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("name", name);
		// intent.put
		startActivity(intent);
	}
}
