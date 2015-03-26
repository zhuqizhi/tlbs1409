package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.implAsmack.RegisterBiz;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity{
	EditText etUsername,etPassword,etConfirmPassword,etName;
	TextView tvSubmit;
	public final static int MSG_TYPE_REGISTER=1; 
	Handler handler=new Handler(){
		/**
		 * ����UI
		 */
		public void handleMessage(android.os.Message msg) {
			//�ж���ʲô��Ϣ
			int what=msg.what;
			//��ͬ����Ϣ��ͬ�Ĵ���
			switch (what) {
			case MSG_TYPE_REGISTER:
				//ȡ��Ϣ�е�����
				//1.msg.obj  2,msg.getData()
				Bundle bundle=msg.getData();
				int status=bundle.getInt(Const.KEY_DATA);
				switch (status) {
				case Const.STATUS_SUCCESS:
					//���½���
					Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
					break;
				case Const.STATUS_REGISTER_FAILURE:
					Toast.makeText(RegisterActivity.this, "ע��ʧ��", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.register);
			setupView();
			addListener();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void addListener() {
		tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String username=etUsername.getText().toString();
					String password=etPassword.getText().toString();
					String confirmPassword=etConfirmPassword.getText().toString();
					String name=etName.getText().toString();
					
					//���ݼ��
					StringBuilder builder=new StringBuilder();
					if (!password.equals(confirmPassword))
					{
						builder.append("������ȷ�����벻һ��");
					}
					
					if (!TextUtils.isEmpty(builder.toString()))
					{
						//���ûͨ��
						Toast.makeText(RegisterActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
						return;
					}
					
					RegisterBiz biz=new RegisterBiz();
					UserEntity userEntity=new UserEntity(username, password, name);
					biz.register(handler,userEntity);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
	private void setupView() {
		// TODO Auto-generated method stub
		etUsername=(EditText) findViewById(R.id.et_register_username);
		etPassword=(EditText) findViewById(R.id.et_register_password);
		etConfirmPassword=(EditText) findViewById(R.id.et_register_confirm_password);
		etName=(EditText) findViewById(R.id.et_register_name);
		tvSubmit=(TextView) findViewById(R.id.tv_register_submit);
	}

}
