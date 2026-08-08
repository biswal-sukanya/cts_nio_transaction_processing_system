package com.iispl.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.iispl.dao.FileProcessingDao;
import com.iispl.model.FileProcessingSummary;
import com.iispl.util.DBUtil;

public class FileProcessingDaoImpl implements FileProcessingDao {

		@Override
		public boolean saveFileProcessingSummary(String batchId,String fileName,FileProcessingSummary summary) {

		String sql = "INSERT INTO file_processing	(batch_id,file_name,total_records,successful_records,failed_records,processing_status) VALUES	(?,?,?,?,?,?)";

			try (Connection connection = DBUtil.getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

				preparedStatement.setString(1, batchId);

				preparedStatement.setString(2, fileName);

				preparedStatement.setInt(3,summary.getTotalRecords());

				preparedStatement.setInt(4,summary.getProcessedRecords());

				preparedStatement.setInt(5,summary.getFailedRecords());

				preparedStatement.setString(6,summary.getFailedRecords() == 0 ? "SUCCESS":"PARTIAL_SUCCESS");

				return preparedStatement.executeUpdate() > 0;

			} catch (SQLException exception) {

				exception.printStackTrace();

			}

			return false;

		}

	}

