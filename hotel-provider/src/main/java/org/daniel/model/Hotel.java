package org.daniel.model;

public class Hotel {
	private String location;
	private String title;
	private String identifier;
	private String area;

	public Hotel(String location, String title, String identifier, String area) {
		this.location = location;
		this.title = title;
		this.identifier = identifier;
		this.area = area;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public String getArea() {
		return area;
	}
}
