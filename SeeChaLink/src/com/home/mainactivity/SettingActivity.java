package com.home.mainactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;

import com.home.constants.Configer;
import com.home.util.DataClearTools;
import com.home.util.Utility;
import com.home.view.CommonTitleView;
import com.home.view.SlideToggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 这是设置的界面
 * 
 * @author Catherine
 * */
public class SettingActivity extends Activity {
	ListView setting_list_two = null, setting_list_one = null;
	CommonTitleView commantitleview = null;
	Button btn_exit = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initView();
	}

	String[] str_one = { "手势密码", "AP设置" };
	String[] str_two = { "意见反馈", "检测新版本", "关于SeeChaLink" };
	int[] img_one = {};
	int[] img_two = {};
	SettingBaseAdapter adapter_One, adapter_Two;
	ArrayList<Map<String, Object>> mList_One, mList_Two;

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 
	 * 初始化
	 * */
	public void initView() {
		Configer.PAGER =4;
		commantitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commantitleview.initData(SettingActivity.this, null, "我的设置");
		setting_list_one = (ListView) findViewById(R.id.setting_list_one);
		setting_list_two = (ListView) findViewById(R.id.setting_list_two);
		btn_exit = (Button) findViewById(R.id.btn_Exit);
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 清除缓存就oaky
				ClearCache();
				/***
				 * 如果清除成功，那么应该将首页的用户信息也给置空，且将退出账号的这个按钮给隐藏了
				 * 
				 * */

			}
		});
		mList_One = new ArrayList<Map<String, Object>>();
		mList_Two = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < str_one.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", str_one[i].toString());
			mList_One.add(map);
		}
		for (int i = 0; i < str_two.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", str_two[i].toString());
			mList_Two.add(map);
		}
		adapter_One = new SettingBaseAdapter(this, mList_One);
		adapter_Two = new SettingBaseAdapter(this, mList_Two);
		setting_list_one.setAdapter(adapter_One);
		setting_list_two.setAdapter(adapter_Two);
		Utility.setListViewHeightBasedOnChildren(setting_list_one);
		Utility.setListViewHeightBasedOnChildren(setting_list_two);
		setting_list_one.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {

					// 打开通信录好友列表页面
					// ContactsPage contactsPage = new ContactsPage();
					// contactsPage.show(SettingActivity.this);
				} else if (position == 1) {
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			}
		});
		setting_list_two.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					startActivity(new Intent().setClass(SettingActivity.this,
							FeedBackActivity.class));

				} else if (position == 1) {
					UpdateVersion();
				} else if (position == 2) {
					startActivity(new Intent().setClass(SettingActivity.this,
							HelpActivity.class));
				}
			}
		});
	}

	/**
	 * 
	 * 这是我的里面的清除缓存
	 * */

	public void ClearCache() {
		/**
		 * 
		 * 实现缓存清理
		 * */
		// AlertDialog.Builder builder = new Builder(this);
		// builder.setMessage("确定要清除所有缓存");
		// builder.setTitle("提示");
		// builder.setPositiveButton("取消", new DialogInterface.OnClickListener()
		// {u7]'
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// });
		// builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		// {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// DataClearTools.cleanApplicationData(getActivity(), null);
		// }
		//
		// });
		// builder.create().show();

		DataClearTools.cleanApplicationData(this, null);
		Configer.ISLOGIN = 0;
		btn_exit.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 这是我的里面的版本更新
	 * */

	String version;

	public void UpdateVersion() {
		showUpdataDialog();
		try {
			version = getVersionName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("the version is===>" + version);

	}

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(this);
		builer.setTitle("版本升级");
		builer.setMessage("检测到最新版本，请及时更新");
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		// 当点取消按钮时进行登录
		builer.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * 获取当前程序的版本号
	 */
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = this.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				this.getPackageName(), 0);
		return packInfo.versionName;
	}

}

class SettingBaseAdapter extends BaseAdapter {
	Context mContext = null;
	ArrayList<Map<String, Object>> mList = null;

	public SettingBaseAdapter(Context mContext,
			ArrayList<Map<String, Object>> mlist) {
		this.mContext = mContext;
		this.mList = mlist;
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
					R.layout.setting_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.text_title.setText(mList.get(position).get("title")
				.toString());
		return convertView;
	}

	class ViewHolder {
		TextView text_title, text_dot_go;

		// SlideToggle toggle_one;

		public ViewHolder(View view, int pos) {
			text_title = (TextView) view.findViewById(R.id.text_title);
			// toggle_one = (SlideToggle) view.findViewById(R.id.toggle_one);
			text_dot_go = (TextView) view.findViewById(R.id.text_dot);
			view.setTag(this);
		}
	}
}
