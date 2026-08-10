package com.iispl.exception;

public class ArchivalException extends Exception{

	@Override
	public String getMessage() {
		return "Failed to Archive the File.";
	}
}
