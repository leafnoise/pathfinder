package com.leafnoise.pathfinder.ws.artifacts;

import java.io.Serializable;

/**
 * Generic WSResponse Object.</br>
 * This Object will be converted to JSON and served as a generic response from any WS in the REST Web Service API
 * </br></br>
 * Conversion to JSON will be marshalled by JacksonJSON Library
 * @author Jorge Morando
 */
public class WSResponse implements Serializable{

	private static final long serialVersionUID = -6502804761564563020L;
	private String message;
	private Boolean success;
	
	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}
	/**
	 * @param success the success to set
	 */
	public WSResponse setSuccess(Boolean success) {
		this.success = success;
		return this;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public WSResponse setMessage(String message) {
		this.message = message;
		return this;
	}
}
