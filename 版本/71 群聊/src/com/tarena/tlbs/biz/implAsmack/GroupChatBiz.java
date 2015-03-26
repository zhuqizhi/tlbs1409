package com.tarena.tlbs.biz.implAsmack;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.app.Activity;
import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.GroupChatEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.view.GroupChatActivity;

public class GroupChatBiz {
	/**
	 * 加入聊天室
	 * 
	 * @param roomName
	 *            服务器上的房间名
	 * @param name
	 *            呢称
	 */
	public void join(final Activity activity, final String roomName,
			final String name) {
		new Thread() {
			public void run() {
				try {
					String room = roomName + "@conference."
							+ TApplication.serviceName;

					// 群聊
					MultiUserChat groupChat = new MultiUserChat(
							TApplication.xmppConnection, room);
					groupChat.join(name);
					TApplication.name=name;

					TApplication.groupChat = groupChat;
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.startActivity(new Intent(activity,
									GroupChatActivity.class));
						}
					});
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}

	public void sendMessage(final String body) {
		new Thread() {
			public void run() {
				try {
					Message message = new Message();
					message.setFrom(TApplication.currentUser);
					message.setTo(TApplication.groupChat.getRoom());
					message.setType(Type.groupchat);
					message.setBody(body);

					TApplication.groupChat.sendMessage(message);

					GroupChatEntity.addMessage(
							TApplication.groupChat.getRoom(), message);
					Intent intent = new Intent(Const.ACTION_SHOW_GROUP_MESSAGE);
					TApplication.instance.sendBroadcast(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		}.start();
	}
}
