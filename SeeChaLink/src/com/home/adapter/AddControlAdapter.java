package com.home.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.home.mainactivity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 这是添加遥控的adapoter 用于显示添加遥控的适配
 * 
 * @author Catherine
 * 
 * */
public class AddControlAdapter extends BaseAdapter {
	public static String TAG = "AddControlAdapter";
	Context mContext = null;
	ArrayList<Map<String, Object>> mList;

	public AddControlAdapter(Context mContext,
			ArrayList<Map<String, Object>> mList) {
		this.mContext = mContext;
		this.mList = mList;
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

	public void ReSetList(ArrayList<Map<String, Object>> mList) {
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
					R.layout.add_control_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.text_control_name.setText(mList.get(position).get("name")
				.toString());
		viewHolder.image_control.setBackgroundResource((Integer) mList.get(
				position).get("image"));

		return convertView;
	}

	class ViewHolder {
		TextView text_control_name;
		ImageView image_control;

		public ViewHolder(View view, int pos) {
			text_control_name = (TextView) view
					.findViewById(R.id.text_name_control);
			image_control = (ImageView) view.findViewById(R.id.image_control);
			view.setTag(this);
		}
	}
}
