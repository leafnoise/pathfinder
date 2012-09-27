package com.leafnoise.pathfinder.exceptions.handlers;

import javax.enterprise.context.NonexistentConversationException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Logger;
import org.jboss.weld.context.http.HttpConversationContext;

@HandlesExceptions
public class NonexistentConversationHandler {
    
	@Inject
    private Logger log;
	
	 
	public void onNonexistentConversation(
            @Handles CaughtException<NonexistentConversationException> evt, HttpConversationContext conversationContext, HttpServletRequest httpServletRequest ) {
        log.error("NonexistentConversationException!\n" + evt.getException().getMessage());
        evt.handled();
        try {
        	httpServletRequest.getRequestURL().toString();
        	String dirEntero=httpServletRequest.getRequestURL().toString();
        	String dir= dirEntero.substring(0, dirEntero.lastIndexOf("/"));
        //	conversationContext.activate(null); // Workaround WELD-855 - Create a new transient conversation.
        	FacesContext contex = FacesContext.getCurrentInstance();
        	
            contex.getExternalContext().redirect(dir + "/errorTimeOut.xhtml");
        }catch (Exception e) {
            log.error(e);
        }
    }
}