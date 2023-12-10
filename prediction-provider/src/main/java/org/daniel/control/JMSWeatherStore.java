package org.daniel.control;

import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.daniel.exception.StoreException;
import org.daniel.model.Weather;

import java.time.Instant;
import java.util.List;
import javax.jms.*;
import java.lang.reflect.Type;

public class JMSWeatherStore implements WeatherStore {
	private final String topicName = "prediction.Weather";
	private final String brokerUrl = "tcp://localhost:61616";
	private Connection connection;
	private Session session;

	private void establishConnection() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
		connection = connectionFactory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void publishMessagesToTopic(List<Weather> weatherPrediction) throws JMSException {
		Destination destination = session.createTopic(topicName);
		MessageProducer producer = session.createProducer(destination);
		for (Weather weather : weatherPrediction) {
			String weatherSerialized = serializeWeatherToJson(weather);
			TextMessage message = session.createTextMessage(weatherSerialized);
			producer.send(message);
			System.out.println(weatherSerialized);
		}
	}

	private String serializeWeatherToJson(Weather weather) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Instant.class, new CustomInstantAdapter())
				.create();
		return gson.toJson(weather);
	}

	private void closeActiveConnection() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

	@Override
	public void save(List<Weather> weatherPrediction) throws StoreException {
		try {
			establishConnection();
			publishMessagesToTopic(weatherPrediction);
			closeActiveConnection();
		} catch (JMSException e) {
			throw new StoreException(e.getMessage());
		}
	}

	@Override
	public void save(Weather weather) {

	}

	private static class CustomInstantAdapter implements JsonSerializer<Instant> {
		@Override
		public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}
	}
}

