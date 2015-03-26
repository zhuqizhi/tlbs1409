package com.tarena.tlbs.view;

import java.util.ArrayList;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.MyFreindAdapter;
import com.tarena.tlbs.util.Const;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFriendActivity extends Activity {
	ImageView ivShowMenu;
	Menu menu;
	TextView tvAddFriend;

	ExpandableListView expandableListView;
	MyFreindAdapter myFreindAdapter;

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
			e.printStackTrace();
		}
	}

	private void addListener() {
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				try {
					RosterEntry rosterEntry=(RosterEntry) myFreindAdapter.getChild(groupPosition, childPosition);
					String friendUser=rosterEntry.getUser();
					
					Intent intent=new Intent(MyFriendActivity.this, PrivateChatActivity.class);
					intent.putExtra(Const.KEY_DATA, friendUser);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		tvAddFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					startActivity(new Intent(MyFriendActivity.this,
							AddFriendActivity.class));
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
		tvAddFriend = (TextView) findViewById(R.id.tv_my_friend_addFriend);

		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
		// 得好友分组数据
		Roster roster = TApplication.xmppConnection.getRoster();

		ArrayList<RosterGroup> listGroup = new ArrayList<RosterGroup>(
				roster.getGroups());

		myFreindAdapter = new MyFreindAdapter(this, listGroup);
		expandableListView.setAdapter(myFreindAdapter);
	}

}
