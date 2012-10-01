package com.leafnoise.pathfinder.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.leafnoise.pathfinder.model.Message;
import com.leafnoise.pathfinder.mongo.MongoGateway;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewaySingleton;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewayConfig;
import com.leafnoise.pathfinder.service.MessageService;

@Named("mongoMessageService")
public class MessageServiceMongo implements MessageService {

	@Inject
	@MongoGatewaySingleton
	@MongoGatewayConfig(collection="messages")
	private MongoGateway gateway;
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#save(com.leafnoise.pathfinder.model.Message)
	 */
	@Override
	public boolean save(Message message) {
		gateway.persist(message.toMap());
		return gateway.hasError();
	}

	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.service.MessageService#getAll()
	 */
	@Override
	public List<Message> getAll() {
		return null;
	}
	
}
