package com.tarena.tlbs.util;

import android.util.Log;

import com.tarena.tlbs.TApplication;

/**
 * 公司日志是很重要的，要统一处理
 * 记录每个模块或每个类的运行次数
 * 记录每个方法执行的时间。
 * 日志强烈推荐用 baidu android log4j
 * @author tarena
 *
 */
public class LogUtil {
	public static void i(String tag,Object msg)
	{
		if (TApplication.isRelease)
		{
			//运行在真机。
			if ("方法执行时间".equals(tag))
			{
				//保存到sdcard
				//发送给开发人员
			}
		}else
		{
			//开发程序
			Log.i(tag, String.valueOf(msg));
		}
	}

}
