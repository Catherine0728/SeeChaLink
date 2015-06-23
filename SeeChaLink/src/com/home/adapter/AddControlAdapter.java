package com.home.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.home.constants.Configer;
import com.home.mainactivity.R;
import com.home.mainactivity.To_Define_Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 这是添加遥控的adapoter 用于显示添加遥控的适配，也是添加开关的adapter
 * 
 * @author Catherine
 * 
 * 
 *         遥控的名字以及图片，图片懿路径的方式传了一个进来，没有值的时候，就以图片代替
 * */
public class AddControlAdapter extends BaseAdapter {
	public static String TAG = "AddControlAdapter";
	Context mContext = null;
	// ArrayList<Map<String, String>> mList;
	ArrayList<String> mList = null;

	public AddControlAdapter(Context mContext, ArrayList<String> mList) {
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
					R.layout.add_control_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.text_control_name.setText(mList.get(position).toString());
		// String uri = mList.get(position).get("image_uri").toString();
		// if (uri.equals("") || null == uri) {
		viewHolder.image_control.setBackgroundResource(R.drawable.btn_tv_press);
		// } else {
		//
		// BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		// bitmapOptions.inSampleSize = 4;
		// Bitmap bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);
		// bitmap = Configer.zoomBitmap(bitmap, 120, 120);
		// Bitmap output = Configer.getRoundedCornerBitmap(mContext, bitmap,
		// R.drawable.btn_tv_press);
		// if (bitmap != null && !bitmap.isRecycled()) {
		// bitmap.recycle();
		// }
		// viewHolder.image_control.setImageBitmap(output);
		// }

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
