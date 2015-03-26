package com.tarena.tlbs;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.loopj.android.http.AsyncHttpClient;

import android.app.Activity;
import android.app.Application;
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
 * Object��������Ӻ��ѵĽ��Presence
 * Ҳ�����Ǳ�Ķ���
 */
	public static ArrayList<Object> listMessage=new ArrayList<Object>();
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
	}

	public void connectChatServer() {
		// �����д������run,��loginBiz����ָ��
		// ���÷�������Ϣ
		ConnectionConfiguration configuration = new ConnectionConfiguration(
				host, port, serviceName);
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
			//�жϺ����Ƿ�ͬ��
			String packetString=packet.toString();
			if ("unsubscribe".equals(packetString))
			{
				//���Ѳ�ͬ��
				//xml��<presence>
				Presence presence=(Presence) packet;
				listMessage.add(presence);
				
				String friendUser=presence.getFrom();
				Log.i("AllListener", friendUser+"��ͬ��");
				
			}
			if ("subscribed".equals(packetString))
			{
				//���Ѳ�ͬ��
				//xml��<presence>
				Presence presence=(Presence) packet;
				listMessage.add(presence);

				String friendUser=presence.getFrom();
				Log.i("AllListener", friendUser+"ͬ��");
				
			}

		}

	}
}
