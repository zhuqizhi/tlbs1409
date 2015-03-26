package com.tarena.tlbs.biz;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.Const;

/**
 * 发送私聊内容
 * @author tarena
 *
 */
public class PrivateChatBiz {
     public void sendMessage(final String friendUser,final String body)
     {
    	 new Thread(){public void run() {
    		 try {
				//创建Message
    			 Message message=new Message();
    			 message.setTo(friendUser);
    			 message.setBody(body);
    			 message.setType(Type.chat);
    			 //from是登录的用户
    			 //全局变量Tapplication.currentUser
    			 //在登录成功后给Tapplication.currentUser设值
    			 message.setFrom(TApplication.currentUser);
    			 
    			 //发送
					TApplication.xmppConnection.sendPacket(message);
					
					//把我说的话放到实体类中
					Vector<Message> vector=PrivateChatEntity.map.get(friendUser);
					//第一次取vector是空
					if (vector==null)
					{
						vector=new Vector<Message>();
					}
					vector.add(message);
					
					PrivateChatEntity.map.put(friendUser, vector);
					
					//发广播通知activit显示
					Intent intent=new Intent(Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	 };}.start();
     }
}
