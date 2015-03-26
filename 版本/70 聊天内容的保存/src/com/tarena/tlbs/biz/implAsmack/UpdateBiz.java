package com.tarena.tlbs.biz.implAsmack;

import org.apache.http.Header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.UpdateEntity;
import com.tarena.tlbs.parser.UpdateParser;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.Tools;
import com.tarena.tlbs.view.SettingActivity;

/**
 * apk����
 * @author tarena
 *
 */
public class UpdateBiz {
	public void downloadApk(final Handler handler,final String apkUrl)
	{
		TApplication.asyncHttpClient.get(apkUrl, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

				//apkUrl=http://192.168.188.98:8080/tlbsServer/a.apk
				String fileName=apkUrl.substring(apkUrl.lastIndexOf("/")+1);
				//����apk��sdcard
				Tools.writeToSdcard(Const.APK_DOWNLOAD_PATH, fileName, responseBody);
				
				//����Ϣ
				String apkPathName=Const.APK_DOWNLOAD_PATH+"/"+fileName;
				Message message=handler.obtainMessage();
				message.what=SettingActivity.MSG_TYPE_INSTALL_APK;
				Bundle bundle=new Bundle();
				bundle.putString(Const.KEY_DATA, apkPathName);
				message.setData(bundle);
				
				handler.sendMessage(message);
				
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * ȥtomcat�ϵ�apk�İ汾��Ϣ
	 */
	public void getNewVersionInfo(final Handler handler)
	{
		
			//������api
			//1,httpClient
			//2,java.net.HttpUrlConnection
			//3,AsyncHttpClient ��github�ϵ�һ����Դ��Ŀ��ʵ����http����������ǿ���ȶ�
			String url="http://192.168.188.98:8080/tlbsServer/servlet/ApkUpdateServlet";
			//������Thread,asyncHttpClient�ڲ���Thread
			TApplication.asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
				
				//����ȡ�����ݺ��Զ�����
				/**
				 * statusCode 200:�ɹ�  404��url������
				 * Header[]:
				 * responseBody:���ص�ʵ��
				 */
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					String string=new String(responseBody);
					//ȥ��ǰ���utf��־��
					string=string.substring(2);
					
					//������
					UpdateParser updateParser=new UpdateParser();
					UpdateEntity updateEntity=updateParser.parser(string);
					Log.i("getNewVersionInfo", string);
					
					//����Ϣ
					Message message=handler.obtainMessage();
					message.what=SettingActivity.MSG_TYPE_SHOW_DIALOG;
					Bundle bundle=new Bundle();
					bundle.putSerializable(Const.KEY_DATA, updateEntity);
					message.setData(bundle);
					
					handler.sendMessage(message);
					
				}
				
				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					
				}
			});
		
	}

}
