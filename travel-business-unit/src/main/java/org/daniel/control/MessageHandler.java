package org.daniel.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.daniel.exception.ReceiveException;

import javax.jms.*;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.MessageConsumer;
import javax.jms.JMSException;

public class MessageHandler {
	private static String brokerUrl = "tcp://localhost:61616";
	private String topicName;
	private Connection connection;
	private Session session;
	private DataHandler dataHandler;
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public MessageHandler(String topicName, DataHandler dataHandler) {
		this.topicName = topicName;
		this.dataHandler = dataHandler;
	}

	public void init() throws ReceiveException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			connection = (Connection) connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageConsumer consumer = session.createConsumer(session.createTopic(topicName));

			startScheduledTask();

			consumer.setMessageListener(this::processMessage);
		} catch (JMSException e) {
			throw new ReceiveException(e.getMessage(), e);
		}
	}

	private void processMessage(javax.jms.Message message) {
		try {
			String text = ((TextMessage) message).getText();
			System.out.println("Received message: " + text);

			if (topicName.substring(11).equals("Weather")) {
				dataHandler.addWeatherData(text);
			} else {
				dataHandler.addHotelData(text);
			}

		} catch (JMSException | SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void startScheduledTask() {
		scheduler.scheduleAtFixedRate(this::clearTable, 0, 6, TimeUnit.HOURS);
	}

	private void clearTable() {
		dataHandler.removeTable(topicName);
		System.out.println("Table successfully cleared.");
	}
}

