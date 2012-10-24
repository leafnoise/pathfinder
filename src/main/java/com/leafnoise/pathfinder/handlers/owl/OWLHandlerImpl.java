package com.leafnoise.pathfinder.handlers.owl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import com.leafnoise.pathfinder.exceptions.TechnicalException;
import com.leafnoise.pathfinder.handlers.OWLHandler;
import com.leafnoise.pathfinder.model.BaseEvent;

@Named("owlHandler")
public class OWLHandlerImpl implements OWLHandler {

	@Inject
	Logger log;
	
	/* (non-Javadoc)
	 * @see com.leafnoise.pathfinder.handlers.OWLHandler#create(com.leafnoise.pathfinder.model.PFMessage)
	 */
	@Override
	public String createOntologyFromEventList(List<BaseEvent> evts) throws TechnicalException {
		try {
			//Build file name
			String fileName = "syslog_"+ new Date().getTime() + ".rdf";
			//Calculate base directory path
			String path = this.getClass().getClassLoader().getResource("mongoConf.properties").getPath().split("/WEB-INF")[0];
			
			// Get hold of an ontology manager
	        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	        
	        //Create ontology from scratch
	        IRI eventOntologyIRI = IRI.create("http://www.leafnoise.com/patfhinder/eventOntologies/"+fileName);
	        OWLOntology ontology = manager.createOntology(eventOntologyIRI);
	        
	        //start manipulation through data factory
	        OWLDataFactory dataFactory = manager.getOWLDataFactory();
	        //Get base definition reference IRI
	        String baseDefinitions = "http://www.leafnoise.com/pathfinder/owl/";
	        PrefixManager pm = new DefaultPrefixManager(baseDefinitions);
	        // Get the reference to the :Events class (the full IRI will be
	        // <http://www.leafnoise.com/pathfinder/owl/JSON_ENCODE_NAM>)
	        OWLClass event = dataFactory.getOWLClass(":Event", pm);
	        
	        for (BaseEvent evt : evts) {
	        	// Get the reference to the :JSON_NAMED_EVENT class (the full IRI will be
		        // <http://www.leafnoise.com/pathfinder/owl/JSON_ENCODE_NAME>)
		        OWLNamedIndividual eventInstance = dataFactory.getOWLNamedIndividual(":SYSLOG-"+evt.get_id(), pm);
		        
		        // Prepare de content property
		        OWLDataProperty hasContent = dataFactory.getOWLDataProperty(":hasContent", pm);
		        // Now create a ClassAssertion to specify that :JSON_EVENT is an instance of :Event
		        // We need to add the class assertion to the ontology that we want
		        // specify that :JSON is a :Event
		        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(event, eventInstance);
		        //add axiom to manager
		        manager.addAxiom(ontology, classAssertion);
		        
		        // To specify that :JSON_EVENT has specific content we create a data property
		        // assertion and add it to the ontology
		        OWLDataPropertyAssertionAxiom dataPropertyAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(hasContent, eventInstance, evt.getProduct());
		        // add axiom to manager
		        manager.addAxiom(ontology, dataPropertyAssertion);
			}
	     
	        // Now save a local copy of the ontology
	        File file = new File(path+"/eventOntologies/"+fileName);
	        manager.saveOntology(ontology, IRI.create(file.toURI()));
	        return "/eventOntologies/"+fileName;
		} catch (OWLOntologyStorageException e){
			log.error(e);
			throw new TechnicalException(e);
		} catch (OWLOntologyCreationException e){
			log.error(e);
			throw new TechnicalException(e);
		}
	}
}
