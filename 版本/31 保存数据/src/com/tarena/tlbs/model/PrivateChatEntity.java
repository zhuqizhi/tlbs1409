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
}
