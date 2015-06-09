package com.home.adapter;

import java.util.ArrayList;

import com.home.mainactivity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 这是添加设备的adapter
 * 
 * @author Catherine
 * 
 * */
public class AddSceneAdapter extends BaseAdapter {
	public static String TAG = "AddSceneAdapter";
	Context mContext = null;
	ArrayList<String> mList;

	public AddSceneAdapter(Context mContext, ArrayList<String> mList) {
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
					R.layout.add_scene_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		if (position % 2 == 0) {
			viewHolder.text_Name_left.setText(mList.get(position).toString());
			viewHolder.text_name_right.setVisibility(View.INVISIBLE);
		} else {

			viewHolder.text_name_right.setText(mList.get(position).toString());
			viewHolder.text_Name_left.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	class ViewHolder {
		TextView text_Name_left, text_name_right;

		public ViewHolder(View view, int pos) {
			text_Name_left = (TextView) view.findViewById(R.id.text_Name_left);
			text_name_right = (TextView) view
					.findViewById(R.id.text_Name_right);
			view.setTag(this);
		}
	}
}
