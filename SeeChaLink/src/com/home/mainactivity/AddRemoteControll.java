package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.home.constants.Configer;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 添加遥控的页面
 * 
 * @see {提供一系列常见的列表，然后进行选择以及自定义}
 * 
 * */
public class AddRemoteControll extends Activity {
	public static String TAG = "AddRemoteControll";
	ListView list_control = null;
	ControlListAdapter controlAdapter = null;
	CommonTitleView commTitleView = null;
	String FromWhere = "";
	public static int REQCODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remote_controll);
		initView();
	}

	ArrayList<Map<String, Object>> mList = null;
	String[] strList = { "空调", "音响", "机顶盒", "电视", "TC", "插座", "手势", "自定义" };

	public void initView() {

		Configer.PAGER = 9;
		commTitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commTitleView.initData(this, null, "选择遥控");
		FromWhere = getIntent().getStringExtra("fromwhere");

		list_control = (ListView) findViewById(R.id.list_control);
		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < strList.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", strList[i]);
			mList.add(map);

		}

		controlAdapter = new ControlListAdapter(this, mList);
		list_control.setAdapter(controlAdapter);
		Log.d(TAG, "FromWhere is==>" + FromWhere);
		list_control.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String str = parent.getAdapter().getItem(position).toString();
				str = str.substring(str.indexOf("=") + 1, str.indexOf("}"))
						.toString();
				if (FromWhere.equals("添加场景")) {

					Intent mIntent = new Intent();
					mIntent.putExtra("name", str);
					// 设置结果，并进行传送
					setResult(REQCODE, mIntent);
					finish();

				} else {
					// Toast.makeText(AddRemoteControll.this, str,
					// Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					if (position == 0 || position == 1) {

						intent.setClass(AddRemoteControll.this,
								Controll_AirConditioner.class);

					} else if (position == 7) {
						intent.setClass(AddRemoteControll.this,
								User_Define_Activity.class);
					} else {
						intent.setClass(AddRemoteControll.this,
								Number_Controll_Activity.class);
					}
					startActivity(intent);
				}

			}
		});

	}
}

class ControlListAdapter extends BaseAdapter {

	Context mContext = null;
	ArrayList<Map<String, Object>> mList = null;

	public ControlListAdapter(Context mContext,
			ArrayList<Map<String, Object>> mLsit) {

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
		viewHolder.text_Name
				.setText(mList.get(position).get("name").toString());
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
