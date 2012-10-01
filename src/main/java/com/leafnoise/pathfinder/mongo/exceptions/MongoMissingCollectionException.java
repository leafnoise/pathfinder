/**
 * 
 */
package com.leafnoise.pathfinder.mongo.exceptions;

import com.leafnoise.pathfinder.exceptions.TechnicalException;

/**
 * Exception thrown when trying to perform an action with the gateway not yet connected.
 * @author Jorge Morando
 */
public class MongoMissingCollectionException extends TechnicalException {

	private static final long serialVersionUID = -2908054425458648605L;

	public MongoMissingCollectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MongoMissingCollectionException(Throwable cause) {
		super(cause);
	}
	
}
