package com.tarena.tlbs.view;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.model.TopicEntity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class NearbyTopicActivity extends BaseActivity {
	ImageView ivShowMenu;
	Menu menu;
	MapView mapView;
	int intLongitude = 116570959;
	int intLatitude = 3999628;
	MyOverLay myOverLay;
	TextView tvUsername,tvBody,tvAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.nearby_topic);
			setupView();
			addListener();
			menu = new Menu(this);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		ivShowMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					menu.showMenu();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
tvUsername=(TextView) findViewById(R.id.tv_nearby_topic_username);
tvBody=(TextView) findViewById(R.id.tv_nearby_topic_body);
tvAddress=(TextView) findViewById(R.id.tv_nearby_topic_address);
		ivShowMenu = (ImageView) findViewById(R.id.iv_topic_showMenu);
		mapView = (MapView) findViewById(R.id.mapView_nearbyTopic);

		mapView.getController().setZoom(14);
		mapView.setBuiltInZoomControls(true);
		// 移动地图中心点
		mapView.getController().animateTo(
				new GeoPoint(intLatitude, intLongitude));
		// 要在地图上显示多个话题。
		Drawable drawable = getResources().getDrawable(
				R.drawable.map_overlay_red);
		myOverLay = new MyOverLay(drawable, mapView);
		mapView.getOverlays().add(myOverLay);
		for (int i = 0; i < TApplication.listTopic.size(); i++) {
			TopicEntity topicEntity = TApplication.listTopic.get(i);
			double douLongitude = topicEntity.getLongitude();
			int intLongitude = (int) (douLongitude * 1000000);

			double douLatitude = topicEntity.getLatitude();
			int intLatitude = (int) (douLatitude * 1000000);

			GeoPoint geoPoint = new GeoPoint(intLatitude, intLongitude);
			OverlayItem item = new OverlayItem(geoPoint, topicEntity.getBody(),
					topicEntity.getBody());
			item.setMarker(drawable);
			myOverLay.addItem(item);
		}
		mapView.refresh();

	}

	class MyOverLay extends ItemizedOverlay {

		public MyOverLay(Drawable arg0, MapView arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			// TODO Auto-generated method stub
			return super.onTap(arg0, arg1);
		}
		@Override
		protected boolean onTap(int itemIndex) {
			// TODO Auto-generated method stub
			TopicEntity topicEntity=TApplication.listTopic.get(itemIndex);
			tvUsername.setText(topicEntity.getUsername());
			tvBody.setText(topicEntity.getBody());
			tvAddress.setText(topicEntity.getLocationAddress());
			
			return super.onTap(itemIndex);
		}
	}
}
