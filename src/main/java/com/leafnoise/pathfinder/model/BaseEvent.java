package com.leafnoise.pathfinder.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.mongodb.DBObject;

/**
 * Base Event Model that locates all the specific information handled by the application.<br/>
 * This POJO holds the logic for JSON String representation of itself
 * @author Jorge Morando
 *
 */
public class BaseEvent implements Serializable {

	private static final long serialVersionUID = -2145878502099973973L;
	private static final Logger log = Logger.getLogger(BaseEvent.class);
	private String _id;
	private String type;
	private String agent;
	private Long time;
	private Date received;
	private String location;
	private String parent;
	private String product;
	@JsonIgnore
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public BaseEvent(String id, DBObject message){
		_id = id;
		this.type = message.get("type").toString();
		this.agent = message.get("agent").toString();
		this.product = message.get("product").toString();
		this.time = (Long)message.get("time");
		
		if(message.get("location")!=null)
			this.location = message.get("location").toString();
		if(message.get("parent")!=null)
			this.parent = message.get("parent").toString();
		try {
			this.received = sdf.parse(message.get("received").toString());
		} catch (ParseException e) {
			TechnicalException ex = new TechnicalException("RECEIVED date parameter could not be parse correctly",e);
			log.error(ex);
			throw ex;
		}
	}
	
	public BaseEvent(String type, String agent, String location, Long time, String product){
		this.type = type;
		this.agent = agent;
		this.location = location;
		this.time = time;
		this.parent = null;
		this.product = product;
		this.received = new Date();
	}
	
	public BaseEvent(String type, String agent, String location, Long time, String parentEvent, String product){
		this.type = type;
		this.agent = agent;
		this.location = location;
		this.time = time;
		this.parent = parentEvent;
		this.product = product;
		this.received = new Date();
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
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param Location the geospatial to set
	 */
	public void setLocation(String Location) {
		this.location = Location;
	}

	/**
	 * @return the parentEvent
	 */
	public String getParentEvent() {
		return parent;
	}

	/**
	 * @param parentEvent the parentEvent to set
	 */
	public void setParentEvent(String parentEvent) {
		this.parent = parentEvent;
	}

	/**
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the received
	 */
	public Date getReceived() {
		return received;
	}

	public String toJsonStr(){
		JSONObject json = new JSONObject();
		try {
			json.put("agent", agent);
			json.put("type", type);
			json.put("received", sdf.format(received));
			if(time!=null){
				json.put("time", time);
			}
			if(location!=null && location.isEmpty()){
				json.put("location", location);
			}
			if(parent!=null && parent.isEmpty()){
				json.put("parent", parent);
			}
			json.put("product", new JSONObject(product));
		} catch (JSONException e) {
			TechnicalException ex = new TechnicalException("Error in BEAN->JSON construction",e);
			log.error(ex);
			throw ex;
		}
		
		return json.toString();
	}
}
