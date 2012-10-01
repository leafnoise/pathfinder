/**
 * 
 */
package com.leafnoise.pathfinder.mongo.exceptions;

import com.leafnoise.pathfinder.exceptions.TechnicalException;

/**
 * Exception thrown when any of the following MongoDB configuration parameters is missing.<br/>
 * The parameters are ordered by importance/precedence.
 * <ol>
 * <li>host</li>
 * <li>port</li>
 * </ol> 
 * @author Jorge Morando
 * @see com.leafnoise.pathfinder.mongo.exceptions.MissingMongoHostException
 * @see com.leafnoise.pathfinder.mongo.exceptions.MissingMongoPortException
 */
public class MissingMongoConfigFileException extends TechnicalException {

	private static final long serialVersionUID = -8531369606967216349L;

	public MissingMongoConfigFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingMongoConfigFileException(Throwable cause) {
		super(cause);
	}
	
}
