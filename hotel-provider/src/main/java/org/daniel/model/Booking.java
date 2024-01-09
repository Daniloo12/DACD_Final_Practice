package org.daniel.model;

import java.time.Instant;

public class Booking {

	private Instant ts;
	private String ss;
	private Hotel hotel;
	private String arrival;
	private String departure;
	private String platformUsed;
	private Double totalPrice;

	public Booking(Hotel hotel, String arrival, String departure, String platformUsed, Double totalPrice) {
		this.ts = Instant.now();
		this.ss = "hotel-provider";
		this.hotel = hotel;
		this.arrival = arrival;
		this.departure = departure;
		this.platformUsed = platformUsed;
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "Booking{" +
				"ts=" + ts +
				", ss='" + ss + '\'' +
				", hotel=" + hotel.getName() +
				", arrival='" + arrival + '\'' +
				", departure='" + departure + '\'' +
				", platformUsed='" + platformUsed + '\'' +
				", totalPrice=" + totalPrice +
				'}';
	}
}

