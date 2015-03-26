package com.tarena.tlbs.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import com.tarena.tlbs.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyMsgAdapter extends BaseAdapter {
	Context context;
	ArrayList<Object> list;

	public MyMsgAdapter(Context context, ArrayList<Object> list) {
		super();
		this.context = context;
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<Object>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/**
	 * listView�Ѿ������ݣ�������ʾ�ˣ�
	 * �Ժ������и��£�Ҫ����listView
	 * @param list
	 */
	public void updateData(ArrayList<Object> list)
	{
		this.list=list;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.my_msg_item, null);

			viewHolder = new ViewHolder();
			viewHolder.linearLayoutAddFriendResult = (LinearLayout) convertView
					.findViewById(R.id.linearLayout_my_msg_item_addFriend);
			viewHolder.linearLayoutChatContent = (LinearLayout) convertView
					.findViewById(R.id.linearLayout_my_msg_item_privateChat);
			viewHolder.tvAddFrinedResult = (TextView) convertView
					.findViewById(R.id.tv_my_msg_item_addFriendResult);
			viewHolder.tvChatContent = (TextView) convertView
					.findViewById(R.id.tv_my_msg_item_chatContent);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// ȡ����
        Object data=list.get(position);		
		// ���ؼ�����ֵ
        //�ҵ���Ϣ���������Ͳ�һ��
        //��ͬ��������ʾ��ͬ�Ŀؼ�
        //�ж����ݵ�����
        if (data instanceof Presence)
        {
        	//��ʾ��Ӧ�Ŀؼ�
        	viewHolder.linearLayoutAddFriendResult.setVisibility(View.VISIBLE);
        	
        	//����һ����Ӻ��ѽ��������
        	Presence presence=(Presence) data;
        	Type type=presence.getType();
        	String text="";
        	String friendUser=presence.getFrom();
        	if (type==Type.unsubscribe)
        	{
        		text=friendUser+"��ͬ��";
        	}
        	if (type==Type.subscribe)
        	{
        		text=friendUser+"ͬ��";
        	}
        	viewHolder.tvAddFrinedResult.setText(text);
        }
        
        if (data instanceof Message)
        {
        	//����һ����������ݣ���ʾ��Ӧ�Ŀؼ�
        }

		return convertView;
	}

	class ViewHolder {
		LinearLayout linearLayoutAddFriendResult, linearLayoutChatContent;
		TextView tvAddFrinedResult, tvChatContent;
	}

}
