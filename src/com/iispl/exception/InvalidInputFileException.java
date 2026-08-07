package com.iispl.exception;

public class InvalidInputFileException extends Exception{
	
	@Override
	public String getMessage() {
		return "Invalid Input File. File must be a non empty regular xml Files";	

}
}
