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
//RosterGroup代表是好友分组如1409,tarena
//一个分组下有多个好友RosterEntry
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

	//有多少个组
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return listGroup.size();
	}

	//一个分组下有多少个好友
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

	//返回选中的分组，选中的好友
	@Override
	public Object getChild(
			int groupPosition,
			int childPosition) {
		// TODO Auto-generated method stub
		//1,得到选中的分组
		RosterGroup rosterGroup=listGroup.get(groupPosition);
		//2,得到好友
		//rosterGroup.getEntries()返回的是一个分组下所有好友
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
//Stable 固定
	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;//表示数据没有固定的id
	}

	/**
	 * 显示好友分组加载my_friend_group.xml
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
	 * 显示好友加载my_friend_friend.xml
	 */
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		//加载my_friend_friend.xml
		View view=View.inflate(context, R.layout.my_friend_friend, null);
		//得数据
		RosterEntry rosterEntry=(RosterEntry) this.getChild(groupPosition, childPosition);
		String friendName=rosterEntry.getName();
		//给控件设值
		TextView tvFriendName=(TextView) view.findViewById(R.id.tv_my_friend_group_friendName);
		tvFriendName.setText(friendName);
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;//child可以被选中。
	}

}
