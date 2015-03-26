package com.tarena.tlbs.receiver;

import com.tarena.tlbs.TApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 用户打开或关闭网络时执行
 * 
 * @author tarena
 * 
 */
public class NetworkStateChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// 判断是打开还是关闭
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				// 关闭
				TApplication.network_state = "1";
				Log.i("网络状态改变", "关闭");

			} else {
				// 打开
				// 如果用户的手机是gprs ，3g,4g
				// 1MB 是1块钱
				// 4g 每秒是100MB
				// 小白用你的软件下了一天的电影
				// 24*60*60*100=8640000
				// 判断用户的当前网络是gprs还是wifi
				NetworkInfo mobileNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobileNetworkInfo != null
						&& mobileNetworkInfo.isConnected()) {
					Log.i("网络状态改变", "打开了mobile");
					TApplication.network_state = "02";

				}

				NetworkInfo wifiNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
					Log.i("网络状态改变", "打开了wifi");
					TApplication.network_state = "01";

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
