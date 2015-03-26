package com.tarena.tlbs;

import com.tarena.tlbs.biz.implAsmack.LoginBiz;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.Tools;
import com.tarena.tlbs.view.BaseActivity;
import com.tarena.tlbs.view.LoginActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//����LoginBiz
//		UserEntity userEntity=new UserEntity("zhangjiujun", "1");
//		LoginBiz loginBiz=new LoginBiz();
//		loginBiz.login(userEntity);
		try {
			//int a=9/0;
			setContentView(R.layout.activity_main);
			TextView tv = (TextView) findViewById(R.id.tv_main_versionName);
			String versionName = Tools.getVersion(this);
			tv.setText(versionName);

			new Thread() {
				public void run() {
					try {
						sleep(1000);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// ���������߳�
								Intent intent = new Intent(MainActivity.this,
										LoginActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.login_enter,
										R.anim.main_exit);
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}

				};
			}.start();
		} catch (Exception e) {
			//���쳣��Ϣ����������Ա
			//��httpClient,new Socket()
			//���ʼ�
			ExceptionUtil.handleException(e);
		}
		
		//testLogUseTime();
		
	}

	private void testLogUseTime() {
		long st=System.currentTimeMillis();
		int sum=0;
		for (int i=0;i<10000;i++)
		{
			sum=sum+i;
			Log.i("testLogUseTime", "i="+i);
		}
		long et=System.currentTimeMillis();
		Log.i("testLogUseTime", "log time"+(et-st));

		
		 st=System.currentTimeMillis();
		 sum=0;
		for ( int i=0;i<10000;i++)
		{
			sum=sum+i;
			//Log.i("testLogUseTime", "i="+i);
		}
		 et=System.currentTimeMillis();
		Log.i("testLogUseTime", "����log time"+(et-st));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
