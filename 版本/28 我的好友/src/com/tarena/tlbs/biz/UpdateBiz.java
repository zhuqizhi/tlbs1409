package com.tarena.tlbs.biz;

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
 * apk升级
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
				//保存apk到sdcard
				Tools.writeToSdcard(Const.APK_DOWNLOAD_PATH, fileName, responseBody);
				
				//发消息
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
	 * 去tomcat上得apk的版本信息
	 */
	public void getNewVersionInfo(final Handler handler)
	{
		
			//联网的api
			//1,httpClient
			//2,java.net.HttpUrlConnection
			//3,AsyncHttpClient 是github上的一个开源项目，实现了http联网，功能强大，稳定
			String url="http://192.168.188.98:8080/tlbsServer/servlet/ApkUpdateServlet";
			//不用启Thread,asyncHttpClient内部启Thread
			TApplication.asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
				
				//联网取到数据后自动运行
				/**
				 * statusCode 200:成功  404：url不存在
				 * Header[]:
				 * responseBody:返回的实体
				 */
				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
					String string=new String(responseBody);
					//去掉前面的utf标志。
					string=string.substring(2);
					
					//调解析
					UpdateParser updateParser=new UpdateParser();
					UpdateEntity updateEntity=updateParser.parser(string);
					Log.i("getNewVersionInfo", string);
					
					//发消息
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
