package com.tarena.tlbs.biz.implAsmack;



import java.util.HashMap;

import org.jivesoftware.smack.AccountManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.IRegisterBiz;
import com.tarena.tlbs.model.UserEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.view.RegisterActivity;

public class RegisterBiz implements IRegisterBiz{
  public void register(final Handler handler,final UserEntity userEntity)
  {
	  new Thread(){public void run() {
		  int status=-1;
		  try {
			AccountManager accountManager=TApplication.xmppConnection.getAccountManager();
			HashMap<String, String> map=new HashMap<String, String>();
			//key��name,�ŵ����سơ�
			map.put("name", userEntity.getName());
			accountManager.createAccount(userEntity.getUsername(), userEntity.getPassword(), map);
			status=Const.STATUS_SUCCESS;
		} catch (Exception e) {
			String eInfo=e.toString();
			status=Const.STATUS_REGISTER_FAILURE;
			//��ϸ���ж���ʲô�쳣��
			if ("conflict(409)".equals(eInfo))
			{
				
			}
			Log.i("RegisterBiz", eInfo);
		}finally
		{
		//1,�õ�message
			Message message=handler.obtainMessage();
			message.what=RegisterActivity.MSG_TYPE_REGISTER;
			//2,���ô�����
			Bundle bundle=new Bundle();
			bundle.putInt(Const.KEY_DATA, status);
			message.setData(bundle);
			//obj=activity
			//3 ����
			handler.sendMessage(message);
		}
		  
	  };}.start();
  }


}
