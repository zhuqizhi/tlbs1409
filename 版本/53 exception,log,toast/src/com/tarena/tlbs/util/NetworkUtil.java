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
 * ������صĹ�����
 * 
 * @author tarena
 * 
 */
public class NetworkUtil {
	public static void checkNetworkState(final Activity activity) {
		// �ж���û����
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {// û��

			// û����ʾһ��dialog
			AlertDialog.Builder dialog = new Builder(activity);
			dialog.setTitle("û��");
			dialog.setMessage("�ף�����ֻ�û��");

			// �� ����
			dialog.setPositiveButton("��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						// �ֻ�����һ����������activity,���activity��google����Աд���ˣ�ע��ʱ��filter
						Intent intent = new Intent(
								Settings.ACTION_WIRELESS_SETTINGS);
						activity.startActivity(intent);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			dialog.setNegativeButton("ȡ��", new OnClickListener() {

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
