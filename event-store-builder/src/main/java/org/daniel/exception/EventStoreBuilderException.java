package org.daniel.exception;

import java.io.IOException;

public class EventStoreBuilderException extends IOException {
	public EventStoreBuilderException(String message, Throwable cause) {
		super(message, cause);
	}
}
