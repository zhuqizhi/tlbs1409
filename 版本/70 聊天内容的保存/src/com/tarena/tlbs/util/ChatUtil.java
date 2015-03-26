package com.tarena.tlbs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ChatUtil {
	// ����������������
	public final static int TYPE_TEXT = 1;
	public final static int TYPE_FACE = 2;
	public final static int TYPE_IMAGE = 3;
	public final static int TYPE_AUDIO = 4;
	public final static int TYPE_MAP = 5;
	// ���������������͵�tag
	public final static String TAG_TEXT = "<!--text>";
	public final static String TAG_FACE = "<!--face>";
	public final static String TAG_IMAGE = "<!--image>";
	public final static String TAG_AUDIO = "<!--audio>";
	public final static String TAG_MAP = "<!--map>";
	public final static String TAG_END = "<!end>";

	/**
	 * ��face��tag
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
	 * �õ����ݵ�����
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
	 * ��ͼƬ���ݼ�tag
	 * @param imageData
	 * @return body
	 */
	public static String addImageTag
	(byte[] imageData)
	{
		StringBuilder builder=new StringBuilder();
		//��Ϊ�����Ϸ��͵���xml,xml��ֻ�ܷ�String
		//��byte[]ת��String 
		//ת�����ַ���
		//1:�ʺ��ڷ��Ͷ˺ͽ��ն˶���android�ֻ�
		 //���Ͷ� new String(imageData);
		//���ն� "body".getBytes()�õ�ͼ������
//2���ַ�������android,iphone��֧�ֵģ���base64
		//base64 ���԰�byte[]ת���ַ������ٰ��ַ���ת��byte[]
		//base64����android,iphone��֧�֡�
		//���Ͷˣ����õ��ֻ���android,
		//���ն��õ�iphone6
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
	 * ��һ�εõ�ͼƬ<body>��Base�����ͼ������
	 * @param body
	 * @return
	 */
	public static String saveImageToSdcard(String body)
	{
		String fileName=null;
		int start=TAG_IMAGE.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		byte[] imageData=Base64.decode(body, Base64.DEFAULT);
		fileName=UUID.randomUUID().toString()+".png";
		Tools.writeToSdcard(Const.IMAGE_PATH, fileName, imageData);
		
		//bodyԭ�ȷŵ���ͼ�����ݣ����ڷ��ļ� ������ռ�ڴ�
		body=TAG_IMAGE+fileName+TAG_END;
		return body;
	}
	/**
	 * �յ�ͼƬʱ����
	 * @param body ��ŵ���ͼ���ļ���
	 * @return ͼ
	 */
	public static Bitmap getImage(String body)
	{
		//<message><body>��ͼת�ɵ��ַ���</body></message>
		//ȥ��tag
		int start=TAG_IMAGE.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		byte[] data=null;
		//��sdcard�϶�ͼ������
		
		try {
			File file=new File(Const.IMAGE_PATH, body);
			FileInputStream fileInputStream=new FileInputStream(file);
			int size=fileInputStream.available();
			data=new byte[size];
			fileInputStream.read(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//byte[]-->bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}
	
	/**
	 * ¼����¼������Ҫ�ŵ�����ļ���
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
	 * ������ʱ����
	 * 
	 * @return
	 */
	public static String addAudioTag()
	{
		StringBuilder builder=new StringBuilder();
		FileInputStream fileInputStream=null;
		try {
			//��/mnt/sdcard/tlbs1409/audio/1.mp3
			File file=getAudioFile();
			int fileSize=(int) file.length();
			byte[] data=new byte[fileSize];
			fileInputStream=new FileInputStream(file);
			fileInputStream.read(data);			
			//��tag
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
		//<message><body>��ͼת�ɵ��ַ���</body></message>
		//ȥ��tag
		int start=TAG_MAP.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		//���ַ�����base64�����byte[]
		byte[] data=Base64.decode(body, Base64.DEFAULT);
		//byte[]-->bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}
}
