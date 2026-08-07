package com.iispl.main;

import java.sql.Connection;

import com.iispl.util.DBUtil;

public class CTSBatchApplication {

	public static void main(String[] args) {
		
		try(Connection connection = DBUtil.getConnection()){
			
			if(connection != null)
			System.out.println("Database Connected Successfully...");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}
