package com.home.mainactivity;

import java.util.ArrayList;

import com.home.adapter.AddControlAdapter;
import com.home.adapter.AddSwitchAdapter;
import com.home.db.AllSwitchDB;
import com.home.util.Utility;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 所有开关显示
 * 
 * @author Catherine
 * */
public class SwitchFragment extends Fragment {
	public static String TAG = "SwitchFragment";
	View v = null;
	ListView list_switch;
	AllSwitchDB switchDB = null;
	AddSwitchAdapter switchAdpter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == v) {
			v = LayoutInflater.from(getActivity()).inflate(
					R.layout.switch_fragment, null);
		}
		ViewGroup p = (ViewGroup) v.getParent();
		if (p != null) {
			p.removeView(v);
		}
		initView();
		return v;
	}

	ArrayList<String> mList = new ArrayList<String>();

	public void initView() {

		list_switch = (ListView) v.findViewById(R.id.list_switch);

		SetView();
	}

	/**
	 * 
	 * 填充数据
	 * */
	public void SetView() {
		CheckDB();
		if (null == switchAdpter) {
			switchAdpter = new AddSwitchAdapter(getActivity(), mList, 0);
		}
		list_switch.setAdapter(switchAdpter);
		Utility.setListViewHeightBasedOnChildren(list_switch);
		list_switch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 执行
			}
		});

	}

	public void CheckDB() {
		mList.clear();
		if (null == switchDB) {
			switchDB = new AllSwitchDB(getActivity());
		}
		Cursor cursor = switchDB.select();
		cursor.moveToFirst();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(switchDB.s_NAME));

			mList.add(name);
			Log.d(TAG, name);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CheckDB();
		if (null == switchAdpter) {
			switchAdpter = new AddSwitchAdapter(getActivity(), mList, 0);
		}
		switchAdpter.ReSetList(mList);
	}
}
