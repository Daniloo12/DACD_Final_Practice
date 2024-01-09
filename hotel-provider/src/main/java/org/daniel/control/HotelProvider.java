package org.daniel.control;

import org.daniel.model.Booking;

import java.util.List;

public interface HotelProvider {
	List<Booking> getBooking();
}
