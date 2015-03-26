package com.tarena.tlbs.view;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.implAsmack.GroupChatBiz;
import com.tarena.tlbs.model.GroupChatEntity;
import com.tarena.tlbs.util.ChatUtil;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;
import com.tarena.tlbs.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GroupChatActivity extends BaseActivity {
	EditText etBody;
	Button btnSubmit;
	ScrollView scrollView;
	LinearLayout linearLayout;
	Handler handler = new Handler();
	ShowGroupMessageReceiver showGroupMessageReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			this.setContentView(R.layout.group_chat);
			setupView();
			addListener();

			showGroupMessageReceiver = new ShowGroupMessageReceiver();
			this.registerReceiver(showGroupMessageReceiver, new IntentFilter(
					Const.ACTION_SHOW_GROUP_MESSAGE));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(showGroupMessageReceiver);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String body = etBody.getText().toString();
					etBody.getText().clear();

					GroupChatBiz groupChatBiz = new GroupChatBiz();
					groupChatBiz.sendMessage(body);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		etBody = (EditText) findViewById(R.id.et_group_chat_body);
		btnSubmit = (Button) findViewById(R.id.btn_group_chat_submit);
		scrollView = (ScrollView) findViewById(R.id.scrollView_group_chat);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout_group_chat);
	}

	class ShowGroupMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			try {
				Vector<Message> vector = GroupChatEntity.map
						.get(TApplication.groupChat.getRoom());
				if (vector != null) {
					for (Message message : vector) {

						String from = message.getFrom();
						String body = message.getBody();
						View view = null;
						if (from.equals(TApplication.currentUser)) {
							// 我说的
							view = View.inflate(context, R.layout.right, null);
						} else {
							// 别人说的
							view = View.inflate(context, R.layout.left, null);
						}
						linearLayout.addView(view);
						LogUtil.i("groupchat", "receiver 原始的"+body);

						//int type = ChatUtil.getType(body);
						//if (type == ChatUtil.TYPE_TEXT) {
							TextView tvBody = (TextView) view
									.findViewById(R.id.tv_text);
							tvBody.setVisibility(View.VISIBLE);
							LogUtil.i("groupchat", "receiver 要显示的"+body);

							tvBody.setText("test "+body);
						//}
						handler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int linearLayoutHeight = linearLayout
										.getHeight();
								int scrollViewHeight = scrollView.getHeight();
								if (linearLayoutHeight > scrollViewHeight) {
									scrollView.scrollTo(0, linearLayoutHeight
											- scrollViewHeight);
								}
							}
						});
						// 移走
						vector.remove(message);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
}
