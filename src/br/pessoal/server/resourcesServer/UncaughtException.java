package br.pessoal.server.resourcesServer;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UncaughtException extends Throwable implements ExceptionMapper<Throwable>{

	private static final long serialVersionUID  = 1L;

	@Override
	public Response toResponse(Throwable exception) {		
		return Response.status(500).entity("Ocorreu um erro, tente novamente mais tarde").type("text/plain").build();
	}
	
	

}
