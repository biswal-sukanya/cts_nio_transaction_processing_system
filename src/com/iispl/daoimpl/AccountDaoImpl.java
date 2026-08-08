package com.iispl.daoimpl;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.iispl.dao.AccountDao;
import com.iispl.enums.AccountStatus;
import com.iispl.model.Account;
import com.iispl.util.DBUtil;


     

public class AccountDaoImpl implements AccountDao {
	

	@Override
	public boolean isAccountExists(String accountNumber) {

		String sql = "SELECT COUNT(*) FROM account WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, accountNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				return resultSet.getInt(1) > 0;

			}

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;

	}

	@Override
	public boolean isAccountActive(String accountNumber) {

		String sql = "SELECT account_status FROM account WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, accountNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				return AccountStatus.valueOf(resultSet.getString("account_status")) == AccountStatus.ACTIVE;

			}

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;

	}

	@Override
	public boolean hasSufficientBalance(String accountNumber,
			BigDecimal amount) {

		String sql = "SELECT balance FROM account WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, accountNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				BigDecimal balance = resultSet.getBigDecimal("balance");

				return balance.compareTo(amount) >= 0;

			}

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;

	}

	@Override
	public Account getAccountByAccountNumber(String accountNumber) {

		String sql = "SELECT * FROM account WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, accountNumber);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				Account account = new Account();

				account.setAccountNumber(resultSet.getString("account_number"));

				account.setAccountHolderName(resultSet.getString("customer_name"));

				account.setAccountType(resultSet.getString("account_type"));

				account.setBalance(resultSet.getBigDecimal("balance"));

				account.setStatus(AccountStatus.valueOf(resultSet.getString("account_status")));

				return account;

			}

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return null;

	}
	
	@Override
	public boolean debitAmount(String accountNumber,BigDecimal amount) {

		String sql = "UPDATE account SET balance = balance - ? WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setBigDecimal(1, amount);
			preparedStatement.setString(2, accountNumber);

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;

	}

	@Override
	public boolean creditAmount(String accountNumber,BigDecimal amount) {

		String sql ="UPDATE account SET balance = balance + ? WHERE account_number = ?";

		try (Connection connection = DBUtil.getConnection();

			PreparedStatement preparedStatement =connection.prepareStatement(sql)) {

			preparedStatement.setBigDecimal(1, amount);
			preparedStatement.setString(2, accountNumber);

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;

	}

}
