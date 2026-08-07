package com.iispl.daoimpl;

import com.iispl.dao.TransactionDao;
import com.iispl.model.TransactionRequest;

public class TransactionDaoImpl implements TransactionDao {

	@Override
	public boolean isTransactionExists(String transactionId) {
		
		return false;
	}

	@Override
	public boolean saveTransaction(TransactionRequest transactionRequest) {
		
		return false;
	}

}
