package org.daniel.control;

import org.daniel.exception.ReceiveException;


public class Main {
	public static void main(String[] args) throws ReceiveException {
		JMSSubscriber mapSubscriber = new JMSSubscriber(new EventBuilder(args[0]));
		mapSubscriber.start();
	}
}