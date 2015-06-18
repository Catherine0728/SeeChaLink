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
	boolean isShow = false;
	SelectAll_Listener isSelectAll = null;
	// 定义一个数组来响应点击的按钮的显示选择
	public boolean[] isChose;

	public ControlListAdapter(Context mContext, ArrayList<String> mLsit,
			Boolean isShow, SelectAll_Listener isSelectAll) {

		this.mContext = mContext;
		this.mList = mLsit;
		this.isShow = isShow;
		isChose = new boolean[mLsit.size()];
		for (int i = 0; i < mLsit.size(); i++) {
			isChose[i] = false;
		}
		this.isSelectAll = isSelectAll;
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
		viewHolder.btn_Check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				ChooseState(pos, false, false);
			}
		});
		viewHolder.btn_Check.setBackgroundDrawable(SetBackGroundView(position));
		return convertView;
	}

	class ViewHolder {
		TextView text_Name;
		Button btn_Check;

		public ViewHolder(View view, int pos) {
			text_Name = (TextView) view.findViewById(R.id.text_name);
			btn_Check = (Button) view.findViewById(R.id.btn_check);
			btn_Check.setTag(pos);
			if (isShow) {
				btn_Check.setVisibility(View.VISIBLE);
			} else {
				btn_Check.setVisibility(View.GONE);
			}
			view.setTag(this);
		}
	}

	public void ChooseState(int position, boolean flag, boolean isAll) {
		Log.d(TAG, "ChooseState");
		if (isAll) {
			isChose[position] = isChose[position] == true ? flag : flag;
		} else {

			isChose[position] = isChose[position] == true ? false : true;

		}
		if (Add_CheckAll()) {
			// 一个也没有选中
			isSelectAll.IsSelectAll(true);
		} else {
			// 有选中
			isSelectAll.IsSelectAll(false);

		}
		if (Add_Check()) {
			// 有选中

		} else {

		}
		if (Add_CheckAll()) {

		} else {
			// 一个也没有选中
		}
		notifyDataSetChanged();
	}

	// 主要就是下面的代码了
	private LayerDrawable SetBackGroundView(int post) {
		Log.d(TAG, "SetBackGroundView");
		Bitmap bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(
				R.drawable.icon_check_default)).getBitmap();
		Bitmap bitmap2 = null;
		LayerDrawable la = null;
		if (isChose[post] == true) {
			bitmap2 = BitmapFactory.decodeResource(mContext.getResources(),
					R.drawable.icon_check_foucs);
		}

		if (bitmap2 != null) {
			Drawable[] array = new Drawable[1];
			// array[0] = new BitmapDrawable(bitmap);
			array[0] = new BitmapDrawable(bitmap2);
			la = new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0); // 第几张图离各边的间距
			// la.setLayerInset(1, 0, 65, 65, 0);
		} else {
			Drawable[] array = new Drawable[1];
			array[0] = new BitmapDrawable(bitmap);
			la = new LayerDrawable(array);
			la.setLayerInset(0, 0, 0, 0, 0);
		}
		return la; // 返回叠加后的图
	}

	/**
	 * 检测当前是否有选中的
	 * 
	 * */
	public boolean Add_Check() {
		for (int i = 0; i < mList.size(); i++) {
			if (isChose[i]) {
				return true;

			}
		}
		return false;

	}

	public ArrayList<String> CheckSelected() {
		ArrayList<String> AddList = new ArrayList<String>();
		for (int i = 0; i < mList.size(); i++) {
			if (isChose[i]) {
				AddList.add(mList.get(i).toString());
				Log.d(TAG, mList.get(i).toString() + "===had checked");
			}

		}
		return AddList;
	}

	/**
	 * 检测当前是否该处于全选还是取消全选的状态
	 * */
	public boolean Add_CheckAll() {
		Log.d(TAG, "Add_CheckAll");
		boolean isSelectAll = false;
		for (int i = 0; i < mList.size(); i++) {
			if (i == 0) {
				isSelectAll = isChose[i];
			} else {
				if (isSelectAll == isChose[i]) {
					if (isSelectAll) {

					} else {

						return false;
					}

				} else {
					// 全选
					return false;
				}
			}

		}
		// 取消全选
		return true;

	}
}