package com.tarena.tlbs.util;

import java.io.FileInputStream;

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
	public static String addFaceTag(int faceId) {
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
	 * �յ�ͼƬʱ����
	 * @param body
	 * @return ͼ
	 */
	public static Bitmap getImage(String body)
	{
		//<message><body>��ͼת�ɵ��ַ���</body></message>
		//ȥ��tag
		int start=TAG_IMAGE.length();
		int end=body.length()-TAG_END.length();
		body=body.substring(start, end);
		//���ַ�����base64�����byte[]
		byte[] data=Base64.decode(body, Base64.DEFAULT);
		//byte[]-->bitmap
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
		return bitmap;
	}
}
