package br.pessoal.server.resources;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import br.pessoal.server.ServerFuncionario;
import br.pessoal.server.resources.CustomLoggingFilter;;

public class Config extends ResourceConfig {

	public Config() {
		
		packages(CustomLoggingFilter.class.getPackage().getName());
		packages(ServerFuncionario.class.getPackage().getName());
		register(LoggingFeature.class);
		property(ServerProperties.TRACING, "ALL");
	}
}
