package org.daniel.control;

import org.daniel.exception.ReceiveException;

public interface EventSubscriber {
	void start() throws ReceiveException;
}
