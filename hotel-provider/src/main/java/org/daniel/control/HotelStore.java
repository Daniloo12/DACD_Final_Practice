package org.daniel.control;

import org.daniel.exception.StoreException;
import org.daniel.model.Booking;

import java.util.List;

public interface HotelStore {
	void save(List<Booking> bookingList) throws StoreException;

}
