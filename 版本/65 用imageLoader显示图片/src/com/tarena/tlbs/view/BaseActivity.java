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
		// ��MainActivity,LoginActivity...��Ҫ��activity�ŵ�listActivity
		TApplication.instance.listActivity.add(this);

		// �����û�����磬û��������������
		// ��һ��MainActivity�����
		if (!(this instanceof MainActivity)) {
			NetworkUtil.checkNetworkState(this);
		}
	}

}
