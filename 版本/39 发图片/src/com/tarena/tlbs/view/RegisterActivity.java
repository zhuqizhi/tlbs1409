package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.RegisterBiz;
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
		 * 更新UI
		 */
		public void handleMessage(android.os.Message msg) {
			//判断是什么消息
			int what=msg.what;
			//不同的消息不同的处理
			switch (what) {
			case MSG_TYPE_REGISTER:
				//取消息中的数据
				//1.msg.obj  2,msg.getData()
				Bundle bundle=msg.getData();
				int status=bundle.getInt(Const.KEY_DATA);
				switch (status) {
				case Const.STATUS_SUCCESS:
					//更新界面
					Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
					break;
				case Const.STATUS_REGISTER_FAILURE:
					Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
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
					
					//数据检查
					StringBuilder builder=new StringBuilder();
					if (!password.equals(confirmPassword))
					{
						builder.append("密码与确认密码不一致");
					}
					
					if (!TextUtils.isEmpty(builder.toString()))
					{
						//检查没通过
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
