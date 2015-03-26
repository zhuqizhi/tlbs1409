package com.tarena.tlbs.biz;

import com.tarena.tlbs.biz.implAsmack.LoginBiz;
import com.tarena.tlbs.biz.implAsmack.RegisterBiz;

public class Factory {
 public static ILoginBiz getIloginBiz()
 {
	 //a�ͻ�Ҫ����asmack
	 return new  LoginBiz();
 }
 public static IRegisterBiz getIRegisterBiz()
 {
	 return new RegisterBiz();
 }
}
