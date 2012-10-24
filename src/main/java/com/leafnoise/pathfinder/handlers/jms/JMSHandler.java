/**
 * 
 */
package com.leafnoise.pathfinder.handlers.jms;

import javax.jms.JMSException;
import javax.naming.NamingException;

import com.leafnoise.pathfinder.handlers.MessageHandler;

/**
 * @author Jorge Morando
 *
 */
public interface JMSHandler extends MessageHandler{

	void enqueue(String message) throws JMSException,NamingException;
	
}
