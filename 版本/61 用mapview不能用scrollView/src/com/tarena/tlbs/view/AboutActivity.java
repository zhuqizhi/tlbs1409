package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.util.ExceptionUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AboutActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.about);
			WebView webView=(WebView) findViewById(R.id.webView1);
			webView.setWebViewClient(new WebViewClient(){
				//单击超链接
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// 自定义标签。
					//href="tarena:tel/010-88888888"
					if (url.startsWith("tarena:tel"))
					{
						//由android 打电话
						int index=url.indexOf("tarena:tel/");
						String mobile=url.substring(index+"tarena:tel/".length());
						Uri uri=Uri.parse("tel:"+mobile);
						Intent intent=new Intent(Intent.ACTION_CALL,uri);
						startActivity(intent);
						return true;//这个超链接,android已经处理过了，webView不处理
						
					}
					return super.shouldOverrideUrlLoading(view, url);
				}
			});
			webView.loadUrl("file:///android_asset/about.html");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
