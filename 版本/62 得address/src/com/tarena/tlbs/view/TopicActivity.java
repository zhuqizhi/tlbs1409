package com.tarena.tlbs.view;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TopicActivity extends BaseActivity {
	Button btnShowMenu;
	Menu menu;
	TextView tvAddTopic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.topic);
			setupView();
			addListener();
			menu = new Menu(this);

		} catch (Exception e) {
			// TODO: handle exception
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
	}

}
