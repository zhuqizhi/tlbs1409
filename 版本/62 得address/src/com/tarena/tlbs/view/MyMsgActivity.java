package com.tarena.tlbs.view;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.adapter.MyMsgAdapter;
import com.tarena.tlbs.util.Const;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

public class MyMsgActivity extends Activity {
	ImageView ivShowMenu;
	Menu menu;
	ListView listView;
	MyMsgAdapter msgAdapter;
	updateListViewReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.my_msg);
			setupView();
			addListener();
			menu = new Menu(this);
			receiver=new updateListViewReceiver();
			this.registerReceiver(receiver, new IntentFilter(Const.ACTION_UPDATE_MSG));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	try {
		this.unregisterReceiver(receiver);
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
		// TODO Auto-generated method stub
		ivShowMenu = (ImageView) findViewById(R.id.iv_topic_showMenu);
		listView = (ListView) findViewById(R.id.lv_my_msg);

		msgAdapter = new MyMsgAdapter(this, TApplication.listMessage);
		listView.setAdapter(msgAdapter);
	}
class updateListViewReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		msgAdapter.updateData(TApplication.listMessage);
	}
	
}
}
