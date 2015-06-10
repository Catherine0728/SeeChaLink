package com.home.service;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import org.eclipse.paho.android.service.Constants;
import org.eclipse.paho.android.service.SharePreferenceUtil;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.home.constants.Configer;
import com.home.mainactivity.ConfigActivity;
import com.home.mainactivity.MainActivity;
import com.home.mainactivity.R;
import com.home.util.Connection;
import com.home.util.Connections;
import com.home.util.MQTTClientUtil;
import com.home.util.MqttCallbackHandler;

/**
 * 1： 开启后台服务 2：然后绑定服务 3：然后打开连接 4：发送订阅
 * 
 * */
public class BackgroundService extends Service {

	private MQTTClientUtil mcu;
	private String clientHandle;// 这是拼接的连接服务的地址
	private SharePreferenceUtil preferens;
	private ChangeListener changeListener = new ChangeListener();
	protected static final String TAG = "hef";

	// public static String TAG = "BackgroundService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		bindService();
		preferens = new SharePreferenceUtil(getBaseContext());
		mcu = MQTTClientUtil.getInstance(getBaseContext());
		clientHandle = "tcp://" + Configer.MQTT_SEVER + ":"
				+ Configer.MQTT_PORT + "" + preferens.getDeviceId();
		Log.d(TAG, "clientHandle===>" + clientHandle);
		if (null != Connections.getInstance(getBaseContext()).getConnection(
				clientHandle)) {
			// stand for that there is a device is exsit
			Log.d(TAG,
					"connection is===>"
							+ Connections.getInstance(getBaseContext())
									.getConnection(clientHandle));
			if (!Connections.getInstance(getBaseContext())
					.getConnection(clientHandle).isConnected()) {
				Log.d(TAG, "当前服务未连接");

				mcu.reconnect();
				mcu.setSubscribe(false);
			}
		} else {
			// or to connect the broker server
			mcu.connectAction();
		}
	}

	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// Log.d(TAG, "onStartCommand");
	// // Recover connections.
	// Map<String, Connection> connections = Connections.getInstance(this)
	// .getConnections();
	//
	// // Register receivers again
	// for (Connection connection : connections.values()) {
	// connection.getClient().registerResources(this);
	// connection.getClient().setCallback(
	// new MqttCallbackHandler(this, connection.getClient()
	// .getServerURI()
	// + connection.getClient().getClientId()));
	// }
	// // return super.onStartCommand(intent, flags, startId);
	// return super.onStartCommand(intent, Service.START_REDELIVER_INTENT,
	// startId);
	// }

	/**
	 * 持久化service
	 */
	private void bindService() {
		Log.d(TAG, "bindService");
		Intent i = new Intent(this, MainActivity.class);
		// 注意Intent的flag设置：FLAG_ACTIVITY_CLEAR_TOP:
		// 如果activity已在当前任务中运行，在它前端的activity都会被关闭，它就成了最前端的activity。FLAG_ACTIVITY_SINGLE_TOP:
		// 如果activity已经在最前端运行，则不需要再加载。设置这两个flag，就是让一个且唯一的一个activity（服务界面）运行在最前端。
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		Notification myNotify = new Notification.Builder(this)
				.setSmallIcon(R.drawable.mqtt)
				.setTicker("mqtt backgroud service")
				.setContentTitle("Background Service For Start MQTT Service")
				.setContentText("Ummmm, clear").setContentIntent(pi)
				.getNotification();

		// 设置notification的flag，表明在点击通知后，通知并不会消失，也在最右图上仍在通知栏显示图标。这是确保在activity中退出后，状态栏仍有图标可提下拉、点击，再次进入activity。

		// DEFAULT_ALL 使用所有默认值，比如声音，震动，闪屏等等
		// DEFAULT_LIGHTS 使用默认闪光提示
		// DEFAULT_SOUNDS 使用默认提示声音
		// DEFAULT_VIBRATE 使用默认手机震动

		// 设置flag位
		// FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
		// FLAG_NO_CLEAR 该通知能被状态栏的清除按钮给清除掉
		// FLAG_ONGOING_EVENT 通知放置在正在运行
		// FLAG_INSISTENT 是否一直进行，比如音乐一直播放，知道用户响应

		// contentIntent 设置PendingIntent对象，点击时发送该Intent
		// defaults 添加默认效果
		// flags 设置flag位，例如FLAG_NO_CLEAR等
		// icon 设置图标
		// sound 设置声音
		// tickerText 显示在状态栏中的文字
		// when 发送此通知的时间戳
		// 步骤 2：startForeground( int,
		// Notification)将服务设置为foreground状态，使系统知道该服务是用户关注，低内存情况下不会killed，并提供通知向用户表明处于foreground状态。
		// NotificationManager常用方法介绍：
		// public void cancelAll() 移除所有通知(只是针对当前Context下的Notification)
		// public void cancel(int id) 移除标记为id的通知 (只是针对当前Context下的所有Notification)
		// public void notify(String tag ,int id, Notification notification)
		// 将通知加入状态栏，标签为tag，标记为id
		// public void notify(int id, Notification notification) 将通知加入状态栏，标记为id
		// myNotify.flags |= Notification.FLAG_AUTO_CANCEL;
		myNotify.flags |= Notification.FLAG_NO_CLEAR;
		startForeground(12345, myNotify);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		Map<String, Connection> connections = Connections.getInstance(this)
				.getConnections();

		for (Connection connection : connections.values()) {
			connection.registerChangeListener(changeListener);
			connection.getClient().unregisterResources();
		}

		// MQTTClientUtil mcu = MQTTClientUtil.getInstance(getBaseContext());
		// mcu.disconnect();
		stopForeground(true);
		// Notify.toast(getBaseContext(), "失去连接", Toast.LENGTH_LONG);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind");
		// TODO Auto-generated method stub
		return null;
	}

	private class ChangeListener implements PropertyChangeListener {

		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(PropertyChangeEvent event) {

			if (!event.getPropertyName().equals(
					Constants.ConnectionStatusProperty)) {
				return;
			}

		}

	}

}
