package com.pacdepot.domain;

import java.util.Map;
import java.util.Set;

import com.pacdepot.database.DataAdapter;

public class Answer extends Entity {

	public Answer(Map attributes, Set errors) {
		super(attributes, errors);
	}

	public Answer() {
		setLabel("answer");
	}
	
	public User getUser() {
		long userid = getAttributeAsLong("userid");
		return DataAdapter.getInstance().getUser(new User(userid));
	}
}
