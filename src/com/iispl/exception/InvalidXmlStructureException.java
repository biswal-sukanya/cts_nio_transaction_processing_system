package com.iispl.exception;

public class InvalidXmlStructureException extends Exception{
	@Override
	public String getMessage() {
		return "XML structure is invalid";
	}
}
