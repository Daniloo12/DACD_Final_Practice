package org.daniel.control;

import org.daniel.exception.EventStoreBuilderException;

public interface EventStore {
	void save(String message) throws EventStoreBuilderException;
}
