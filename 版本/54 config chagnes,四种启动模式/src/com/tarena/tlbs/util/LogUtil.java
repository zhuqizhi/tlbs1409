package com.tarena.tlbs.util;

import android.util.Log;

import com.tarena.tlbs.TApplication;

/**
 * ��˾��־�Ǻ���Ҫ�ģ�Ҫͳһ����
 * ��¼ÿ��ģ���ÿ��������д���
 * ��¼ÿ������ִ�е�ʱ�䡣
 * ��־ǿ���Ƽ��� baidu android log4j
 * @author tarena
 *
 */
public class LogUtil {
	public static void i(String tag,Object msg)
	{
		if (TApplication.isRelease)
		{
			//�����������
			if ("����ִ��ʱ��".equals(tag))
			{
				//���浽sdcard
				//���͸�������Ա
			}
		}else
		{
			//��������
			Log.i(tag, String.valueOf(msg));
		}
	}

}
