package com.iispl.util;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
public class DBUtil {

	private static ComboPooledDataSource comboPooledDataSource;
	private static Properties properties;
	
	
	  static {

	        try {

	            comboPooledDataSource = new ComboPooledDataSource();
	            properties = new Properties();

	            InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");

	            if (inputStream == null) {
	                throw new RuntimeException("db.properties file not found.");
	            }

	            properties.load(inputStream);

	            comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
	            comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
	            comboPooledDataSource.setUser(properties.getProperty("user"));
	            comboPooledDataSource.setPassword(properties.getProperty("password"));

	       

	        } catch (PropertyVetoException | IOException e) {
	            throw new RuntimeException("Error while initializing database connection pool.", e);
	        }

	    }
	  
	  public static Connection getConnection() throws SQLException {
		    return comboPooledDataSource.getConnection();
		}
	
}
