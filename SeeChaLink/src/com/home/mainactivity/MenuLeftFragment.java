package com.home.mainactivity;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;

import com.home.adapter.LeftListAdapter;
import com.home.constants.Configer;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 这是右边个人详情页面
 * 
 * @author Catherine
 * 
 * 
 * @see{在未登录的情况下，我们应该给一个默认的用户。值然后，在登录后，第一步：存进prefence，且设置当前的签名， 
 *                                                              当然，这里每次进来应该有个加载，loadpreference
 *                                                              。然后再通过设置头像的时候，。
 *                                                              将图片的路径分贝存进去，且赋值}
 * */
public class MenuLeftFragment extends Fragment {
	public static String TAG = "MenuLeftFragment";
	View v = null;
	ImageView Image_Head = null;
	ListView left_List = null;
	LeftListAdapter adapter = null;
	TextView text_moto;
	private boolean ready;

	/* 请求码 */
	static final int IMAGE_REQUEST_CODE = 0;
	static final int RESULT_REQUEST_CODE = 2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null == v) {
			v = LayoutInflater.from(getActivity()).inflate(
					R.layout.layout_left_menu, null);
		}
		initView();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	public String[] leftInfoArray = { "我的设备", "添加设备", "设置", "帮助" };

	public void initView() {
		Image_Head = (ImageView) v.findViewById(R.id.image_head);
		left_List = (ListView) v.findViewById(R.id.left_list);

		text_moto = (TextView) v.findViewById(R.id.text_moto);
		loadSharePrefrence();
		Image_Head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Configer.ISLOGIN == 0
						&& text_moto.getText().toString().equals(moteDefault)) {

					GetLogin();
				} else {
					ToShowDialog();
				}

			}
		});
		adapter = new LeftListAdapter(getActivity(), leftInfoArray);
		left_List.setAdapter(adapter);
		left_List.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();

				if (leftInfoArray[position].toString().equals("帮助")) {
					intent.setClass(getActivity(), HelpActivity.class);

				} else if (leftInfoArray[position].toString().equals("我的设备")) {
					intent.setClass(getActivity(), MyControl_Equipment.class);

				} else if (leftInfoArray[position].toString().equals("添加设备")) {
					intent.setClass(getActivity(), ConfigActivity.class);
				} else {
					// 跳转到设置的页面
					// startActivity(new
					// Intent(Settings.ACTION_WIRELESS_SETTINGS));
					intent.setClass(getActivity(), SettingActivity.class);
				}
				startActivity(intent);
			}
		});
		text_moto = (TextView) v.findViewById(R.id.text_moto);
	}

	// 显示对话框
	private void ToShowDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
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

	// 跳转到照相机
	private File mPhotoFile;
	private String mPhotoPath;
	public final static int CAMERA_RESULT = 8888;

	public void ToCamera() {

		// this is the second method to get the image
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if (Configer.hasSdcard()) {
			// File file = new File(Configer.sd_Path);
			// if (!file.exists()) {
			// if (file.mkdirs()) {
			// mPhotoPath = Configer.sd_Path + getPhotoFileName();
			// mPhotoFile = new File(mPhotoPath);
			// if (!mPhotoFile.exists()) {
			// Log.d(TAG, "此照片不存在...");
			// try {
			// mPhotoFile.createNewFile();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// }
			// intent.putExtra(MediaStore.EXTRA_OUTPUT,
			// Uri.fromFile(mPhotoFile));
			// startActivityForResult(intent, CAMERA_RESULT);
			// } else {
			// Toast.makeText(getActivity(), "文件创建不成功", Toast.LENGTH_SHORT)
			// .show();
			//
			// }

			// }
			mPhotoPath = Configer.sd_Path + getPhotoFileName();
			mPhotoFile = new File(mPhotoPath);
			if (!mPhotoFile.exists()) {
				Log.d(TAG, "此照片不存在...");
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
			Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
					.show();
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
		if (resultCode != getActivity().RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_RESULT:

				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath,
						bitmapOptions);

				bitmap = Configer.zoomBitmap(bitmap, 120, 120);
				Bitmap output = Configer.getRoundedCornerBitmap(getActivity(),
						bitmap, R.drawable.btn_tv_press);
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
				}

				Image_Head.setImageBitmap(output);
				SharedPreferences p = getActivity().getSharedPreferences(
						"username", Context.MODE_PRIVATE);
				String moto = p.getString("moto", moteDefault);
				text_moto.setText(moto);

				break;
			case RESULT_REQUEST_CODE:
				if (data != null) {
					getImageToView(data);
				}
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

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
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
			Bitmap output = Configer.getRoundedCornerBitmap(getActivity(),
					photo, R.drawable.user_icon);
			if (photo != null && !photo.isRecycled()) {
				photo.recycle();
			}
			Image_Head.setImageBitmap(output);
			SharedPreferences p = getActivity().getSharedPreferences(
					"username", Context.MODE_PRIVATE);
			String moto = p.getString("moto", moteDefault);
			text_moto.setText(moto);
			// Drawable drawable = new BitmapDrawable(photo);
			// Image_Head.setImageDrawable(drawable);
		}
	}

	/**
	 * 点击头像进行注册
	 * */
	public void GetLogin() {
		initSDK();
		// 打开注册页面
		RegisterPage registerPage = new RegisterPage();
		registerPage.setRegisterCallback(new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				// 解析注册结果
				if (result == SMSSDK.RESULT_COMPLETE) {
					@SuppressWarnings("unchecked")
					HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
					String country = (String) phoneMap.get("country");
					String phone = (String) phoneMap.get("phone");
					// 提交用户信息
					registerUser(country, phone);
				}
			}
		});
		registerPage.show(getActivity());
	}

	/**
	 * 第一步：初始化
	 * 
	 * */

	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(getActivity(), Configer.APPKEY, Configer.APPSECRET);
		// final Handler handler = new Handler(getActivity());
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				myHander.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;

		SMSSDK.getNewFriendsCount();

	}

	/**
	 * 第五步：提交用户信息
	 * 
	 * */
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		String nickName = "SmsSDK_User_" + uid;
		String avatar = Configer.AVATARS[id % 12];

		SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
		Configer.ISLOGIN = 1;
		/**
		 * 将得到的数据存储起来
		 * 
		 * */
		moteDefault = phone;
		setSharePrefrence(phone, R.drawable.user_icon);

	}

	String MOTO;
	int IAMGEHEAD;
	String moteDefault = "这家伙很懒，什么也没有留下....";

	private void loadSharePrefrence() {
		SharedPreferences p = getActivity().getSharedPreferences("username",
				Context.MODE_PRIVATE);
		MOTO = p.getString("moto", moteDefault);
		IAMGEHEAD = p.getInt("imagehand", R.drawable.user_icon);
		// fileUri = p.getString("fileuri",
		// Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
		text_moto.setText(MOTO);
		Bitmap bitmap = null;
		// if (IAMGEHEAD == 0) {
		// bitmap = BitmapFactory.decodeFile(fileUri);
		// } else {
		bitmap = BitmapFactory.decodeResource(getResources(), IAMGEHEAD);
		// }

		Bitmap output = Configer.getRoundedCornerBitmap(getActivity(), bitmap,
				R.drawable.user_icon);
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		Image_Head.setImageBitmap(output);
	}

	private void setSharePrefrence(String moto, int imagehead) {
		SharedPreferences p = getActivity().getSharedPreferences("username",
				Context.MODE_PRIVATE);
		Editor edit = p.edit();
		edit.putString("moto", moto);
		// edit.putString("fileuri", fileUri);
		edit.putInt("imagehand", imagehead);

		edit.commit();
		text_moto.setText(moto);
	}

	Handler myHander = new Handler() {
		public void handleMessage(Message msg) {

			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (result == SMSSDK.RESULT_COMPLETE) {
					Toast.makeText(getActivity(),
							R.string.smssdk_user_info_submited,
							Toast.LENGTH_SHORT).show();
				} else {
					((Throwable) data).printStackTrace();
				}
			} else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					Log.d(TAG, "SMSSDK.RESULT_COMPLETE");
					// refreshViewCount(data);
				} else {
					((Throwable) data).printStackTrace();
				}
			}
		};
	};

	@Override
	public void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}

}
