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

	// 录音
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
				// 得到选中的图
				Uri imageUri = data.getData();
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(
						getContentResolver(), imageUri);

				// 图的数据放到输出流
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				// 1：格式
				// 2:质量，最大值是100,质量好
				// 4:图压缩到输出流
				bitmap.compress(CompressFormat.PNG, 30, outputStream);

				// 再从输出流中得到byte[]
				byte[] imageData = outputStream.toByteArray();

				// 调ChatUtil.addImageTag()
				String body = ChatUtil.addImageTag(imageData);

				// 发送
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
					// 显示dialog
					alertDialog = new AlertDialog.Builder(
							PrivateChatActivity.this).create();
					View view = View.inflate(PrivateChatActivity.this,
							R.layout.recorder, null);
					alertDialog.setView(view);
					alertDialog.show();

					// dialog有textView
					final TextView tvState = (TextView) view
							.findViewById(R.id.tv_recorder_state);

					// dialog上的button
					final Button btnStart = (Button) view
							.findViewById(R.id.btn_recorder_start);
					btnStart.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							try {
								int action = event.getAction();
								switch (action) {
								case MotionEvent.ACTION_DOWN:// 按下
									tvState.setText("正在录音");
									mediaRecorder = new MediaRecorder();
									// 设置数据源
									mediaRecorder
											.setAudioSource(MediaRecorder.AudioSource.MIC);
									// 设置数据格式
									mediaRecorder
											.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
									mediaRecorder
											.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
									// 设置录音保存的文件
									mediaRecorder.setOutputFile(ChatUtil
											.getAudioFile().getAbsolutePath());

									mediaRecorder.prepare();
									mediaRecorder.start();

									break;

								case MotionEvent.ACTION_UP:// 松开
									alertDialog.dismiss();
									alertDialog=null;
									
									mediaRecorder.stop();
									mediaRecorder.reset();
									mediaRecorder.release();
									mediaRecorder = null;
									// 发送语音
									String body = ChatUtil.addAudioTag();
									sendMessage(body);
									break;
								case MotionEvent.ACTION_MOVE:// 移动

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
						// 打开系统图库
						Intent intent = new Intent(Intent.ACTION_PICK,
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, 0);
					} else {
						// 读sdcard;
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

		// 表情
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
				// 根据好友的user从实体类中取到vector
				Vector<Message> vector = PrivateChatEntity.map.get(friendUser);
				// 显示数据
				if (vector != null) {
					// 动态删除控件
					// linearLayout.removeAllViews();

					for (Message message : vector) {
						String from = message.getFrom();
						String body = message.getBody();
						Log.i("PrivateChat", from + ":" + body);
						View view = null;
						// 判断这个message是我说的还是好友说的
						if (from.equals(TApplication.currentUser)) {
							// 我说的
							view = View.inflate(context, R.layout.right, null);
						} else {
							// 好友发过来
							view = View.inflate(context, R.layout.left, null);
						}
						// 得数据的类型
						int type = ChatUtil.getType(body);
						switch (type) {
						case ChatUtil.TYPE_AUDIO:
							// 显示播放Imageview
							final ImageView ivPlay = (ImageView) view
									.findViewById(R.id.iv_audio);
							ivPlay.setVisibility(View.VISIBLE);
							// 把数据保存到ivPlay
							ivPlay.setTag(body);

							// 单击播放imageView,进行播放
							ivPlay.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										// 得到数据
										String body = (String) ivPlay.getTag();
										// 播放
										ChatUtil.getAudio(body);
										
										MediaPlayer mediaPlayer = new MediaPlayer();
										// 数据要放到文件中
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
							// 找到GifimageView
							GifImageView gifImageView = (GifImageView) view
									.findViewById(R.id.gif_face);
							gifImageView.setVisibility(View.VISIBLE);
							// 得表情的id
							int faceId = ChatUtil.getFaceId(body);

							gifImageView.setImageResource(faceId);
							break;
						case ChatUtil.TYPE_TEXT:
							// 找到TextView
							// 显示文本
							TextView tvText = (TextView) view
									.findViewById(R.id.tv_text);
							tvText.setVisibility(View.VISIBLE);
							tvText.setText(body);
							break;

						}

						// 把right.xml放到linearLayout
						linearLayout.addView(view);

						// message已经显示在屏幕上，防止再次显示
						vector.remove(message);
						long threadId = Thread.currentThread().getId();

						// 向上移
						handler.post(new Runnable() {

							@Override
							public void run() {
								Log.i("向上移", "2");
								long threadId = Thread.currentThread().getId();

								// TODO Auto-generated method stub
								try {
									// 向上移
									int scrollviewHeight = scrollView
											.getHeight();
									// 能得到，最后添加（刚刚）进去的textView
									// 解决方法，得高度向后退
									int linearLayoutHeight = linearLayout
											.getHeight();
									Log.i("向上移", scrollviewHeight + ","
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

			Log.i("向上移", "3");

		}

	}
}
