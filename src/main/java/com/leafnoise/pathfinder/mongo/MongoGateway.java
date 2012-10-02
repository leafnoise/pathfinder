package com.leafnoise.pathfinder.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jboss.solder.core.Veto;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.mongo.exceptions.MissingMongoConfigFileException;
import com.leafnoise.pathfinder.mongo.exceptions.MongoMissingCollectionException;
import com.leafnoise.pathfinder.mongo.exceptions.MongoMissingConnectionException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mongodb.util.JSONParseException;

/**
 * Singleton Class for MongoDB interaction.
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
	
	private MongoGateway(){
		log.info("Creating MongoGateway Instance");
		String host = null;
		Integer port = null;
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
	
	private DBCollection getColl(){
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
			db = m.getDB(dbStr);
		}
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
		return this;
	}
	
	/**
	 * Persists a Map&lt;String,Object&gt;
	 * @param jsonMap the mapped valid JSON
	 * @return the current instance of the MongoGateway
	 */
	public MongoGateway persist(Map<String,String> jsonMap){
		reset();
		BasicDBObject dbo = new BasicDBObject(jsonMap);
		Object payload;
		try {
			payload = JSON.parse(jsonMap.get("payload"));
			dbo.put("payload",payload);
			WriteResult wr = getColl().insert(dbo);
			processWriteResult(wr);
		} catch (JSONParseException e) {
			lastErrorMsg = e.getMessage();
			hasError = true;
		}
		return this;
	}
}
