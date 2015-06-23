package com.home.mainactivity;

import java.util.ArrayList;

import com.home.adapter.AddControlAdapter;
import com.home.db.AllCommandDB;
import com.home.util.Utility;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * 遥控
 * */
public class ControllFragment extends Fragment {
	public static String TAG = "ControllFragment";
	View v = null;
	GridView grid_control = null;

	AddControlAdapter controlAdapter = null;

	AllCommandDB commanDB = null;
	ArrayList<String> mList = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == v) {
			v = LayoutInflater.from(getActivity()).inflate(
					R.layout.control_fragment, null);
		}
		ViewGroup p = (ViewGroup) v.getParent();
		if (p != null) {
			p.removeView(v);
		}
		initView();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CheckDB();
		if (null == controlAdapter) {
			controlAdapter = new AddControlAdapter(getActivity(), mList);
		}
		controlAdapter.ReSetList(mList);

	}

	public void initView() {

		grid_control = (GridView) v.findViewById(R.id.grid_control);
		SetView();
	}

	public void SetView() {
		CheckDB();
		if (null == controlAdapter) {
			controlAdapter = new AddControlAdapter(getActivity(), mList);
		}
		grid_control.setAdapter(controlAdapter);
		Utility.setGridViewHeightBasedOnChildren(grid_control, 3);
	}

	public void CheckDB() {
		mList.clear();
		if (null == commanDB) {
			commanDB = new AllCommandDB(getActivity());
		}
		Cursor cursor = commanDB.select();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(commanDB.c_command));
			mList.add(name);

		}
	}
}
