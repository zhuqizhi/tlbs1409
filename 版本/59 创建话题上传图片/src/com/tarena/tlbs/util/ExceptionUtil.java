package com.tarena.tlbs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.tarena.tlbs.TApplication;

import android.util.Log;

/**
 * �쳣ͳһ����
 * 
 * @author tarena
 * 
 */
public class ExceptionUtil {
	public static void handleException(Exception e) {
		// ���쳣��Ϣ�ŵ�string

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);

		String string = stringWriter.toString();

		if (TApplication.isRelease) {
			// ���û����ֻ��з����쳣��Ϣ��������Ա
		} else {
			// ����ʱ��logcat��
			e.printStackTrace();// ��logcat
			// Log.i("handleException", string);
		}
	}
}
