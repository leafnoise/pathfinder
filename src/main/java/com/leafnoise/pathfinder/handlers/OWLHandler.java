package com.leafnoise.pathfinder.handlers;

import java.util.List;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.model.BaseEvent;


/**
 * OWL interface that handles the creation of the ontology file given the list of BaseEvents
 * @see com.leafnoise.pathfinder.model.BaseEvent
 * @author Jorge Morando
 */
public interface OWLHandler {

	/**
	 * Creates an ontology RDF/XML file from a message
	 * @param message
	 * @return the file name of the created ontology
	 * @throws Exception
	 */
	String createOntologyFromEventList(List<BaseEvent> events) throws TechnicalException;
	
}
