package com.tarena.tlbs.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

import org.jivesoftware.smack.packet.*;

public class PrivateChatEntity {
/**
 * 保存与所有好友的聊天内容 
 */
	//用arraylist<>
	//hashMap线程是不安全的。
	//ConcurrentHashMap线程是安全
	//key是string,放的是好友的user
	//value是vector<Message> vector保存多个message
	public static ConcurrentHashMap<String, Vector<Message>> map=
			new ConcurrentHashMap<String, Vector<Message>>();
	
	public static void addMessage(final String friendUser, Message message) {
		//把我说的话放到实体类中
		Vector<Message> vector=PrivateChatEntity.map.get(friendUser);
		//第一次取vector是空
		if (vector==null)
		{
			vector=new Vector<Message>();
		}
		vector.add(message);
		
		PrivateChatEntity.map.put(friendUser, vector);
	}
}
