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

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.loopj.android.http.AsyncHttpClient;
import com.tarena.tlbs.biz.implAsmack.SendNullPackage;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.Const;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

/**
 * TApplication ��Ӧ����һ��Ӧ��
 * 
 * @author gsd1403
 * 
 */
public class TApplication extends Application {

	public static TApplication instance;
	/**
	 * ��һ��socket����
	 */
	public static XMPPConnection xmppConnection;
	public static String host, serviceName;
	public static int port;
	public static long appStartTime;
	public static ArrayList<Activity> listActivity = new ArrayList();
	public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	/**
	 * Object��������Ӻ��ѵĽ��Presence Ҳ�����Ǳ�Ķ���
	 */
	public static ArrayList<Object> listMessage = new ArrayList<Object>();
	public static String currentUser = "";
	
	/**
	 * ��ͼ����
	 */
	public static BMapManager bMapManager;

	/**
	 * ʵ����ȫ�˳�
	 */
	public void exit() {
		// �ص����е�activity
		for (Activity activity : listActivity) {
			activity.finish();
		}
		// �Ͽ�����
		xmppConnection.disconnect();
		
		SendNullPackage.isRun=false;

		// �ص����̣������������android�У�ÿһ��Ӧ����һ�������
		System.exit(0);
	}

	// ��manifest.xml��ע��Tapplication,ϵͳ��Applicationָ��TApplication
	/**
	 * ��Ӧ�õ�һ������ʱִ�� Tapplication�д����Ķ���һ����ȫ�ֵġ�������ֻ����һ�� Tapplication�еķ�������Щִֻ��һ�εķ���
	 * �������е�activity,������Ӧ��,onCreate��ִ�С�
	 * 
	 * �����ǰ����system.exit(0),������Ӧ��,onCreateִ�С�
	 */
	@Override
	public void onCreate() {
		Log.i("appOncrate", "run");
		instance = this;
		appStartTime = System.currentTimeMillis();
		// �������������Ŀ����������ϴ����������http://ip:9090,
		// ����� ������̨������ҳ��˵������ͨ��
		// 9090:���������ص��ǹ�����ҳ,��html
		// 5222:���������ص����������ݣ���xml
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
					Log.i("baiduMap", "Ȩ�޳���");
				}
				
			}
			
			//���������ˣ��ٶȵ�ͼ�������ִ��
			@Override
			public void onGetNetworkState(int errorId) {
				// TODO Auto-generated method stub
				if (errorId==MKEvent.ERROR_NETWORK_CONNECT)
				{
					Log.i("baiduMap", "��������");
				}
				
			}
		});
	}

	public void connectChatServer() {
		// �����д������run,��loginBiz����ָ��
		// ���÷�������Ϣ
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				host, port, serviceName);
		
		//���ò�����
		configuration.setSecurityMode
		(SecurityMode.disabled);
		// ����openfire
		xmppConnection = new XMPPConnection(configuration);

		registerInterceptorListener();

		new Thread("connect thread") {
			public void run() {
				try {
					// this.sleep(20000);

					xmppConnection.connect();
					long time = System.currentTimeMillis();
					Log.i("��¼", "����time=" + (time - appStartTime));
					Log.i("connectChatServer",
							"���ӽ��=" + xmppConnection.isConnected());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	/**
	 * ��res/xml/config.xml
	 */
	private void readConfig() {
		try {
			Resources resources = this.getResources();
			// ���ص���һ���ӿڵ�ʵ���࣬���ʵ������XmlPullParser�����ࡣ
			XmlResourceParser parser = resources.getXml(R.xml.config);
			// getEventType
			int eventType = parser.getEventType();

			// while
			while (eventType != parser.END_DOCUMENT) {
				if (eventType == parser.START_TAG) {
					// ��������ʼ��ǩ<config><host>
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
		// 2.4 ��asmack�������еĽӿ�ָ��ʵ����
		AllInterceptor allInterceptor = new AllInterceptor();
		xmppConnection.addPacketInterceptor(allInterceptor, null);

		AllListener allListener = new AllListener();
		xmppConnection.addPacketListener(allListener, null);
	}

	// 2.3 дʵ����
	class AllInterceptor implements PacketInterceptor {

		/**
		 * ÿ�θ�����������Ϣʱ����
		 */
		@Override
		public void interceptPacket(Packet packet) {
			// packet ���������Ƿ���ȥ��xml��Ϣ
			Log.i("AllInterceptor",
					packet.toString() + " : xml=" + packet.toXML());
		}

	}

	class AllListener implements PacketListener {

		/**
		 * ������������Ϣ��asmack��ܻ��Զ�����processPacket ���ݷ�����������Ϣ������д�������һЩ����
		 */
		@Override
		public void processPacket(Packet packet) {
			Log.i("AllListener", packet.toString() + " : xml=" + packet.toXML());
			// �жϺ����Ƿ�ͬ��
			String packetString = packet.toString();
			if ("unsubscribe".equals(packetString)) {
				// ���Ѳ�ͬ��
				// xml��<presence>
				Presence presence = (Presence) packet;
				listMessage.add(presence);
				sendBroadcast(new Intent(Const.ACTION_UPDATE_MSG));

				String friendUser = presence.getFrom();
				Log.i("AllListener", friendUser + "��ͬ��");

			}
			if ("subscribe".equals(packetString)) {
				// ����ͬ��
				// xml��<presence>
				Presence presence = (Presence) packet;
				listMessage.add(presence);
				sendBroadcast(new Intent(Const.ACTION_UPDATE_MSG));

				String friendUser = presence.getFrom();
				Log.i("AllListener", friendUser + "ͬ��");

			}

			// �жϷ������������ǲ���������Ϣ
			if (packet instanceof Message) {
				Message message = (Message) packet;
				// �ж��ǲ���˽��
				Type type = message.getType();
				if (type == Type.chat) {
					// ˽��
					// �����ݷŵ�ʵ������
					//pc3@tarena.com/tarena.com
					String friendUser = message.getFrom();
					friendUser=friendUser.substring(0, friendUser.lastIndexOf("/"));
					PrivateChatEntity.addMessage(friendUser, message);
					// ���㲥
					Intent intent = new Intent(
							Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);

				}
			}

		}

	}
}
