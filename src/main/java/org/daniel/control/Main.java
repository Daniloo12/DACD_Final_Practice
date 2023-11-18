package org.daniel.control;

import java.sql.SQLException;
import java.util.Timer;

public class Main {
		public static void main(String[] args) throws SQLException {

			Timer timer = new Timer();
			long interval = 6 * 60 * 60 * 1000;

			WeatherController weatherController = new WeatherController(new OpenWeatherMapProvider(args[0]), new SQLiteWeatherStore(args[1]));
			timer.schedule(weatherController, 0, interval);
		}
	}
