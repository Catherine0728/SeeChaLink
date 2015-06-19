package com.home.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.home.listener.SelectAll_Listener;
import com.home.mainactivity.R;

/**
 * 
 * 建立一个适配器来存储当前设备
 * 
 * @author Catherine
 */
public class ControlListAdapter extends BaseAdapter {
	public static String TAG = "ControlListAdapter";
	Context mContext = null;
	ArrayList<String> mList = null;
	SelectAll_Listener isSelectAll = null;

	public ControlListAdapter(Context mContext, ArrayList<String> mLsit,
			SelectAll_Listener isSelectAll) {

		this.mContext = mContext;
		this.mList = mLsit;
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
					R.layout.remote_controll_item, null);
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