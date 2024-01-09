package org.daniel.control;

import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.daniel.exception.StoreException;
import org.daniel.model.Booking;

import javax.jms.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.List;

public class JMSHotelStore implements HotelStore {
	private final String brokerUrl = "tcp://localhost:61616";
	private final String topicName = "booking.prediction";
	private Connection connection;
	private Session session;

	@Override
	public void save(List<Booking> bookingList) throws StoreException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(topicName);
			MessageProducer producer = session.createProducer(destination);

			for (Booking booking : bookingList) {
				String serializedBooking = serializeBookingToJson(booking);
				TextMessage message = session.createTextMessage(serializedBooking);
				producer.send(message);
				System.out.println(serializedBooking);
			}
			connection.close();
		} catch (JMSException e) {
			throw new StoreException(e.getMessage());
		}
	}

	public String serializeBookingToJson(Booking booking) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Instant.class, new JMSHotelStore.InstantAdapter())
				.create();
		return gson.toJson(booking);
	}

	private static class InstantAdapter implements JsonSerializer<Instant> {
		@Override
		public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}
	}
}

