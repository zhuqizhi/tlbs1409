package com.tarena.tlbs.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.implAsmack.UploadImageBiz;
import com.tarena.tlbs.util.Const;
import com.tarena.tlbs.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
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
	String imageAddress;
	CreateTopicReceiver createTopicReceiver;

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

					// 调biz
					UploadImageBiz uploadImageBiz = new UploadImageBiz();
					uploadImageBiz.uploadImage(byteArrayInputStream);
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
	}

	class CreateTopicReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Const.ACTION_GET_IMAGE_ADDRESS.equals(action)) {
				imageAddress = intent.getStringExtra(Const.KEY_DATA);
				LogUtil.i("CreateTopicReceiver", imageAddress);
			}

		}

	}
}
