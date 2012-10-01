/**
 * 
 */
package com.leafnoise.pathfinder.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.leafnoise.pathfinder.ws.artifacts.MessageWSResponse;

/**
 * @author Jorge Morando
 *
 */
@Path("/")
public interface MessageWS {
	
	/**
	 * REST Interface Service that enqueues a rawMessage
	 * @param msg
	 * @param request
	 * @return MessageWSResponse
	 * 
	 */
	@POST
	@Path("/send")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	MessageWSResponse send(String rawMessage, @Context HttpServletRequest request);

}
