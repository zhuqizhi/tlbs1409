package com.tarena.tlbs.util;

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
	public static String addFaceTag(int faceId) {
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
}
