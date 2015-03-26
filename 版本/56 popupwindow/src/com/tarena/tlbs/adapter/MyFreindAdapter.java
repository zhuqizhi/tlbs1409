package com.tarena.tlbs.adapter;

import java.util.ArrayList;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

import com.tarena.tlbs.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyFreindAdapter extends BaseExpandableListAdapter{
Context context;
//RosterGroup�����Ǻ��ѷ�����1409,tarena
//һ���������ж������RosterEntry
ArrayList<RosterGroup> listGroup;


	public MyFreindAdapter(Context context, ArrayList<RosterGroup> listGroup) {
	super();
	this.context = context;
	if (listGroup!=null)
	{
	this.listGroup = listGroup;
	}else
	{
		this.listGroup=new ArrayList<RosterGroup>();
	}
}

	//�ж��ٸ���
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listGroup.size();
	}

	//һ���������ж��ٸ�����
	@Override
	public int getChildrenCount
	(int groupPosition) {
		RosterGroup rosterGroup=listGroup.get(groupPosition);
		
		return rosterGroup.getEntryCount();
	}
/**
 * 
 * @param groupPosition
 * @return rosterGroup
 */
	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return listGroup.get(groupPosition);
	}

	//����ѡ�еķ��飬ѡ�еĺ���
	@Override
	public Object getChild(
			int groupPosition,
			int childPosition) {
		// TODO Auto-generated method stub
		//1,�õ�ѡ�еķ���
		RosterGroup rosterGroup=listGroup.get(groupPosition);
		//2,�õ�����
		//rosterGroup.getEntries()���ص���һ�����������к���
		ArrayList<RosterEntry> listFriend=
				new ArrayList<RosterEntry>
		(rosterGroup.getEntries());
		
		return listFriend.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}
//Stable �̶�
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;//��ʾ����û�й̶���id
	}

	/**
	 * ��ʾ���ѷ������my_friend_group.xml
	 */
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view=View.inflate(context, R.layout.my_friend_group, null);
		TextView tvGroupName=(TextView) view.findViewById(R.id.tv_my_friend_group_groupName);
		RosterGroup rosterGroup=(RosterGroup) this.getGroup(groupPosition);
		String groupName=rosterGroup.getName();
		tvGroupName.setText(groupName);
		return view;
	}
	/**
	 * ��ʾ���Ѽ���my_friend_friend.xml
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//����my_friend_friend.xml
		View view=View.inflate(context, R.layout.my_friend_friend, null);
		//������
		RosterEntry rosterEntry=(RosterEntry) this.getChild(groupPosition, childPosition);
		String friendName=rosterEntry.getName();
		//���ؼ���ֵ
		TextView tvFriendName=(TextView) view.findViewById(R.id.tv_my_friend_group_friendName);
		tvFriendName.setText(friendName);
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;//child���Ա�ѡ�С�
	}

}
