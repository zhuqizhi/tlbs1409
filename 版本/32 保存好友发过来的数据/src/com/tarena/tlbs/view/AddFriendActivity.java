package com.tarena.tlbs.view;

import com.tarena.tlbs.R;
import com.tarena.tlbs.biz.AddFriendBiz;
import com.tarena.tlbs.model.FriendEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class AddFriendActivity extends BaseActivity {
	EditText etFriendName, etName, etGroup;
	TextView tvSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.add_friend);
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
					String friendName = etFriendName.getText().toString();
					String name = etName.getText().toString();
					String group = etGroup.getText().toString();

					FriendEntity friendEntity = new FriendEntity(friendName,
							name, group);
					AddFriendBiz addFriendBiz = new AddFriendBiz();
					addFriendBiz.addFriend(friendEntity);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}

	private void setupView() {
		// TODO Auto-generated method stub
		etFriendName = (EditText) findViewById(R.id.et_add_friend_friendName);
		etName = (EditText) findViewById(R.id.et_add_friend_name);
		etGroup = (EditText) findViewById(R.id.et_add_friend_group);

		tvSubmit = (TextView) findViewById(R.id.tv_add_friend_submit);
	}
}
