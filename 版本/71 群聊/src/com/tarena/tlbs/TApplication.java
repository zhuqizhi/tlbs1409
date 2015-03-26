package com.tarena.tlbs;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.tarena.tlbs.biz.implAsmack.SendNullPackage;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.model.TopicEntity;
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

	//release是发布的。
	public static boolean isRelease=false;
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
	public static String currentUser = "";
	
	/**
	 * 地图管理
	 */
	public static BMapManager bMapManager;
	//状态码  http 200  404 405 
	//0 有网 是wifi 01  还是gprs 02 
	//1：没网
	public static String network_state="";
	public static ArrayList<TopicEntity> listTopic=new ArrayList<TopicEntity>();
	public static String name;
	public static MultiUserChat groupChat;
	

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
		
		SendNullPackage.isRun=false;

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
		initMap();
	}

	private void initMap() {
		// TODO Auto-generated method stub
		bMapManager=new BMapManager(this);
		bMapManager.init(Const.BAIDU_ANDROID_KEY, new MKGeneralListener() {
			
			@Override
			public void onGetPermissionState(int errorId) {
				if (errorId==MKEvent.ERROR_PERMISSION_DENIED)
				{
					Log.i("baiduMap", "权限出错");
				}
				
			}
			
			//联网出错了，百度地图开发框架执行
			@Override
			public void onGetNetworkState(int errorId) {
				// TODO Auto-generated method stub
				if (errorId==MKEvent.ERROR_NETWORK_CONNECT)
				{
					Log.i("baiduMap", "联网出错");
				}
				
			}
		});
	}

	public void connectChatServer() {
		// 这两行代码放在run,在loginBiz出空指针
		// 设置服务器信息
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				host, port, serviceName);
		
		//设置不加密
		configuration.setSecurityMode
		(SecurityMode.disabled);
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
			if (packet instanceof RosterPacket)
			{
				//服务器推送过来的是好友分组信息
				//发广播
				Intent intent=new Intent(Const.ACTION_UPDATE_MY_FRIEND);
				sendBroadcast(intent);
			}
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

			// 判断服务器发过来是不是聊天信息
			if (packet instanceof Message) {
				Message message = (Message) packet;
				// 判断是不是私聊
				Type type = message.getType();
				if (type == Type.chat) {
					// 私聊
					// 把数据放到实体类中
					//pc3@tarena.com/tarena.com
					String friendUser = message.getFrom();
					friendUser=friendUser.substring(0, friendUser.lastIndexOf("/"));
					PrivateChatEntity.addMessage(friendUser, message);
					// 发广播
					Intent intent = new Intent(
							Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);

				}
			}

		}

	}
}
