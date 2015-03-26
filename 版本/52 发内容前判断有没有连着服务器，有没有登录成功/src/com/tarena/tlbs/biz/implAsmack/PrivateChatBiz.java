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
 * 发送私聊内容
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
					//70%的代码在处理异常情况，
					// 用户早上登录成功，过了几个小时发消息
					// 有两个问题
					// 1,没有网 ，有网，但是断开了与服务器的连接
					// 2,如果长时间，服务器会把用户注销了。
					// 判断有没有网
					if (TApplication.network_state.equals("1")) {
						// 没网，在主线程中打开网络设置界面
						status = Const.STATUS_OPEN_NETWORK;
					} else {
						// 有网
						// 一定会连着服务器？有可能没连着，一个月前登录，
						// 判断有没有连着服务器
						if (TApplication.xmppConnection.isConnected() == false) {
							// 开始自动重连
							TApplication.instance.connectChatServer();

							// 等10秒
							int count = 1;
							while (count < 1000
									&& TApplication.xmppConnection
											.isConnected() == false) {
								count++;
								this.sleep(10);
							}
						}
						// 判断重连是否成功
						if (TApplication.xmppConnection.isConnected() == false) {
							status = Const.STATUS_CONNECT_FAILURE;
						} else {
							// 连接服务器成功，
							// 判断客户端是否登录成功
							if (TApplication.xmppConnection.isAuthenticated() == false) {
								// 登录失败，1，时间短自动重新登录 2，时间长显示登录界面
								LoginBiz loginBiz = new LoginBiz();
								// 第一次登录时，保存用户名和加密的密码
								// 读取
								UserEntity userEntity = new UserEntity("", "");
								loginBiz.login(userEntity);

								// 等
								int count = 0;
								while (count < 1000
										&& TApplication.xmppConnection
												.isAuthenticated() == false) {
									count++;
									this.sleep(10);
								}

								// 重新自动登录
								// 判断登录是否成功
								if (TApplication.xmppConnection
										.isAuthenticated() == false) {
									// 登录失败
									status = Const.STATUS_LOGIN_FAILURE;
								} else {
									LoginSuccessSendMessage(friendUser, body);

								}
							}

						}

					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// 发广播通知activit显示
					Intent intent = new Intent(
							Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);
				}
			}

			private void LoginSuccessSendMessage(final String friendUser,
					final String body) {
				// 登录成功
				// 创建Message
				Message message = new Message();
				message.setTo(friendUser);
				message.setBody(body);
				message.setType(Type.chat);
				// from是登录的用户
				// 全局变量Tapplication.currentUser
				// 在登录成功后给Tapplication.currentUser设值
				message.setFrom(TApplication.currentUser);

				// 网络发送加密的数据
				Message msgEncrypt = new Message();
				msgEncrypt.setTo(friendUser);
				byte[] data = body.getBytes();
				for (int i = 0; i < data.length; i++) {
					data[i] = Tools.encrypt(data[i]);
				}
				String encryptBody = new String(data);
				// 加密了
				msgEncrypt.setBody(encryptBody);
				msgEncrypt.setType(message.getType());
				msgEncrypt
						.setFrom(TApplication.currentUser);

				TApplication.xmppConnection
						.sendPacket(msgEncrypt);

				// 实体类中放不加密的
				PrivateChatEntity.addMessage(friendUser,
						message);
			}

			;
		}.start();
	}
}
