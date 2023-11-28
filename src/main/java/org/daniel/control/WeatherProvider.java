package org.daniel.control;

import org.daniel.model.Location;
import org.daniel.model.Weather;

import java.util.List;

public interface WeatherProvider {
	List<Weather> getWeather(Location location);
}
