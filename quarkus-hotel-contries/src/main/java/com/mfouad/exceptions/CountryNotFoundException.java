package com.mfouad.exceptions;

public class CountryNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CountryNotFoundException(String message) {
		super(message);
	}
}
