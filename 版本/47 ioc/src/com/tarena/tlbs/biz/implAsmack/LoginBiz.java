package com.tarena.tlbs.biz.implAsmack;

import android.content.Intent;
import android.util.Log;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.ILoginBiz;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;

public class LoginBiz implements ILoginBiz{
	/**
	 * 面试题1: ui(TextView,ListView)是不是线程安全 类分线程安全和线程不安全 线程安全:这个类的对象在多线程时，访问不会出错。
	 * stringBuffer，Vector是安全 stringBuilder,arrayList是不安全
	 * ui(TextView,ListView)不是线程安全 解决方法 1，发消息 2，发广播 3，runOnUiThread()
	 * 面试题2:发消息和发广播的区别 共同点：都能解决在工作线程不能访问UI 区别：广播能发给另外一个应用，作用域比发消息大。
	 * 项目中，发广播，工作线程只负责发。 发消息，工作线程必须要引用一个handler 推荐用发广播。
	 * 
	 * @param userEntity
	 */
	public void login(final UserEntity userEntity) {
		// 联网，必须启工作线程
		new Thread("login thread") {
			public void run() {
				int status = -1;
				try {
					
					// 登录，需要连接线程连上服务器
					// 退出while
					int count = 0;
					while (count < 10
							&& TApplication.xmppConnection.isConnected() == false) {
						sleep(1000);
						count++;
						Log.i("登录", "登录count=" + (count));
					}

					// 判断连接是否成功
					if (TApplication.xmppConnection.isConnected()) {
						long time = System.currentTimeMillis();
						Log.i("登录", "登录time="
								+ (time - TApplication.appStartTime));
						// this.sleep(20000);
						TApplication.xmppConnection.login(
								userEntity.getUsername(),
								userEntity.getPassword());
						// true:成为 faluse:失败
						Log.i("LoginBiz",
								"登录结果="
										+ TApplication.xmppConnection
												.isAuthenticated());
						if (TApplication.xmppConnection.isAuthenticated()) {
							status = Const.STATUS_SUCCESS;
							TApplication.currentUser=userEntity.getUsername()+"@"+TApplication.serviceName;
						} else {
							status = Const.STATUS_LOGIN_FAILURE;
						}
					} else {
						status = Const.STATUS_CONNECT_FAILURE;
					}

				} catch (Exception e) {
					//出现多种异常 1，空指针，2，连不上服务器 3密码错误
					
					String eInfo=e.toString();
					//不同的异常，eInfo是不一样的
					//判断出是那个异常。不同异常不同处理。
					if ("SASL authentication failed using mechanism DIGEST-MD5: ".equals(eInfo))
					{
						//密码错误
						//断开连接
						TApplication.xmppConnection.disconnect();
						//再重连
						TApplication.instance.connectChatServer();
						
					}
					if ("NullPointerException".equals(eInfo))
					{
						//空指针错误
						
					}
					Log.i("LoginBiz", eInfo);
					
					status = Const.STATUS_LOGIN_FAILURE;
					e.printStackTrace();
				} finally {
					// 用户点了提交，必须发出广播
					Intent intent = new Intent(Const.ACTION_LOGIN);
					intent.putExtra(Const.KEY_DATA, status);

					// android中activity,receiver,service,application不能new
					// 约束
					// 不能new
					// TApplication tApplication=new TApplication();
					// tApplication.sendBroadcast(intent);
					TApplication.instance.sendBroadcast(intent);

				}
			};
		}.start();

	}
}
