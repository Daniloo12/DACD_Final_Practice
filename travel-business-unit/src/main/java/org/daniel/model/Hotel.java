package org.daniel.model;
import java.time.Instant;

public class Hotel {
	private String hotelName;
	private String web;
	private double price;
	private Instant arrival;
	private Location location;

	public Hotel(String hotelName, String web, double price, Instant arrival, Location location) {
		this.hotelName = hotelName;
		this.web = web;
		this.price = price;
		this.arrival = arrival;
		this.location = location;
	}

	public String getHotelName() {
		return hotelName;
	}

	public String getWeb() {
		return web;
	}

	public double getPrice() {
		return price;
	}

	public Instant getArrival() {
		return arrival;
	}

	public Location getLocation() {return location;}
}