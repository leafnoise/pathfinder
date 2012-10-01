package com.leafnoise.pathfinder.mongo.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation that  configures MongoGateway injected Object 
 * @author Jorge Morando
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface MongoGatewayConfig {
	/**
	 * Collection to be used by the MongoGateway
	 */
	public String collection();
	
	/**
	 * Database to used by the MongoGateway
	 */
	public String database() default "";
}