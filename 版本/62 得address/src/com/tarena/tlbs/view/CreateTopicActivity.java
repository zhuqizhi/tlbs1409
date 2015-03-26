package com.tarena.tlbs.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.implAsmack.MapBiz;
import com.tarena.tlbs.biz.implAsmack.UploadImageBiz;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class CreateTopicActivity extends BaseActivity {
	ImageView ivOpenImageLibrary;
	TextView tvSubmit;
	Bitmap bitmap;
	String imageAddress,locationAddress;
	CreateTopicReceiver createTopicReceiver;
	public double longitude = 112.222222;
	public double latitude = 35.666666;
	LocationClient locationClient;
	MapView mapView;
	MyOverLay myOverLay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.create_topic);
			setupView();
			addListener();
			createTopicReceiver = new CreateTopicReceiver();
			this.registerReceiver(createTopicReceiver, new IntentFilter(
					Const.ACTION_GET_IMAGE_ADDRESS));
			this.registerReceiver(createTopicReceiver, new IntentFilter(
					Const.ACTION_GET_LOCATION_ADDRESS));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(createTopicReceiver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		try {
			if (resultCode == this.RESULT_OK) {
				Uri uri = data.getData();
				bitmap = MediaStore.Images.Media.getBitmap(
						getContentResolver(), uri);
				ivOpenImageLibrary.setImageBitmap(bitmap);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void addListener() {
		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// bitmap-->outputStream
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.PNG, 30,
							byteArrayOutputStream);
					// outputStream-->inputStream
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							byteArrayOutputStream.toByteArray());

					//1, 调biz上传图片到124
					UploadImageBiz uploadImageBiz = new UploadImageBiz();
					uploadImageBiz.uploadImage(byteArrayInputStream);
					
					//2,调MapBiz从百度服务器上查询地址
					MapBiz mapBiz=new MapBiz();
					mapBiz.queryAddress(latitude, longitude);
					//3,保存话题。
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		ivOpenImageLibrary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// 打开系统图库
					Intent intent = new Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 0);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

	}

	private void setupView() {
		// TODO Auto-generated method stub
		ivOpenImageLibrary = (ImageView) findViewById(R.id.iv_create_topic_openImageliberary);
		tvSubmit = (TextView) findViewById(R.id.tv_create_topic_submit);
		mapView = (MapView) findViewById(R.id.mapView);

		mapView.getController().setZoom(19);
		mapView.setBuiltInZoomControls(true);

		myOverLay = new MyOverLay(this.getResources().getDrawable(
				R.drawable.map_overlay_red), mapView);
		mapView.getOverlays().add(myOverLay);

		// 定位
		MyLocationListener myLocationListener = new MyLocationListener();
		locationClient = new LocationClient(this);
		LocationClientOption locationClientOption = new LocationClientOption();
		locationClientOption.setOpenGps(true);
		locationClientOption.setCoorType("bd09ll");
		locationClientOption.setScanSpan(1);

		locationClient.registerLocationListener(myLocationListener);
		locationClient.setLocOption(locationClientOption);
		locationClient.start();
	}

	class CreateTopicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Const.ACTION_GET_IMAGE_ADDRESS.equals(action)) {
				imageAddress = intent.getStringExtra(Const.KEY_DATA);
				LogUtil.i("CreateTopicReceiver", imageAddress);
			}else if (Const.ACTION_GET_LOCATION_ADDRESS.equals(action))
			{
				locationAddress = intent.getStringExtra(Const.KEY_DATA);
				LogUtil.i("CreateTopicReceiver", locationAddress);
			}
				

		}

	}

	class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			try {

				LogUtil.i("CreateTopicReceiver", "longitude=" + longitude);

				if (bdLocation.getLongitude() != 4.9E-324) {
					longitude = bdLocation.getLongitude();
					latitude = bdLocation.getLatitude();
				}
				// 移动地图中心点
				GeoPoint geoPoint = new GeoPoint((int) (latitude * 1000000),
						(int) (longitude * 1000000));
				mapView.getController().animateTo(geoPoint);
				// 显示图片
				OverlayItem item = new OverlayItem(geoPoint, "当前位置", "当前位置");
				myOverLay.addItem(item);
				// 通知地图更新
				mapView.refresh();

			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}

	}

	// 通过Myoverlay可以在地图上添加自己的图片
	class MyOverLay extends ItemizedOverlay {

		public MyOverLay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
		@Override
		public boolean onTap(GeoPoint geoPoint, MapView arg1) {
			try {
				//得到带小数点的坐标点
				int intLongitude=geoPoint.getLongitudeE6();
				longitude=((double)intLongitude)/1000000;
				
				int intLatitude=geoPoint.getLatitudeE6();
				latitude=((double)intLatitude)/1000000;
				
				mapView.getController().animateTo(geoPoint);
				
				myOverLay.removeAll();
				OverlayItem item=new OverlayItem(geoPoint, "", "");
				myOverLay.addItem(item);
				mapView.refresh();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return super.onTap(geoPoint, arg1);
		}

	}
}
