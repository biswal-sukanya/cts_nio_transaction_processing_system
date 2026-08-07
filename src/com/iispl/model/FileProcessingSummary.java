package com.iispl.model;

public class FileProcessingSummary {

	private int totalRecords;
	private int processedRecords;
	private int failedRecords;
	private boolean responseGenerated;
	private boolean archived;
	
	public FileProcessingSummary() {
		
	}

	public FileProcessingSummary(int totalRecords, int processedRecords, int failedRecords, boolean responseGenerated,
			boolean archived) {
		super();
		this.totalRecords = totalRecords;
		this.processedRecords = processedRecords;
		this.failedRecords = failedRecords;
		this.responseGenerated = responseGenerated;
		this.archived = archived;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getProcessedRecords() {
		return processedRecords;
	}

	public void setProcessedRecords(int processedRecords) {
		this.processedRecords = processedRecords;
	}

	public int getFailedRecords() {
		return failedRecords;
	}

	public void setFailedRecords(int failedRecords) {
		this.failedRecords = failedRecords;
	}

	public boolean isResponseGenerated() {
		return responseGenerated;
	}

	public void setResponseGenerated(boolean responseGenerated) {
		this.responseGenerated = responseGenerated;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	@Override
	public String toString() {
		return "FileProcessingSummary [totalRecords=" + totalRecords + ", processedRecords=" + processedRecords
				+ ", failedRecords=" + failedRecords + ", responseGenerated=" + responseGenerated + ", archived="
				+ archived + "]";
	}
	
	
}
