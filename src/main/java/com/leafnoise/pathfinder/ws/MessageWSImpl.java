package com.leafnoise.pathfinder.ws;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.leafnoise.pathfinder.ws.api.MessageWS;
import com.leafnoise.pathfinder.ws.artifacts.MessageWSResponse;



/**
 * @author Jorge Morando
 *
 */
@Stateless
public class MessageWSImpl implements MessageWS {
	
	
	private static final Logger log = Logger.getLogger(MessageWS.class);
	
	/* (non-Javadoc)
	 * @see JGMWS#send(JGMRawMessage, HttpServletRequest)
	 */
	public MessageWSResponse send(String rawMessage, HttpServletRequest request) {
		
		MessageWSResponse response = new MessageWSResponse();
		
		return response;
	}
	
	
}
