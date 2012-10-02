package com.leafnoise.pathfinder.handlers;

import com.leafnoise.pathfinder.model.PFMessage;

/**
 * @author Jorge Morando
 */
public interface MessageHandler {

	void enqueue(PFMessage message) throws Exception;
	
}
