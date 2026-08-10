package com.iispl.exception;

public class InvalidTransactionException extends Exception{

	@Override
	public String getMessage() {
		return "Invalid Transaction";
	}
}
