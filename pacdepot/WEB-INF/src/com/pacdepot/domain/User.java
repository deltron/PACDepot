package com.pacdepot.domain;

public class User extends Entity {
	public User() {
		super();
		setLabel("user");
	}

	public User(long id) {
		setAttribute("id", new Long(id));
		setLabel("user");
	}
	
	public boolean isLoggedIn() {
		try {
			return ((String) getAttribute("loggedIn")).matches("true");
		} catch (Exception e) {
			return false;
		}
	}
	
	public void logout() {
		try {
			removeAttribute("loggedIn");
		} catch (Exception e) {
		}	
	}
}
