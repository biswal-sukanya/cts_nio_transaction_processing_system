package com.iispl.main;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import com.iispl.dao.FileProcessingDao;
import com.iispl.daoimpl.FileProcessingDaoImpl;
import com.iispl.enums.TransactionStatus;
import com.iispl.model.FileProcessingSummary;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;
import com.iispl.nio.ArchiveService;
import com.iispl.nio.FileIntakeService;
import com.iispl.nio.NioXmlReader;
import com.iispl.nio.ResponseFileWriter;
import com.iispl.service.TransactionService;

public class CTSBatchApplication {

	public static void main(String[] args) {
		
		   try {

			   FileIntakeService intakeService = new FileIntakeService();

			   NioXmlReader reader = new NioXmlReader();

			   TransactionService transactionService = new TransactionService();

			   ResponseFileWriter responseWriter = new ResponseFileWriter();

			   ArchiveService archiveService = new ArchiveService();

			   FileProcessingDao fileProcessingDao =  new FileProcessingDaoImpl();

			   Path processedFile = intakeService.getFileForProcessing();

			   if (processedFile == null) {

			       System.out.println("No XML files found.");

			       return;

			   }

			   String batchId =  UUID.randomUUID().toString();

			   String corporateId = "CORP101";

			   String originalFileName =  processedFile.getFileName().toString();

			   List<TransactionRequest> requests =  reader.readXml(processedFile, corporateId);

				List<TransactionResult> results = transactionService.processTransactions(batchId, requests);


				
				// 3. DATABASE UPDATE

				System.out.println();
				System.out.println("------------------------------------------------------------");
				System.out.println("3. Database Update");
				System.out.println("------------------------------------------------------------");

				System.out.println("Connecting to Database...\n");

				for (TransactionResult result : results) {

				    if (result.getStatus() == TransactionStatus.SUCCESS) {

				        System.out.println( "Transaction "+ result.getTransactionId()+ " : SUCCESS");

				    } else {

				        System.out.println("Transaction "+ result.getTransactionId()+ " : FAILED ("+ result.getFailureReason()+ ")"
				        );
				    }
				}
                 System.out.println();
				System.out.println("Database Commit Successful\n");


				FileProcessingSummary summary = new FileProcessingSummary();

			   summary.setTotalRecords(results.size());

			   int success = 0;
			   int failed = 0;

			   for (TransactionResult result : results) {

			       if (result.getStatus() == TransactionStatus.SUCCESS) {

			           success++;

			       } else {

			           failed++;

			       }

			   }

			   summary.setProcessedRecords(success);

			   summary.setFailedRecords(failed);

			   summary.setResponseGenerated(true);

			   responseWriter.writeResponseFile(batchId,originalFileName,results);
			   
			   archiveService.moveToArchive(processedFile,Path.of("data", "archive"));
			   
			   summary.setArchived(true);

				responseWriter.writeSummaryFile(originalFileName,summary);

				fileProcessingDao.saveFileProcessingSummary(batchId,originalFileName,summary);

			   System.out.println("CTS Batch Processing Completed Successfully.");  

	        } catch (Exception exception) {

	            exception.printStackTrace();

	        }

		

	}

}
