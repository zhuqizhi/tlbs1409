package com.tarena.tlbs.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
import org.jivesoftware.smack.packet.*;

public class PrivateChatEntity {
/**
 * ���������к��ѵ��������� 
 */
	//��arraylist<>
	//hashMap�߳��ǲ���ȫ�ġ�
	//ConcurrentHashMap�߳��ǰ�ȫ
	//key��string,�ŵ��Ǻ��ѵ�user
	//value��vector<Message> vector������message
	public static ConcurrentHashMap<String, Vector<Message>> map=
			new ConcurrentHashMap<String, Vector<Message>>();
}
