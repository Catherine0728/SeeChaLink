package com.cn.daming.deskclock;

import java.util.Calendar;

import com.home.mainactivity.R;
import com.lhl.adapter.AlarmTimeAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class DeskClockMainActivity extends Activity implements
		OnItemClickListener {

	static final String PREFERENCES = "AlarmClock";

	/**
	 * This must be false for production. If true, turns on logging, test code,
	 * etc.
	 */
	static final boolean DEBUG = false;

	private SharedPreferences mPrefs;
	// 用于加载长按list弹出的contextmenu的布局
	private LayoutInflater mFactory;
	private ListView mAlarmsList;
	private Cursor mCursor;
	View addAlarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_clock);

		// 取getSharedPreferences中key==“AlarmClock”的值
		mPrefs = getSharedPreferences(PREFERENCES, 0);
		// 获取闹钟的cursor
		mCursor = Alarms.getAlarmsCursor(getContentResolver());
		initView();

	}

	/**
	 * 
	 * 初始化控件
	 * */
	public void initView() {
		mAlarmsList = (ListView) findViewById(R.id.alarms_list);
		addAlarm = findViewById(R.id.add_alarm);
		// 更新布局界面
		updateLayout();
	}

	// 加载更新界面布局
	private void updateLayout() {

		AlarmTimeAdapter adapter = new AlarmTimeAdapter(this, mCursor);
		mAlarmsList.setAdapter(adapter);
		mAlarmsList.setVerticalScrollBarEnabled(true);
		mAlarmsList.setOnItemClickListener(this);
		// 给list 绑定ContextMenu 菜单，长按此子控件就会弹出ContextMenu菜单
		mAlarmsList.setOnCreateContextMenuListener(this);

		addAlarm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addNewAlarm();
			}
		});
		// Make the entire view selected when focused.
		addAlarm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				v.setSelected(hasFocus);
			}
		});

	}

	/**
	 * 添加闹钟
	 * */
	private void addNewAlarm() {
		startActivity(new Intent(this, SetAlarm.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 * 创建上下文菜单
	 */
	@Override
	public boolean onContextItemSelected(final MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int id = (int) info.id;
		// Error check just in case.
		if (id == -1) {
			return super.onContextItemSelected(item);
		}
		switch (item.getItemId()) {
		case R.id.delete_alarm:
			// Confirm that the alarm will be deleted.
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.delete_alarm))
					.setMessage(getString(R.string.delete_alarm_confirm))
					.setPositiveButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface d, int w) {
									Alarms.deleteAlarm(
											DeskClockMainActivity.this, id);
								}
							}).setNegativeButton(android.R.string.cancel, null)
					.show();
			return true;

		case R.id.enable_alarm:
			final Cursor c = (Cursor) mAlarmsList.getAdapter().getItem(
					info.position);
			final Alarm alarm = new Alarm(c);
			Alarms.enableAlarm(this, alarm.id, !alarm.enabled);
			if (!alarm.enabled) {
				SetAlarm.popAlarmSetToast(this, alarm.hour, alarm.minutes,
						alarm.daysOfWeek);
			}
			return true;

		case R.id.edit_alarm:
			Intent intent = new Intent(this, SetAlarm.class);
			intent.putExtra(Alarms.ALARM_ID, id);
			startActivity(intent);
			return true;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 * android.view.View, android.view.ContextMenu.ContextMenuInfo) 创建菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		// Inflate the menu from xml.
		getMenuInflater().inflate(R.menu.context_menu, menu);

		// Use the current item to create a custom view for the header.
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		final Cursor c = (Cursor) mAlarmsList.getAdapter().getItem(
				(int) info.position);
		final Alarm alarm = new Alarm(c);

		// Construct the Calendar to compute the time.
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, alarm.hour);
		cal.set(Calendar.MINUTE, alarm.minutes);
		final String time = Alarms.formatTime(this, cal);
		// 取自定义布局的LayoutInflater
		mFactory = LayoutInflater.from(this);
		// Inflate the custom view and set each TextView's text.

		final View v = mFactory.inflate(R.layout.context_menu_header, null);
		TextView textView = (TextView) v.findViewById(R.id.header_time);
		textView.setText(time);
		textView = (TextView) v.findViewById(R.id.header_label);
		textView.setText(alarm.label);

		// Set the custom view on the menu.
		menu.setHeaderView(v);
		// Change the text based on the state of the alarm.
		if (alarm.enabled) {
			menu.findItem(R.id.enable_alarm).setTitle(R.string.disable_alarm);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * 设置菜单的点击事件的处理
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.menu_item_desk_clock:
			// modify by wangxianming in 2012-4-14
			// startActivity(new Intent(this, DeskClock.class));
			return true;
		case R.id.menu_item_add_alarm:
			addNewAlarm();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu) 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long) 创建菜单的点击事件响应
	 */
	public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
		Intent intent = new Intent(this, SetAlarm.class);
		intent.putExtra(Alarms.ALARM_ID, (int) id);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ToastMaster.cancelToast();
		mCursor.close();
	}
}