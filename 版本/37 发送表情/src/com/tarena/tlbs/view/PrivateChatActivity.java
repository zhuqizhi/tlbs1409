package com.tarena.tlbs.view;

import java.util.Vector;

import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.FaceAdapter;
import com.tarena.tlbs.biz.PrivateChatBiz;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.ChatUtil;
import com.tarena.tlbs.util.Const;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
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
	Handler handler = new Handler();

	Button btnFace;
	GridView gridView;
	FaceAdapter faceAdapter;

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
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					int faceId = (Integer) faceAdapter.getItem(position);
					String body = ChatUtil.addFaceTag(faceId);
					sendMessage(body);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnFace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (gridView.getVisibility() == View.GONE) {
						gridView.setVisibility(View.VISIBLE);
					} else if (gridView.getVisibility() == View.VISIBLE) {
						gridView.setVisibility(View.GONE);

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String body = etBody.getText().toString();
					sendMessage(body);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}

	private void sendMessage(String body) {
		PrivateChatBiz biz = new PrivateChatBiz();
		biz.sendMessage(friendUser, body);
	}

	private void setupView() {
		// TODO Auto-generated method stub
		tvFriendName = (TextView) findViewById(R.id.tv_private_chat_friendName);
		etBody = (EditText) findViewById(R.id.et_private_chat_body);
		btnSend = (Button) findViewById(R.id.btn_private_chat_send);

		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutChatContent);

		// ����
		btnFace = (Button) findViewById(R.id.btn_private_chat_face);
		gridView = (GridView) findViewById(R.id.gridView_face);
		faceAdapter = new FaceAdapter(this);
		gridView.setAdapter(faceAdapter);

	}

	class ShowPrivateChatMessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				// ���ݺ��ѵ�user��ʵ������ȡ��vector
				Vector<Message> vector = PrivateChatEntity.map.get(friendUser);
				// ��ʾ����
				if (vector != null) {
					// ��̬ɾ���ؼ�
					// linearLayout.removeAllViews();

					for (Message message : vector) {
						String from = message.getFrom();
						String body = message.getBody();
						Log.i("PrivateChat", from + ":" + body);
						View view = null;
						// �ж����message����˵�Ļ��Ǻ���˵��
						if (from.equals(TApplication.currentUser)) {
							// ��˵��
							view = View.inflate(context, R.layout.right, null);
						} else {
							// ���ѷ�����
							view = View.inflate(context, R.layout.left, null);
						}

						// ��ʾ�ı�
						TextView tvText = (TextView) view
								.findViewById(R.id.tv_text);
						tvText.setText(body);

						// ��right.xml�ŵ�linearLayout
						linearLayout.addView(view);

						// message�Ѿ���ʾ����Ļ�ϣ���ֹ�ٴ���ʾ
						vector.remove(message);
						long threadId = Thread.currentThread().getId();

						// ������
						handler.post(new Runnable() {

							@Override
							public void run() {
								Log.i("������", "2");
								long threadId = Thread.currentThread().getId();

								// TODO Auto-generated method stub
								try {
									// ������
									int scrollviewHeight = scrollView
											.getHeight();
									// �ܵõ��������ӣ��ոգ���ȥ��textView
									// ����������ø߶������
									int linearLayoutHeight = linearLayout
											.getHeight();
									Log.i("������", scrollviewHeight + ","
											+ linearLayoutHeight);
									if (linearLayoutHeight > scrollviewHeight) {
										int y = linearLayoutHeight
												- scrollviewHeight;
										scrollView.scrollTo(0, y);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						});

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			Log.i("������", "3");

		}

	}
}
