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

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.glassfish.jersey.message.internal.ReaderWriter;


@Provider
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    @Context
    private ResourceInfo resourceInfo;
    
	final static Logger logger = Logger.getLogger(CustomLoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        MDC.put("start-time", String.valueOf(System.currentTimeMillis()));

        logger.info("USANDO O RECURSO: "+ requestContext.getUriInfo().getPath());        
        logger.info("NOME DO METODO:  "+ resourceInfo.getResourceMethod().getName());
        logger.info("CLASSE: "+ resourceInfo.getResourceClass().getCanonicalName());
        logQueryParameters(requestContext);
        logMethodAnnotations();
        logRequestHeader(requestContext);

        
        String entity = readEntityStream(requestContext);
        if(null != entity && entity.trim().length() > 0) {
        	logger.debug("ENTITY STREAM: "+entity);
        }
    }

	private void logQueryParameters(ContainerRequestContext requestContext) {
        Iterator<String> iterator = requestContext.getUriInfo().getPathParameters().keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            List<String> obj = requestContext.getUriInfo().getPathParameters().get(name);
            String value = null;
            if(null != obj && obj.size() > 0) {
                value = obj.get(0);
            }
            logger.info("PARAMETROS DA QUERY NOME: "+ name +", VALOR: "+ value);
        }
    }

    private void logMethodAnnotations() {
        Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
        if (annotations != null && annotations.length > 0) {
        	logger.info("----INICIANDO AS ANOTACOES DE RECURSOS----");
            for (Annotation annotation : annotations) {
            	logger.info(annotation.toString());
            }
            logger.info("----FIM DAS ANOTACOES DE RECURSOS----");
        }
    }

    private void logRequestHeader(ContainerRequestContext requestContext) {
        Iterator<String> iterator;
        logger.info("----INICIANDO A SECAO DE REQUISICOES DO HEADER----");
        logger.info("TIPO DO METODO: "+ requestContext.getMethod());
        iterator = requestContext.getHeaders().keySet().iterator();
        while (iterator.hasNext()) {
            String headerName = iterator.next();
            String headerValue = requestContext.getHeaderString(headerName);
            logger.info("NOME DO HEADER: "+headerName+", VALOR DO HEADER: "+ headerValue);
        }
        logger.info("----FIM DA SECAO DE REQUISICOES DO HEADER----");
    }

    private String readEntityStream(ContainerRequestContext requestContext)
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final InputStream inputStream = requestContext.getEntityStream();
        final StringBuilder builder = new StringBuilder();
        try
        {
            ReaderWriter.writeTo(inputStream, outStream);
            byte[] requestEntity = outStream.toByteArray();
            if (requestEntity.length == 0) {
                builder.append("");
            } else {
                builder.append(new String(requestEntity));
            }
            requestContext.setEntityStream(new ByteArrayInputStream(requestEntity) );
        } catch (IOException ex) {
        	logger.error("----OCORREU UMA EXCECAO ENQUANDO A ENTITY STREAM ERA LIDA: "+ex.getMessage());        	
        }
        return builder.toString();
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String stTime = (String) MDC.get("start-time");
        if(null == stTime || stTime.length() == 0) {
        	return;
        }
    	long startTime = Long.parseLong(stTime);
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("TEMPO TOTAL DA REQUISICAO: "+executionTime+"milliseconds");        
        MDC.clear();
    }
}