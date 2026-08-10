package com.iispl.exception;

public class ResponseGenerationException extends Exception{

	@Override
	public String getMessage() {
		return "failed to generate response";
	}
}
