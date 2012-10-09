package com.pacdepot.exception;

public class BrandException extends ErrorException {
	public BrandException(String message) {
		super(message, new BrandException());	
	}

	public BrandException() {}
}