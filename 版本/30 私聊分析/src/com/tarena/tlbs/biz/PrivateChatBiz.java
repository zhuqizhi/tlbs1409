package com.tarena.tlbs.biz;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import com.tarena.tlbs.TApplication;

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
			} catch (Exception e) {
				// TODO: handle exception
			}
    	 };}.start();
     }
}
