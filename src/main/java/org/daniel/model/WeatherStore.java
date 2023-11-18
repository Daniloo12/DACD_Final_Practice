package org.daniel.model;

import java.sql.SQLException;

public interface WeatherStore {
	void save(Weather weather) throws SQLException;
}
