package org.daniel.view;

import java.sql.SQLException;
import java.util.Scanner;

public class Interface {

	private WeatherCalc weatherCalc;
	private BookingCalc bookingCalc;
	private Scanner scanner;

	public Interface(WeatherCalc weatherCalculator, BookingCalc bookingCalc) {
		this.weatherCalc = weatherCalculator;
		this.bookingCalc = bookingCalc;
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		System.out.println("Welcome to Travel Planner!");

		System.out.print("Select an island (GranCanaria, Tenerife, LaPalma, LaGomera, ElHierro, Lanzarote, Fuerteventura, LaGraciosa): ");
		String selectedIsland = scanner.nextLine();

		System.out.print("Enter check-in date (yyyy-MM-dd): ");
		String checkInDate = scanner.nextLine();

		System.out.print("Enter check-out date (yyyy-MM-dd): ");
		String checkOutDate = scanner.nextLine();

		String weatherResult = weatherCalc.weatherCalc(checkInDate, checkOutDate, selectedIsland);
		String bookingResult = bookingCalc.bookingCalc(checkInDate, checkOutDate, selectedIsland);

		System.out.println("\nThis is the proposal for your trip to the selected island: " + selectedIsland +
				" on the selected check-in: " + checkInDate + " and check-out: " + checkOutDate + ":\n\n" +
				"Weather Prediction:\n" + weatherResult + "\n\nBooking Details:\n" + bookingResult);
	}

	public static void main(String[] args) throws SQLException {
		WeatherCalc weatherCalc = new WeatherCalc(args[0]);
		BookingCalc bookingCalculator = new BookingCalc(args[0]);

		Interface cli = new Interface(weatherCalc, bookingCalculator);
		cli.start();
	}
}