package com.tarena.tlbs.receiver;

import com.tarena.tlbs.TApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * �û��򿪻�ر�����ʱִ��
 * 
 * @author tarena
 * 
 */
public class NetworkStateChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			// �ж��Ǵ򿪻��ǹر�
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
			if (activeNetworkInfo == null) {
				// �ر�
				TApplication.network_state = "1";
				Log.i("����״̬�ı�", "�ر�");

			} else {
				// ��
				// ����û����ֻ���gprs ��3g,4g
				// 1MB ��1��Ǯ
				// 4g ÿ����100MB
				// С��������������һ��ĵ�Ӱ
				// 24*60*60*100=8640000
				// �ж��û��ĵ�ǰ������gprs����wifi
				NetworkInfo mobileNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (mobileNetworkInfo != null
						&& mobileNetworkInfo.isConnected()) {
					Log.i("����״̬�ı�", "����mobile");
					TApplication.network_state = "02";

				}

				NetworkInfo wifiNetworkInfo = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
					Log.i("����״̬�ı�", "����wifi");
					TApplication.network_state = "01";

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
