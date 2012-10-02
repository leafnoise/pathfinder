/**
 * 
 */
package com.leafnoise.pathfinder.handlers.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.leafnoise.pathfinder.annotations.DefaultMessageHandler;
import com.leafnoise.pathfinder.handlers.jms.annotations.JMS;
import com.leafnoise.pathfinder.model.PFMessage;

/**
 * JMS Interface that handles interaction with HornetQ.
 * @author Jorge Morando
 */
@JMS
@DefaultMessageHandler
public class JMSHandlerImpl implements JMSHandler {

	InitialContext ctx = null;
	Connection conn = null;
	private static Logger log = Logger.getLogger(JMSHandlerImpl.class);
		
	public void enqueue(PFMessage message) throws JMSException, NamingException {
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
			ObjectMessage msg = session.createObjectMessage(message);

			//Send The Text Message
			messageProducer.send(msg);
			log.info("Enqueued message from: " + message.getHeader().getFrom());
			log.debug("Enqueued message: " + message.getPayload() + "(InternalHQ_ID: "+ msg.getJMSMessageID() +")");
			message.set_id(msg.getJMSMessageID());
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
		
}
