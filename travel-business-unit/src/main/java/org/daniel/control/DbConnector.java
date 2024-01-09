package org.daniel.control;

import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
	public static Connection connectDatabase(String dbPath) throws SQLiteException {
		try {
			String url = "jdbc:sqlite:" + dbPath;
			Connection conn = DriverManager.getConnection(url);
			System.out.println("Connected to SQLite.");
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}

