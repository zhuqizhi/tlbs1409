package com.tarena.tlbs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class Tools {
	static 
	{
		System.loadLibrary("hello-jni");
	}
	//����
	public native static byte encrypt(byte data);
	//����
	public native static byte decrypt(byte data);
	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param fileData
	 */
	public static void writeToSdcard(String path, String fileName,
			byte[] fileData) {
		FileOutputStream fileOutputStream = null;
		try {
			// �ж�sdcard�治����
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {// sdcard����
																// �ж��ļ��д治����
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				// �ж��ļ��治����
				File file = new File(path, fileName);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				// д��
				fileOutputStream = new FileOutputStream(file);
				fileOutputStream.write(fileData);
				fileOutputStream.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static ProgressDialog progressDialog;

	public static void showProgressDialog(Activity activity, String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage(msg);
			// false ��dialog���浥����Ļ��dialog����ر�
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

	}

	public static void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
			System.gc();
		}
	}

	/**
	 * �õ�manifest.vml version Name
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionName = "";
		try {
			// package��com.tarena.tlbs /data/data/com.tarena.tlbs
			PackageManager packageManager = context.getPackageManager();
			// �ð���
			String packageName = context.getPackageName();
			// ������Ϣ��һ�����ж��ٸ�activity,
			PackageInfo packageInfo = packageManager.getPackageInfo(
					packageName, 0);
			versionName = packageInfo.versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
}
