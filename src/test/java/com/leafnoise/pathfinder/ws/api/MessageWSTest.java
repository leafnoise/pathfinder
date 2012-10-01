package com.leafnoise.pathfinder.ws.api;

import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.junit.Test;



public class MessageWSTest {
	
	private static Logger log = Logger.getLogger(MessageWSTest.class);

    private static String baseUri = "http://localhost:8080/pathfinder/rest/";
    
    @Test
    public void testSend() {
		try {
			
			log.info("Enviando solicitud");
    		
			String codigo = "codigo";
			String pass = "pass";
    		
    		//se arma el cliente con la URL del servicio
    		ClientRequest request = new ClientRequest(baseUri+"send");
    		
    		//se arma el mensaje 
    		// codigo = codigo ente emisor, clave = clave ente emisor, roles = credenciales usuario logueado
    		String jsonMsg = "{\"codigo\":\""+ codigo +"\",\"clave\":\"" + pass + "\"}";
    		
    		log.debug(jsonMsg);
    		
    		request.body(MediaType.APPLICATION_JSON, jsonMsg);
    		ClientResponse<String> response = request.post(String.class);
    		String responseStr = response.getEntity();
    		
    		log.info("Se obtuvo: "+responseStr);
    		
    		
    		
		} catch (Exception e) {
			fail(e.getMessage());
		
		}
	}

}
