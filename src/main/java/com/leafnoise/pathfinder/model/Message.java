package com.leafnoise.pathfinder.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {

	private String id;
	
	private Date receivedDate;
	
	private String origin;
		
	private String payload;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public Map<String,String> toMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("receivedDate", receivedDate.toString());
		map.put("origin", origin);
		map.put("payload", payload);
		return map;
	}
}
