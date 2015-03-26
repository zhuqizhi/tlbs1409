package com.tarena.tlbs.biz.implAsmack;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.Tools;

/**
 * ����˽������
 * 
 * @author tarena
 * 
 */
public class PrivateChatBiz {
	public void sendMessage(final String friendUser, final String body) {
		new Thread() {
			public void run() {
				int status = -1;
				try {
					//70%�Ĵ����ڴ����쳣�����
					// �û����ϵ�¼�ɹ������˼���Сʱ����Ϣ
					// ����������
					// 1,û���� �����������ǶϿ����������������
					// 2,�����ʱ�䣬����������û�ע���ˡ�
					// �ж���û����
					if (TApplication.network_state.equals("1")) {
						// û���������߳��д��������ý���
						status = Const.STATUS_OPEN_NETWORK;
					} else {
						// ����
						// һ�������ŷ��������п���û���ţ�һ����ǰ��¼��
						// �ж���û�����ŷ�����
						if (TApplication.xmppConnection.isConnected() == false) {
							// ��ʼ�Զ�����
							TApplication.instance.connectChatServer();

							// ��10��
							int count = 1;
							while (count < 1000
									&& TApplication.xmppConnection
											.isConnected() == false) {
								count++;
								this.sleep(10);
							}
						}
						// �ж������Ƿ�ɹ�
						if (TApplication.xmppConnection.isConnected() == false) {
							status = Const.STATUS_CONNECT_FAILURE;
						} else {
							// ���ӷ������ɹ���
							// �жϿͻ����Ƿ��¼�ɹ�
							if (TApplication.xmppConnection.isAuthenticated() == false) {
								// ��¼ʧ�ܣ�1��ʱ����Զ����µ�¼ 2��ʱ�䳤��ʾ��¼����
								LoginBiz loginBiz = new LoginBiz();
								// ��һ�ε�¼ʱ�������û����ͼ��ܵ�����
								// ��ȡ
								UserEntity userEntity = new UserEntity("", "");
								loginBiz.login(userEntity);

								// ��
								int count = 0;
								while (count < 1000
										&& TApplication.xmppConnection
												.isAuthenticated() == false) {
									count++;
									this.sleep(10);
								}

								
							}
							// �����Զ���¼
							// �жϵ�¼�Ƿ�ɹ�
							if (TApplication.xmppConnection
									.isAuthenticated() == false) {
								// ��¼ʧ��
								status = Const.STATUS_LOGIN_FAILURE;
							} else {
								LoginSuccessSendMessage(friendUser, body);

							}

						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// ���㲥֪ͨactivit��ʾ
					Intent intent = new Intent(
							Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);
				}
			}

			private void LoginSuccessSendMessage(final String friendUser,
					final String body) {
				// ��¼�ɹ�
				// ����Message
				Message message = new Message();
				message.setTo(friendUser);
				message.setBody(body);
				message.setType(Type.chat);
				// from�ǵ�¼���û�
				// ȫ�ֱ���Tapplication.currentUser
				// �ڵ�¼�ɹ����Tapplication.currentUser��ֵ
				message.setFrom(TApplication.currentUser);

				// ���緢�ͼ��ܵ�����
				Message msgEncrypt = new Message();
				msgEncrypt.setTo(friendUser);
				byte[] data = body.getBytes();
				for (int i = 0; i < data.length; i++) {
					data[i] = Tools.encrypt(data[i]);
				}
				String encryptBody = new String(data);
				// ������
				msgEncrypt.setBody(encryptBody);
				msgEncrypt.setType(message.getType());
				msgEncrypt
						.setFrom(TApplication.currentUser);

				TApplication.xmppConnection
						.sendPacket(msgEncrypt);

				// ʵ�����зŲ����ܵ�
				PrivateChatEntity.addMessage(friendUser,
						message);
			}

			;
		}.start();
	}
}
