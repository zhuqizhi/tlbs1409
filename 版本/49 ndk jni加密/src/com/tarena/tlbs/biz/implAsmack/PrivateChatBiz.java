package com.tarena.tlbs.biz.implAsmack;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.Tools;

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
    			 
    			 //网络发送加密的数据
    			 Message msgEncrypt=new Message();
    			 msgEncrypt.setTo(friendUser);
    			 byte[] data=body.getBytes();
    			 for(int i=0;i<data.length;i++)
    			 {
    				 data[i]=Tools.encrypt(data[i]);
    			 }
    			 String encryptBody=new String(data);
    			 //加密了
    			 msgEncrypt.setBody(encryptBody);
    			 msgEncrypt.setType(message.getType());
    			 msgEncrypt.setFrom(TApplication.currentUser);
    			 
					TApplication.xmppConnection.sendPacket(msgEncrypt);
					
					//实体类中放不加密的
					PrivateChatEntity.addMessage(friendUser, message);
					
					//发广播通知activit显示
					Intent intent=new Intent(Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	 }

		;}.start();
     }
}
