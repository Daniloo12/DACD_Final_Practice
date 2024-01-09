package org.daniel.control;

import org.daniel.exception.ReceiveException;
import org.daniel.view.InfoProvider;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws ReceiveException, SQLException {
		MessageHandler messageHandler = new MessageHandler("prediction.Weather", new DataHandler(args[0], new ModelBuilder()));
		MessageHandler messageHandler2 = new MessageHandler("prediction.Booking", new DataHandler(args[0], new ModelBuilder()));
		messageHandler.init();
		messageHandler2.init();

		InfoProvider infoProvider = new InfoProvider();
	}
}

