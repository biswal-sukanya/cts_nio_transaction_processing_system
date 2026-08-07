package com.iispl.daoimpl;

import java.math.BigDecimal;

import com.iispl.dao.AccountDao;
import com.iispl.model.Account;

public class AccountDaoImpl implements AccountDao {

	@Override
	public boolean isAccountExists(String accountNumber) {
		
		return false;
	}

	@Override
	public boolean isAccountActive(String accountNumber) {
		
		return false;
	}

	@Override
	public boolean hasSufficientBalance(String accountNumber, BigDecimal amount) {
		
		return false;
	}

	@Override
	public Account getAccountByAccountNumber(String accountNumber) {
		
		return null;
	}

	@Override
	public boolean debitAmount(String accountNumber, BigDecimal amount) {
		
		return false;
	}

	@Override
	public boolean creditAmount(String accountNumber, BigDecimal amount) {
		
		return false;
	}

}
