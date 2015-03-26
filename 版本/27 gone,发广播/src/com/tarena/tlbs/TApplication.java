package com.tarena.tlbs;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.loopj.android.http.AsyncHttpClient;
import com.tarena.tlbs.util.Const;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * TApplication 代应的是一个应用
 * 
 * @author gsd1403
 * 
 */
public class TApplication extends Application {

	public static TApplication instance;
	/**
	 * 是一个socket连接
	 */
	public static XMPPConnection xmppConnection;
	public static String host, serviceName;
	public static int port;
	public static long appStartTime;
	public static ArrayList<Activity> listActivity = new ArrayList();
	public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	/**
	 * Object可以是添加好友的结果Presence 也可以是别的对象
	 */
	public static ArrayList<Object> listMessage = new ArrayList<Object>();

	/**
	 * 实现完全退出
	 */
	public void exit() {
		// 关掉所有的activity
		for (Activity activity : listActivity) {
			activity.finish();
		}
		// 断开连接
		xmppConnection.disconnect();

		// 关掉进程（虚拟机），在android中，每一个应用启一个虚拟机
		System.exit(0);
	}

	// 在manifest.xml中注册Tapplication,系统的Application指向TApplication
	/**
	 * 在应用第一次启动时执行 Tapplication中创建的对象一般是全局的。并且是只能有一个 Tapplication中的方法是那些只执行一次的方法
	 * 关了所有的activity,再启动应用,onCreate不执行。
	 * 
	 * 如果以前调了system.exit(0),再启动应用,onCreate执行。
	 */
	@Override
	public void onCreate() {
		Log.i("appOncrate", "run");
		instance = this;
		appStartTime = System.currentTimeMillis();
		// 在真机上运行项目，先在真机上打开浏览器访问http://ip:9090,
		// 如果看 不到后台管理网页。说明网不通。
		// 9090:服务器返回的是管理网页,是html
		// 5222:服务器返回的是聊天内容，是xml
		super.onCreate();
		readConfig();
		connectChatServer();
	}

	public void connectChatServer() {
		// 这两行代码放在run,在loginBiz出空指针
		// 设置服务器信息
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				host, port, serviceName);
		// 连接openfire
		xmppConnection = new XMPPConnection(configuration);

		registerInterceptorListener();

		new Thread("connect thread") {
			public void run() {
				try {
					// this.sleep(20000);

					xmppConnection.connect();
					long time = System.currentTimeMillis();
					Log.i("登录", "连接time=" + (time - appStartTime));
					Log.i("connectChatServer",
							"连接结果=" + xmppConnection.isConnected());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	/**
	 * 读res/xml/config.xml
	 */
	private void readConfig() {
		try {
			Resources resources = this.getResources();
			// 返回的是一个接口的实现类，这个实现类是XmlPullParser的子类。
			XmlResourceParser parser = resources.getXml(R.xml.config);
			// getEventType
			int eventType = parser.getEventType();

			// while
			while (eventType != parser.END_DOCUMENT) {
				if (eventType == parser.START_TAG) {
					// 遇到了起始标签<config><host>
					String tagName = parser.getName();
					if ("host".equals(tagName)) {
						host = parser.nextText();
					}
					if ("port".equals(tagName)) {
						String strPort = parser.nextText();
						port = Integer.parseInt(strPort);
					}
					if ("serviceName".equals(tagName)) {
						serviceName = parser.nextText();
					}

				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	};

	public void registerInterceptorListener() {
		// 2.4 让asmack聊天框架中的接口指向实现类
		AllInterceptor allInterceptor = new AllInterceptor();
		xmppConnection.addPacketInterceptor(allInterceptor, null);

		AllListener allListener = new AllListener();
		xmppConnection.addPacketListener(allListener, null);
	}

	// 2.3 写实现类
	class AllInterceptor implements PacketInterceptor {

		/**
		 * 每次给服务器发信息时调用
		 */
		@Override
		public void interceptPacket(Packet packet) {
			// packet 代表是我们发出去的xml信息
			Log.i("AllInterceptor",
					packet.toString() + " : xml=" + packet.toXML());
		}

	}

	class AllListener implements PacketListener {

		/**
		 * 服务器返回信息，asmack框架会自动调用processPacket 根据服务器返回信息，我们写程序会做一些处理
		 */
		@Override
		public void processPacket(Packet packet) {
			Log.i("AllListener", packet.toString() + " : xml=" + packet.toXML());
			// 判断好友是否同意
			String packetString = packet.toString();
			if ("unsubscribe".equals(packetString)) {
				// 好友不同意
				// xml是<presence>
				Presence presence = (Presence) packet;
				listMessage.add(presence);
				sendBroadcast(new Intent(Const.ACTION_UPDATE_MSG));

				String friendUser = presence.getFrom();
				Log.i("AllListener", friendUser + "不同意");

			}
			if ("subscribe".equals(packetString)) {
				// 好友同意
				// xml是<presence>
				Presence presence = (Presence) packet;
				listMessage.add(presence);
				sendBroadcast(new Intent(Const.ACTION_UPDATE_MSG));

				String friendUser = presence.getFrom();
				Log.i("AllListener", friendUser + "同意");

			}

		}

	}
}
