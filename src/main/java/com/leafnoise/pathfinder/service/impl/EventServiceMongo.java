package com.leafnoise.pathfinder.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.handlers.MessageHandler;
import com.leafnoise.pathfinder.handlers.jms.annotations.JMSMessageHandler;
import com.leafnoise.pathfinder.model.BaseEvent;
import com.leafnoise.pathfinder.mongo.MongoGateway;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewayConfig;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewaySingleton;
import com.leafnoise.pathfinder.service.EventService;

/**
 * Pathfinder Implementation of the EventService API
 * @author Jorge Morando
 * @see EventService
 */
@Named("mongoEventService")
public class EventServiceMongo implements EventService {

	@Inject
	@MongoGatewaySingleton
	@MongoGatewayConfig(collection="events")
	private MongoGateway gateway;
	
	@Inject 
	@JMSMessageHandler
	private MessageHandler handler;
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#save(com.leafnoise.pathfinder.model.Message)
	 */
	@Override
	public void save(BaseEvent event) throws BusinessException{
		gateway.persist(event.toJsonStr());
		if(gateway.hasError())
			throw new BusinessException(gateway.getLastErrorMsg());
	}

	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#getAll()
	 */
	@Override
	public List<BaseEvent> findAll() {
		List<BaseEvent> msgs = null;
		msgs = findBy(null);
		return msgs;
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#get()
	 */
	@Override
	public List<BaseEvent> findBy(Map<String,Object> filter) {
		List<BaseEvent> msgs = null;
		if(filter!=null){
			for (String key : filter.keySet()) { //add "event." prefix to filter keys
				filter.put("event."+key, filter.get(key));
				filter.remove(key);
			}
		}
		msgs = gateway.find(filter);
		return msgs;
	}

	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.EventService#enqueue(java.lang.String)
	 */
	@Override
	public void enqueue(String message) throws BusinessException {
		try {
			handler.enqueue(message);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}
	
}
