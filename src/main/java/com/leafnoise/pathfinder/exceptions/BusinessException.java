package com.leafnoise.pathfinder.exceptions;

import javax.ejb.ApplicationException;

/**
 * Exception that rises when a fault in Business Logic is found
 * @author Jorge Morando
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=false)
public class BusinessException extends Exception {

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	@Deprecated
	public BusinessException(String message) {
		super(message);
	}
	
}
