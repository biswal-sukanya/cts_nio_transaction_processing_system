package com.iispl.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.iispl.model.FileProcessingSummary;
import com.iispl.model.TransactionResult;

public class ResponseFileWriter {

    private static final Path OUTPUT_FOLDER = Paths.get("data", "output");
    //write to the response file using File channel and Byte Buffer
    public void writeResponseFile(String batchId,String originalFileName,List<TransactionResult> results) throws IOException {

      Files.createDirectories(OUTPUT_FOLDER);

      String responseFileName = "RESP_" + originalFileName;

      Path responsePath = OUTPUT_FOLDER.resolve(responseFileName);

       StringBuilder xml = new StringBuilder();

       xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

       xml.append("<response batchId=\"").append(batchId).append("\">\n");

             for (TransactionResult result : results) {

                   xml.append("\t<transaction>\n");

                   xml.append("\t\t<transactionId>").append(result.getTransactionId()).append("</transactionId>\n");

                   xml.append("\t\t<status>").append(result.getStatus()).append("</status>\n");

                   xml.append("\t\t<failureCode>").append(result.getFailureCode() == null ? "": result.getFailureCode()).append("</failureCode>\n");

                   xml.append("\t\t<failureReason>").append(result.getFailureReason() == null ? "": result.getFailureReason()).append("</failureReason>\n");

                   xml.append("\t</transaction>\n");
}

                   xml.append("</response>");

                  ByteBuffer buffer =ByteBuffer.wrap(xml.toString().getBytes(StandardCharsets.UTF_8));

                 try (FileChannel channel = FileChannel.open(responsePath,StandardOpenOption.CREATE,StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING)) {

                  channel.write(buffer);
}

                 System.out.println("Response XML Created Successfully.");

                 BasicFileAttributes attributes =Files.readAttributes(responsePath,BasicFileAttributes.class);

                 System.out.println("Response File Size : "+ attributes.size() + " bytes");

                 System.out.println("Last Modified      : "+ attributes.lastModifiedTime());
}
    //Write the Processing Summary to a file
               public void writeSummaryFile(String originalFileName,FileProcessingSummary summary) throws IOException {

                Files.createDirectories(OUTPUT_FOLDER);

                String summaryFileName = "SUMMARY_" + originalFileName.replace(".xml", ".txt");

                Path summaryPath = OUTPUT_FOLDER.resolve(summaryFileName);

        List<String> lines = List.of(
                "CTS BULK TRANSACTION PROCESSING SUMMARY",
                "--------------------------------------",
                "Total Records      : " + summary.getTotalRecords(),
                "Processed Records  : " + summary.getProcessedRecords(),
                "Failed Records     : " + summary.getFailedRecords(),
                "Response Generated : " + summary.isResponseGenerated(),
                "Archived           : " + summary.isArchived()
        );

        Files.write(summaryPath,lines,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.println("Summary File Created Successfully.");
    }

}
 