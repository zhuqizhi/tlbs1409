package com.tarena.tlbs.util;

import android.content.Intent;
import android.os.Environment;

public class Const {

	public final static String ACTION_LOGIN="com.tarena.tlbs.action_login";
	public final static String ACTION_UPDATE_MSG="com.tarena.tlbs.action_update_My_msg";
	public final static String ACTION_SHOW_PRIVATE_CHAT_MSG="com.tarena.tlbs.action_show_private_chat_msg";
	public final static String ACTION_UPDATE_MY_FRIEND="com.tarena.tlbs.action_update_my_friend";
	public final static String ACTION_GET_IMAGE_ADDRESS="com.tarena.tlbs.action_get_image_address";
	public final static String ACTION_GET_LOCATION_ADDRESS="com.tarena.tlbs.action_get_location_address";
	public final static String ACTION_SHOW_TOPIC="com.tarena.tlbs.action_show_topic";
	public final static String KEY_DATA="key_data";
	
	//״̬��
	public final static int STATUS_SUCCESS=1;//�����ɹ�
	public final static int STATUS_CONNECT_FAILURE=2;//���ӷ�����ʧ��
	public final static int STATUS_LOGIN_FAILURE=3;//���ӷ�����ʧ��
	public final static int STATUS_REGISTER_FAILURE=4;//���ӷ�����ʧ��
	public final static int STATUS_OPEN_NETWORK=5;//�ֻ��������ˣ��û�������
	
	//����apk /mnt/sdcard/tlbs1409/apk
	//����ͼƬ/mnt/sdcard/tlbs1409/images
	public final static String SDCARD_ROOT=Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String APK_DOWNLOAD_PATH=SDCARD_ROOT+"/tlbs1409/apk";
	public final static String AUDIO_PATH=SDCARD_ROOT+"/tlbs1409/audio";
	public final static String IMAGE_PATH=SDCARD_ROOT+"/tlbs1409/image";
	
	public final static String BAIDU_ANDROID_KEY="505rzcodexm6lONkz4SaxXF4";
	public final static String BAIDU_SERVER_KEY="mAM8VWEFuxkB2cvNMklg9bVn";
	public final static String BAIDU_TABLE_ID="92767";
	
	
	//�ٶ���ص�����ur
	public static final String MAP_POI_CREATE = "http://api.map.baidu.com/geodata/v2/poi/create";
	public static final String MAP_QUERY_NEARBY_POI_LIST = "http://api.map.baidu.com/geodata/v2/poi/list";
	public static final String MAP_QUERY_ADDRESS = "http://api.map.baidu.com/geocoder/v2/";
	public static final String ACTION_SHOW_GROUP_MESSAGE="ACTION_SHOW_GROUP_MESSAGE";
}