package org.daniel.model;

public class Location {
	private double lat;
	private double lon;
	private String island;

	public Location(double lat, double lon, String island) {
		this.lat = lat;
		this.lon = lon;
		this.island = island;
	}

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lon;
	}

	public String getIsland() {
		return island;
	}
}
