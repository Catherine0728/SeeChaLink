package com.home.adapter;

import com.home.mainactivity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LeftListAdapter extends BaseAdapter {
	String[] string_List;
	Context mContext = null;

	public LeftListAdapter(Context mContext, String[] left_String) {
		this.mContext = mContext;
		this.string_List = left_String;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return string_List.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return string_List[position].toString();
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
					R.layout.left_menu_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.item_Name.setText(string_List[position].toString());
		return convertView;
	}

	class ViewHolder {
		TextView item_Name;

		public ViewHolder(View view, int pos) {
			item_Name = (TextView) view.findViewById(R.id.item_name);
			view.setTag(this);
		}
	}

}
