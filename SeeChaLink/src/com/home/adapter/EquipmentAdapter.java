package com.home.adapter;

import java.util.ArrayList;

import com.home.mainactivity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * 建立一个显示设备的adapter
 * 
 * @author Catherine
 * */
public class EquipmentAdapter extends BaseAdapter {

	Context mContext = null;
	ArrayList<String> mList = null;

	public EquipmentAdapter(Context mContext, ArrayList<String> mList) {
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

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.my_equipment_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.text_Name.setText(mList.get(position).toString());
		return convertView;
	}

	class ViewHolder {
		TextView text_Name;

		public ViewHolder(View view, int pos) {
			text_Name = (TextView) view.findViewById(R.id.text_name);
			view.setTag(this);
		}
	}
}
