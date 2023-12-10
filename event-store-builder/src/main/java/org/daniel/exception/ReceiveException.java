package org.daniel.exception;

import java.io.IOException;

import javax.jms.JMSException;

public class ReceiveException extends JMSException {
	public ReceiveException(String reason, Exception e) {
		super(reason);
	}
}
