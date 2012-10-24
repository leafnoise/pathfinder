package com.leafnoise.pathfinder.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.leafnoise.pathfinder.exceptions.BusinessException;
import com.leafnoise.pathfinder.handlers.OWLHandler;
import com.leafnoise.pathfinder.model.BaseEvent;
import com.leafnoise.pathfinder.service.OntologyService;

/**
 * Pathfinder Implementation of the Ontology Service API
 * @see OntologyService
 * @author Jorge Morando
 *
 */
@Named("ontologyService")
public class OntologyServiceImpl implements OntologyService {

	@Inject
	Logger log;
	
	@Inject
	OWLHandler oh;
	
	@Override
	public String create(List<BaseEvent> events) throws BusinessException {
		return oh.createOntologyFromEventList(events);
	}

}
