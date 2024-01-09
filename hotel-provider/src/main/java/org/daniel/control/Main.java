package org.daniel.control;
import java.util.Timer;

public class Main {
	public static void main(String[] args) {
		Timer timer = new Timer();
		long interval = 6 * 60 * 60 * 1000;
		HotelController hotelController = new HotelController(new XoteloAPIProvider("Hotel.tsv"), new JMSHotelStore());
		timer.schedule(hotelController, 0, interval);
	}
}
