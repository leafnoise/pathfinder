package com.leafnoise.pathfinder.service;

import java.util.List;

import com.leafnoise.pathfinder.model.Message;

public interface MessageService {
	
	/**
	 * Saves a message
	 * @param message
	 */
	boolean save(Message message);
	
	/**
	 * Retrieves all messages
	 * @return List&lt;Message&gt;
	 */
	List<Message> getAll();
	
}
