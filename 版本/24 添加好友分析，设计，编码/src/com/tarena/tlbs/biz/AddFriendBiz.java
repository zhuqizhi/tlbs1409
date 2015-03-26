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
			 //聊天用户有三个称呼
			 //1,username pc1
			 //2,name 小王
			 //3,user pc1@tarena.com
			 String user=friendEntity.getUsername()+"@"+TApplication.serviceName;
			 //放多个组，一个好友在多个组里面。
			 String[] groups={friendEntity.getGroup()};
			 roster.createEntry(user, friendEntity.getName(), groups);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 };}.start();
 }
}
