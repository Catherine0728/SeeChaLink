package com.home.constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
}
