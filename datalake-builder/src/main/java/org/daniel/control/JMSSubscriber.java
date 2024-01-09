package org.daniel.control;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.daniel.exception.EventStoreBuilderException;
import org.daniel.exception.ReceiveException;


public class JMSSubscriber implements EventSubscriber {
	private EventBuilder eventBuilder;
	private final String brokerUrl = "tcp://localhost:61616";
	private final String topicName = "prediction.Weather";
	private Session session;
	private Connection connection;

	public JMSSubscriber(EventBuilder weatherEventStore) {
		this.eventBuilder = eventBuilder;
	}

	private void handleMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				String text = ((TextMessage) message).getText();
				System.out.println("Message received: " + text);
				eventBuilder.save(text);
			} else {
				throw new RuntimeException("Invalid message received");
			}
		} catch (JMSException | EventStoreBuilderException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void start() throws ReceiveException {
		try {
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
			connection = connectionFactory.createConnection();
			connection.setClientID("weather-provider");
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(topicName);
			MessageConsumer consumer = session.createDurableSubscriber(topic, "weather-provider_" + topicName);
			consumer.setMessageListener(this::handleMessage);
		} catch (JMSException e) {
			throw new ReceiveException(e.getMessage(), e);
		}
	}

}
