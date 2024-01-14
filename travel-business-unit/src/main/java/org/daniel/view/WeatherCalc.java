package org.daniel.view;

import org.sqlite.SQLiteException;
import org.daniel.control.DbConnector;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.text.DecimalFormat;

public class WeatherCalc {

	private Connection connection;
	private DbConnector dbConnector;
	private String dbPath;

	public WeatherCalc(String arg) throws SQLiteException {
		this.dbPath = dbPath;
		this.connection = dbConnector.connectDatabase(dbPath);
	}

	public String weatherCalc(String checkIn, String checkOut, String island) {
		Map<String, Map<String, Double>> dataByZone = fetchData(checkIn, checkOut, island);

		if (dataByZone.isEmpty()) {
			return "No weather data available.";
		}

		if (!isDateAvailable(checkIn) || !isDateAvailable(checkOut)) {
			return "No weather data available.";
		}

		return generateWeatherReport(dataByZone);
	}

	private Map<String, Map<String, Double>> fetchData(String checkIn, String checkOut, String island) {
		String sql = "SELECT island, zone, AVG(temperature) AS avg_temperature, AVG(precipitation) AS avg_precipitation, AVG(clouds) AS avg_clouds FROM prediction_Weather WHERE predictionTime BETWEEN ? AND ? AND island = ? GROUP BY island, zone";
		Map<String, Map<String, Double>> dataByZone = new HashMap<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, checkIn);
			preparedStatement.setString(2, checkOut);
			preparedStatement.setString(3, island);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					String zone = resultSet.getString("zone");
					double avgTemperature = resultSet.getDouble("avg_temperature");
					double avgPrecipitation = resultSet.getDouble("avg_precipitation");
					double avgClouds = resultSet.getDouble("avg_clouds");

					dataByZone.computeIfAbsent(zone, k -> new HashMap<>())
							.put("avgTemperature", avgTemperature);
					dataByZone.get(zone).put("avgPrecipitation", avgPrecipitation);
					dataByZone.get(zone).put("avgClouds", avgClouds);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error processing weather data.");
		}

		return dataByZone;
	}

	private String generateWeatherReport(Map<String, Map<String, Double>> dataByZone) {
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		StringBuilder weatherResult = new StringBuilder();

		dataByZone.forEach((zone, averagesByZone) ->
				weatherResult.append("Zone: ").append(zone)
						.append(", Average temperature: ").append(decimalFormat.format(averagesByZone.get("avgTemperature")))
						.append(" (").append(categorizeTemp(averagesByZone.get("avgTemperature"))).append(")")
						.append(", Average precipitation: ").append(decimalFormat.format(averagesByZone.get("avgPrecipitation")))
						.append(" (").append(categorizePrep(averagesByZone.get("avgPrecipitation"))).append(")")
						.append(", Average clouds: ").append(decimalFormat.format(averagesByZone.get("avgClouds")))
						.append(" (").append(categorizeClouds(averagesByZone.get("avgClouds"))).append(")\n")
		);

		return weatherResult.toString();
	}

	private boolean isDateAvailable(String date) {
		String sql = "SELECT COUNT(*) FROM prediction_Weather WHERE predictionTime = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, date);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int count = resultSet.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static String categorizePrep(double precipitation) {
		if (precipitation < 7.5) {
			return "Exceptional";
		} else if (precipitation < 12.5) {
			return "Fine";
		} else {
			return "Poor";
		}
	}

	private static String categorizeTemp(double temperature) {
		if (temperature >= 30.0) {
			return "Exceptional";
		} else if (temperature >= 20.0) {
			return "Fine";
		} else {
			return "Poor";
		}
	}

	private static String categorizeClouds(double clouds) {
		if (clouds < 25.0) {
			return "Exceptional";
		} else if (clouds < 65.0) {
			return "Fine";
		} else {
			return "Poor";
		}
	}
}