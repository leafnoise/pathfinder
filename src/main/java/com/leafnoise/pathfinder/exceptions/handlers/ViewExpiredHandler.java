package com.leafnoise.pathfinder.exceptions.handlers;

import javax.faces.application.ViewExpiredException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Logger;
import org.jboss.weld.context.http.HttpConversationContext;

@HandlesExceptions
public class ViewExpiredHandler {
    
	@Inject
    private Logger log;
	
	
    
	public void onViewExpiredException(
            @Handles CaughtException<ViewExpiredException> evt, HttpConversationContext conversationContext, HttpServletRequest httpServletRequest) {
        log.error("ViewExpiredException!\n" + evt.getException().getMessage());
        evt.handled();
        try {
        	httpServletRequest.getRequestURL().toString();
        	String dirEntero=httpServletRequest.getRequestURL().toString();
        	String dir= dirEntero.substring(0, dirEntero.lastIndexOf("/"));
	        FacesContext contex = FacesContext.getCurrentInstance();
	        contex.getExternalContext().redirect(dir + "/index.jsf");
        }catch (Exception e) {
            log.error(e);
        }
    }
}