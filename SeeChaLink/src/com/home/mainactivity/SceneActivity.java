package com.home.mainactivity;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.android.service.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import com.home.listener.CommanTitle_Right_Listener;
import com.home.service.BackgroundService;
import com.home.adapter.Switch_ListAdapter;
import com.home.constants.Configer;
import com.home.db.AllSceneDB;
import com.home.util.MQTTClientUtil;
import com.home.util.Notify;
import com.home.utils.Logger;
import com.home.view.CommonTitleView;
import com.home.view.SlideToggle;
import com.home.view.SlideToggle.OnChangedListener;
import com.home.view.SwitchButton;
import com.home.view.SwitchButton.SwitchChangedListner;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 点击场景
 * 
 * 
 * @author Catherine 进入场景后，根据传过来的场景名字进行数据库查询...
 * 
 * 
 * @see{ 点击场景里面的编辑可以对场景进行下次编辑【若下次点击编辑进入，就直接是一个保存即可】}
 * 
 * */
public class SceneActivity extends Activity implements OnClickListener {
	String TAG = "SceneActivity";
	CommonTitleView commanTitle = null;
	EditText edit_scene_name = null;
	ListView Switch_List = null;
	Switch_ListAdapter switch_Adapter = null;
	Button image_scene = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scene_layout);
		id = getIntent().getIntExtra("id", 0);
		scene_Str = getIntent().getStringExtra("name").toString();
		initView();
	}

	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		if (Configer.ISCONNECT) {
		} else {

			startServer();
		}

	};

	public void startServer() {
		Log.d(TAG, "startServer");
		if (isConnect(getBaseContext())) {
			Intent intent = new Intent(this, BackgroundService.class);
			startService(intent);
		} else {
			Notify.toast(getBaseContext(), "请检查您的网络，无连接 或者 连接不正确！",
					Toast.LENGTH_LONG);
		}
	}

	int id = 0;
	ArrayList<Map<String, Object>> mList = null;
	int[] img = { R.drawable.icon_light, R.drawable.icon_tv,
			R.drawable.icon_electricity };
	String scene_Str = "";
	String Command_Info = "";
	Map<String, String> scene_Info = null;
	ArrayList<String> controlName = null;

	/**
	 * 初始化声明控件
	 * */
	public void initView() {
		Configer.PAGER = 2;
		commanTitle = (CommonTitleView) findViewById(R.id.toplayout);
		edit_scene_name = (EditText) findViewById(R.id.edit_name);

		image_scene = (Button) findViewById(R.id.btn_scene);
		image_scene.setOnClickListener(this);
		commanTitle.initData(SceneActivity.this, right_listener, scene_Str);
		Switch_List = (ListView) findViewById(R.id.switch_list);
		scene_Info = new HashMap<String, String>();
		controlName = new ArrayList<String>();
		SetView();

	}

	/**
	 * 赋值控件
	 * */
	public void SetView() {

		edit_scene_name.setText(scene_Str);
		scene_Info = CheckDB(scene_Str);
		if (scene_Info.size() == 0) {

		} else {
			String Command_Info = scene_Info.get("command_info").toString();
			String image_uri = scene_Info.get("image_uri").toString();
			if (null == image_uri || image_uri.equals("")) {

			} else {
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(image_uri,
						bitmapOptions);
				bitmap = Configer.zoomBitmap(bitmap, 650, 300);
				// image_scene.setImageBitmap(bitmap);
				Drawable db = new BitmapDrawable(bitmap);
				image_scene.setBackground(db);
				// if (bitmap != null && !bitmap.isRecycled()) {
				// bitmap.recycle();
				// }
			}
			String[] str = Command_Info.split(",");
			for (int i = 0; i < str.length; i++) {
				controlName.add(str[i]);
			}

		}

		mList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < controlName.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", controlName.get(i).toString());
			map.put("icon", img[i % img.length]);
			mList.add(map);
		}
		switch_Adapter = new Switch_ListAdapter(this, mList, id);
		Switch_List.setAdapter(switch_Adapter);

	}

	/**
	 * 查询数据库
	 * 
	 * */
	AllSceneDB sceneDB = null;

	public Map<String, String> CheckDB(String sceneName) {
		Map<String, String> sceneinfo = new HashMap<String, String>();
		String command = "";
		String image_uri = "";
		if (null == sceneDB) {
			sceneDB = new AllSceneDB(this);
		}
		if (sceneDB.select(sceneName) == 0) {
			// 没有数据
		} else {

			// 有数据，然后把相关的需要执行的命令给查询出来
			Cursor cursor = sceneDB.selectName(sceneName);
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				command = cursor.getString(cursor
						.getColumnIndex(sceneDB.s_Command));
				image_uri = cursor.getString(cursor
						.getColumnIndex(sceneDB.s_Image_Uri));
				// Map<String, String> map = new HashMap<String, String>();
				sceneinfo.put("command_info", command);
				sceneinfo.put("image_uri", image_uri);
				// sceneinfo.add(map);

			}
			Log.d(TAG, "要执行的命令有===》" + sceneinfo);
		}
		return sceneinfo;
	}

	CommanTitle_Right_Listener right_listener = new CommanTitle_Right_Listener() {

		@Override
		public void DotRight(boolean isDot) {

		}

		@Override
		public void DotLeft(boolean isDot) {
			if (isDot) {

			}

		}

		@Override
		public void DotRightEdit(boolean isEdit) {
			// TODO Auto-generated method stub
			if (isEdit) {

				edit_scene_name.setFocusable(true);
				edit_scene_name.setFocusableInTouchMode(true);
				edit_scene_name.setEnabled(true);
				edit_scene_name.setBackgroundResource(R.drawable.edit_selector);

				for (int i = 0; i < controlName.size(); i++) {
					switch_Adapter.chooseState(i, true);
				}

			}
			Toast.makeText(SceneActivity.this, "请对" + scene_Str + "进行编辑",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void DotRightFinish(boolean isFinish) {

			if (isFinish) {
				StringBuffer sb = new StringBuffer();
				ArrayList<Map<String, Object>> newList = switch_Adapter
						.ReturnList();
				String newName = edit_scene_name.getText().toString();
				for (int i = 0; i < newList.size(); i++) {
					String name = newList.get(i).get("title").toString();
					sb.append(name + ",");
				}
				Toast.makeText(SceneActivity.this,
						"完成编辑==" + newName + "==其遥控为==" + sb,
						Toast.LENGTH_SHORT).show();
				// 存入数据库
				if (null == sceneDB) {
					sceneDB = new AllSceneDB(SceneActivity.this);
				}

				sceneDB.update(scene_Str, newName, sb.toString(), mPhotoPath);
				finish();
			}
		}

	};

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");

		// if (isConnect(getBaseContext())) {
		// Intent intent = new Intent(this, BackgroundService.class);
		// stopService(intent);
		// }
		super.onDestroy();
	}

	/**
	 * 判断网络是否连接
	 * */
	public boolean isConnect(Context context) {
		Log.d(TAG, "isConnect");
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.image_scene:
			ToShowDialog();
			break;

		default:
			break;
		}
	}

	/**
	 * 显示选择对话框
	 * 
	 */
	/* 请求码 */
	static final int IMAGE_REQUEST_CODE = 0;
	static final int RESULT_REQUEST_CODE = 2;
	// 跳转到照相机
	private File mPhotoFile;
	private String mPhotoPath = "";
	public final static int CAMERA_RESULT = 8888;

	private void ToShowDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(SceneActivity.this)
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.content_picture_choose);
		// 设置宽高
		window.setLayout(LayoutParams.FILL_PARENT, 350);
		window.setGravity(Gravity.BOTTOM);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);
		// 设置监听
		final Button btn_picture = (Button) window.findViewById(R.id.btn_photo);
		final Button btn_camera = (Button) window.findViewById(R.id.btn_camera);

		Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
		btn_picture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// btn_picture.setTextColor(0xFF14a1e3);
				// btn_picture.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.btn_copy_icon2, 0, 0);
				TOPhoto();
				dialog.cancel();

			}
		});
		btn_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// btn_camera.setTextColor(0xFF14a1e3);
				// btn_camera.setCompoundDrawablesWithIntrinsicBounds(0,
				// R.drawable.btn_copy_icon2, 0, 0);
				ToCamera();
				dialog.cancel();
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		// 因为我们用的是windows的方法，所以不管ok活cancel都要加上“dialog.cancel()”这句话，
		// 不然有程序崩溃的可能，仅仅是一种可能，但我们还是要排除这一点，对吧？
		// 用AlertDialog的两个Button，即使监听里什么也不写，点击后也是会吧dialog关掉的，不信的同学可以去试下

	}

	// 选择本地图片
	public void TOPhoto() {
		Intent intentFromGallery = new Intent();
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
	}

	public void ToCamera() {

		// this is the second method to get the image
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if (Configer.hasSdcard()) {
			mPhotoPath = Configer.sd_Path + getPhotoFileName();
			mPhotoFile = new File(mPhotoPath);
			if (!mPhotoFile.exists()) {
				try {
					mPhotoFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
			startActivityForResult(intent, CAMERA_RESULT);

		} else {
			Toast.makeText(SceneActivity.this, "未找到存储卡，无法存储照片！",
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 用时间戳生成照片名称
	 * 
	 * @return
	 */
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;

			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
				break;
			case CAMERA_RESULT:
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath,
						bitmapOptions);
				bitmap = Configer.zoomBitmap(bitmap, 650, 300);
				// Bitmap output = Configer.getRoundedCornerBitmap(
				// SceneActivity.this, bitmap, R.drawable.btn_tv_press);
				// if (bitmap != null && !bitmap.isRecycled()) {
				// bitmap.recycle();
				// }
				// image_scene.setImageBitmap(bitmap);
				Drawable db = new BitmapDrawable(bitmap);
				image_scene.setBackground(db);

				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Log.d(TAG, "startPhotoZoom");
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 650);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {

			Bitmap photo = extras.getParcelable("data");
			// Bitmap output =
			// Configer.getRoundedCornerBitmap(SceneActivity.this,
			// photo, R.drawable.btn_tv_press);
			// if (photo != null && !photo.isRecycled()) {
			// photo.recycle();
			// }
			// image_scene.setImageBitmap(photo);
			Drawable db = new BitmapDrawable(photo);
			image_scene.setBackground(db);
		}
	}
}
