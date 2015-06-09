package com.home.mainactivity;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import com.home.constants.Configer;
import com.home.util.Get_Img_Tools;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 这是右边个人详情页面
 * 
 * @author Catherine
 * */
public class MenuLeftFragment extends Fragment {
	public static String TAG = "MenuLeftFragment";
	View v = null;
	ImageView Image_Head = null;
	ListView left_List = null;
	ListAdapter adapter = null;
	TextView text_moto;
	private boolean ready;
	private String[] items = new String[] { "选择本地图片", "拍照" };

	/* 头像名称 */
	static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	static final int IMAGE_REQUEST_CODE = 0;
	static final int CAMERA_REQUEST_CODE = 1;
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

	public String[] leftInfoArray = { "我的设备", "设置", "帮助" };

	public void initView() {
		Image_Head = (ImageView) v.findViewById(R.id.image_head);
		left_List = (ListView) v.findViewById(R.id.left_list);
		Image_Head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Configer.ISLOGIN == 0) {
					GetLogin();
				} else {
					GetImage();
				}

			}
		});
		text_moto = (TextView) v.findViewById(R.id.text_moto);
		loadSharePrefrence();

		adapter = new ListAdapter(getActivity(), leftInfoArray);
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

	public void GetImage() {
		showDialog();
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(getActivity())
				.setTitle("设置头像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intentFromGallery = new Intent();
							intentFromGallery.setType("image/*"); // 设置文件类型
							intentFromGallery
									.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(intentFromGallery,
									IMAGE_REQUEST_CODE);
							break;
						case 1:

							Intent intentFromCapture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// 判断存储卡是否可以用，可用进行存储
							if (Get_Img_Tools.hasSdcard()) {

								intentFromCapture.putExtra(
										MediaStore.EXTRA_OUTPUT,
										Uri.fromFile(new File(Environment
												.getExternalStorageDirectory(),
												IMAGE_FILE_NAME)));
							}

							startActivityForResult(intentFromCapture,
									CAMERA_REQUEST_CODE);
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != getActivity().RESULT_CANCELED) {

			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				startPhotoZoom(data.getData());
				break;
			case CAMERA_REQUEST_CODE:
				if (Get_Img_Tools.hasSdcard()) {
					File tempFile = new File(
							Environment.getExternalStorageDirectory()
									+ IMAGE_FILE_NAME);
					startPhotoZoom(Uri.fromFile(tempFile));
				} else {
					Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！",
							Toast.LENGTH_LONG).show();
				}

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
		intent.putExtra("outputX", 320);
		intent.putExtra("outputY", 320);
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
			Bitmap output = getRoundedCornerBitmap(photo);
			Image_Head.setImageBitmap(output);
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
		setSharePrefrence(phone, R.drawable.image_head);

	}

	String MOTO, fileUri = Environment.getExternalStorageDirectory()
			+ IMAGE_FILE_NAME;
	int IAMGEHEAD;
	String moteDefault = "这家伙很懒，什么也没有留下....";

	private void loadSharePrefrence() {
		SharedPreferences p = getActivity().getSharedPreferences("username",
				Context.MODE_PRIVATE);
		MOTO = p.getString("moto", moteDefault);
		IAMGEHEAD = p.getInt("imagehand", R.drawable.image_head);
		// fileUri = p.getString("fileuri",
		// Environment.getExternalStorageDirectory() + IMAGE_FILE_NAME);
		text_moto.setText(MOTO);
		Bitmap bitmap = null;
		// if (IAMGEHEAD == 0) {
		// bitmap = BitmapFactory.decodeFile(fileUri);
		// } else {
		bitmap = BitmapFactory.decodeResource(getResources(), IAMGEHEAD);
		// }

		Bitmap output = getRoundedCornerBitmap(bitmap);
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

	/**
	 * 圆形头像
	 * 
	 * @param bitmap
	 * @param ratio
	 *            按照截取比例来获取圆形图片
	 * @return
	 */
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.image_head);
		}
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPX = bitmap.getWidth() / 2 < bitmap.getHeight() / 2 ? bitmap
				.getWidth() : bitmap.getHeight();
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return outBitmap;
	}
}

class ListAdapter extends BaseAdapter {
	String[] string_List;
	Context mContext = null;

	public ListAdapter(Context mContext, String[] left_String) {
		this.mContext = mContext;
		this.string_List = left_String;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return string_List.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return string_List[position].toString();
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
					R.layout.left_menu_item, null);
			new ViewHolder(convertView, position);
		}
		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.item_Name.setText(string_List[position].toString());
		return convertView;
	}

	class ViewHolder {
		TextView item_Name;

		public ViewHolder(View view, int pos) {
			item_Name = (TextView) view.findViewById(R.id.item_name);
			view.setTag(this);
		}
	}

}
