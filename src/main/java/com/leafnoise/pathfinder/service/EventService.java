package com.leafnoise.pathfinder.service;

import java.util.List;
import java.util.Map;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.model.BaseEvent;

/**
 * Service Interface that handles Event Business Logic throughout the application
 * @author Jorge Morando
 *
 */
public interface EventService {
	
	/**
	 * Enqueues an event in &quot;jms.queue.pathfinder&quot; queue
	 * @param message the message to enqueue
	 */
	void enqueue(String event) throws BusinessException;
	
	/**
	 * Saves an event instance
	 * @param message
	 */
	void save(BaseEvent event) throws BusinessException;
	
	/**
	 * Retrieves all events
	 * @return List&lt;Message&gt;
	 */
	List<BaseEvent> findAll();
	
	/**
	 * Retrieves messages according to specified filters
	 * @return List&lt;Message&gt;
	 */
	List<BaseEvent> findBy(Map<String,Object> filters);
	
}
