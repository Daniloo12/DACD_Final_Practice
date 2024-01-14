package org.daniel.control;

import org.daniel.exception.ReceiveException;
import org.daniel.view.BookingCalc;
import org.daniel.view.Interface;
import org.daniel.view.WeatherCalc;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws ReceiveException, SQLException {
		MessageHandler messageHandler = new MessageHandler("prediction.Weather", new DataHandler(args[0], new ModelBuilder()));
		MessageHandler messageHandler2 = new MessageHandler("prediction.Booking", new DataHandler(args[0], new ModelBuilder()));
		messageHandler.init();
		messageHandler2.init();

		new Interface(new WeatherCalc(args[0]), new BookingCalc(args[0]));
	}
}

