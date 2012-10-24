package com.leafnoise.pathfinder.handlers;



/**
 * Application Interface that handles the JMS message
 * @author Jorge Morando
 */
public interface MessageHandler {

	/**
	 * 
	 * @param message The String representation of the un-marshalled message to be enqueued as a TextMessage in HornetQ queue.
	 * @throws Exception 
	 */
	void enqueue(String message) throws Exception;
	
}
