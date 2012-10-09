package com.pacdepot.exception;

public class ErrorException extends Exception {
	public ErrorException(String message) {
		super(message);	
	}

	public ErrorException(String message, Throwable object) {
		super(message, new ErrorException());
	}

	public ErrorException() {}
}
