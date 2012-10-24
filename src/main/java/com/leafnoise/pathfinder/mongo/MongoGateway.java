package com.leafnoise.pathfinder.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.solder.core.Veto;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.model.BaseEvent;
import com.leafnoise.pathfinder.mongo.exceptions.MissingMongoConfigFileException;
import com.leafnoise.pathfinder.mongo.exceptions.MongoMissingCollectionException;
import com.leafnoise.pathfinder.mongo.exceptions.MongoMissingConnectionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

/**
 * Singleton Class for MongoDB interaction.<br/>
 * Is default values are loaded in the following order:
 * <ol>
 * <li><b>mongoConf.properties</b>: Compulsory configuration file available in the classpath. It should provide at the very least the host and port of the mondgodb location</li>
 * <li><b>MongoGateway.class</b>: Holds every other information necesary for mongodb successful connection given the default mongodb configuration</li>
 * <li><b>Injection Attribute Annotation</b>: Aditional configuration can be loaded via the annotation</li>
 * </ol></br>
 * This Pojo will NOT be loaded by solder at deploy time for injection due to @Veto annotation</br>
 * 
 * @see com.leafnoise.pathfinder.mongo.annotations.MongoGatewayConfig
 * @see org.jboss.solder.core.Veto
 * @author Jorge Morando
 */
@Veto
public class MongoGateway {
	
	private static Logger log = Logger.getLogger(MongoGateway.class);
	
	public enum PropertyKeys {
		HOST("host","localhost"),PORT("port","27017"),USER("user",null),PASS("pass",null);
		
		private String key;
		private String value;
		
		PropertyKeys(String key,String value){
			this.key = key;
			this.value=value;
		}
		
		public String getKey(){
			return key;
		}
		
		public String getDefault(){
			return value;
		}
	}
	
	private static Properties config;
	
	private DB db;
	
	private DBCollection collection;
	
	private static MongoGateway instance;
	
	private Mongo m;
	
	private String lastErrorMsg;
	
	private Boolean hasError = false;
	
	private boolean authenticate = false;

	private boolean authenticated = false;
	
	private String[] credentials = new String[2];
	
	private MongoGateway(){
		log.info("Creating MongoGateway Instance");
		String host = null;
		Integer port = null;
		String user = null;
		String pass = null;
		try {
			log.info("Retrieving mongoConf.properties");
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("mongoConf.properties");
			config = new Properties();
			try {
				config.load(stream);
			} catch (IOException e) {
				throw new MissingMongoConfigFileException(e);
			}
			
			log.debug("Retrieving host value");
			host = config.getProperty(PropertyKeys.HOST.getKey(),PropertyKeys.HOST.getDefault());
			log.debug("Retrieving port value");
			port = Integer.valueOf(config.getProperty(PropertyKeys.PORT.getKey(),PropertyKeys.PORT.getDefault()));
			log.debug("Retrieving user value");
			user = config.getProperty(PropertyKeys.USER.getKey(),PropertyKeys.USER.getDefault());
			log.debug("Retrieving pass value");
			pass = config.getProperty(PropertyKeys.PASS.getKey(),PropertyKeys.PASS.getDefault());
			if((user!=null && pass!=null) && (!user.isEmpty() && !pass.isEmpty())){
				authenticate = true;
				credentials[0] = user;
				credentials[1] = pass;
			}
		} catch (MissingMongoConfigFileException e) {
			log.warn("No MongoDB configuration file found, using default values",e);
			host = PropertyKeys.HOST.getDefault();
		}
		
		try {
			if(port!=null && port.intValue()!=0){
				m = new Mongo(host,port);
			}else m = new Mongo(host);
		} catch (Exception e) {
			throw new TechnicalException("Mongo Exception",e);
		}
	}
	
	/**
	 * Retrieve MongoGateway instance
	 * @return Singleton instance of MongoGateway
	 */
	public static MongoGateway getInstance(){
		log.info("Retrieving MongoGateway Instance");
		
		if(instance == null){
			instance = new MongoGateway();
		}
		
		return instance;
	}
	
