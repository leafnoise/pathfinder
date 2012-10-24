package com.leafnoise.pathfinder.ws.artifacts;

import java.io.Serializable;
import java.util.List;

import com.leafnoise.pathfinder.model.BaseEvent;

/**
 * EventWSResponse Object.</br>
 * This Object will get converted to JSON and be served as a response in an Event Listing REST Web Service in the REST Web service API
 * </br></br> Conversion to JSON will be marshalled by JacksonJSON Library
 * @author Jorge Morando
 */
public class EventWSResponse extends WSResponse implements Serializable{

	private static final long serialVersionUID = -7801570664066705117L;
	private List<BaseEvent> events;
	private String ontologyURL;
	
	/**
	 * @return the events
	 */
	public List<BaseEvent> getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(List<BaseEvent> events) {
		this.events = events;
	}
	/**
	 * @return the ontologyURL
	 */
	public String getOntologyURL() {
		return ontologyURL;
	}
	/**
	 * @param ontologyURL the ontologyURL to set
	 */
	public void setOntologyURL(String ontologyURL) {
		this.ontologyURL = ontologyURL;
	}
}
