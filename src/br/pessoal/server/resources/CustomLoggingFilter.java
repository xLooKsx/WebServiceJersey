package br.pessoal.server.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.message.internal.ReaderWriter;

public class CustomLoggingFilter extends LoggingFeature implements ContainerRequestFilter, ContainerResponseFilter{

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		 
		StringBuilder builder = new StringBuilder();
		builder.append("Header: ").append(responseContext.getHeaders())
				.append(" - Entity: ").append(responseContext.getEntity());
		
		System.out.println("HTTP RESPONSE: "+builder.toString());	
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		 
		StringBuilder builder = new StringBuilder();
		builder.append("User: ").append(requestContext.getSecurityContext().getUserPrincipal()==null?"UNKNOW":requestContext.getSecurityContext().getUserPrincipal())
				.append(" - Path: ").append(requestContext.getUriInfo().getPath())
				.append(" - Header: ").append(requestContext.getHeaders())
				.append(" - Entity: ").append(getEntityBody(requestContext));
		
		System.out.println("HTTP REQUEST: " + builder.toString());
		
	}

	private Object getEntityBody(ContainerRequestContext requestContext) {
		 
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = requestContext.getEntityStream();
			
			final StringBuilder builder = new StringBuilder();
		try {
		
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			InputStream inputStream = requestContext.getEntityStream();
		
			ReaderWriter.writeTo(in, out);
			byte[] requestEntity = out.toByteArray();
			
			if (requestEntity.length == 0) {
				builder.append("").append("\n");
			}else {
				builder.append(new String(requestEntity)).append("\n");
			}
			requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
		} catch (IOException e) {			 
			e.printStackTrace();
		}
		return builder.toString();
	}

}
