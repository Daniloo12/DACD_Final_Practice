package org.daniel.control;

import org.daniel.model.Weather;
import org.daniel.model.WeatherStore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteWeatherStore implements WeatherStore {
	private final String dbpath;


	public Connection open() {
		Connection connectionToDB;
		try {
			connectionToDB = DriverManager.getConnection(this.dbpath);
		} catch (SQLException exception) {
			throw new RuntimeException(exception);
		}
		return connectionToDB;
	}

	public SQLiteWeatherStore(String dbpath) {
		this.dbpath = "jdbc:sqlite:" + dbpath;
	}


	private void createTable(Statement statement, Weather weather) throws SQLException {
		String island = weather.getLocation().getIsland();
		statement.execute("CREATE TABLE IF NOT EXISTS " + island + " (" +
				"TimeStamp TEXT,\n" +
				"Clouds REAL,\n" +
				"Wind REAL,\n" +
				"Temperature REAL,\n" +
				"Precipitation REAL,\n" +
				"Humidity REAL);" +
				");");
	}

	@Override
	public void save(Weather weather) throws SQLException {
		try (Connection connection = this.open();
			 Statement statement = connection.createStatement()) {
			String timeStamp = weather.getTs().toString();
			String island = weather.getLocation().getIsland();
			String query = "SELECT * FROM " + island + " WHERE TimeStamp = '" + timeStamp + "'";

			boolean hasResult = statement.execute(query);
			if (hasResult && statement.getResultSet().next()) {
				String updateQuery = String.format("UPDATE %s SET Temperature = %f, Humidity = %f, Precipitation = %f, Wind = %f, Clouds = %f WHERE TimeStamp = '%s'",
						island, weather.getTemperature(), weather.getHumidity(), weather.getPrecipitation(), weather.getWind(), weather.getClouds(), timeStamp);
				statement.execute(updateQuery);
			} else {
				String insertQuery = String.format("INSERT INTO %s VALUES ('%s', %f, %f, %f, %f, %f)",
						island, timeStamp, weather.getTemperature(), weather.getHumidity(), weather.getPrecipitation(), weather.getWind(), weather.getClouds());
				statement.execute(insertQuery);
			}
		}
	}
}
