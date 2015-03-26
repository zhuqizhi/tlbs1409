package com.tarena.tlbs.biz;

import android.content.Intent;
import android.util.Log;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;

public class LoginBiz {
	/**
	 * ������1: ui(TextView,ListView)�ǲ����̰߳�ȫ ����̰߳�ȫ���̲߳���ȫ �̰߳�ȫ:�����Ķ����ڶ��߳�ʱ�����ʲ������
	 * stringBuffer��Vector�ǰ�ȫ stringBuilder,arrayList�ǲ���ȫ
	 * ui(TextView,ListView)�����̰߳�ȫ ������� 1������Ϣ 2�����㲥 3��runOnUiThread()
	 * ������2:����Ϣ�ͷ��㲥������ ��ͬ�㣺���ܽ���ڹ����̲߳��ܷ���UI ���𣺹㲥�ܷ�������һ��Ӧ�ã�������ȷ���Ϣ��
	 * ��Ŀ�У����㲥�������߳�ֻ���𷢡� ����Ϣ�������̱߳���Ҫ����һ��handler �Ƽ��÷��㲥��
	 * 
	 * @param userEntity
	 */
	public void login(final UserEntity userEntity) {
		// �����������������߳�
		new Thread("login thread") {
			public void run() {
				int status = -1;
				try {
					
					// ��¼����Ҫ�����߳����Ϸ�����
					// �˳�while
					int count = 0;
					while (count < 10
							&& TApplication.xmppConnection.isConnected() == false) {
						sleep(1000);
						count++;
						Log.i("��¼", "��¼count=" + (count));
					}

					// �ж������Ƿ�ɹ�
					if (TApplication.xmppConnection.isConnected()) {
						long time = System.currentTimeMillis();
						Log.i("��¼", "��¼time="
								+ (time - TApplication.appStartTime));
						// this.sleep(20000);
						TApplication.xmppConnection.login(
								userEntity.getUsername(),
								userEntity.getPassword());
						// true:��Ϊ faluse:ʧ��
						Log.i("LoginBiz",
								"��¼���="
										+ TApplication.xmppConnection
												.isAuthenticated());
						if (TApplication.xmppConnection.isAuthenticated()) {
							status = Const.STATUS_SUCCESS;
						} else {
							status = Const.STATUS_LOGIN_FAILURE;
						}
					} else {
						status = Const.STATUS_CONNECT_FAILURE;
					}

				} catch (Exception e) {
					//���ֶ����쳣 1����ָ�룬2�������Ϸ����� 3�������
					
					String eInfo=e.toString();
					//��ͬ���쳣��eInfo�ǲ�һ����
					//�жϳ����Ǹ��쳣����ͬ�쳣��ͬ����
					if ("SASL authentication failed using mechanism DIGEST-MD5: ".equals(eInfo))
					{
						//�������
						//�Ͽ�����
						TApplication.xmppConnection.disconnect();
						//������
						TApplication.instance.connectChatServer();
						
					}
					if ("NullPointerException".equals(eInfo))
					{
						//��ָ�����
						
					}
					Log.i("LoginBiz", eInfo);
					
					status = Const.STATUS_LOGIN_FAILURE;
					e.printStackTrace();
				} finally {
					// �û������ύ�����뷢���㲥
					Intent intent = new Intent(Const.ACTION_LOGIN);
					intent.putExtra(Const.KEY_DATA, status);

					// android��activity,receiver,service,application����new
					// Լ��
					// ����new
					// TApplication tApplication=new TApplication();
					// tApplication.sendBroadcast(intent);
					TApplication.instance.sendBroadcast(intent);

				}
			};
		}.start();

	}
}
