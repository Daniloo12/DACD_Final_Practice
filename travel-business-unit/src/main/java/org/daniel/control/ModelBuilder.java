package org.daniel.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.daniel.model.Hotel;
import org.daniel.model.Location;
import org.daniel.model.Weather;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ModelBuilder {
	public Weather buildWeatherData(String jsonData) {
		JsonObject responseJson = new Gson().fromJson(jsonData, JsonObject.class);
		Instant instant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(responseJson.get("predictionTs").getAsString()));
		Location location = getLocation(responseJson.getAsJsonObject("location"));
		return new Weather(instant, location, responseJson.get("clouds").getAsInt(),
				responseJson.get("precipitation").getAsDouble(), responseJson.get("temperature").getAsDouble());
	}

	public Hotel buildHotelData(String jsonData) {
		JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);
		JsonObject hotelObject = jsonObject.getAsJsonObject("hotel");
		Instant instant = LocalDate.parse(jsonObject.get("checkIn").getAsString()).atStartOfDay(ZoneId.systemDefault()).toInstant();
		Location location = getLocation(hotelObject);
		return new Hotel(hotelObject.get("name").getAsString(), jsonObject.get("platform").getAsString(),
				jsonObject.get("price").getAsDouble(), instant, location);
	}

	private Location getLocation(JsonObject locationObject) {
		return new Location(locationObject.get("zone").getAsString(), locationObject.get("island").getAsString());
	}
}

