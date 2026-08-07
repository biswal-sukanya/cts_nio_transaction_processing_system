
package com.iispl.nio;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.iispl.enums.TransactionType;
import com.iispl.exception.InvalidInputFileException;
import com.iispl.exception.InvalidTransactionException;
import com.iispl.exception.InvalidXmlStructureException;
import com.iispl.model.TransactionRequest;

public class NioXmlReader {
	

	    private static final int BUFFER_SIZE = 1024;

	    public String readXml(Path filePath) throws IOException, InvalidInputFileException {

	        validateFile(filePath);

	        StringBuilder xmlContent = new StringBuilder();

	        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {

	            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

	            while (fileChannel.read(buffer) != -1) {

	                buffer.flip();

	                xmlContent.append(StandardCharsets.UTF_8.decode(buffer));

	                buffer.clear();
	            }
	        }

	        return xmlContent.toString();
	    }

	    private static void validateFile(Path filePath) throws InvalidInputFileException, IOException {

	        if (filePath == null) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.exists(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.isRegularFile(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.isReadable(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!filePath.toString().toLowerCase().endsWith(".xml")) {
	            throw new InvalidInputFileException();
	        }

	        if (Files.size(filePath) == 0) {
	            throw new InvalidInputFileException();
	        }
	    }
	    public List<TransactionRequest> parseTransactions(String xmlContent)
	            throws InvalidXmlStructureException, InvalidTransactionException {
	    	
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    	DocumentBuilder builder;

	    	try {
	    	    builder = factory.newDocumentBuilder();
	    	} catch (ParserConfigurationException e) {
	    	    throw new InvalidXmlStructureException();
	    	}
	    	
	    	Document document;

	    	try {

	    	    InputSource inputSource = new InputSource(new StringReader(xmlContent));

	    	    document = builder.parse(inputSource);

	    	    document.getDocumentElement().normalize();

	    	} catch (SAXException | IOException e) {
	    	    throw new InvalidXmlStructureException();
	    	}
	    	// Get the root element
	    	Element rootElement = document.getDocumentElement();

	    	// Read root attributes
	    	String batchId = rootElement.getAttribute("batchId");
	    	String corporateId = rootElement.getAttribute("corporateId");
	    	
	    	batchId      = "BATCH001";
	    	corporateId  = "CORP101";
	    	
	    	NodeList transactionNodes = document.getElementsByTagName("transaction");
	    	
	    	List<TransactionRequest> transactionList = new ArrayList<>();
	    	
	    	for (int i = 0; i < transactionNodes.getLength(); i++) {

	    	    Node node = transactionNodes.item(i);

	    	    if (node.getNodeType() == Node.ELEMENT_NODE) {

	    	        Element transactionElement = (Element) node;

	    	        String transactionId = transactionElement.getElementsByTagName("transactionId")
	    	                .item(0).getTextContent();

	    	        String fromAccount = transactionElement.getElementsByTagName("fromAccount")
	    	                .item(0).getTextContent();

	    	        String toAccount = transactionElement.getElementsByTagName("toAccount")
	    	                .item(0).getTextContent();

	    	        String typeStr = transactionElement.getElementsByTagName("type")
	    	                .item(0).getTextContent();

	    	        String amountStr = transactionElement.getElementsByTagName("amount")
	    	                .item(0).getTextContent();

	    	        String dateStr = transactionElement.getElementsByTagName("transactionDate")
	    	                .item(0).getTextContent();

	    	       
	    	        NodeList remarksNode = transactionElement.getElementsByTagName("remarks");
	    	        String remark = (remarksNode.getLength() > 0) 
	    	                ? remarksNode.item(0).getTextContent() 
	    	                : "";

	    	        // --- basic validation  ---
	    	        if (transactionId == null || transactionId.isBlank()
	    	                || fromAccount == null || fromAccount.isBlank()
	    	                || toAccount == null || toAccount.isBlank()
	    	                || typeStr == null || typeStr.isBlank()
	    	                || amountStr == null || amountStr.isBlank()
	    	                || dateStr == null || dateStr.isBlank()) {
	    	            throw new InvalidTransactionException();
	    	                   
	    	        }

	    	        // --- type conversion ---
	    	        BigDecimal amount;
	    	        try {
	    	            amount = new BigDecimal(amountStr.trim());
	    	        } catch (NumberFormatException e) {
	    	            throw new InvalidTransactionException(
	    	                    );
	    	        }

	    	        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
	    	            throw new InvalidTransactionException(
	    	                    );
	    	        }

	    	        LocalDate transactionDate;
	    	        try {
	    	            transactionDate = LocalDate.parse(dateStr.trim());
	    	        } catch (DateTimeParseException e) {
	    	            throw new InvalidTransactionException(
	    	                    );
	    	        }
	    	        TransactionType type;
	    	        try {
	    	            type = TransactionType.valueOf(typeStr.trim().toUpperCase());
	    	        } catch (IllegalArgumentException e) {
	    	            throw new InvalidTransactionException();
	    	        }

	    	      
	    	        TransactionRequest txn = new TransactionRequest(
	    	                transactionId, fromAccount, toAccount, type, amount, transactionDate, remark);

	    	        transactionList.add(txn);
	    	    }
	    	}

	    	return transactionList;
	    
	    	
	    	
	    	
	    	
	    }


}
