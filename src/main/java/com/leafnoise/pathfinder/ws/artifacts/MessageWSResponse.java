package com.leafnoise.pathfinder.ws.artifacts;

import java.io.Serializable;

/**
 * Generic MessageWSResponse. Conversion to JSON will be marshalled by JacksonJSON Library
 * @author Jorge Morando
 */
public class MessageWSResponse implements Serializable{

	private static final long serialVersionUID = -2845618712511648250L;

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
	public void setSuccess(Boolean success) {
		this.success = success;
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
	public void setMessage(String message) {
		this.message = message;
	}
}
