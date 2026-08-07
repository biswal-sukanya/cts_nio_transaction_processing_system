
package com.iispl.nio;

import java.io.ByteArrayInputStream;
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
import com.iispl.service.ValidationService;

public class NioXmlReader {
	

	private static final int BUFFER_SIZE = 1024;

	private ValidationService validationService = new ValidationService();

	public List<TransactionRequest> readXml(Path filePath,
			String fileCorporateId) throws Exception {

		String xmlContent = readFile(filePath);

		return parseTransactions(xmlContent, fileCorporateId);

	}

	private String readFile(Path filePath)
			throws IOException, InvalidInputFileException {

		validateFile(filePath);

		StringBuilder xmlContent = new StringBuilder();

		try (FileChannel fileChannel = FileChannel.open(filePath,
				StandardOpenOption.READ)) {

			ByteBuffer buffer =	ByteBuffer.allocate(BUFFER_SIZE);

			while (fileChannel.read(buffer) != -1) {

				buffer.flip();

				xmlContent.append(StandardCharsets.UTF_8.decode(buffer));

				buffer.clear();
			}

		}

		return xmlContent.toString();

	}

	private List<TransactionRequest> parseTransactions(
			String xmlContent,
			String fileCorporateId)
			throws Exception {

		List<TransactionRequest> transactionList = new ArrayList<>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		Document document = builder.parse(new ByteArrayInputStream(
								xmlContent.getBytes(StandardCharsets.UTF_8)));

		document.getDocumentElement().normalize();

		Element root = document.getDocumentElement();

		String xmlCorporateId =	root.getAttribute("corporateId");

		NodeList transactionNodes =	root.getElementsByTagName("transaction");

		for (int index = 0;index < transactionNodes.getLength();index++) {

			Node node = transactionNodes.item(index);

			if (node.getNodeType()	== Node.ELEMENT_NODE) {

				Element transactionElement =(Element) node;

				TransactionRequest request =	new TransactionRequest();

				request.setTransactionId(	transactionElement
								.getElementsByTagName("transactionId")
								.item(0)
								.getTextContent());

				request.setFromAccount(transactionElement
								.getElementsByTagName("fromAccount")
								.item(0)
								.getTextContent());

				request.setToAccount(transactionElement
								.getElementsByTagName("toAccount")
								.item(0)
								.getTextContent());

				request.setType(TransactionType.valueOf(
								transactionElement
										.getElementsByTagName("type")
										.item(0)
										.getTextContent()));

				request.setAmount(new BigDecimal(
								transactionElement
										.getElementsByTagName("amount")
										.item(0)
										.getTextContent()));

				request.setTransactionDate(LocalDate.parse(
								transactionElement
										.getElementsByTagName("transactionDate")
										.item(0)
										.getTextContent()));

				request.setRemark(transactionElement
								.getElementsByTagName("remarks")
								.item(0)
								.getTextContent());

				validationService.validateTransaction(request,fileCorporateId,
						xmlCorporateId);

				transactionList.add(request);

			}

		}

		return transactionList;

	}

	private void validateFile(Path filePath) throws IOException,InvalidInputFileException {

		if (filePath == null) {
			throw new InvalidInputFileException();
		}

		if (!java.nio.file.Files.exists(filePath)) {
			throw new InvalidInputFileException();
		}

		if (!java.nio.file.Files.isRegularFile(filePath)) {
			throw new InvalidInputFileException();
		}

		if (!java.nio.file.Files.isReadable(filePath)) {
			throw new InvalidInputFileException();
		}

		if (java.nio.file.Files.size(filePath) == 0) {
			throw new InvalidInputFileException();
		}

	}


}
