package com.tarena.tlbs.view;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu {
	Activity activity;
	SlidingMenu slidingMenu;
	Button btnAllTopic, btnNearbyTopic, btnMyTopic, btnMyFriend, btnMyMsg,
			btnMyRes, btnSetting;

	public Menu(Activity activity) {
		super();
		this.activity = activity;
		createSlidingMenu(activity);
		setupView();
		addListener();
	}

	private void addListener() {

		btnAllTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, TopicActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnNearbyTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity,
							NearbyTopicActivity.class);
					// ���������ǲ�һ����
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnMyTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, TopicActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnMyFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, MyFriendActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnMyMsg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, MyMsgActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnMyRes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, MyResActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(activity, SettingActivity.class);
					activity.startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {

		btnAllTopic = (Button) activity
				.findViewById(R.id.btn_leftmenu_allTopic);
		btnNearbyTopic = (Button) activity
				.findViewById(R.id.btn_leftmenu_nearbyTopic);
		btnMyTopic = (Button) activity.findViewById(R.id.btn_leftmenu_my_topic);
		btnMyFriend = (Button) activity
				.findViewById(R.id.btn_leftmenu_my_friend);
		btnMyMsg = (Button) activity.findViewById(R.id.btn_leftmenu_my_msg);
		btnMyRes = (Button) activity.findViewById(R.id.btn_leftmenu_my_res);
		btnSetting = (Button) activity.findViewById(R.id.btn_leftmenu_setting);
	}

	private void createSlidingMenu(Activity activity) {
		slidingMenu = new SlidingMenu(activity);
		// ���ü���leftmenu.xml
		slidingMenu.setMenu(R.layout.leftmenu);
		// ������ʾ���Ǹ�activity��
		slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
		// ����behindOffset

		int screenWidth = activity.getWindowManager().getDefaultDisplay()
				.getWidth();

		slidingMenu.setBehindOffset(screenWidth / 2);
	}

	public void showMenu() {
		slidingMenu.showMenu();
	}

}
