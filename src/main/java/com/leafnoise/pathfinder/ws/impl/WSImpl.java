package com.leafnoise.pathfinder.ws.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.model.BaseEvent;
import com.leafnoise.pathfinder.service.EventService;
import com.leafnoise.pathfinder.service.OntologyService;
import com.leafnoise.pathfinder.ws.WS;
import com.leafnoise.pathfinder.ws.artifacts.EventWSResponse;
import com.leafnoise.pathfinder.ws.artifacts.WSResponse;



/**
 * Pathfinder Implementation of the Web Service API
 * @author Jorge Morando
 * @see WS
 */
public class WSImpl implements WS {
	
	@Inject
	Logger log;
	
	@Inject
	@Named("mongoEventService")
	private EventService es;
	
	@Inject
	@Named("ontologyService")
	private OntologyService os;
	
	
	
	/* (non-Javadoc)
	 * @see JGMWS#send(JGMRawMessage, HttpServletRequest)
	 */
	@Override
	public WSResponse send(String rawMessage, HttpServletRequest request) {
		WSResponse response = new WSResponse();
		
		try {
			es.enqueue(rawMessage);
			response.setMessage("Mensaje enviado:" + new Date().toString());
			response.setSuccess(true);
		} catch (BusinessException e) {
			log.error("Error trying to send message");
			response.setMessage("El mensaje no se pudo enviar: "+e.getMessage());
			response.setSuccess(false);
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receive(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receive(Map<String,Object> filters, HttpServletRequest request) {
		log.info("-Started WS logic");
		log.debug("processing filters...");
		
		List<BaseEvent> evts = null;
		String fileName = null;
		try {
			if(filters == null || filters.size()==0)
				evts = es.findAll();
			else
				evts = es.findBy(filters);
			
			fileName = os.create(evts);
		} catch (TechnicalException e) {
			String msg = "Error trying to retrieve events";
			log.error(msg,e);
			WSResponse response = new WSResponse();
			response.setMessage(msg +": "+e.getMessage());
			response.setSuccess(false);
			return response;
		} catch (BusinessException e) {
			String msg = "Error trying to retrieve events";
			log.error(msg,e);
			WSResponse response = new WSResponse();
			response.setMessage(msg +": "+e.getMessage());
			response.setSuccess(false);
			return response;
		}
		log.info("-Finished WS logic.");
		if(evts == null || evts.size()==0){
			WSResponse response = new WSResponse();
			response.setMessage("No events found");
			response.setSuccess(true);
			return response;
		}else{
			String ctxPath = request.getContextPath();
			String server = request.getRequestURL().toString().split(ctxPath)[0];
			EventWSResponse response = new EventWSResponse();
			response.setEvents(evts);
			response.setOntologyURL(server + ctxPath + fileName);
			response.setMessage("Found "+evts.size()+" Events Total");
			response.setSuccess(true);
			return response;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receive(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receiveAll(HttpServletRequest request) {
		return receiveTypeFromAgent(null,null,request);
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receiveFrom(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receiveFrom(String agent, HttpServletRequest request) {
		log.debug("Receiving from agent: "+agent);
		return receiveTypeFromAgent(agent,null,request);
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receiveType(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receiveType(String type, HttpServletRequest request) {
		log.debug("Receiving with type: "+type);
		return receiveTypeFromAgent(null,type,request);
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receive(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receiveTypeFromAgent(String type, String agent, HttpServletRequest request) {
		log.info("-Started WS logic");
		log.debug("processing filters...");
		Map<String,Object> filters = new HashMap<String,Object>();
		//both agent and type are compulsory attributes of the event
		if(agent!=null){
			log.debug("Adding \"agent\"="+agent+" filter to query");
			filters.put("agent",agent);
		}
		if(type!=null){
			log.debug("Adding \"type\"="+type+" filter to query");
			filters.put("type",type);
		}
		return receive(filters, request);
	}
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.ws.api.WS#receive(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public WSResponse receiveBetweenTimes(String first, String last, HttpServletRequest request) {
		log.info("-Started WS logic");
		log.debug("processing filters...");
		
		Long firstLong = null;
		Long lastLong = null;
		try {
			firstLong = Long.parseLong(first);
		} catch (NumberFormatException e) {
			WSResponse response = new WSResponse();
			BusinessException be = new BusinessException("Error trying to parse Long attribute "+first,e);
			log.error(be);
			response.setSuccess(false);
			response.setMessage(be.getMessage());
			return response;
		}
		
		try {
			lastLong = Long.parseLong(last);
		} catch (NumberFormatException e) {
			WSResponse response = new WSResponse();
			BusinessException be = new BusinessException("Error trying to parse Long attribute "+last,e);
			log.error(be);
			response.setSuccess(false);
			response.setMessage(be.getMessage());
			return response;
		}
		
		Map<String,Object> filters = new HashMap<String,Object>();
		
		if(first!=null){
			log.debug("Adding \"first\"="+first+" filter to query");
			filters.put("$gte",firstLong);
		}
		if(last!=null){
			log.debug("Adding \"last\"="+last+" filter to query");
			filters.put("$lte",lastLong);
		}
		Map<String,Object> filter = new HashMap<String, Object>();
		filter.put("time",filters);
		return receive(filter, request);
	}
}
