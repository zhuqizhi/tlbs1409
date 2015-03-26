package com.tarena.tlbs.biz;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import com.tarena.tlbs.TApplication;

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
			} catch (Exception e) {
				// TODO: handle exception
			}
    	 };}.start();
     }
}
