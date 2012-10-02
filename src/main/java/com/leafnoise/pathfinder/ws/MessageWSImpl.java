package com.leafnoise.pathfinder.ws;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.model.PFMessage;
import com.leafnoise.pathfinder.service.MessageService;
import com.leafnoise.pathfinder.vo.MessageHeader;
import com.leafnoise.pathfinder.ws.api.MessageWS;
import com.leafnoise.pathfinder.ws.artifacts.MessageWSResponse;



/**
 * @author Jorge Morando
 *
 */
public class MessageWSImpl implements MessageWS {
	
	
	private static final Logger log = Logger.getLogger(MessageWS.class);
	
	@Inject
	@Named("mongoMessageService")
	private MessageService mms;
	
	/* (non-Javadoc)
	 * @see JGMWS#send(JGMRawMessage, HttpServletRequest)
	 */
	public MessageWSResponse send(String rawMessage, HttpServletRequest request) {
		PFMessage msg = null;
		MessageWSResponse response = new MessageWSResponse();
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			//JSON TreeNode managing
			JsonNode rootNode = jsonMapper.readValue(rawMessage, JsonNode.class);
			
			//Header and Payload Nodes
			JsonNode headerNode = rootNode.path("header");
			JsonNode payloadNode = rootNode.path("payload");
			
			//Parse header node to header java object
			MessageHeader header = jsonMapper.readValue(headerNode, MessageHeader.class);
			header.setSource(headerNode.toString());
			
			String payload = payloadNode.toString();
			
			//Construct the Message
			msg = new PFMessage(header,payload);
		}catch (Exception e) {
			response.setMessage(e.getMessage());
			response.setSuccess(false);
			return response;
		}
		
		try {
			mms.enqueue(msg);
			response.setMessage("Mensaje enviado:" + new Date().toString());
			response.setSuccess(true);
		} catch (BusinessException e) {
			log.error("Error trying to send message");
			response.setMessage("El mensaje no se pudo enviar: "+e.getMessage());
			response.setSuccess(false);
		}
		return response;
	}
}
