package com.tarena.tlbs.biz.implAsmack;

import java.io.InputStream;
import java.util.UUID;

import org.apache.http.Header;

import android.content.Intent;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.parser.UploadImageParser;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.LogUtil;

/**
 * �ϴ�ͼƬ
 * 
 * @author tarena
 * 
 */
public class UploadImageBiz {

	/**
	 * 
	 * @param inputStream
	 *            �ϴ��ļ�������
	 */
	public void uploadImage(InputStream inputStream)
	{
		//1,httpClient
		//2,java.net.HttpUrlConnection
		//3,AsyncHttpClient,�ڲ��ᴴ��AsyncTask
		
		//1,׼���ϴ�����
		RequestParams postData=new RequestParams();
		//postData.put("username", "1");
		//key��input��name
		//stream������
		//name���ļ� ��
		//contentType���ļ�����
		String fileName=UUID.randomUUID().toString()+".png";
		postData.put("a", inputStream, fileName, "image/png");
		//2,��������
		String url="http://"+TApplication.host+":8080/tlbsServer/tlbsFile.jsp";
		TApplication.instance.asyncHttpClient.post(url, postData, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					String jsonString=new String(responseBody);
					LogUtil.i("uploadImage", jsonString);
					//������
					UploadImageParser uploadImageParser=new UploadImageParser();
					String address=uploadImageParser.parser(jsonString);
					
					//���㲥
					Intent intent=new Intent(Const.ACTION_GET_IMAGE_ADDRESS);
					intent.putExtra(Const.KEY_DATA, address);
					TApplication.instance.sendBroadcast(intent);
				} catch (Exception e) {
					ExceptionUtil.handleException(e);
				}
				
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
