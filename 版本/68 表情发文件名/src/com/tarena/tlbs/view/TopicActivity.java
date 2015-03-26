package com.tarena.tlbs.view;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.TopicAdapter;
import com.tarena.tlbs.biz.implAsmack.MapBiz;
import com.tarena.tlbs.model.TopicEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.ExceptionUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class TopicActivity extends BaseActivity {
	Button btnShowMenu;
	Menu menu;
	TextView tvAddTopic;
	double longitude = 116.471499;
	double latitude = 39.882367;

	ListView listView;
	TopicAdapter topicAdapter;
	TopicReceiver topicReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.topic);
			setupView();
			addListener();
			menu = new Menu(this);
			// 先注册
			topicReceiver = new TopicReceiver();
			this.registerReceiver(topicReceiver, new IntentFilter(
					Const.ACTION_SHOW_TOPIC));

			// 联网从百度云服务器上查询数据
			MapBiz mapBiz = new MapBiz();
			mapBiz.getAllData(longitude, latitude);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(topicReceiver);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	private void addListener() {
		tvAddTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					startActivity(new Intent(TopicActivity.this,
							CreateTopicActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

		btnShowMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				menu.showMenu();
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		btnShowMenu = (Button) findViewById(R.id.btn_topic_showMenu);
		tvAddTopic = (TextView) findViewById(R.id.tv_topic_addTopic);
		listView = (ListView) findViewById(R.id.lv_topic);
	}

	class TopicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				ArrayList<TopicEntity> list = (ArrayList<TopicEntity>) intent
						.getSerializableExtra(Const.KEY_DATA);
				//做成全局，在NearbyTopicActivity中也可以访问。
				TApplication.listTopic=list;
				
				topicAdapter = new TopicAdapter(context, list);
				listView.setAdapter(topicAdapter);
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		}
	}
}
