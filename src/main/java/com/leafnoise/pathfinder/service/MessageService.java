package com.leafnoise.pathfinder.service;

import java.util.List;
import java.util.Map;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.model.PFMessage;

public interface MessageService {
	
	/**
	 * Enqueues a message in &quot;jms.queue.pathfinder&quot; queue
	 * @param message the message to enqueue
	 */
	void enqueue(PFMessage message) throws BusinessException;
	
	/**
	 * Saves a message
	 * @param message
	 */
	void save(PFMessage message) throws BusinessException;
	
	/**
	 * Retrieves all messages
	 * @return List&lt;Message&gt;
	 */
	List<PFMessage> getAll();
	
	/**
	 * Retrieves messages according to specified filters
	 * @return List&lt;Message&gt;
	 */
	List<PFMessage> get(Map<String,Object> filters);
	
}
