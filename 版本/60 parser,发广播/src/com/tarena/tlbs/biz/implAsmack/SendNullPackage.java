package com.tarena.tlbs.biz.implAsmack;

import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.TApplication;

/**
 * ֻ�з�����Ҫ���������ݸ��ͻ���
 * 
 */
public class SendNullPackage extends Thread {
	public static boolean isRun = true;
	private static SendNullPackage instance;

	private SendNullPackage() {
		this.start();
	}

	public synchronized static SendNullPackage newInstance() {
		if (instance == null) {
			instance = new SendNullPackage();
		}
		return instance;
	}

	@Override
	public void run() {
		while (isRun) {
			try {
				this.sleep(4 * 60 * 1000);
				Message message = new Message();
				TApplication.xmppConnection.sendPacket(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
