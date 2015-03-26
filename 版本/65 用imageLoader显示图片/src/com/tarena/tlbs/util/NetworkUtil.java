package com.tarena.tlbs.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * 网络相关的工具类
 * 
 * @author tarena
 * 
 */
public class NetworkUtil {
	public static void checkNetworkState(final Activity activity) {
		// 判断有没有网
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {// 没网

			// 没网显示一个dialog
			AlertDialog.Builder dialog = new Builder(activity);
			dialog.setTitle("没网");
			dialog.setMessage("亲，你的手机没网");

			// 打开 网络
			dialog.setPositiveButton("打开", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						// 手机中有一个网络设置activity,这个activity由google程序员写好了，注册时加filter
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						activity.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			dialog.setNegativeButton("取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					try {
						dialog.dismiss();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			});

			dialog.show();

		}
	}
}
