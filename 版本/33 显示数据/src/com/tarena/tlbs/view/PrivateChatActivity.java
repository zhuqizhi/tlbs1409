package com.tarena.tlbs.view;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.PrivateChatBiz;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.Const;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PrivateChatActivity extends BaseActivity {
	TextView tvFriendName;
	EditText etBody;
	Button btnSend;
	String friendUser;
	ShowPrivateChatMessageReceiver receiver;
	
	ScrollView scrollView;
	LinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			friendUser = this.getIntent().getStringExtra(Const.KEY_DATA);
			setContentView(R.layout.private_chat);
			setupView();
			addListener();

			receiver = new ShowPrivateChatMessageReceiver();
			this.registerReceiver(receiver, new IntentFilter(
					Const.ACTION_SHOW_PRIVATE_CHAT_MSG));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(receiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addListener() {
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String body = etBody.getText().toString();
					PrivateChatBiz biz = new PrivateChatBiz();
					biz.sendMessage(friendUser, body);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void setupView() {
		// TODO Auto-generated method stub
		tvFriendName = (TextView) findViewById(R.id.tv_private_chat_friendName);
		etBody = (EditText) findViewById(R.id.et_private_chat_body);
		btnSend = (Button) findViewById(R.id.btn_private_chat_send);
		
		scrollView=(ScrollView) findViewById(R.id.scrollView1);
		linearLayout=(LinearLayout) findViewById(R.id.linearLayoutChatContent);
	}

	class ShowPrivateChatMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				// 根据好友的user从实体类中取到vector
				Vector<Message> vector = PrivateChatEntity.map.get(friendUser);
				// 显示数据
				if (vector != null) {
					//动态删除控件
					//linearLayout.removeAllViews();
					
					for (Message message : vector) {
						String from = message.getFrom();
						String body = message.getBody();
						Log.i("PrivateChat", from + ":" + body);
						View view=null;
						//判断这个message是我说的还是好友说的
						if (from.equals(TApplication.currentUser))
						{
							//我说的
							view=View.inflate(context, R.layout.right, null);
						}else
						{
							//好友发过来
							view=View.inflate(context, R.layout.left, null);
						}
						
						//显示文本
						TextView tvText=(TextView) view.findViewById(R.id.tv_text);
						tvText.setText(body);
						
						//把right.xml放到linearLayout
						linearLayout.addView(view);
						
						//message已经显示在屏幕上，防止再次显示
						vector.remove(message);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
