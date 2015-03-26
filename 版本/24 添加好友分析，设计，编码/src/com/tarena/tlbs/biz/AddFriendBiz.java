package com.tarena.tlbs.biz;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPException;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.FriendEntity;

public class AddFriendBiz {
 public void addFriend(final FriendEntity friendEntity)
 {
	 new Thread(){public void run() {
		 try {
			Roster roster=TApplication.xmppConnection.getRoster();
			 //�����û��������ƺ�
			 //1,username pc1
			 //2,name С��
			 //3,user pc1@tarena.com
			 String user=friendEntity.getUsername()+"@"+TApplication.serviceName;
			 //�Ŷ���飬һ�������ڶ�������档
			 String[] groups={friendEntity.getGroup()};
			 roster.createEntry(user, friendEntity.getName(), groups);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 };}.start();
 }
}
