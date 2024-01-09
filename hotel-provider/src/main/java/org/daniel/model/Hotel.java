package org.daniel.model;

public class Hotel {
	private String location;
	private String name;
	private String identifier;
	private String area;

	public Hotel(String location, String name, String identifier, String area) {
		this.location = location;
		this.name = name;
		this.identifier = identifier;
		this.area = area;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String getArea() {
		return area;
	}
}
