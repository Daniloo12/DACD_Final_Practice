package org.daniel.model;

import java.time.Instant;

public class Weather {
	private Instant ts;
	private Location location;
	private double clouds;
	private double wind;
	private double temperature;
	private double precipitation;
	private double humidity;

	public Weather(Instant ts, Location location, double clouds, double wind, double temperature, double precipitation, double humidity) {

		this.ts = ts;
		this.location = location;
		this.clouds = clouds;
		this.wind = wind;
		this.temperature = temperature;
		this.precipitation = precipitation;
		this.humidity = humidity;
	}

	public Instant getTs() {
		return ts;
	}

	public Location getLocation() {
		return location;
	}

	public double getClouds() {
		return clouds;
	}

	public double getWind() {
		return wind;
	}

	public double getTemperature() {
		return temperature;
	}

	public double getPrecipitation() {
		return precipitation;
	}

	public double getHumidity() {
		return humidity;
	}
}
