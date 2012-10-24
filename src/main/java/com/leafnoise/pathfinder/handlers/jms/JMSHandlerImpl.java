/**
 * 
 */
package com.leafnoise.pathfinder.handlers.jms;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.handlers.jms.annotations.JMSMessageHandler;
import com.leafnoise.pathfinder.model.BaseEvent;

/**
 * JMS Interface that handles interaction with HornetQ.
 * @author Jorge Morando
 */
@JMSMessageHandler
public class JMSHandlerImpl implements JMSHandler {

	InitialContext ctx = null;
	Connection conn = null;
	private static Logger log = Logger.getLogger(JMSHandlerImpl.class);
		
	public void enqueue(String message) throws JMSException, NamingException {
		try {
			//Lookup the initial context
			ctx = new InitialContext();

			//Look up the XA (for transactions) or Simple () Connection Factory
			ConnectionFactory cf = (ConnectionFactory) ctx.lookup("/ConnectionFactory");

			//Look up the Queue
			Queue queue = (Queue) ctx.lookup("/jms/queue/Pathfinder");

			//Create a connection, a session and a message producer for the queue
			conn = cf.createConnection();
			Session session = conn.createSession(false,Session.AUTO_ACKNOWLEDGE);
			log.debug("-----CREATING PRODUCER----");
			MessageProducer messageProducer = session.createProducer(queue);

			//Create a Text Message
			TextMessage msg = session.createTextMessage(message);

			//Send The Text Message
			messageProducer.send(msg);
			log.info("Enqueued message from: " + message);
			log.debug("Enqueued message: " + message + "(InternalHQ_ID: "+ msg.getJMSMessageID() +")");
//			message.set_id(msg.getJMSMessageID());
		} finally {
			// close all resources
			if (ctx != null) {
				ctx.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	 public BaseEvent parse(Message rawMessage) throws TechnicalException {
			BaseEvent msg = null;
			ObjectMapper jsonMapper = new ObjectMapper();
			TextMessage tm = (TextMessage)rawMessage;
			try {
				//JSON TreeNode managing
				JsonNode rootNode = jsonMapper.readValue(tm.getText(), JsonNode.class);
				
				//Header and Body Nodes
				String agent = rootNode.path("agent").getTextValue();
				String type = rootNode.path("type").getTextValue();
				String geoSpatial = rootNode.path("geoSpatial").getTextValue();
				
				Long timestamp = null;
				JsonNode timeNode = rootNode.path("time");
				if(timeNode.isMissingNode()){
					timestamp = new Date().getTime();
				}else{
					try {
						timestamp = Long.valueOf(rootNode.path("time").getTextValue());
					} catch (NumberFormatException e) {
						log.error("Error trying to parse timestamp in event message",e);
						log.info("Error trying to parse timestamp in event message, proceeding with default date (NOW)");
						timestamp = new Date().getTime();
					}
				}
				String parentEvent = null;
				JsonNode parentEventNode = rootNode.path("parentEvent");
				if(!parentEventNode.isMissingNode())
					parentEvent = parentEventNode.getTextValue();
				String product = rootNode.path("product").toString();
				
				//a reasoner and processer should be used to parse the event instance
				
				//Construct the Message
				msg = new BaseEvent(type,agent,geoSpatial,timestamp,parentEvent,product);
			}catch (Exception e) {
				log.error("Coul not parse JSON message",e);
				throw new TechnicalException("Could not parse JSON message", e);
			}
			
			return msg;
		}
		
}
