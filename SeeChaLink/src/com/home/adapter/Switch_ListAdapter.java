package com.home.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.home.constants.Configer;
import com.home.mainactivity.R;
import com.home.util.MQTTClientUtil;
import com.home.view.SwitchButton;
import com.home.view.SwitchButton.SwitchChangedListner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

/**
 * 
 * 进入场景里面所有遥控的列表
 * 
 * @author Catherine
 * */
public class Switch_ListAdapter extends BaseAdapter {
	Context mContext = null;
	ArrayList<Map<String, Object>> mList = null;
	int id = 1;
	public Map<String, String> editorValue = new HashMap<String, String>();
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
		init();
	}

	public void init() {

		editorValue.clear();
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
	private Integer index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.scene_layout_item, null);
			new ViewHolder(convertView, position);
		}

		viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.edit_Title.setText(mList.get(position).get("title")
				.toString());
		viewHolder.edit_Title.setCompoundDrawablesWithIntrinsicBounds(
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
			viewHolder.edit_Title.setEnabled(true);
		} else {
			viewHolder.edit_Title.setEnabled(false);
		}

		return convertView;
	}

	public ArrayList<Map<String, Object>> ReturnList() {
		return mList;
	}

	class ViewHolder {
		EditText edit_Title;
		SwitchButton switchButton;

		public ViewHolder(View view, int pos) {

			edit_Title = (EditText) view.findViewById(R.id.toggle_text_one);
			edit_Title.setTag(pos);
			switchButton = (SwitchButton) view.findViewById(R.id.toggle_one);
			edit_Title.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
					}
					return false;
				}
			});

			edit_Title.addTextChangedListener(new MyTextWatcher(this));
			view.setTag(this);
		}
	}

	// 监听edittext里面的变化
	class MyTextWatcher implements TextWatcher {
		public MyTextWatcher(ViewHolder holder) {
			mHolder = holder;
		}

		private ViewHolder mHolder;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s != null && !"".equals(s.toString())) {
				int position = (Integer) mHolder.edit_Title.getTag();
				mList.get(position).put("title", s.toString());// 当EditText数据发生改变的时候存到data变量中
			}
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