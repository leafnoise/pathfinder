package com.leafnoise.pathfinder.vo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.mongodb.DBObject;

/**
 * @author Jorge Morando
 *
 */
public class MessageHeader implements  Serializable {

	
	private static final long serialVersionUID = 1780901948775215069L;
	private String from;
	private String type;
	@JsonIgnore
	private Date received;
	@JsonIgnore
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public MessageHeader(){
		this.received = new Date();
	}

	public MessageHeader(DBObject header){
		try {
			received = sdf.parse((String) header.get("received"));
		} catch (ParseException e) {
			received = null;
		} 
		from = (String) header.get("from");
		type = (String) header.get("type");
	}
	
	/**
	 * @return the origin
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setFrom(String f) {
		this.from = f;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the received
	 */
	public Date getReceived() {
		return received;
	}
	/**
	 * @param received the date the message was received
	 */
	public void setReceived(Date received) {
		this.received = received;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 32;
		int result = 16;
		result = prime * result
				+ ((from == null) ? 0 : from.hashCode())
				+ ((type == null) ? 0 : type.hashCode())
				+ ((received == null) ? 0 : received.hashCode());
		return result;
	}
	
	public String toJsonStr(){
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("{");
		jsonStr.append("\"from\"");
		jsonStr.append(":");
		jsonStr.append("\""+from+"\"");
		jsonStr.append(",");
		if(type!=null){
			jsonStr.append("\"type\"");
			jsonStr.append(":");
			jsonStr.append("\""+type+"\"");
			jsonStr.append(",");
		}
		jsonStr.append("\"received\"");
		jsonStr.append(":");
		jsonStr.append("\""+sdf.format(received)+"\"");
		jsonStr.append("}");
		return jsonStr.toString();
	}
}

