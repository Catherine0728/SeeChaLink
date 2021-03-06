package com.home.constants;

import org.json.JSONException;
import org.json.JSONObject;

import com.home.mainactivity.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author Catherine
 * 
 *         建立一个公共类
 * */
public class Configer {

	// 服务器地址
	// public static final String baseUrl = "http://apps.cdltv.com/";
	//
	// public static final String base1Url = "http://apidev.itvyun.com/";
	//
	// public static final String base2Url = "http://test.cdltv.com/";
	//
	// public static final String VIBRATION = "vibration";// 震动
	// public static final String GESTUREPASS = "gesturepass";// 手势密码
	// public static final String LOCK_KEY = "lock_key";
	// public static final String UPDATE_REMOTE = "update_remote";
	//
	// public static final String PUSHNOTIFICATION = "pushnotification";// 推送通知
	//
	// public static final String TYPE_TV = "电视";
	// public static final String TYPE_AIR = "空调";
	// public static final String TYPE_SOUND = "音响";
	//
	// public static String imagePath = "/sdcard/hyc/";
	//
	// public static String isTempLock = "0";
	//
	// public static String isPrimaryLock = "0";
	//
	// public static boolean isRefresh = false;
	//
	// public static boolean isupDown = false;
	//
	// public static int sceneId = 0;
	//
	// public static Scene scene;
	//
	// public static String info;
	//
	// public static String help;
	//
	// public static String message;
	//
	// public static String agreement;
	// public static String privacy;
	//
	// public static String userIcon = "";
	//
	// /**
	// * 定义声音存储变量
	// */
	// public static final String BACK = "back";
	// public static final String BACK_TO_TOP = "back_to_top";
	// public static final String COMFIRE = "comfire";
	// public static final String MOVE_DOWN = "move_bottom";
	// public static final String MOVE_LEFT = "move_left";
	// public static final String MOVE_RIGHT = "move_right";
	// public static final String NET_CONNECTED = "net_connected";
	// public static final String NET_FOUND = "net_found";
	// public static final String TOP_FLOAT_DISABLED = "top_float_disabled";
	// public static final String TOP_FLOAT = "top_float";
	// public static final String PAGE_CHANGE = "top_page_move";
	// public static final String LOCK = "lock";
	// public static final String EFFECT_TICK = "effect_tick";

	/**
	 * @param context
	 * @param id
	 * @param viewGroup
	 * @return 返回 从配置文件中实例化的view对象
	 */
	public static View getView(Context context, int id, ViewGroup viewGroup) {
		return LayoutInflater.from(context).inflate(id, viewGroup);
	}

	/**
	 * 定义一个变量来表示当前所在的页面
	 * 
	 * @see{如若pager为1的时候，代表显示为可以有左滑动右滑动，且为MainActivity的主页面 
	 *                                                     ，如若pager为0，那么这里应该是不显示任何图标
	 *                                                     ，仅仅一个导航栏，
	 *                                                     如若pager为-1的话
	 *                                                     ，那么这里应该显示的是返回的按键
	 *                                                     ，且是在需要点击的时候返回到首页面的
	 *                                                     界面，如若这里的pager为其他的话，
	 *                                                     代表这里左按钮显示的是返回的按钮，
	 *                                                     且点击的时候需要finish当前页面即可}
	 * 
	 * */

	public static int PAGER = 1;

	public static final String topicName = "smarthome/rf315-x";
	/**
	 * 订阅当前服务器的地址
	 * 
	 * */
	public static String MQTT_SEVER = "124.205.17.106";
	/**
	 * MQTT 服务器所在的端口号
	 */
	public static int MQTT_PORT = 1883;

	// 填写从短信SDK应用后台注册得到的APPKEY
	public static String APPKEY = "7e08c9b5604e";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	public static String APPSECRET = "cf1cba324fa19aa8c77172a79b1a24df";

	// 短信注册，随机产生头像
	public static final String[] AVATARS = {
			"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
			"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
			"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
			"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
			"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
			"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
			"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
			"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
			"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
			"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
			"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
	/**
	 * 定义一个变量来表示是否登录
	 * 
	 * @see 0默认为未登录
	 * */
	public static int ISLOGIN = 0;
	/**
	 * 定义一个开关变量来判断当前是否连接
	 * 
	 * @see {默认值为:false}
	 * 
	 * */
	public static boolean ISCONNECT = false;
	/**
	 * 定义一个变量来判断是否解除广播
	 * */
	public static boolean ISUNREGISTER = false;

	/**
	 * 定义一个常量
	 * */
	public static String FUNDGESTURE = "GESTURE";
	/**
	 * 
	 * 定义一个常量来存储当前设备，做临时用，如果今后设备增加，应该用数据库进行存储
	 * */
	public static String ISBOKER = "HOSTNAME";

	/**
	 * 网络是否可用
	 * 
	 * @author Catherine
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] info = mgr.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 先定义这几个暂时拥有的遥控，等数据做活了，然后就利用数据库存储
	 * */
	public static String[] strList = { "空调", "音响", "机顶盒", "电视", "TC", "插座",
			"手势", "自定义" };

	/**
	 * 圆形头像
	 * 
	 * @param bitmap
	 * @param ratio
	 *            按照截取比例来获取圆形图片
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Context mContext,
			Bitmap bitmap, int draw) {
		if (bitmap == null) {
			bitmap = BitmapFactory
					.decodeResource(mContext.getResources(), draw);
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

	/**
	 * 定义一个常量用来存储当前获取图片
	 * 
	 * 
	 * 【出现部分手机文件创建不了，导致无法照相成功】
	 * 
	 * 
	 * 还是创建不了。。。。
	 * */
	public static String sd_Path = Environment.getExternalStorageDirectory()
			+ "/seechalink/image_cut/";// seechalink/image_cut/

	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/** 根据自己指定的大小来缩放Bitmap图片 **/
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	/**
	 * 根据差传过来的数据返回包装的数据
	 * 
	 * */
	public static String GetRf3o4MWirelessCtrlClickJson(int id) {
		String one_TAG = "GetRf3o4MWirelessCtrlClickJson";
		JSONObject json = new JSONObject();
		try {
			json.put("api", "v1.0.0");
			json.put("service", "smarthome-mqtt");
			json.put("delay", 0.3);
			json.put("id", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(one_TAG, "转化后的json 是===》" + json.toString());
		return json.toString();
	}

}
