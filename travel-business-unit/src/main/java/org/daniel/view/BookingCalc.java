package org.daniel.view;

import org.daniel.control.DbConnector;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.time.LocalTime;
import java.sql.ResultSet;

public class BookingCalc {

	private Connection connection;
	private DbConnector dbConnector;
	private String dbPath;

	public BookingCalc(String dbPath) throws SQLException {
		this.dbPath = dbPath;
		this.connection = dbConnector.connectDatabase(dbPath);
	}

	public String bookingCalc(String checkIn, String checkOut, String island) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalTime noon = LocalTime.of(12, 0);
		String sql = "SELECT island, zone, hotel_name, platform, price AS price FROM prediction_Booking WHERE check_in = ? AND island = ? GROUP BY island, zone, hotel_name, platform";
		StringBuilder bookingResult = new StringBuilder();
		boolean dataAvailable = fetchBookingDetails(sql, checkIn, checkOut, island, bookingResult);

		if (isPastNoonToday(checkIn, currentDateTime, noon)) {
			return "Due to the current time being past 12:00:00, today's booking will be scheduled for tomorrow.";
		}

		LocalDateTime limitDateTime = calculateBookingLimit(currentDateTime, noon);

		if (isCheckOutAfterLimit(checkOut, limitDateTime)) {
			return "Booking is limited to a maximum of 5 days.";
		}

		if (!dataAvailable) {
			return "There is no available data for the booking.";
		}

		return bookingResult.toString();
	}

	private boolean isPastNoonToday(String checkIn, LocalDateTime currentDateTime, LocalTime noon) {
		return checkIn.equals(currentDateTime.toLocalDate().toString()) && currentDateTime.toLocalTime().isAfter(noon);
	}

	private LocalDateTime calculateBookingLimit(LocalDateTime currentDateTime, LocalTime noon) {
		LocalDateTime limitDateTime = currentDateTime.toLocalDate().atTime(23, 59, 59);

		if (currentDateTime.toLocalTime().isAfter(noon)) {
			limitDateTime = limitDateTime.plusDays(1);
		}

		return limitDateTime.plusDays(4);
	}

	private boolean isCheckOutAfterLimit(String checkOut, LocalDateTime limitDateTime) {
		return LocalDateTime.parse(checkOut + "T23:59:59").isAfter(limitDateTime);
	}

	private boolean fetchBookingDetails(String sql, String checkIn, String checkOut, String island, StringBuilder bookingResult) {
		boolean dataAvailable = false;

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, checkIn);
			preparedStatement.setString(2, island);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				while (resultSet.next()) {
					dataAvailable = true;
					String zone = resultSet.getString("zone");
					String hotel = resultSet.getString("hotel_name");
					String platform = resultSet.getString("platform");
					double price = resultSet.getDouble("price");

					LocalDate checkInDay = LocalDate.parse(checkIn);
					LocalDate checkOutDay = LocalDate.parse(checkOut);
					long nights = ChronoUnit.DAYS.between(checkInDay, checkOutDay);

					double totalPrice = price * nights;

					bookingResult.append("Zone: ").append(zone)
							.append(", Hotel: ").append(hotel)
							.append(", Platform: ").append(platform)
							.append(", Total price for ").append(nights).append(" nights: ").append(totalPrice).append("\n");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error processing booking data.");
		}

		return dataAvailable;
	}
}