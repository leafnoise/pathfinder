package com.leafnoise.pathfinder.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.DependsOn;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.handlers.jms.JMSHandlerImpl;
import com.leafnoise.pathfinder.handlers.jms.annotations.JMSMessageHandler;
import com.leafnoise.pathfinder.model.BaseEvent;
import com.leafnoise.pathfinder.service.EventService;

/**
 * Message Driven Bean entitled with receiving one by one every message delivered to the jms.queue.Pathfinder queue
 * @author Jorge Morando
 */
@Named("mdb")
@ResourceAdapter("hornetq-ra.rar")
@DependsOn("jboss.web.deployment:war=/pathfinder")
@MessageDriven(name = "MDBListener", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/Pathfinder") })
public class MDB implements MessageListener {

	@Inject
	@Named("mongoEventService")
	private EventService ms;
	
	@Inject
	@JMSMessageHandler
	private JMSHandlerImpl mh;
	
	@Inject
	private static Logger log;

	public void onMessage(Message message) {
		
		log.info("---EVENT LISTENER FIRED----");
		
		TextMessage msg = (TextMessage) message;
		BaseEvent pfm;
		try {
			log.debug("Event Received, processing...");
			//Message Retrieval
			pfm = mh.parse(msg);
			
			log.debug("Finished event processing.");
			//BI
//			messageProcessor.process(jgmm);
			log.debug("Persisting event...");
			ms.save(pfm);
			log.debug("Finished event persistence.");
//		} catch (JMSException e) {
//			log.fatal("Se produjo un error fatal", e);
//			throw new TechnicalException("Internal Error. Please contact your Systems Administrator",e);
		} catch (BusinessException e) {
			log.error("Se produjo un error al tratar de recibir un mensaje. "+e.getMessage(),e);
		}
	}
}