package com.iispl.service;

import java.util.ArrayList;
import java.util.List;

import com.iispl.dao.AccountDao;
import com.iispl.dao.TransactionDao;
import com.iispl.daoimpl.AccountDaoImpl;
import com.iispl.daoimpl.TransactionDaoImpl;
import com.iispl.enums.TransactionStatus;
import com.iispl.model.TransactionRequest;
import com.iispl.model.TransactionResult;

public class TransactionService {
	
	private AccountDao accountDao;
	private TransactionDao transactionDao;

	public TransactionService() {

		accountDao = new AccountDaoImpl();
		transactionDao = new TransactionDaoImpl();

	}

	public List<TransactionResult> processTransactions(String batchId,List<TransactionRequest> transactionRequests) {

		List<TransactionResult> transactionResults = new ArrayList<>();

		for (TransactionRequest request : transactionRequests) {

			TransactionResult result = new TransactionResult();

			result.setTransactionId(request.getTransactionId());

			/* Duplicate Transaction */

			if (transactionDao.isTransactionExists(request.getTransactionId())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("T001");

				result.setFailureReason("Duplicate Transaction");

				transactionResults.add(result);

				continue;

			}

			/* From Account Exists */

			if (!accountDao.isAccountExists(request.getFromAccount())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A001");

				result.setFailureReason("Source Account Not Found");

				transactionResults.add(result);

				continue;

			}

			/* To Account Exists */

			if (!accountDao.isAccountExists(request.getToAccount())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A002");

				result.setFailureReason("Destination Account Not Found");

				transactionResults.add(result);

				continue;

			}

			/* Source Account Active */

			if (!accountDao.isAccountActive(request.getFromAccount())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A003");

				result.setFailureReason("Source Account Inactive");

				transactionResults.add(result);

				continue;

			}

			/* Destination Account Active */

			if (!accountDao.isAccountActive(request.getToAccount())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A004");

				result.setFailureReason("Destination Account Inactive");

				transactionResults.add(result);

				continue;

			}

			/* Balance Validation */

			if (!accountDao.hasSufficientBalance(request.getFromAccount(),request.getAmount())) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A005");

				result.setFailureReason("Insufficient Balance");

				transactionResults.add(result);

				continue;

			}
			
			boolean debitSuccess = accountDao.debitAmount(request.getFromAccount(),request.getAmount());

			if (!debitSuccess) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A006");

				result.setFailureReason("Unable to Debit Source Account");

				transactionResults.add(result);

				continue;

			}

			/* Credit Destination Account */

			boolean creditSuccess = accountDao.creditAmount(request.getToAccount(),request.getAmount());

			if (!creditSuccess) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("A007");

				result.setFailureReason("Unable to Credit Destination Account");

				transactionResults.add(result);

				continue;

			}

			/* Save Transaction */

			boolean saved = transactionDao.saveTransaction(batchId, request);

			if (!saved) {

				result.setStatus(TransactionStatus.FAILED);

				result.setFailureCode("T002");

				result.setFailureReason("Unable to Save Transaction");

				transactionResults.add(result);

				continue;

			}

			result.setStatus(TransactionStatus.SUCCESS);

			result.setFailureCode(null);

			result.setFailureReason(null);

			transactionResults.add(result);

		}

		return transactionResults;

	}
	

}
