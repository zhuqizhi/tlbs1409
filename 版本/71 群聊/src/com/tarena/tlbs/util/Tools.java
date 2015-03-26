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
import android.widget.Toast;

public class Tools {

	public static void showInfo(Context context, String msg) {
		// dialog

		// Toast toast=new Toast(context);
		// toast.setView(view);

		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	static {
		System.loadLibrary("hello-jni");
	}

	// 加密
	public native static byte encrypt(byte data);

	// 解密
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
			// 判断sdcard存不存在
			String sdcardState = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {// sdcard存在
																// 判断文件夹存不存在
				File filePath = new File(path);
				if (!filePath.exists()) {
					filePath.mkdirs();
				}
				// 判断文件存不存在
				File file = new File(path, fileName);
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				// 写入
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
			// false 在dialog外面单击屏幕，dialog不会关闭
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
	 * 得到manifest.vml version Name
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String versionName = "";
		try {
			// package是com.tarena.tlbs /data/data/com.tarena.tlbs
			PackageManager packageManager = context.getPackageManager();
			// 得包名
			String packageName = context.getPackageName();
			// 包的信息，一个包有多少个activity,
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
