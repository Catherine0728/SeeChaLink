package com.lhl.adapter;

import java.util.Calendar;

import com.cn.daming.deskclock.Alarm;
import com.cn.daming.deskclock.Alarms;
import com.cn.daming.deskclock.DeskClockMainActivity;
import com.cn.daming.deskclock.DigitalClock;
import com.cn.daming.deskclock.SetAlarm;
import com.home.mainactivity.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * listview的适配器继承CursorAdapter
 * 
 * @author wangxianming 也可以使用BaseAdapter
 */
public class AlarmTimeAdapter extends CursorAdapter {
	private LayoutInflater mFactory;
	Context mContext = null;

	public AlarmTimeAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		this.mContext = context;
		mFactory = LayoutInflater.from(context);
	}

	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View ret = mFactory.inflate(R.layout.alarm_time, parent, false);

		DigitalClock digitalClock = (DigitalClock) ret
				.findViewById(R.id.digitalClock);
		digitalClock.setLive(false);
		return ret;
	}

	// 把view绑定cursor的每一项
	public void bindView(View view, Context context, Cursor cursor) {
		final Alarm alarm = new Alarm(cursor);

		View indicator = view.findViewById(R.id.indicator);

		// Set the initial resource for the bar image.
		final ImageView barOnOff = (ImageView) indicator
				.findViewById(R.id.bar_onoff);
		barOnOff.setImageResource(alarm.enabled ? R.drawable.ic_indicator_on
				: R.drawable.ic_indicator_off);

		// Set the initial state of the clock "checkbox"
		final CheckBox clockOnOff = (CheckBox) indicator
				.findViewById(R.id.clock_onoff);
		clockOnOff.setChecked(alarm.enabled);

		// Clicking outside the "checkbox" should also change the state.
		// 对checkbox设置监听，使里外一致
		indicator.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				clockOnOff.toggle();
				updateIndicatorAndAlarm(clockOnOff.isChecked(), barOnOff, alarm);
			}
		});

		DigitalClock digitalClock = (DigitalClock) view
				.findViewById(R.id.digitalClock);

		// set the alarm text
		final Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, alarm.hour);
		c.set(Calendar.MINUTE, alarm.minutes);
		digitalClock.updateTime(c);
		digitalClock.setTypeface(Typeface.DEFAULT);

		// Set the repeat text or leave it blank if it does not repeat.
		TextView daysOfWeekView = (TextView) digitalClock
				.findViewById(R.id.daysOfWeek);
		final String daysOfWeekStr = alarm.daysOfWeek.toString(mContext, false);
		if (daysOfWeekStr != null && daysOfWeekStr.length() != 0) {
			daysOfWeekView.setText(daysOfWeekStr);
			daysOfWeekView.setVisibility(View.VISIBLE);
		} else {
			daysOfWeekView.setVisibility(View.GONE);
		}

		// Display the label
		TextView labelView = (TextView) view.findViewById(R.id.label);
		if (alarm.label != null && alarm.label.length() != 0) {
			labelView.setText(alarm.label);
			labelView.setVisibility(View.VISIBLE);
		} else {
			labelView.setVisibility(View.GONE);
		}
	}

	// 更新checkbox
	private void updateIndicatorAndAlarm(boolean enabled, ImageView bar,
			Alarm alarm) {
		bar.setImageResource(enabled ? R.drawable.ic_indicator_on
				: R.drawable.ic_indicator_off);
		Alarms.enableAlarm(mContext, alarm.id, enabled);
		if (enabled) {
			SetAlarm.popAlarmSetToast(mContext, alarm.hour, alarm.minutes,
					alarm.daysOfWeek);
		}
	}

};
