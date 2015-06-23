package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.cn.daming.deskclock.SetAlarm;
import com.home.adapter.SceneListAdapter;
import com.home.db.AllSceneDB;
import com.home.listener.ContextListener;
import com.home.util.Utility;

/**
 * @author Catherine 我的设备
 * 
 * */

public class SceneFragment extends Fragment {
	String TAG = "SceneFragment";
	public View v = null;

	GridView grid_scene = null;
	SceneListAdapter SceneAdapter = null;
	ArrayList<Map<String, String>> SceneList = new ArrayList<Map<String, String>>();
	/**
	 * 查询数据库，然后得到所有的场景
	 * */
	AllSceneDB SceneDB = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == v) {
			v = LayoutInflater.from(getActivity()).inflate(
					R.layout.scene_fragment, null);
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
		if (null == SceneAdapter) {
			SceneAdapter = new SceneListAdapter(getActivity(), SceneList);
		}
		SceneAdapter.ReGetList(SceneList);
	}

	public void initView() {
		grid_scene = (GridView) v.findViewById(R.id.grid_scene);
		CheckDB();
		SetView();
		grid_scene.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TOINTETTN(position, SceneList.get(position).get("name")
				// .toString());

			}
		});
		grid_scene.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				new BaseActivity().ToShowDialog(getActivity(), position,
						SceneList.get(position).get("name").toString(),
						contextListener);
				OperaId = position;
				OperaName = SceneList.get(position).get("name").toString();
				return false;
			}
		});

	}

	int OperaId = 0;
	String OperaName = "";

	public void SetView() {
		if (null == SceneAdapter) {
			SceneAdapter = new SceneListAdapter(getActivity(), SceneList);
		}
		grid_scene.setAdapter(SceneAdapter);
		Utility.setGridViewHeightBasedOnChildren(grid_scene, 2);
	}

	public void CheckDB() {
		Log.d(TAG, "CheckDB");
		SceneList.clear();
		if (null == SceneDB) {
			SceneDB = new AllSceneDB(getActivity());
		}

		Cursor cursor = SceneDB.select();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex(SceneDB.s_NAME));
			String image_uri = cursor.getString(cursor
					.getColumnIndex(SceneDB.s_Image_Uri));
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", name);
			map.put("image", image_uri);
			SceneList.add(map);
			Log.d(TAG, "CheckDB场景有====>" + name);
		}

	}

	public void TOToast(String str) {
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}

	Intent intent = null;

	public boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("isconnect error", e.toString());
		}
		return false;
	}

	public void TOINTETTN(int id, String name) {
		intent = new Intent();
		intent.setClass(getActivity(), SceneActivity.class);
		intent.putExtra("id", id);
		intent.putExtra("name", name);
		// intent.put
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		menu.setHeaderTitle("操作场景");
		// 添加菜单项
		menu.add(0, Menu.FIRST, 0, "编辑");
		menu.add(0, Menu.FIRST + 1, 0, "删除");
		menu.add(0, Menu.FIRST + 2, 0, "定时开启");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Toast.makeText(getActivity(),
				"content" + item.getItemId() + info.position, Toast.LENGTH_LONG)
				.show();
		return super.onContextItemSelected(item);
	}

	ContextListener contextListener = new ContextListener() {

		@Override
		public void DotListener(int id) {
			if (id == 0) {
				TOINTETTN(OperaId, OperaName);
			} else if (id == 1) {
				/**
				 * @see{这里应该有一个检测，若此场景已经有了定时启动了，然后点击定时启动， 只是打开启用，如果没有 就进入设置界面}
				 * */
				Intent intent_alarm = new Intent();
				intent_alarm.setClass(getActivity(), SetAlarm.class);
				intent_alarm.putExtra("name", OperaName);

				startActivity(intent_alarm);
			} else if (id == 2) {
				SceneDB.delete(OperaName);

				CheckDB();
				if (null == SceneAdapter) {
					SceneAdapter = new SceneListAdapter(getActivity(),
							SceneList);
				}
				SceneAdapter.ReGetList(SceneList);
				
			}
		}
	};
}
