package com.tarena.tlbs.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Vector;

import org.fengwx.gif.GifImageView;
import org.jivesoftware.smack.packet.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.FaceAdapter;
import com.tarena.tlbs.biz.implAsmack.PrivateChatBiz;
import com.tarena.tlbs.model.PrivateChatEntity;
import com.tarena.tlbs.util.ChatUtil;
import com.tarena.tlbs.util.Const;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
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

	MapView mapView;

	// ָ��һ��λ�ã�����õ�����4.9E-324,����ָ����λ�ꡣ
	double longitude = 116.471499;
	double latitude = 39.882367;

	// ��λ�õĿͻ���
	LocationClient locationClient;

	MyOverLay myOverLay;

	TextView tvMore;
	Button btnMap;
	LinearLayout linearLayoutMore;
	
	@Override
	public void onConfigurationChanged(
			Configuration newConfig) {
		// TODO Auto-generated method stub
		int newOrientiation=newConfig.orientation;
		super.onConfigurationChanged(newConfig);
	}

	// ������ĵ����ֻ���װһ����������꣬������
	// �����ֻ��ϵõ��������mapView��ʾ����ĵ��˵�λ�á�
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
		btnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					linearLayoutMore.setVisibility(View.GONE);
					mapView.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		tvMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (linearLayoutMore.getVisibility() == View.GONE) {
						linearLayoutMore.setVisibility(View.VISIBLE);
					} else if (linearLayoutMore.getVisibility() == View.VISIBLE) {
						linearLayoutMore.setVisibility(View.GONE);

					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					linearLayoutMore.setVisibility(View.GONE);

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
									alertDialog = null;

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
					linearLayoutMore.setVisibility(View.GONE);

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
					String faceFileName = (String) faceAdapter.getItem(position);
					String body = ChatUtil.addFaceTag(faceFileName);
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
					linearLayoutMore.setVisibility(View.GONE);

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
					if (mapView.getVisibility() == View.VISIBLE) {
						// �ÿ��ȥ��ʵ����MyMapViewListener
						mapView.getCurrentMap();
					} else {
						String body = etBody.getText().toString();
						sendMessage(body);
						etBody.getText().clear();
					}

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

		mapView = (MapView) findViewById(R.id.mapview_private_chat);
		// ��ʾ�Ŵ���С��ť
		mapView.setBuiltInZoomControls(true);

		// ���õ�ͼ��ʾ����4-19
		mapView.getController().setZoom(19);

		// ��λ���
		locationClient = new LocationClient(this);
		// �ðٶȵ�ͼ��������еĽӿ�ָ��ʵ����
		MyLocationListener myLocationListener = new MyLocationListener();
		locationClient.registerLocationListener(myLocationListener);
		// ����client������
		LocationClientOption option = new LocationClientOption();
		// ��gps
		option.setOpenGps(true);
		// ������������ bd09ll,������������ǰٶ��Լ����塣
		option.setCoorType("bd09ll");
		// ���ø��೤ʱ���һ������
		// ʱ������1000,��ʾֻ��һ��
		option.setScanSpan(1);

		locationClient.setLocOption(option);

		locationClient.start();

		// ���ͼ���ͼƬ
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.map_overlay_blue);
		myOverLay = new MyOverLay(drawable, mapView);

		// myOverLay�ŵ�mapView��
		mapView.getOverlays().add(myOverLay);

		// �ðٶȵ�ͼ����еĽӿ�ָ��ʵ����
		MyMapViewListener mapViewListener = new MyMapViewListener();
		mapView.regMapViewListener(TApplication.bMapManager, mapViewListener);

		tvMore = (TextView) findViewById(R.id.tv_private_chat_more);
		btnMap = (Button) findViewById(R.id.btn_private_chat_map);
		linearLayoutMore = (LinearLayout) findViewById(R.id.linearLayoutMore);

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
						case ChatUtil.TYPE_MAP:

							// ������mapView
							ImageView ivMap = (ImageView) view
									.findViewById(R.id.iv_map);
							ivMap.setVisibility(View.VISIBLE);
							Bitmap bitmap2 = ChatUtil.getMap(body);
							ivMap.setImageBitmap(bitmap2);
							break;
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

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			// ������4.9E-324����Ϊ��λʧ��
			if (bdLocation.getLongitude() != 4.9E-324) {// ȡ������ȷ����
				longitude = bdLocation.getLongitude();
				latitude = bdLocation.getLatitude();
			}

			// �õ�ͼ���ĵ��ǵ�ǰλ��
			// geoPoint �������һ�����ꡣ
			GeoPoint geoPoint = new GeoPoint((int) (latitude * 1000000),
					(int) (longitude * 1000000));
			// animateTo�Զ����ķ�ʽ�ƶ���geoPoint
			mapView.getController().animateTo(geoPoint);
			Log.i("MyLocationListener", "longitude=" + longitude + ",latitude="
					+ latitude);

			// �ڵ�ǰλ����ʾһ��ͼ blue.png
			// OverlayItem�൱��listView�е�һ��
			OverlayItem overlayItem = new OverlayItem(geoPoint, "��ǰλ��", "��ǰλ��");
			// myOverLay �൱��listView
			myOverLay.addItem(overlayItem);
			// listview ,adapter.notifyDataSetChanged
			mapView.refresh();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * �ڵ�ͼ����ӵ�ͼ�ļ���
	 * 
	 * @author tarena
	 * 
	 */
	class MyOverLay extends ItemizedOverlay {

		public MyOverLay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}

		/**
		 * �ڵ�ͼ�ϵ���
		 */
		@Override
		public boolean onTap(GeoPoint geoPoint, MapView mapView) {
			// �����ǵ�������������ʾͼƬ
			try {
				// ������ǰ�ӵ�ͼ
				myOverLay.removeAll();

				OverlayItem overlayItem = new OverlayItem(geoPoint, "ѡ�е�λ��",
						"ѡ�е�λ��");
				myOverLay.addItem(overlayItem);
				mapView.refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return super.onTap(geoPoint, mapView);
		}

	}

	class MyMapViewListener implements MKMapViewListener {

		@Override
		public void onClickMapPoi(MapPoi arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * �ٶȵ�ͼ��ܵ�
		 */
		@Override
		public void onGetCurrentMap(Bitmap bitmap) {
			try {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 30, outputStream);

				byte[] data = outputStream.toByteArray();
				String body = ChatUtil.addMapTag(data);
				sendMessage(body);
				mapView.setVisibility(View.GONE);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onMapAnimationFinish() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMapLoadFinish() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMapMoveFinish() {
			// TODO Auto-generated method stub

		}

	}
}
