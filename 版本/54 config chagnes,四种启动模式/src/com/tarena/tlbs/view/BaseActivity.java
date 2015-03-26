package com.tarena.tlbs.view;

import com.tarena.tlbs.MainActivity;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.util.NetworkUtil;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 在MainActivity,LoginActivity...都要把activity放到listActivity
		TApplication.instance.listActivity.add(this);

		// 检测有没有网络，没网，打开网络设置
		// 第一个MainActivity不检测
		if (!(this instanceof MainActivity)) {
			NetworkUtil.checkNetworkState(this);
		}
	}

}
