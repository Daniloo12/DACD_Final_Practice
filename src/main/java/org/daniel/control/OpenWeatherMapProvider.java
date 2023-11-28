package org.daniel.control;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.daniel.model.Location;
import org.daniel.model.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapProvider {
	private final String apikey;

	public OpenWeatherMapProvider(String apikey) {

		this.apikey = apikey;
	}

	public List<Weather> getWeather(Location location) {
		try {
			String apiCall = "https://api.openweathermap.org/data/2.5/forecast" +
					"?lat=" + location.getLatitude() +
					"&lon=" + location.getLongitude() +
					"&units=metric" +
					"&appid=" + apikey;

			Document weatherDocument = Jsoup.connect(apiCall).ignoreContentType(true).get();
			String information = weatherDocument.text();
			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(information, JsonObject.class);
			JsonArray list = jsonObject.getAsJsonArray("list");
			List<Weather> weatherList = new ArrayList<>();

			for (int i = 0; i < list.size(); i++) {
				JsonObject listItem = list.get(i).getAsJsonObject();
				String hour = String.valueOf(listItem.get("dt_txt")).substring(12, 20);
				Instant ts = Instant.ofEpochSecond(list.get(i).getAsJsonObject().get("dt").getAsLong());
				if (hour.equals("12:00:00")) {
					double clouds = listItem.get(String.valueOf(i)).getAsJsonObject().getAsJsonObject("clouds").get("all").getAsDouble();
					double wind = listItem.get(String.valueOf(i)).getAsJsonObject().getAsJsonObject("wind").get("speed").getAsDouble();
					double temperature = listItem.get(String.valueOf(i)).getAsJsonObject().getAsJsonObject("main").get("temp").getAsDouble();
					double precipitation = listItem.get(String.valueOf(i)).getAsJsonObject().get("pop").getAsDouble();
					double humidity = listItem.get(String.valueOf(i)).getAsJsonObject().getAsJsonObject("main").get("humidity").getAsDouble();

					Weather weather = new Weather(ts, location, clouds, wind, temperature, precipitation, humidity);
					weatherList.add(weather);
				}
			}
			return weatherList;

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}
