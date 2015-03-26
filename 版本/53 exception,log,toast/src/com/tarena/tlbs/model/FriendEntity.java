package com.tarena.tlbs.model;

import java.io.Serializable;

public class FriendEntity implements Serializable{
	/**
	 * 在服务器上的用户名
	 */
	private String username;
	/**
	 * 呢称
	 */
	private String name;
	private String group;
	
	
	public FriendEntity(String username, String name, String group) {
		super();
		this.username = username;
		this.name = name;
		this.group = group;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	

}
