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
import com.tarena.tlbs.util.ExceptionUtil;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyFriendActivity extends Activity {
	ImageView ivShowMenu;
	Menu menu;
	TextView tvAddFriend;

	ExpandableListView expandableListView;
	MyFreindAdapter myFreindAdapter;

	PopupWindow popupWindow;
	RelativeLayout title;
	UpdateMyFriend updateMyFriend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.my_friend);
			setupView();
			addListener();
			menu = new Menu(this);
			updateMyFriend = new UpdateMyFriend();
			this.registerReceiver(updateMyFriend, new IntentFilter(
					Const.ACTION_UPDATE_MY_FRIEND));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			this.unregisterReceiver(updateMyFriend);
		} catch (Exception e) {
			// TODO: handle exception
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		// dialog
		// 在主界面，按返回键显示toast("再按一次退出");
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			// 我们来处理
			if (popupWindow != null && popupWindow.isShowing()) {
				popupWindow.dismiss();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	private void addListener() {
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				try {
					RosterEntry rosterEntry = (RosterEntry) myFreindAdapter
							.getChild(groupPosition, childPosition);
					String friendUser = rosterEntry.getUser();

					Intent intent = new Intent(MyFriendActivity.this,
							PrivateChatActivity.class);
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
					// 显示一个新的布局
					View view = View.inflate(MyFriendActivity.this,
							R.layout.my_friend_more, null);
					popupWindow = new PopupWindow(view,
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);

					// 相对于某个控件(title)来显示
					popupWindow.showAtLocation(title, Gravity.RIGHT
							| Gravity.TOP, 0, title.getHeight() + 100);

					Button btnAddFriend = (Button) view
							.findViewById(R.id.button_my_friend_more_add);
					btnAddFriend.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							try {
								startActivity(new Intent(MyFriendActivity.this,
										AddFriendActivity.class));

								popupWindow.dismiss();
							} catch (Exception e) {
								ExceptionUtil.handleException(e);
							}

						}
					});

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

		title = (RelativeLayout) findViewById(R.id.title);
	}

	class UpdateMyFriend extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				ArrayList<RosterGroup> list = new ArrayList<RosterGroup>(
						TApplication.xmppConnection.getRoster().getGroups());

				myFreindAdapter.updateData(list);
			} catch (Exception e) {
				// TODO: handle exception
				ExceptionUtil.handleException(e);
			}

		}
	}
}
