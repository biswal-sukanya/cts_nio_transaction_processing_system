package com.iispl.exception;

public class InvalidFileNameException extends Exception {

@Override
public String getMessage() {
	return "Invalid file name. Expected format: TXN_<CORPORATE_ID>_<YYYYMMDD>_<SEQUENCE>.xml";
	
}
}
