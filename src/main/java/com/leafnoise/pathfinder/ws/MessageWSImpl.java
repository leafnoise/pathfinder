package com.leafnoise.pathfinder.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.model.PFMessage;
import com.leafnoise.pathfinder.service.MessageService;
import com.leafnoise.pathfinder.vo.MessageHeader;
import com.leafnoise.pathfinder.ws.api.MessageWS;
import com.leafnoise.pathfinder.ws.artifacts.MessageWSResponse;
import com.leafnoise.pathfinder.ws.artifacts.WSResponse;



/**
 * @author Jorge Morando
 *
 */
public class MessageWSImpl implements MessageWS {
	
	@Inject
	Logger log;
	
	@Inject
	@Named("mongoMessageService")
	private MessageService mms;
	
	/* (non-Javadoc)
	 * @see JGMWS#send(JGMRawMessage, HttpServletRequest)
	 */
	public WSResponse send(String rawMessage, HttpServletRequest request) {
		PFMessage msg = null;
		WSResponse response = new WSResponse();
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			//JSON TreeNode managing
			JsonNode rootNode = jsonMapper.readValue(rawMessage, JsonNode.class);
			
			//Header and Payload Nodes
			JsonNode headerNode = rootNode.path("header");
			JsonNode payloadNode = rootNode.path("payload");
			
			
			
			//Parse header node to header java object
			MessageHeader header = jsonMapper.readValue(headerNode, MessageHeader.class);
			
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

	@Override
	public WSResponse receive(HttpServletRequest request) {
		return receive(null,request);
	}
	
	@Override
	public WSResponse receive(String from, HttpServletRequest request) {
		return receive(from,null,request);
	}
	
	@Override
	public WSResponse receive(String from, String type, HttpServletRequest request) {
		Map<String,Object> filters = new HashMap<String,Object>();
		if(from!=null){
			log.info("Adding \"from\"="+from+" filter to query");
			filters.put("message.header.from",from);
		}
		if(type!=null){
			log.info("Adding \"type\"="+type+" filter to query");
			filters.put("message.header.type",type);
		}
		
		List<PFMessage> msgs = null;
		
		try {
			if(filters==null || filters.size()==0)
				msgs = mms.getAll();
			else
				msgs = mms.get(filters);
		} catch (TechnicalException e) {
			String msg = "Error trying to retrieve all messages";
			log.error(msg,e);
			WSResponse response = new WSResponse();
			response.setMessage(msg +": "+e.getMessage());
			response.setSuccess(false);
			return response;
		}
		
		if(msgs == null || msgs.size()==0){
			WSResponse response = new WSResponse();
			response.setMessage("No messages found");
			response.setSuccess(true);
			return response;
		}else{
			MessageWSResponse response = new MessageWSResponse();
			response.setMessages(msgs);
			response.setMessage("Found "+msgs.size()+" Messages Total");
			response.setSuccess(true);
			return response;
		}
	}
}
