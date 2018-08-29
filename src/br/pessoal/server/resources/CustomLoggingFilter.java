package br.pessoal.server.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.ReaderWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;




@Provider
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter{

	@Context
	private ResourceInfo resourceInfo;
	private static final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// Logs que serão gerados na requisição
		MDC.put("Tempo de Inicio", String.valueOf(System.currentTimeMillis()));
		
		log.debug("Usando o recurso: /{} ", requestContext.getUriInfo().getPath());
		log.debug("Nome do Metodo: {} ", resourceInfo.getResourceMethod().getName());
		log.debug("Class {} ", resourceInfo.getResourceClass().getCanonicalName());
		
		logQueryParamters(requestContext);
		logMethodAnnotations();
		logRequestHeader(requestContext);
		
		String entity = readerEntityStream(requestContext);
		if (null != entity && entity.trim().length() > 0) {
			log.debug("Entity Stream: {} ", entity);
		}
	}

	private String readerEntityStream(ContainerRequestContext requestContext) {
		 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final InputStream inputStream = requestContext.getEntityStream();
		final StringBuilder builder = new StringBuilder();
		try {
			ReaderWriter.writeTo(inputStream, outputStream);
			byte[] requestEntity = outputStream.toByteArray();
			if (requestEntity.length == 0) {
				builder.append("");
			}else {
				builder.append(new String(requestEntity));
			}
			requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
		} catch (Exception e) {
			 log.debug("------OCORREU UMA EXCEÇAO ENQUANTO ESTAVA LENDO A ENTITY STREAM: {} ", e.getMessage());
		}
		return builder.toString();
	}

	private void logRequestHeader(ContainerRequestContext requestContext) {
		 
		Iterator<String> iterator;
		log.debug("------INICIO DA SEÇAO DE REQUISIÇAO DO HEADER------");
		log.debug("Tipo do Metodo: {} ", requestContext.getMethod());
		iterator = requestContext.getHeaders().keySet().iterator();
		while (iterator.hasNext()) {
			String headerName = iterator.next();
			String headerValue = requestContext.getHeaderString(headerName);
			log.debug("NOME DO HEADER: {}, VALOR DO HEADER: {} ", headerName, headerValue);		
		}
		log.debug("------FIM DA SEÇAO DE REQUISICAO DO HEADER------");
	}

	private void logMethodAnnotations() {
		 
		Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
		if (annotations != null && annotations.length > 0) {
			log.debug("------------INICIO DAS ANOTAÇOES DE RECURSO------------");
			for (Annotation anotacaoDaVez : annotations) {
				log.debug(anotacaoDaVez.toString());
			}
			log.debug("------------FIM DAS ANOTAÇOES DE RECURSO------------");
		}
	}

	private void logQueryParamters(ContainerRequestContext requestContext) {
		 
		Iterator<String> iterator = requestContext.getUriInfo().getPathParameters().keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			List<String> obj = requestContext.getUriInfo().getPathParameters().get(name);
			String value = null;
			if (null != obj && obj.size() > 0) {
				value = obj.get(0);
			}
			log.debug("Parametros da Query  NOME: {}, VALUE: {}", name, value);
		}
		
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		 
		String stTime = MDC.get("Tempo de Inicio");
		if (null == stTime || stTime.length() == 0) {
			return;
		}
		
		long startTime = Long.parseLong(stTime);
		long executionTime = System.currentTimeMillis() - startTime;
		log.debug("TEMPO TOTAL DA REQUISIÇÃO: {} MILLISECONDS", executionTime);
		MDC.clear();
	}


}
