package com.tarena.tlbs.model;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.packet.Message;

/**
 * 存放在所有聊天室中的内容
 * 
 * @author gsd1403
 * 
 */
public class GroupChatEntity {
	// key 是聊天室的名称如14082@conference.tarena.com
	public static ConcurrentHashMap<String, Vector<Message>> map = new ConcurrentHashMap<String, Vector<Message>>();

	public static void addMessage(String room, Message message) {
		Vector<Message> vector = map.get(room);
		if (vector == null) {
			vector = new Vector<Message>();
			map.put(room, vector);
		}
		vector.add(message);

	}

}
