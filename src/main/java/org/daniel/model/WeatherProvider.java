package org.daniel.model;

import java.util.List;
public interface WeatherProvider {
	List<Weather> getWeather(Location location);
}
