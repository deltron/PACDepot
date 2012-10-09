package com.pacdepot.domain;

import java.util.Map;
import java.util.Set;


public class Question extends Entity {
	public Question(Map attributes, Set errors) {
		super(attributes, errors);
	}
	
	public Question() {
		setLabel("question");
	}	
}
