package com.tarena.tlbs.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.tarena.tlbs.TApplication;

import android.util.Log;

/**
 * 异常统一处理
 * 
 * @author tarena
 * 
 */
public class ExceptionUtil {
	public static void handleException(Exception e) {
		// 把异常信息放到string

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);

		String string = stringWriter.toString();

		if (TApplication.isRelease) {
			// 在用户的手机中发送异常信息给开发人员
		} else {
			// 开发时用logcat看
			e.printStackTrace();// 打到logcat
			// Log.i("handleException", string);
		}
	}
}
