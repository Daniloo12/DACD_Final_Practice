package org.daniel.model;

import java.time.Instant;

public class Weather {
	private Instant ts;
	private Location location;
	private float clouds;
	private double precipitation;
	private double temperature;

	public Weather(Instant ts, Location location, float clouds, double precipitation, double temperature) {
		this.ts = ts;
		this.location = location;
		this.clouds = clouds;
		this.precipitation = precipitation;
		this.temperature = temperature;

	}
	public Instant ts() {
		return ts;
	}

	public Location getLocation() {
		return location;
	}
	public float getClouds() {
		return clouds;
	}
	public double getPrecipitation() {
		return precipitation;
	}
	public double getTemperature() {
		return temperature;
	}
}
