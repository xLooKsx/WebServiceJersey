package br.pessoal.server.config;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import br.pessoal.server.resources.CustomLoggingFilter;

public class ConfigServer extends ResourceConfig {

	public ConfigServer() {
		
		packages("br.pessoal.server.config");
		register(JacksonFeature.class);
		
		register(CustomLoggingFilter.class);
	}
}
