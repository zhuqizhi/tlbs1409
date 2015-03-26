package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.Factory;
import com.tarena.tlbs.biz.ILoginBiz;
import com.tarena.tlbs.biz.implAsmack.LoginBiz;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.Tools;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	EditText etUsername, etPassword;
	TextView tvSubmit, tvRegister;
	LoginReceiver loginReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {

			setContentView(R.layout.login);
			setView();
			addListener();
			loginReceiver = new LoginReceiver();
			this.registerReceiver(loginReceiver, new IntentFilter(
					Const.ACTION_LOGIN));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(loginReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addListener() {
		MyOnclickLisgener myOnclickLisgener = new MyOnclickLisgener();
		tvSubmit.setOnClickListener(myOnclickLisgener);
		tvRegister.setOnClickListener(myOnclickLisgener);

	}

	private void setView() {

		etUsername = (EditText) findViewById(R.id.et_login_username);
		etPassword = (EditText) findViewById(R.id.et_login_password);
		tvSubmit = (TextView) findViewById(R.id.tv_login_submit);
		tvRegister = (TextView) findViewById(R.id.tv_login_toRegister);
	}

	class MyOnclickLisgener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.tv_login_submit:
					doSubmit();
					break;
				case R.id.tv_login_toRegister:
					doRegister();
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void doRegister() {

			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			LoginActivity.this.startActivity(intent);
		}

		private void doSubmit() {
			// ��������
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			// ���
			StringBuilder builder = new StringBuilder();
			if (TextUtils.isEmpty(username)) {
				builder.append("�û�������Ϊ��\n");
			}

			if (TextUtils.isEmpty(password)) {
				builder.append("���벻��Ϊ��\n");
			}
			if (!TextUtils.isEmpty(builder.toString())) {
				// ���ûͨ��
				Toast.makeText(LoginActivity.this, builder.toString(),
						Toast.LENGTH_LONG).show();
				return;
			}
			Tools.showProgressDialog(LoginActivity.this, "���ڵ�¼");
			// ֱ�ӵ�asmackʵ�ֵ�biz,��ϸ�
			//LoginBiz biz = new LoginBiz();
			UserEntity userEntity = new UserEntity(username, password);
			//biz.login(userEntity);
			
			//���ӿ�
			ILoginBiz iLoginBiz=Factory.getIloginBiz();
			iLoginBiz.login(userEntity);
		}

	}

	class LoginReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				Tools.closeProgressDialog();
				int status = intent.getIntExtra(Const.KEY_DATA, -1);
				// ����״̬����ʾ��ͬ����Ϣ
				switch (status) {
				case Const.STATUS_SUCCESS:
					Intent intentToTopic = new Intent(context,
							TopicActivity.class);
					context.startActivity(intentToTopic);
					
					Tools.showInfo(context, "��¼�ɹ�");
					break;
				case Const.STATUS_CONNECT_FAILURE:
					Toast.makeText(context, "���������Ӳ���", Toast.LENGTH_LONG)
							.show();
					break;
				case Const.STATUS_LOGIN_FAILURE:
					Toast.makeText(context, "��¼ʧ��", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
