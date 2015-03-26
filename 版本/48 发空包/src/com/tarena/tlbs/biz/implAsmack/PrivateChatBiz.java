package com.tarena.tlbs.biz.implAsmack;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import android.content.Intent;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.Const;

/**
 * ����˽������
 * @author tarena
 *
 */
public class PrivateChatBiz {
     public void sendMessage(final String friendUser,final String body)
     {
    	 new Thread(){public void run() {
    		 try {
				//����Message
    			 Message message=new Message();
    			 message.setTo(friendUser);
    			 message.setBody(body);
    			 message.setType(Type.chat);
    			 //from�ǵ�¼���û�
    			 //ȫ�ֱ���Tapplication.currentUser
    			 //�ڵ�¼�ɹ����Tapplication.currentUser��ֵ
    			 message.setFrom(TApplication.currentUser);
    			 
    			 //����
					TApplication.xmppConnection.sendPacket(message);
					
					PrivateChatEntity.addMessage(friendUser, message);
					
					//���㲥֪ͨactivit��ʾ
					Intent intent=new Intent(Const.ACTION_SHOW_PRIVATE_CHAT_MSG);
					TApplication.instance.sendBroadcast(intent);
			} catch (Exception e) {
				// TODO: handle exception
			}
    	 }

		;}.start();
     }
}
