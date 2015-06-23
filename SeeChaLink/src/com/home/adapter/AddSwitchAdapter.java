package com.home.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.home.constants.Configer;
import com.home.mainactivity.R;
import com.home.mainactivity.To_Define_Activity;
import com.home.util.MQTTClientUtil;
import com.home.view.SwitchButton;
import com.home.view.SwitchButton.SwitchChangedListner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 这是添加开关的adapter
 * 
 * @author Catherine
 * 
 * 
 *         开关的名字以及开关
 * */
public class AddSwitchAdapter extends BaseAdapter {
	public static String TAG = "AddSwitchAdapter";
	Context mContext = null;
	ArrayList<String> mList;
	int id = 0;

	public AddSwitchAdapter(Context mContext, ArrayList<String> mList, int pos) {
		this.mContext = mContext;
		this.mList = mList;
		this.id = pos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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

	public void ReSetList(ArrayList<String> mList) {
		Log.d(TAG, "ReSetList");
		this.mList = mList;
		notifyDataSetChanged();
	}

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView");
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.switch_layout_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.text_swtich_name.setText(mList.get(position).toString());
		// int id = (Integer) mList.get(position).get("id");
		if (position == 0) {
			viewHolder.switchButton.setOnSwitchListner(new Changelisten_one());
		} else if (position == 1) {
			viewHolder.switchButton.setOnSwitchListner(new Changelisten_two());
		} else {

			viewHolder.switchButton
					.setOnSwitchListner(new Changelisten_three());

		}
		return convertView;
	}

	class ViewHolder {
		TextView text_swtich_name;
		SwitchButton switchButton;

		public ViewHolder(View view, int pos) {
			text_swtich_name = (TextView) view
					.findViewById(R.id.text_switch_name);
			switchButton = (SwitchButton) view.findViewById(R.id.toggle_one);
			view.setTag(this);
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
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(9);
				} else if (id == 1) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(12);

				} else {

					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(15);
				}

				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case ONE_OFF:

				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 0,
				// "released",
				// false, 0);
				if (id == 0) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(1);
				} else if (id == 1) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(4);
				} else {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(7);

				}
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case TWO_ON:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1, "pressed",
				// false, 0);
				if (id == 0) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(10);
				} else {

					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(13);
				}
				mcu.publish(Configer.topicName, getJson, 2);

				break;
			case TWO_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 1,
				// "released",
				// false, 0);
				if (id == 0) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(2);
				} else {

					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(5);
				}
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case THREE_ON:
				// getJson = GetJson(false, 2, "pressed", false, 0);
				if (id == 0) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(11);
				} else {

					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(14);
				}
				mcu.publish(Configer.topicName, getJson, 2);
				break;
			case THREE_OFF:
				// getJson = GetRf3o4MWirelessMultiCtrlJson(false, 2,
				// "released",
				// false, 0);

				if (id == 0) {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(3);
				} else {
					getJson = Configer.GetRf3o4MWirelessCtrlClickJson(6);
				}

				mcu.publish(Configer.topicName, getJson, 2);
				break;
			}
		}
	};
}
