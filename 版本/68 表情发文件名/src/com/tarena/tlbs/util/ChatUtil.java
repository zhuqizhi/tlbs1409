package com.tarena.tlbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ChatUtil {
	// 定义五种数据类型
	public final static int TYPE_TEXT = 1;
	public final static int TYPE_FACE = 2;
	public final static int TYPE_IMAGE = 3;
	public final static int TYPE_AUDIO = 4;
	public final static int TYPE_MAP = 5;
	// 定义五种数据类型的tag
	public final static String TAG_TEXT = "<!--text>";
	public final static String TAG_FACE = "<!--face>";
	public final static String TAG_IMAGE = "<!--image>";
	public final static String TAG_AUDIO = "<!--audio>";
	public final static String TAG_MAP = "<!--map>";
	public final static String TAG_END = "<!end>";

	/**
	 * 给face加tag
	 * 
	 * @param faceId
	 */
	public static String addFaceTag(String faceId) {
		StringBuilder builder = new StringBuilder();
		builder.append(TAG_FACE);
		builder.append(faceId);
		builder.append(TAG_END);
		return builder.toString();

	}

	/**
	 * 得到数据的类型
	 * 
	 * @param body
	 * @return
	 */
	public static int getType(String body) {
		if (body.startsWith(TAG_FACE)) {
			return TYPE_FACE;
		} else if (body.startsWith(TAG_IMAGE)) {
			return TYPE_IMAGE;
		} else if (body.startsWith(TAG_AUDIO)) {
			return TYPE_AUDIO;
		} else if (body.startsWith(TAG_MAP)) {
			return TYPE_MAP;
		}
		return TYPE_TEXT;
	}

	public static int getFaceId(String body) {
		// body=<!--face>7515</end>
		int start = TAG_FACE.length();
		int end = body.length() - TAG_END.length();
		String strFace = body.substring(start, end);
		return Integer.parseInt(strFace);
	}
	
	/**
	 * 给图片数据加tag
	 * @param imageData
	 * @return body
	 */
	public static String addImageTag
	(byte[] imageData)
	{
		StringBuilder builder=new StringBuilder();
		//因为在网上发送的是xml,xml中只能放String
		//把byte[]转成String 
		//转有两种方法
		//1:适合于发送端和接收端都是android手机
		 //发送端 new String(imageData);
		//接收端 "body".getBytes()得到图的数据
//2：字符编码是android,iphone都支持的，用base64
		//base64 可以把byte[]转成字符串，再把字符串转成byte[]
		//base64编码android,iphone都支持。
		//发送端，你用的手机是android,
		//接收端用的iphone6
		//byte[]-->String <body>string</body>
		String body=Base64.encodeToString(imageData, Base64.DEFAULT);
		builder.append(TAG_IMAGE);
		builder.append(body);
		builder.append(TAG_END);
		
		return builder.toString();
	}
	public static byte[] getSdcardImage()
	{
		String fileName=Const.SDCARD_ROOT+"/1.jpg";
		byte[] data=null;
		try {
			FileInputStream fileInputStream=new FileInputStream(fileName);
			int size=fileInputStream.available();
			data=new byte[size];
			fileInputStream.read(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 收到图片时调用
	 * @param body
	 * @return 图
	 */
	public static Bitmap getImage(String body)
	{
		//<message><body>由图转成的字符串</body></message>
		//去掉tag
		int start=TAG_IMAGE.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		//把字符串用base64解码成byte[]
		byte[] data=Base64.decode(body, Base64.DEFAULT);
		//byte[]-->bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}
	
	/**
	 * 录音，录的数据要放到这个文件中
	 * @return file
	 */
	public static File getAudioFile()
	{
		//mnt/sdcard/tlbs1409/audio/1.mp3
		File file=null;
		File fileDirectory=new File(Const.AUDIO_PATH);
		if (!fileDirectory.exists()){
			fileDirectory.mkdirs();
		}
		
		file=new File(Const.AUDIO_PATH, "1.mp3");
		return file;
	}
	
	/**
	 * 发语音时调用
	 * 
	 * @return
	 */
	public static String addAudioTag()
	{
		StringBuilder builder=new StringBuilder();
		FileInputStream fileInputStream=null;
		try {
			//读/mnt/sdcard/tlbs1409/audio/1.mp3
			File file=getAudioFile();
			int fileSize=(int) file.length();
			byte[] data=new byte[fileSize];
			fileInputStream=new FileInputStream(file);
			fileInputStream.read(data);			
			//加tag
			String body=Base64.encodeToString(data, Base64.DEFAULT);
			builder.append(TAG_AUDIO);
			builder.append(body);
			builder.append(TAG_END);
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try {
				if (fileInputStream!=null)
				{
					fileInputStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		return builder.toString();
	
	}
	
	public static void getAudio(String body)
	{
		FileOutputStream fileOutputStream=null;
		try {
			int start=TAG_AUDIO.length();
			int end=body.length()-TAG_END.length();
			body=body.substring(start, end);
			
			byte[] data=Base64.decode(body, Base64.DEFAULT);
			
			fileOutputStream=new FileOutputStream(getAudioFile());
			fileOutputStream.write(data);
			fileOutputStream.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally
		{
			try {
				if (fileOutputStream!=null)
				{
					fileOutputStream.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public static String addMapTag
	(byte[] imageData)
	{
		StringBuilder builder=new StringBuilder();
		
		String body=Base64.encodeToString(imageData, Base64.DEFAULT);
		builder.append(TAG_MAP);
		builder.append(body);
		builder.append(TAG_END);
		
		return builder.toString();
	}
	
	public static Bitmap getMap(String body)
	{
		//<message><body>由图转成的字符串</body></message>
		//去掉tag
		int start=TAG_MAP.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		//把字符串用base64解码成byte[]
		byte[] data=Base64.decode(body, Base64.DEFAULT);
		//byte[]-->bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}
}
