package com.leafnoise.pathfinder.service;

import java.util.List;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.model.BaseEvent;

/**
 * Service Interface that handles Ontology Business Logic throughout the application
 * @author Jorge Morando
 *
 */
public interface OntologyService {
	
	/**
	 * Creates an ontology file with given events
	 * @param message List&lt;BaseEvent&gt; the events instances of the ontology
	 */
	String create(List<BaseEvent> events) throws BusinessException;
	
}
