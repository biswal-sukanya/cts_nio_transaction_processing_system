package com.iispl.dao;

import com.iispl.model.FileProcessingSummary;

public interface FileProcessingDao {

	boolean saveFileProcessingSummary(String batchId,String fileName,FileProcessingSummary summary);
	
}
