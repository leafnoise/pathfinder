package com.leafnoise.pathfinder.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.DependsOn;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.model.PFMessage;
import com.leafnoise.pathfinder.service.MessageService;

@Named("mdb")
@ResourceAdapter("hornetq-ra.rar")
@DependsOn("jboss.web.deployment:war=/pathfinder")
@MessageDriven(name = "MDBListener", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/Pathfinder") })
public class MDB implements MessageListener {

	@Inject
	@Named("mongoMessageService")
	private MessageService ms;
	
	@Inject
	private static Logger log;

	public void onMessage(Message message) {
		
		log.info("---MESSAGE LISTENER FIRED----");
		
		ObjectMessage msg = (ObjectMessage) message;
		PFMessage pfm;
		try {
			log.debug("Message Received, processing...");
			//Message Retrieval
			pfm = (PFMessage) msg.getObject();
			
			//BI
//			messageProcessor.process(jgmm);
			ms.save(pfm);
			
		} catch (JMSException e) {
			log.fatal("Se produjo un error fatal", e);
			throw new TechnicalException("Internal Error. Please contact your Systems Administrator",e);
		} catch (BusinessException e) {
			log.error("Se produjo un error al tratar de recibir un mensaje. "+e.getMessage());
		}
	}
}