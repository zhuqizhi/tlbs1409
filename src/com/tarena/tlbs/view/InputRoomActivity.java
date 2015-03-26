package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.implAsmack.GroupChatBiz;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class InputRoomActivity extends BaseActivity {
	EditText etRoomName, etName;
	TextView tvSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.input_room);
			setupView();
			addListener();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void addListener() {
		// TODO Auto-generated method stub
		tvSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String roomName = etRoomName.getText().toString();
					String name = etName.getText().toString();
					GroupChatBiz biz = new GroupChatBiz();
					biz.join(InputRoomActivity.this,roomName, name);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		etRoomName = (EditText) findViewById(R.id.et_input_room_roomName);
		etName = (EditText) findViewById(R.id.et_input_room_name);
		tvSubmit = (TextView) findViewById(R.id.tv_input_room_submit);
	}

}
