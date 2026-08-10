
package com.iispl.nio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.iispl.enums.TransactionType;
import com.iispl.exception.InvalidInputFileException;
import com.iispl.exception.InvalidTransactionException;
import com.iispl.exception.InvalidXmlStructureException;
import com.iispl.model.TransactionRequest;
import com.iispl.service.ValidationService;


public class NioXmlReader {
	
	private static final int BUFFER_SIZE = 1024;

	private ValidationService validationService = new ValidationService();

	public List<TransactionRequest> readXml(Path filePath,String fileCorporateId) throws Exception {

		String xmlContent = readFile(filePath);

		return parseTransactions(xmlContent, fileCorporateId);
	}

	private String readFile(Path filePath) throws IOException {

		StringBuilder xmlContent = new StringBuilder();

		try (FileChannel channel = FileChannel.open(filePath,StandardOpenOption.READ)) {

			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

			while (channel.read(buffer) != -1) {

				buffer.flip();

				xmlContent.append(StandardCharsets.UTF_8.decode(buffer));

				buffer.clear();
			}
		}

		return xmlContent.toString();
	}

	private List<TransactionRequest> parseTransactions(String xmlContent,String fileCorporateId)throws Exception {

		List<TransactionRequest> transactionList = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

		document.getDocumentElement().normalize();

		Element root = document.getDocumentElement();

		String xmlCorporateId = root.getAttribute("corporateId");

		NodeList transactionNodes = root.getElementsByTagName("transaction");

		for (int index = 0;index < transactionNodes.getLength();index++) {

			Node node =	transactionNodes.item(index);

			if (node.getNodeType()	== Node.ELEMENT_NODE) {

				Element transactionElement = (Element) node;

				TransactionRequest request = new TransactionRequest();
				
				  NodeList transactionIdNode = transactionElement.getElementsByTagName("transactionId");

	              NodeList fromAccountNode =  transactionElement.getElementsByTagName("fromAccount");

	              NodeList toAccountNode =  transactionElement.getElementsByTagName("toAccount");

	              NodeList typeNode = transactionElement.getElementsByTagName("type");

	              NodeList amountNode = transactionElement.getElementsByTagName("amount");

	              NodeList transactionDateNode = transactionElement.getElementsByTagName("transactionDate");

	              NodeList remarkNode = transactionElement.getElementsByTagName("remarks");

	                if (transactionIdNode.getLength() == 0
	                        || fromAccountNode.getLength() == 0
	                        || toAccountNode.getLength() == 0
	                        || typeNode.getLength() == 0
	                        || amountNode.getLength() == 0
	                        || transactionDateNode.getLength() == 0
	                        || remarkNode.getLength() == 0) {

	                    throw new InvalidXmlStructureException();
	                }

	                request.setTransactionId(transactionIdNode.item(0).getTextContent().trim());

	                request.setFromAccount(fromAccountNode.item(0).getTextContent().trim());

	                request.setToAccount(toAccountNode.item(0).getTextContent().trim());

	                String transactionType =typeNode.item(0).getTextContent().trim();

	                try {

	                    request.setType(TransactionType.valueOf(transactionType));

	                } catch (IllegalArgumentException exception) {

	                    throw new InvalidTransactionException();

	                }

	                request.setAmount(new BigDecimal(amountNode.item(0).getTextContent().trim()));

	                request.setTransactionDate(LocalDate.parse(transactionDateNode.item(0).getTextContent().trim()));

	                request.setRemark(remarkNode.item(0).getTextContent().trim());

	                try {

	                    validationService.validateTransaction(request,fileCorporateId,xmlCorporateId);

	                    transactionList.add(request);

	                } catch (InvalidTransactionException
	                        | InvalidXmlStructureException e) {

	                	System.out.println(e.getMessage());
	                   

	                    continue;
	                }

	            }

	        }

	        return transactionList;

	    }

	}

		
