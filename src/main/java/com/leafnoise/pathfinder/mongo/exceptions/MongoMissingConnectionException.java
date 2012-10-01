/**
 * 
 */
package com.leafnoise.pathfinder.mongo.exceptions;

import com.leafnoise.pathfinder.exceptions.TechnicalException;

/**
 * Exception thrown when trying to perform an action with the gateway not yet connected.
 * @author Jorge Morando
 */
public class MongoMissingConnectionException extends TechnicalException {
	 
	private static final long serialVersionUID = 1162559012149330387L;

	public MongoMissingConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public MongoMissingConnectionException(Throwable cause) {
		super(cause);
	}
	
}
