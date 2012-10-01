package com.leafnoise.pathfinder.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.log4j.Logger;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.mongo.MongoGateway;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewaySingleton;
import com.leafnoise.pathfinder.mongo.annotations.MongoGatewayConfig;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private Logger log;
 * </pre>
 * @author Jorge Morando
 */
public class CDIBeanProducer {

	private static Logger log = Logger.getLogger(CDIBeanProducer.class);
	
//	@Inject
//	private PathfinderProperties pathfinderProperties;
	
	/**
	 * Produces a valid CDI (JSR-299) Logger Object to be injected via &#64Inject Annotation<br/>
	 * Like so:
	 *  * <pre>
	 * &#064;Inject
	 * private Logger log;
	 * </pre>
	 * @param injectionPoint
	 * @return
	 */
	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}	
	
	/**
	 * Produces a valid CDI (JSR-299) MongoGateway Singleton Object to be injected via &#64Inject Annotation<br/>
	 * Like so:
	 * <pre>
	 * &#064;Inject
	 * &#064;SingletonMongoGateway
	 * private MongoGateway gateway;
	 * </pre>
	 * @return Connected MongoGateway Instance and using &quot;messages&quot; collection
	 */
	@Produces
	@MongoGatewaySingleton
	public MongoGateway producesMongoGateway(InjectionPoint ip) {
		log.debug("Attempting to produce MongoGateway for injection");
		log.debug("Retrieving Configuration gateway configuration");
		MongoGatewayConfig config = ip.getAnnotated().getAnnotation(MongoGatewayConfig.class);
		
		try {
			MongoGateway mg = MongoGateway.getInstance();
			if(config==null){
				log.debug("No annotated configuration found, using defaults");
				mg =  mg.useDefaultDB();
			} else {
				mg = mg.useDB(config.database()).useCollection(config.collection());
			}
			
			return mg;
		} catch (TechnicalException e) {
			log.error("MongoGateway CDI object production failed: "+e.getMessage());
			return null;
		}
	}
}
