package com.leafnoise.pathfinder.exceptions;

import javax.ejb.ApplicationException;

@SuppressWarnings("serial")
@ApplicationException(rollback=false)
public class BusinessException extends Exception {

	public BusinessException(String message) {
		super(message);
		
	}
	
}
