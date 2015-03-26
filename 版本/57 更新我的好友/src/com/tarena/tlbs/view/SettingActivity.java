package com.tarena.tlbs.view;

import java.io.File;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tarena.tlbs.R;
import com.tarena.tlbs.TApplication;
import com.tarena.tlbs.biz.implAsmack.UpdateBiz;
import com.tarena.tlbs.model.UpdateEntity;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {
	ImageView ivShowMenu;
	Menu menu;
	Button btnExit, btnUpdate,btnAbout;
	public final static int MSG_TYPE_SHOW_DIALOG = 1;
	public final static int MSG_TYPE_INSTALL_APK = 2;
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ȡ��Ϣ����
			int what = msg.what;
			Bundle bundle = msg.getData();
			switch (what) {
			case MSG_TYPE_SHOW_DIALOG:

				// ȡUpdateEntity

				UpdateEntity entity = (UpdateEntity) bundle
						.getSerializable(Const.KEY_DATA);
				// �жϰ汾
				String localVersion = Tools.getVersion(SettingActivity.this);
				if (Double.parseDouble(entity.getVersion()) > Double
						.parseDouble(localVersion)) {// ���°汾
					showDialog(entity);
				} else {
					// ���°汾
					Toast.makeText(SettingActivity.this, "��İ汾�����µ�",
							Toast.LENGTH_LONG).show();
				}

				break;
			case MSG_TYPE_INSTALL_APK:
				// ����Ϣ�еõ�apk��λ��
				String apkPathName = bundle.getString(Const.KEY_DATA);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				File apkFile = new File(apkPathName);
				// type��ʾ���ݵ�����

				// MIME(Multipurpose Internet Mail Extensions)
				// ����;�������ʼ���չ���͡�
				// ���趨ĳ����չ�����ļ���һ��Ӧ�ó������򿪵ķ�ʽ���ͣ�
				// ������չ���ļ������ʵ�ʱ����������Զ�ʹ��ָ��Ӧ�ó�������
				// mime:contentType�ļ�����
				// .html: text/html
				// .xml :text/xml
				intent.setDataAndType(Uri.fromFile(apkFile),
						"application/vnd.android.package-archive");
				startActivity(intent);
				break;

			}
		}

		private void showDialog(final UpdateEntity entity) {
			// show dialog
			AlertDialog.Builder dialog = new Builder(SettingActivity.this);
			dialog.setMessage("���°汾:" + entity.getVersion() + "\n"
					+ entity.getChangeLog());
			// ȡ����ť
			dialog.setNegativeButton("ȡ��", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			// ������ť
			dialog.setPositiveButton("����", new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					UpdateBiz biz = new UpdateBiz();
					biz.downloadApk(SettingActivity.this.handler,
							entity.getApkUrl());
				}
			});
			dialog.show();

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			TApplication.listActivity.add(this);

			setContentView(R.layout.setting);
			setupView();
			addListener();
			menu = new Menu(this);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addListener() {
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					UpdateBiz biz = new UpdateBiz();
					biz.getNewVersionInfo(handler);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		btnExit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					TApplication.instance.exit();
				} catch (Exception e) {
					// TODO: handle exception
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
		btnAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					startActivity(new Intent(SettingActivity.this, AboutActivity.class));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		ivShowMenu = (ImageView) findViewById(R.id.iv_topic_showMenu);
		btnExit = (Button) findViewById(R.id.btn_setting_exit);
		btnUpdate = (Button) findViewById(R.id.btn_setting_update);
		btnAbout = (Button) findViewById(R.id.btn_setting_about);
	}

}
