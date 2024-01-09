package org.daniel.control;

import org.daniel.model.Hotel;
import org.daniel.model.Weather;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DataHandler {
	private final String dbPath;
	private DbConnector dbConnection;
	private Connection connection;
	private Statement statement;
	private ModelBuilder eventBuilder;

	public DataHandler(String dbPath, ModelBuilder eventBuilder) throws SQLException {
		this.dbPath = dbPath;
		this.eventBuilder = eventBuilder;
		try {
			initializeDatabase();
		} catch (SQLException e) {
			handleSQLException(e, "Error initializing database.");
		}
	}

	private void initializeDatabase() throws SQLException {
		new File(this.dbPath).getParentFile().mkdirs();
		this.connection = dbConnection.connectDatabase(dbPath);
		this.statement = connection.createStatement();
		createTable("prediction_Weather", "id INTEGER PRIMARY KEY AUTOINCREMENT, predictionTime TEXT, zone TEXT, island TEXT, temperature REAL, clouds REAL, precipitation REAL");
		createTable("prediction_Booking", "check_in TEXT, zone TEXT, island TEXT, hotel_name TEXT, platform TEXT, price REAL, UNIQUE(check_in, hotel_name)");
	}

	private void createTable(String tableName, String columns) throws SQLException {
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" + columns + ")");
	}

	public String formatDate(Instant date) {
		LocalDateTime localDateTime = date.atZone(ZoneId.systemDefault()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return localDateTime.format(formatter);
	}

	public void addWeatherData(String jsonData) throws SQLException {
		Weather weather = eventBuilder.buildWeatherData(jsonData);
		String insertQuery = "INSERT INTO prediction_Weather (predictionTime, zone, island, temperature, clouds, precipitation) " +
				"VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
			preparedStatement.setString(1, formatDate(weather.ts()));
			preparedStatement.setString(2, weather.getLocation().getArea());
			preparedStatement.setString(3, weather.getLocation().getLocation());
			preparedStatement.setDouble(4, weather.getTemperature());
			preparedStatement.setFloat(5, weather.getClouds());
			preparedStatement.setDouble(6, weather.getPrecipitation());
			preparedStatement.executeUpdate();
			System.out.println("Data successfully inserted into prediction_Weather table");
		} catch (SQLException e) {
			handleSQLException(e, "Error inserting data into prediction_Weather table");
		}
	}

	public void addHotelData(String jsonData) throws SQLException {
		Hotel hotel = eventBuilder.buildHotelData(jsonData);
		if (hotel.getPrice() != 0.0) {
			String insertOrUpdateQuery = "INSERT INTO prediction_Booking (check_in, zone, island, hotel_name, platform, price)" +
					"VALUES (?, ?, ?, ?, ?, ?) " +
					"ON CONFLICT(check_in, hotel_name)" +
					"DO UPDATE SET" +
					"    check_in = excluded.check_in," +
					"    zone = excluded.zone," +
					"    island = excluded.island," +
					"    platform = excluded.platform," +
					"    price = CASE WHEN excluded.price < prediction_Booking.price THEN excluded.price ELSE prediction_Booking.price END " +
					"WHERE prediction_Booking.price > excluded.price";
			try (PreparedStatement preparedStatement = connection.prepareStatement(insertOrUpdateQuery)) {
				preparedStatement.setString(1, formatDate(hotel.getArrival()));
				preparedStatement.setString(2, hotel.getLocation().getArea());
				preparedStatement.setString(3, hotel.getLocation().getLocation());
				preparedStatement.setString(4, hotel.getHotelName());
				preparedStatement.setString(5, hotel.getWeb());
				preparedStatement.setDouble(6, hotel.getPrice());
				preparedStatement.executeUpdate();
				System.out.println("Data successfully inserted into prediction_Booking table");
			} catch (SQLException e) {
				handleSQLException(e, "Error inserting data into prediction_Booking table");
			}
		}
	}

	public void removeTable(String topicName) {
		try {
			String deleteQuery = (topicName.substring(11).equals("Weather")) ?
					"DELETE FROM prediction_Weather" : "DELETE FROM prediction_Booking";
			statement.executeUpdate(deleteQuery);
			System.out.println("Delete operation completed successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void handleSQLException(SQLException e, String message) throws SQLException {
		throw new SQLException(message, e);
	}
}

