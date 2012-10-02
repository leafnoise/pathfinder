/**
 * 
 */
package com.leafnoise.pathfinder.handlers.jms;

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.leafnoise.pathfinder.handlers.MessageHandler;
import com.leafnoise.pathfinder.model.PFMessage;

/**
 * @author Jorge Morando
 *
 */
public interface JMSHandler extends MessageHandler{

	void enqueue(PFMessage message) throws JMSException,NamingException;
	
}
