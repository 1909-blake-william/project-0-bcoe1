package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

	public static Connection getConnection() throws SQLException {
		String url = System.getenv("TRAINING_DB_URL");
		String username = System.getenv("BANK_DB_USERNAME");
		String password = System.getenv("BANK_DB_PASSWORD");
		return DriverManager.getConnection(url, username, password);
	}
}
