package com.iispl.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.TransactionStatus;
import com.iispl.enums.TransactionType;

public class TransactionRequest {

	private String transactionId;
	private String fromAccount;
	private String toAccount;
	private TransactionType type;
	private BigDecimal amount;
	private LocalDate transactionDate;
	private String remark;
	
	public TransactionRequest() {
		
	}

	public TransactionRequest(String transactionId, String fromAccount, String toAccount, TransactionType type,
			BigDecimal amount, LocalDate transactionDate, String remark) {
		super();
		this.transactionId = transactionId;
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.type = type;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.remark = remark;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "TransactionRequest [transactionId=" + transactionId + ", fromAccount=" + fromAccount + ", toAccount="
				+ toAccount + ", type=" + type + ", amount=" + amount + ", transactionDate=" + transactionDate
				+ ", remark=" + remark + "]";
	}
	
	
	
}
