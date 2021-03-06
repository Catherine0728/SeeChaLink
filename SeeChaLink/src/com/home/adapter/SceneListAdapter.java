package com.home.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.home.constants.Configer;
import com.home.mainactivity.R;
import com.home.mainactivity.R.array;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 所有的场景进行填充显示
 * 
 * @author Catherine
 * 
 * */
public class SceneListAdapter extends BaseAdapter {
	public static String TAG = "SceneListAdapter";
	Context mContext = null;
	ArrayList<Map<String, String>> Scene_String = null;

	public SceneListAdapter(Context mContext,
			ArrayList<Map<String, String>> sceneString) {
		this.mContext = mContext;
		this.Scene_String = sceneString;
	}

	@Override
	public int getCount() {
		// Log.d(TAG, "getCount");
		// TODO Auto-generated method stub
		return Scene_String.size();
	}

	@Override
	public Object getItem(int position) {
		// Log.d(TAG, "getItem");
		// TODO Auto-generated method stub
		return Scene_String.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Log.d(TAG, "getItemId");
		// TODO Auto-generated method stub
		return position;
	}

	// 重新得到mlist
	public void ReGetList(ArrayList<Map<String, String>> mList) {
		// Log.d(TAG, "ReGetList");
		this.Scene_String = mList;
		this.notifyDataSetChanged();
	}

	ViewHolder viewHolder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d(TAG, "getView");
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.scene_fragment_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		//
		// if (position == Scene_String.size()) {
		// viewHolder.text_name_one.setText("添加场景");
		// viewHolder.image_one.setBackgroundResource(R.drawable.icon_add);
		// } else {
		viewHolder.text_name_one.setText(Scene_String.get(position).get("name")
				.toString());
		String image_uri = Scene_String.get(position).get("image").toString();
		if (null == image_uri || image_uri.equals("")) {
			viewHolder.image_one
					.setBackgroundResource(R.drawable.home_addimg_bg);
		} else {
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = 4;
			Bitmap bitmap = BitmapFactory.decodeFile(image_uri, bitmapOptions);
			bitmap = Configer.zoomBitmap(bitmap, 450, 240);

			viewHolder.image_one.setImageBitmap(bitmap);

		}

		return convertView;
	}

	class ViewHolder {
		ImageView image_one;
		TextView text_name_one;
		FrameLayout framelayout_one;

		public ViewHolder(View view, int pos) {
			image_one = (ImageView) view.findViewById(R.id.image_one);
			text_name_one = (TextView) view.findViewById(R.id.text_name_one);
			framelayout_one = (FrameLayout) view
					.findViewById(R.id.framelayout_one);
			view.setTag(this);
		}
	}

}