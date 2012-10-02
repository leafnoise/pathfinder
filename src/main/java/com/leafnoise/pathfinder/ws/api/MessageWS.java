/**
 * 
 */
package com.leafnoise.pathfinder.ws.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
	
	/**
	 * REST Interface Service that retrieves a list of persisted messages from all senders
	 * @param sender the sender of wich the messages will be fetch from
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive")
	@Produces(MediaType.APPLICATION_JSON)
	MessageWSResponse receive(@Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted messages from its sender
	 * @param sender the sender of wich the messages will be fetch from
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/from/{sender}")
	@Produces(MediaType.APPLICATION_JSON)
	MessageWSResponse receive(@PathParam("sender") String from,@Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted messages from its sender and its type
	 * @param sender the sender of wich the messages will be fetch from
	 * @param type the type of wich the messages will be fetch from
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/from/{sender}/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	MessageWSResponse receive(@PathParam("sender") String from,@PathParam("type") String type, @Context HttpServletRequest request);

}
