package org.daniel.control;

import org.daniel.exception.StoreException;
import org.daniel.model.Booking;

import java.util.List;
import java.util.TimerTask;

public class HotelController extends TimerTask {
	private XoteloAPIProvider xoteloAPIProvider;
	private JMSHotelStore jmsHotelStore;

	public HotelController(XoteloAPIProvider xoteloAPIProvider, JMSHotelStore jmsHotelStore) {
		this.xoteloAPIProvider = xoteloAPIProvider;
		this.jmsHotelStore = jmsHotelStore;
	}

	private void processHotel() {
		try {
			List<Booking> bookings = xoteloAPIProvider.getBooking();
			for (Booking booking : bookings) {
				System.out.println("DISPLAY OBJECT: " + booking.toString());
			}
			jmsHotelStore.save(bookings);
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		processHotel();
	}
}

