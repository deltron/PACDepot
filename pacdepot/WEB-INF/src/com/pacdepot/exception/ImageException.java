package com.pacdepot.exception;

public class ImageException extends Exception {
	public ImageException(String message) {
		super(message);
	}

	public ImageException() {
		super();
	}

	public ImageException(Exception e) {
		super(e);
	}
}