	private DB getDB(){
		if(db==null) throw new MongoMissingConnectionException("Gateway is not connected", new NullPointerException("Null DB attribute"));
		return db;
	}
	
	private DBCollection getCollection(){
		if(collection==null) throw new MongoMissingCollectionException("No collection selected", new NullPointerException("Null Collection attribute"));
		return collection;
	}
	
	public String getLastErrorMsg(){
		return lastErrorMsg;
	}
	
	public boolean hasError(){
		return hasError;
	}
	
	private void processWriteResult(WriteResult wr){
		lastErrorMsg = wr.getError();
		hasError = !(lastErrorMsg == null);
	}
	
	private void reset(){
		lastErrorMsg = null;
		hasError = false;
	}
	
	private DBObject getDBO(Object obj) {
		return (DBObject) obj;
	}
	
	/*------GATEWAY ACTION METHODS------*/
	
	/**
	 * Connects to <b>&quot;pathfinder&quot;</b> shard database
	 * @see com.leafnoise.pathfinder.mongo.MongoGateway#connect(String)
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway useDefaultDB(){
		return useDB("pathfinder");
	}
	
	/**
	 * Connects to a given mongo shard database
	 * @param dbStr The name of the mongo DB to retrieve, if none is found, mongo creates one with specified name.
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway useDB(String dbStr){
		if(dbStr == null || dbStr.trim().isEmpty()){
			useDefaultDB();
		}else{
			try {
				db = m.getDB(dbStr);
				if(authenticate && !authenticated){
					db.authenticate(credentials[0], credentials[1].toCharArray());
					authenticated = true;
				}
			} catch (Exception e) {
				log.error(e);
				throw new TechnicalException("Unable to use \""+dbStr+"\" DB.",e);
			}
		}
		log.debug("Using DB: \""+db.getName()+"\"");
		return this;
	}
	
	/**
	 * Retrieves the specified collection.
	 * @param col the name of the collection to retrieve
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway useCollection(String col){
		if(col == null) return this;
		collection = getDB().getCollection(col);
		log.debug("Using collection: \""+collection.getName()+"\"");
		return this;
	}
	
	/**
	 * Persists a Map&lt;String,Object&gt; under a key of value &quot:message&quot;
	 * @param jsonMap the mapped valid JSON
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway persist(String event){
		return persist("event",event);
	}
	
	/**
	 * Persists a Map&lt;String,Object&gt; under a given key
	 * @param jsonMap the mapped valid JSON
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway persist(String key, String message){
		reset();
		BasicDBObject dbo = new BasicDBObject();
		Object msg;
		try {
			msg = JSON.parse(message);
			dbo.put(key,msg);
			WriteResult wr = getCollection().insert(dbo);
			processWriteResult(wr);
		} catch (JSONParseException e) {
			lastErrorMsg = e.getMessage();
			hasError = true;
		}
		return this;
	}
	
	/**
	 * Find all messages
	 * @return List&lt;PFMessage&gt;
	 */
	public List<BaseEvent> findAll(){
		return find(null);
	}
	
	/**
	 * Find messages according to filter object specifications<br/>
	 * If query map is null a generic find() will be executed.
	 * @param query the Map&lt;String,Object&gt; with the filters for the specific search
	 * @return List&lt;PFMessage&gt;
	 */
	public List<BaseEvent> find(Map<String,Object> query){
		List<BaseEvent> msgs = new ArrayList<BaseEvent>();
		DBCursor cursor = null;
		if(query==null){//find all
			 cursor = getCollection().find();
		}else{//find with filters
			cursor = getCollection().find(new BasicDBObject(query));
		}
		if(cursor!=null){
			try {
				while(cursor.hasNext()) {
					DBObject obj = cursor.next();
					DBObject evtIns = getDBO(obj.get("event"));
					BaseEvent evt = new BaseEvent(obj.get("_id").toString(),evtIns);
					msgs.add(evt);
				}
			}catch(Exception e){
				log.error(e);
				throw new TechnicalException(e);
			}finally {
				cursor.close();
			}
		}
		return msgs;
	}
	
}
