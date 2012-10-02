package com.leafnoise.pathfinder.model;

import java.io.Serializable;

import com.leafnoise.pathfinder.vo.MessageHeader;
import com.mongodb.DBObject;

/**
 * @author Jorge Morando
 *
 */
public class PFMessage implements Serializable {

	private static final long serialVersionUID = -7921027587494879013L;
	private String _id;
	private MessageHeader header;
	private String payload;
	
	public PFMessage(String id, DBObject message){
		_id = id;
		header = new MessageHeader((DBObject) message.get("header"));
		payload = message.get("payload").toString();
	}
	
	public PFMessage(MessageHeader header, String payload){
		this.header = header;
		this.payload = payload;
	}

	public MessageHeader getHeader() {
		return header;
	}

	public void setHeader(MessageHeader header) {
		this.header = header;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}

	/**
	 * @param _id the _id to set
	 */
	public void set_id(String _id) {
		this._id = _id;
	}

	public String toJsonStr(){
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("{");
		jsonStr.append("\"header\"");
		jsonStr.append(":");
		jsonStr.append(header.toJsonStr());
		jsonStr.append(",");
		jsonStr.append("\"payload\"");
		jsonStr.append(":");
		jsonStr.append(payload);
		jsonStr.append("}");
		return jsonStr.toString();
	}
}
