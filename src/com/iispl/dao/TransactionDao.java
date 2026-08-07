package com.iispl.dao;

import com.iispl.model.TransactionRequest;

public interface TransactionDao {

	boolean isTransactionExists(String transactionId);
	boolean saveTransaction(TransactionRequest transactionRequest);
}
