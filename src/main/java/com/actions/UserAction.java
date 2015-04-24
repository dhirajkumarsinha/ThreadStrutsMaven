package com.actions;

import com.bean.User;

public class UserAction {
	
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String execute() {
		return "success";
	}

}
