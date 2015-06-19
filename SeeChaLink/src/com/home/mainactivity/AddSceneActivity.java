package com.home.mainactivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.home.adapter.AddSceneAdapter;
import com.home.adapter.ControlListAdapter;
import com.home.adapter.ShowControlAdapter;
import com.home.constants.Configer;
import com.home.db.AllSceneDB;
import com.home.listener.CommanTitle_Right_Listener;
import com.home.listener.SelectAll_Listener;
import com.home.util.Utility;
import com.home.view.CommonTitleView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author Catherine 添加设备，场景，遥控，根据已经拥有的设备，场景下面就进行添加
 * @see {应该是选择一个当前已经拥有的设备，然后进行}
 * 
 *      {修改：将选择直接显示在本页面，在点击完成后直接跳转到场景显示的页面==SceneActivity.class}
 * */
public class AddSceneActivity extends Activity implements OnClickListener {
	public static String TAG = "AddSceneActivity";
	CommonTitleView commontitleview = null;
	// EditText edit_add_scene, edit_add_control, edit_add_device;
	Button image_scene;
	EditText edit_scene_name;
	ListView list_command;
	Button btn_select_all, btn_add;
	AddSceneAdapter addSceneAdapter = null;
	// 填充设备列表的adapter
	ShowControlAdapter controlAdapter = null;

