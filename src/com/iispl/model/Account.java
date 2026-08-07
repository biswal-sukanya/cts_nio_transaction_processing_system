package com.iispl.model;

import java.math.BigDecimal;

import com.iispl.enums.AccountStatus;

public class Account {

	private String accountNumber;
	private String customerName;
	private String accountType;
	private BigDecimal balance;
	private AccountStatus status;
	
	public Account() {
		
	}

	public Account(String accountNumber, String customerName,String accountType, BigDecimal balance, AccountStatus status) {
		super();
		this.accountNumber = accountNumber;
		this.customerName = customerName;
		this.accountType = accountType;
		this.balance = balance;
		this.status = status;
	}
	
	public String getAccountType() {
		return this.accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setAccountHolderName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", customerName=" + customerName + ", balance="
				+ balance + ", status=" + status + "]";
	}
	
	
}
