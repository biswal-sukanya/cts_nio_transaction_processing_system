package com.iispl.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.exception.InvalidTransactionException;
import com.iispl.exception.InvalidXmlStructureException;
import com.iispl.model.TransactionRequest;

public class ValidationService {

	public void validateXmlStructure(TransactionRequest transaction)
			throws InvalidXmlStructureException {

		if (transaction == null) {
			throw new InvalidXmlStructureException();
		}

	}

	public void validateMandatoryFields(TransactionRequest transaction)
			throws InvalidTransactionException {

		if (transaction.getTransactionId() == null
				|| transaction.getTransactionId().isBlank()) {
			throw new InvalidTransactionException();
		}

		if (transaction.getFromAccount() == null
				|| transaction.getFromAccount().isBlank()) {
			throw new InvalidTransactionException();
		}

		if (transaction.getToAccount() == null
				|| transaction.getToAccount().isBlank()) {
			throw new InvalidTransactionException();
		}

		if (transaction.getType() == null) {
			throw new InvalidTransactionException();
		}

		if (transaction.getAmount() == null) {
			throw new InvalidTransactionException();
		}

		if (transaction.getTransactionDate() == null) {
			throw new InvalidTransactionException();
		}

	}

	public void validateTransactionId(String transactionId)
			throws InvalidTransactionException {

		if (!transactionId.matches("TXN\\d+")) {
			throw new InvalidTransactionException();
		}

	}

	public void validateAccountNumber(String accountNumber)
			throws InvalidTransactionException {

		if (!accountNumber.matches("\\d{6}")) {
			throw new InvalidTransactionException();
		}

	}

	public void validateAmount(BigDecimal amount)
			throws InvalidTransactionException {

		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new InvalidTransactionException();
		}

	}

	public void validateTransactionDate(LocalDate transactionDate)
			throws InvalidTransactionException {

		if (transactionDate.isAfter(LocalDate.now())) {
			throw new InvalidTransactionException();
		}

	}

	public void validateCorporateId(String fileCorporateId,
			String xmlCorporateId)
			throws InvalidTransactionException {

		if (!fileCorporateId.equalsIgnoreCase(xmlCorporateId)) {
			throw new InvalidTransactionException();
		}

	}

	public void validateTransaction(TransactionRequest transaction,
			String fileCorporateId,
			String xmlCorporateId)
			throws InvalidTransactionException,
			InvalidXmlStructureException {

		validateXmlStructure(transaction);

		validateMandatoryFields(transaction);

		validateTransactionId(transaction.getTransactionId());

		validateAccountNumber(transaction.getFromAccount());

		validateAccountNumber(transaction.getToAccount());

		validateAmount(transaction.getAmount());

		validateTransactionDate(transaction.getTransactionDate());

		validateCorporateId(fileCorporateId, xmlCorporateId);

	}

	

	
}
