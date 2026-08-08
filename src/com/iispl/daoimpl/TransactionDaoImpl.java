package com.iispl.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.iispl.dao.TransactionDao;
import com.iispl.model.TransactionRequest;
import com.iispl.util.DBUtil;

public class TransactionDaoImpl implements TransactionDao {

	@Override
	public boolean isTransactionExists(String transactionId) {

		String sql = "SELECT COUNT(*) FROM bank_transaction WHERE transaction_id = ?";

		try (Connection connection = DBUtil.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, transactionId);

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
	public boolean saveTransaction(String batchId,
	                               TransactionRequest request) {

		String sql = """
				INSERT INTO bank_transaction (transaction_id,batch_id,from_account,to_account,transaction_type,amount,transaction_date,transaction_status) VALUES (?,?,?,?,?,?,?,?)""";

		try (Connection connection = DBUtil.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1,request.getTransactionId());

			preparedStatement.setString(2,batchId);

			preparedStatement.setString(3,request.getFromAccount());

			preparedStatement.setString(4,request.getToAccount());

			preparedStatement.setString(5,request.getType().name());

			preparedStatement.setBigDecimal(6,request.getAmount());

			preparedStatement.setDate(7,java.sql.Date.valueOf(request.getTransactionDate()));

			preparedStatement.setString(8,"SUCCESS");

			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException exception) {

			exception.printStackTrace();

		}

		return false;
	}
}
