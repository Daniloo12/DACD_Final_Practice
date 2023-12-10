package org.daniel.control;

import org.daniel.exception.StoreException;
import org.daniel.model.Weather;

import java.util.List;


public interface WeatherStore {
	void save(List<Weather> weatherPrediction) throws StoreException;

	void save(Weather weather);
}
