package com.pacdepot.domain;

import java.util.Map;
import java.util.Set;

public class RegistrationBitmap extends Entity {
	public RegistrationBitmap(Map attributes, Set errors) {
		super(attributes, errors);
	}

	public RegistrationBitmap() {
		setLabel("registrationBitmap");
	}
}
