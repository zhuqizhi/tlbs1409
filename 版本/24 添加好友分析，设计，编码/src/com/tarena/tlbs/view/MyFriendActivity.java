package com.tarena.tlbs.view;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFriendActivity extends Activity {
	ImageView ivShowMenu;
	Menu menu;
	TextView tvAddFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.my_friend);
			setupView();
			addListener();
			menu = new Menu(this);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addListener() {
		tvAddFriend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				try {
					startActivity(new Intent(MyFriendActivity.this,AddFriendActivity.class));
				} catch (Exception e) {
e.printStackTrace();
				}
			}
		});
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
		// TODO Auto-generated method stub
		ivShowMenu = (ImageView) findViewById(R.id.iv_topic_showMenu);
		tvAddFriend=(TextView) findViewById(R.id.tv_my_friend_addFriend);
	}

}
