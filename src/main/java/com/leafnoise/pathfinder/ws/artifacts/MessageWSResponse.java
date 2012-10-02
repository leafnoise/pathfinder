package com.leafnoise.pathfinder.ws.artifacts;

import java.io.Serializable;
import java.util.List;

import com.leafnoise.pathfinder.model.PFMessage;

/**
 *  MessageWSResponse. Conversion to JSON will be marshalled by JacksonJSON Library
 * @author Jorge Morando
 */
public class MessageWSResponse extends WSResponse implements Serializable{

	private static final long serialVersionUID = -7801570664066705117L;
	private List<PFMessage> messages;
	
	/**
	 * @return the messages
	 */
	public List<PFMessage> getMessages() {
		return messages;
	}
	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<PFMessage> messages) {
		this.messages = messages;
	}
}
