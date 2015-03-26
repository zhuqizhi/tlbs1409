package com.tarena.tlbs.util;

import android.os.Environment;

public class Const {

	public final static String ACTION_LOGIN="com.tarena.tlbs.action_login";
	public final static String ACTION_UPDATE_MSG="com.tarena.tlbs.action_update_My_msg";
	public final static String ACTION_SHOW_PRIVATE_CHAT_MSG="com.tarena.tlbs.action_show_private_chat_msg";
	public final static String KEY_DATA="key_data";
	
	//状态码
	public final static int STATUS_SUCCESS=1;//操作成功
	public final static int STATUS_CONNECT_FAILURE=2;//连接服务器失败
	public final static int STATUS_LOGIN_FAILURE=3;//连接服务器失败
	public final static int STATUS_REGISTER_FAILURE=4;//连接服务器失败
	
	//保存apk /mnt/sdcard/tlbs1409/apk
	//保存图片/mnt/sdcard/tlbs1409/images
	public final static String SDCARD_ROOT=Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String APK_DOWNLOAD_PATH=SDCARD_ROOT+"/tlbs1409/apk";
}
