package com.leafnoise.pathfinder.exceptions;


/**
 * Exception that rises unchecked when a fault in programming logic is found
 * @author Jorge Morando
 *
 */
@SuppressWarnings("serial")
public class TechnicalException extends RuntimeException {

	public TechnicalException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TechnicalException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	@Deprecated
	public TechnicalException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
}
