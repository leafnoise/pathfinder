package com.leafnoise.pathfinder.service.impl;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.annotations.DefaultMessageHandler;
import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.handlers.MessageHandler;
import com.leafnoise.pathfinder.handlers.jms.annotations.JMS;
import com.leafnoise.pathfinder.model.PFMessage;
import com.leafnoise.pathfinder.mongo.MongoGateway;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewayConfig;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewaySingleton;
import com.leafnoise.pathfinder.service.MessageService;

@Named("mongoMessageService")
public class MessageServiceMongo implements MessageService {

	@Inject
	@MongoGatewaySingleton
	@MongoGatewayConfig(collection="messages")
	private MongoGateway gateway;
	
	@Inject
	@JMS @DefaultMessageHandler
	private MessageHandler handler;
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#save(com.leafnoise.pathfinder.model.Message)
	 */
	@Override
	public void save(PFMessage message) throws BusinessException{
		gateway.persist(message.toJsonStr());
		if(gateway.hasError())
			throw new BusinessException(gateway.getLastErrorMsg());
	}

	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#getAll()
	 */
	@Override
	public List<PFMessage> getAll() {
		List<PFMessage> msgs = null;
		msgs = get(null);
		return msgs;
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#get()
	 */
	@Override
	public List<PFMessage> get(Map<String,Object> filter) {
		List<PFMessage> msgs = null;
		msgs = gateway.find(filter);
		return msgs;
	}

	@Override
	public void enqueue(PFMessage message) throws BusinessException {
		try {
			handler.enqueue(message);
		} catch (Exception e) {
			throw new TechnicalException(e);
		}
	}
	
}
