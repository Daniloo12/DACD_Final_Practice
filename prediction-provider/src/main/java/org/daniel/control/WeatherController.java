package org.daniel.control;

import org.daniel.model.Location;
import org.daniel.model.Weather;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.TimerTask;

public class WeatherController extends TimerTask {
	private final OpenWeatherMapProvider openWeatherMapProvider;
	private final JMSWeatherStore jmsWeatherStore;

	public WeatherController(OpenWeatherMapProvider openWeatherMapProvider, JMSWeatherStore jmsWeatherStore) {
		this.openWeatherMapProvider = openWeatherMapProvider;
		this.jmsWeatherStore = jmsWeatherStore;
	}

	List<Location> locationList = List.of(
			new Location(28.6835100, -17.7642100, "LaPalma"),
			new Location(28.0916300, -17.1133100, "LaGomera"),
			new Location(27.74350835, -18.0381796182099, "ElHierro"),
			new Location(28.4682400, -16.2546200, "Tenerife"),
			new Location(28.0997300, -15.4134300, "GranCanaria"),
			new Location(28.4050694, -16.5405673, "Fuerteventura"),
			new Location(29.03970805, -13.6362916086041, "Lanzarote"),
			new Location(29.2523295, -13.509064, "LaGraciosa"));

	public void execute() throws SQLException, ParseException {
		for (Location location : locationList) {
			List<Weather> weatherList = openWeatherMapProvider.getWeather(location);
			jmsWeatherStore.save((Weather) weatherList);
		}
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (SQLException | ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
