package com.home.mainactivity;

import java.io.File;

import com.home.constants.Configer;
import com.home.db.AllCommandDB;
import com.home.util.Get_Img_Tools;
import com.home.view.CommonTitleView;

import android.R.id;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 点击自定义里面的添加以及点击已经添加的遥控进行编辑
 * 
 * @author Catherine
 * 
 * */
public class To_Define_Activity extends Activity {
	public static String TAG = "To_Define_Activity";
	CommonTitleView commtitleView = null;
	ImageView image_control = null;
	EditText edit_Name, edit_Name_info;
	Button btn_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_define);

	}

	@Override
	protected void onResume() {
		super.onResume();
		initDB();
		initView();
	};

	AllCommandDB commandDB = null;

	public void initDB() {
		name = getIntent().getStringExtra("name");
		Log.d(TAG, "name is-===>" + name);
		FromWhere = getIntent().getStringExtra("fromwhere");
		if (null == commandDB) {
			commandDB = new AllCommandDB(To_Define_Activity.this);

		}
		if (FromWhere.equals("编辑")) {

			Cursor cursor = commandDB.selectName(name);
			if (cursor.moveToFirst()) {
				nameInfo = cursor.getString(cursor
						.getColumnIndex(commandDB.c_Command_Info));
			}

		} else {

		}

	}

	String editName = "";
	public static final int REQUESTQUDE = 1;
	String FromWhere = "";
	String name = "自定义";
	String nameInfo = "遥控描述";

	public void initView() {
		Configer.PAGER = 9;
		commtitleView = (CommonTitleView) findViewById(R.id.toplayout);
		commtitleView.initData(To_Define_Activity.this, null, "添加遥控");

		edit_Name = (EditText) findViewById(R.id.edit_name);
		edit_Name_info = (EditText) findViewById(R.id.edit_name_info);
		edit_Name.setText(name);
		edit_Name_info.setText(nameInfo);
		image_control = (ImageView) findViewById(R.id.image_control);
		image_control.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToShowDialog();
			}
		});
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = edit_Name.getText().toString();
				String nameInfo = edit_Name_info.getText().toString();
				if (null == name) {
					name = "自定义";
				} else {
				}
				Intent intent = new Intent();
				intent.putExtra("name", name);
				intent.putExtra("nameInfo", nameInfo);
				setResult(REQUESTQUDE, intent);
				CheckDB(name, nameInfo);
				finish();

			}
		});

	}

	public void CheckDB(String newname, String nameInfo) {
		Log.d(TAG, "CheckDB");
		if (null == commandDB) {
			commandDB = new AllCommandDB(To_Define_Activity.this);

		}
		if (FromWhere.equals("编辑")) {
			// 应该是update
			commandDB.update(name, newname, nameInfo);
		} else {

			// a当fromwhere为添加的时候应该自己去insert
			commandDB.insert(newname, nameInfo);
		}
	}

	/**
	 * 显示选择对话框
	 * 
	 * @see{用于提供相片
	 */
	/* 头像名称 */
	static final String IMAGE_FILE_NAME = "faceImage.jpg";

	/* 请求码 */
	static final int IMAGE_REQUEST_CODE = 0;
	static final int CAMERA_REQUEST_CODE = 1;
	static final int RESULT_REQUEST_CODE = 2;

	// 显示对话框
	private void ToShowDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(
				To_Define_Activity.this).create();
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

	public void ToCamera() {

		Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// 判断存储卡是否可以用，可用进行存储
		if (Get_Img_Tools.hasSdcard()) {

			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
					.fromFile(new File(Environment
							.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
		}

		startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 结果码不等于取消时候
		if (resultCode != RESULT_CANCELED) {

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
					Toast.makeText(To_Define_Activity.this, "未找到存储卡，无法存储照片！",
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
			image_control.setImageBitmap(output);
		}
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
