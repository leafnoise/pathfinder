/**
 * 
 */
package com.leafnoise.pathfinder.ws;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.leafnoise.pathfinder.ws.artifacts.WSResponse;

/**
 * Web Service Interface that publishes the REST Web Services available for the Pathfinder Application
 * @author Jorge Morando
 *
 */
@Path("/")
public interface WS {
	
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
	WSResponse send(String rawMessage, @Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events from the given filters
	 * @param filters the filters to take into account when performing the search
	 * @return MessageWSResponse
	 * 
	 */
	@POST
	@Path("/receive")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receive(Map<String,Object> filters, @Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events from all agents
	 * @param agent the agent of wich the events will be fetch from
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receiveall")
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receiveAll(@Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events from its agent
	 * @param agent the agent of wich the events will be fetch from
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/agent/{agent}")
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receiveFrom(@PathParam("agent") String agent,@Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events according to its type
	 * @param type the type of wich the events will be fetch by
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/type/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receiveType(@PathParam("type") String type, @Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events from its sender and its type
	 * @param agent the agent of wich the events will be fetch from
	 * @param type the type of wich the events will be fetch by
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/typeandagent/{type}/{agent}")
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receiveTypeFromAgent(@PathParam("type") String type, @PathParam("agent") String agent, @Context HttpServletRequest request);
	
	/**
	 * REST Interface Service that retrieves a list of persisted events from its sender and its type
	 * @param agent the agent of wich the events will be fetch from
	 * @param type the type of wich the events will be fetch by
	 * @return MessageWSResponse
	 * 
	 */
	@GET
	@Path("/receive/between/{first}/and/{last}")
	@Produces(MediaType.APPLICATION_JSON)
	WSResponse receiveBetweenTimes(@PathParam("first") String first, @PathParam("last") String last, @Context HttpServletRequest request);

}