	// TextView text_error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_scene);
		initView();
	}

	// 这是定义用来存储填充遥控列表的
	ArrayList<String> mList = new ArrayList<String>();

	// 定义一个来得到需要添加的遥控列表
	ArrayList<String> AddList = new ArrayList<String>();

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
	}

	public static int REQCODE = 0;

	public void initView() {

		Configer.PAGER = 3;
		// Configer.PAGER = -1;
		commontitleview = (CommonTitleView) findViewById(R.id.toplayout);
		commontitleview.initData(AddSceneActivity.this, right_listener, "添加场景");
		image_scene = (Button) findViewById(R.id.btn_scene);
		image_scene.setOnClickListener(this);
		edit_scene_name = (EditText) findViewById(R.id.edit_scene_name);
		btn_select_all = (Button) findViewById(R.id.btn_select_all);
		btn_add = (Button) findViewById(R.id.btn_add);
		// text_error = (TextView) findViewById(R.id.text_error);
		btn_add.setOnClickListener(this);
		btn_select_all.setOnClickListener(this);
		list_command = (ListView) findViewById(R.id.list_command);
		mList = new ArrayList<String>();
		for (int i = 0; i < Configer.strList.length; i++) {
			mList.add(Configer.strList[i]);

		}

		controlAdapter = new ShowControlAdapter(this, mList, IsSelectAll);
		list_command.setAdapter(controlAdapter);
		Utility.setListViewHeightBasedOnChildren(list_command);
		edit_scene_name.addTextChangedListener(textW);
		edit_scene_name.setHintTextColor(0xFFFFFFFF);

	}

	SelectAll_Listener IsSelectAll = new SelectAll_Listener() {

		@Override
		public void IsSelectAll(boolean isSelectAll) {
			if (isSelectAll) {
				btn_select_all.setText("取消全选");
			} else {

				btn_select_all.setText("全选");
			}
		}
	};
	TextWatcher textW = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Log.d(TAG, "onTextChanged===>" + s.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// Log.d(TAG, "beforeTextChanged===>" + s.toString());
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Log.d(TAG, "afterTextChanged===" + s.toString());
			if (s != null && !"".equals(s.toString())) {
				// text_error.setVisibility(View.INVISIBLE);
			}
		}
	};
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	//
	// String name = data.getStringExtra("name");
	// Log.d(TAG, "选择的遥控为====>" + name);
	// mList.add(name);
	// SetAdapter(mList);
	// }

	// /**
	// * 得到点击得到的数据，然后填充数据
	// * */
	// public void SetAdapter(ArrayList<String> mList) {
	//
	// if (null == addSceneAdapter) {
	// addSceneAdapter = new AddSceneAdapter(this, mList);
	// } else {
	// addSceneAdapter.ReSetList(mList);
	// }
	// list_command.setAdapter(addSceneAdapter);
	// Utility.setListViewHeightBasedOnChildren(list_command);
	// }

	String sceneName = "快捷场景";
	AllSceneDB sceneDB = null;
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
				// 清空所有操作
				edit_scene_name.setText("");
				edit_scene_name.setHint("设置场景");
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, false, true);
				}
				finish();

			}
		}

		@Override
		public void DotRightFinish(boolean isFinish) {
			// TODO Auto-generated method stub

		}

	};
	StringBuffer sb = null;

	// 将已经完成的场景添加到数据库
	public boolean InsertSceneDB(String sceneName, ArrayList<String> mList,
			String mphotoPath) {
		sb = new StringBuffer();
		if (null == sceneDB) {
			sceneDB = new AllSceneDB(AddSceneActivity.this);

		}
		String name = sceneName;
		for (int i = 0; i < mList.size(); i++) {
			sb.append(mList.get(i).toString() + ",");
		}
		// Toast.makeText(AddSceneActivity.this,
		// "完成添加==" + name + "==遥控为==" + sb.toString(), Toast.LENGTH_SHORT)
		// .show();
		sceneDB.insert(name, sb.toString(), mphotoPath);
		return true;
	}

	private boolean is_select_all = false;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_select_all:
			if (is_select_all) {
				btn_select_all.setText("全选");
				is_select_all = false;
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, false, true);
				}

			} else {
				btn_select_all.setText("取消全选");
				is_select_all = true;
				for (int i = 0; i < mList.size(); i++) {
					controlAdapter.ChooseState(i, true, true);
				}
			}
			break;
		case R.id.btn_add:
			sceneName = edit_scene_name.getText().toString();
			// 判断当前的是否有选中
			if (controlAdapter.Add_Check()) {
				if (null == sceneName || sceneName.equals("")) {
					// text_error.setVisibility(View.VISIBLE);
					sceneName = GetSceneName();
				}
				ShowToast("确定添加这些操作数据", 1);
			} else {
				ShowToast("请先选择要添加的数据", 0);
			}

			break;
		case R.id.btn_scene:
			ToShowDialog();
			break;
		default:
			break;
		}

	}

	/**
	 * 用时间戳生成默认场景的名字
	 * */

	public String GetSceneName() {

		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'场景'_yyyyMMdd_HHmmss");
		// "'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date);
	}

	/**
	 * 显示提醒添加
	 * 
	 * @see{这里的is_go如果为0，代表没有数据选中，所以点击确定无操作，否则进行添加
	 * 
	 * */
	public void ShowToast(String Title, final int is_go) {
		final AlertDialog dialog = new AlertDialog.Builder(
				AddSceneActivity.this).create();
		dialog.show();
		Window window = dialog.getWindow();
		// 设置布局
		window.setContentView(R.layout.show_toast);
		// 设置宽高
		window.setLayout(LayoutParams.FILL_PARENT, 400);
		window.setGravity(Gravity.CENTER);
		// 设置弹出的动画效果
		window.setWindowAnimations(R.style.AnimBottom);
		TextView text_title = (TextView) window.findViewById(R.id.text_title);
		text_title.setText(Title);
		Button btn_cancel = (Button) window.findViewById(R.id.btn_cancel);
		Button btn_sure = (Button) window.findViewById(R.id.btn_sure);
		if (is_go == 0) {
			btn_cancel.setVisibility(View.GONE);
		}
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		btn_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (is_go == 0) {

				} else {

					// 获取到数据，然后添加到数据库，且跳转到场景查看的页面==SceneActivity.class
					AddList = controlAdapter.CheckSelected();

					if (InsertSceneDB(sceneName, AddList, mPhotoPath)) {

						finish();
					}

				}
				dialog.cancel();
			}
		});
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
		final AlertDialog dialog = new AlertDialog.Builder(
				AddSceneActivity.this).create();
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
			// mPhotoPath = Configer.sd_Path + getPhotoFileName();
			// mPhotoFile = new File(mPhotoPath);
			// if (!mPhotoFile.exists()) {
			// try {
			// mPhotoFile.createNewFile();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }

			Uri imageUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "seechalink.jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, CAMERA_RESULT);

		} else {
			Toast.makeText(AddSceneActivity.this, "未找到存储卡，无法存储照片！",
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
				// Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath,
				// bitmapOptions);
				Bitmap bitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory() + "/seechalink.jpg");
				bitmap = Configer.zoomBitmap(bitmap, 650, 300);
				// Bitmap output = Configer.getRoundedCornerBitmap(
				// SceneActivity.this, bitmap, R.drawable.btn_tv_press);
				// if (bitmap != null && !bitmap.isRecycled()) {
				// bitmap.recycle();
				// }
				// image_scene.setImageBitmap(bitmap);
				Drawable db = new BitmapDrawable(bitmap);
				image_scene.setBackground(db);
				SaveBitmap(bitmap);

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
			SaveBitmap(photo);

		}
	}

	public void SaveBitmap(Bitmap b) {
		String timeName = getPhotoFileName();
		mPhotoPath = Configer.sd_Path + timeName;
		savePhotoToSDCard(Configer.sd_Path, timeName, b);
	}

	/** Save image to the SD card **/
	public static void savePhotoToSDCard(String path, String photoName,
			Bitmap photoBitmap) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File photoFile = new File(path, photoName); // 在指定路径下创建文件
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
