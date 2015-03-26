package com.tarena.tlbs.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.fengwx.gif.GifImageView;
import org.jivesoftware.smack.packet.Message;

import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.FaceAdapter;
import com.tarena.tlbs.biz.PrivateChatBiz;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.ChatUtil;
import com.tarena.tlbs.util.Const;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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

	Button btnImage;
	Button btnAudio;
	AlertDialog alertDialog;

	// ¼��
	MediaRecorder mediaRecorder;

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == this.RESULT_OK) {
				// �õ�ѡ�е�ͼ
				Uri imageUri = data.getData();
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(
						getContentResolver(), imageUri);

				// ͼ�����ݷŵ������
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				// 1����ʽ
				// 2:���������ֵ��100,������
				// 4:ͼѹ���������
				bitmap.compress(CompressFormat.PNG, 30, outputStream);

				// �ٴ�������еõ�byte[]
				byte[] imageData = outputStream.toByteArray();

				// ��ChatUtil.addImageTag()
				String body = ChatUtil.addImageTag(imageData);

				// ����
				sendMessage(body);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addListener() {
		btnAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// ��ʾdialog
					alertDialog = new AlertDialog.Builder(
							PrivateChatActivity.this).create();
					View view = View.inflate(PrivateChatActivity.this,
							R.layout.recorder, null);
					alertDialog.setView(view);
					alertDialog.show();

					// dialog��textView
					final TextView tvState = (TextView) view
							.findViewById(R.id.tv_recorder_state);

					// dialog�ϵ�button
					final Button btnStart = (Button) view
							.findViewById(R.id.btn_recorder_start);
					btnStart.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							try {
								int action = event.getAction();
								switch (action) {
								case MotionEvent.ACTION_DOWN:// ����
									tvState.setText("����¼��");
									mediaRecorder = new MediaRecorder();
									// ��������Դ
									mediaRecorder
											.setAudioSource(MediaRecorder.AudioSource.MIC);
									// �������ݸ�ʽ
									mediaRecorder
											.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
									mediaRecorder
											.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
									// ����¼��������ļ�
									mediaRecorder.setOutputFile(ChatUtil
											.getAudioFile().getAbsolutePath());

									mediaRecorder.prepare();
									mediaRecorder.start();

									break;

								case MotionEvent.ACTION_UP:// �ɿ�
									alertDialog.dismiss();
									alertDialog=null;
									
									mediaRecorder.stop();
									mediaRecorder.reset();
									mediaRecorder.release();
									mediaRecorder = null;
									// ��������
									String body = ChatUtil.addAudioTag();
									sendMessage(body);
									break;
								case MotionEvent.ACTION_MOVE:// �ƶ�

									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							return false;
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		btnImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					boolean isUseMobile = false;
					if (isUseMobile) {
						// ��ϵͳͼ��
						Intent intent = new Intent(Intent.ACTION_PICK,
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, 0);
					} else {
						// ��sdcard;
						byte[] imageData = ChatUtil.getSdcardImage();
						String body = ChatUtil.addImageTag(imageData);
						sendMessage(body);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					int faceId = (Integer) faceAdapter.getItem(position);
					String body = ChatUtil.addFaceTag(faceId);
					sendMessage(body);
					gridView.setVisibility(View.GONE);

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

		btnImage = (Button) findViewById(R.id.btn_private_chat_image);
		btnAudio = (Button) findViewById(R.id.btn_private_chat_audio);

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
						// �����ݵ�����
						int type = ChatUtil.getType(body);
						switch (type) {
						case ChatUtil.TYPE_AUDIO:
							// ��ʾ����Imageview
							final ImageView ivPlay = (ImageView) view
									.findViewById(R.id.iv_audio);
							ivPlay.setVisibility(View.VISIBLE);
							// �����ݱ��浽ivPlay
							ivPlay.setTag(body);

							// ��������imageView,���в���
							ivPlay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										// �õ�����
										String body = (String) ivPlay.getTag();
										// ����
										ChatUtil.getAudio(body);
										
										MediaPlayer mediaPlayer = new MediaPlayer();
										// ����Ҫ�ŵ��ļ���
										// mnt/sdcard/tlbs1409/audio/1.mp3;
										String filePath = ChatUtil
												.getAudioFile()
												.getAbsolutePath();
										mediaPlayer.setDataSource(filePath);
										mediaPlayer.prepare();
										mediaPlayer.start();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							break;
						case ChatUtil.TYPE_IMAGE:
							Bitmap bitmap = ChatUtil.getImage(body);
							ImageView imageView = (ImageView) view
									.findViewById(R.id.iv_Image);
							imageView.setVisibility(View.VISIBLE);
							imageView.setImageBitmap(bitmap);

							break;
						case ChatUtil.TYPE_FACE:
							// �ҵ�GifimageView
							GifImageView gifImageView = (GifImageView) view
									.findViewById(R.id.gif_face);
							gifImageView.setVisibility(View.VISIBLE);
							// �ñ����id
							int faceId = ChatUtil.getFaceId(body);

							gifImageView.setImageResource(faceId);
							break;
						case ChatUtil.TYPE_TEXT:
							// �ҵ�TextView
							// ��ʾ�ı�
							TextView tvText = (TextView) view
									.findViewById(R.id.tv_text);
							tvText.setVisibility(View.VISIBLE);
							tvText.setText(body);
							break;

						}

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
