package com.iispl.dao;

import java.math.BigDecimal;

import com.iispl.model.Account;

public interface AccountDao {

	    boolean isAccountExists(String accountNumber);

	    boolean isAccountActive(String accountNumber);

	    boolean hasSufficientBalance(String accountNumber, BigDecimal amount);

	    Account getAccountByAccountNumber(String accountNumber);

	    boolean isDebitedAmount(String accountNumber, BigDecimal amount);

	    boolean isCreditedAmount(String accountNumber, BigDecimal amount);
	
}
