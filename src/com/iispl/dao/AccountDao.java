package com.iispl.dao;

import java.math.BigDecimal;

import com.iispl.model.Account;

public interface AccountDao {

	    boolean isAccountExists(String accountNumber);

	    boolean isAccountActive(String accountNumber);

	    boolean hasSufficientBalance(String accountNumber, BigDecimal amount);

	    Account getAccountByAccountNumber(String accountNumber);

	    boolean debitAmount(String accountNumber, BigDecimal amount);

	    boolean creditAmount(String accountNumber, BigDecimal amount);
	
}
