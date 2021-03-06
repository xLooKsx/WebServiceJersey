package br.pessoal.cliente;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import br.pessoal.to.EmpregadoTO;

public class JerseyClientFuncionario {

	public static void main(String[] args) {

		uploadFile();
	}

	private static void apagarFuncionario() {
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget target = client.target("http://127.0.0.1:8080/WebService/funcionario").path("rmrf/funcionario").path("3");
		
		Invocation.Builder builder = target.request();
		Response response = builder.delete();
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}
	
	private static void atualizarFuncionario() {
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget target = client.target("http://127.0.0.1:8080/WebService/funcionario").path("attProfFuncionarios").path("3");
		
		Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);
		Response response = builder.put(Entity.entity("Advogado", MediaType.APPLICATION_XML));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}
	
	private static void adicionarEmpregado() {
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget target = client.target("http://127.0.0.1:8080/WebService/funcionario").path("cadFuncionarios");
		
		EmpregadoTO empregadoTO = new EmpregadoTO(03, "Guilherme", 35, "Bombeiro ");
		
		Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);
		Response response = builder.post(Entity.entity(empregadoTO, MediaType.APPLICATION_XML));
		
		System.out.println(response.getStatus());
		System.out.println(response.readEntity(String.class));
	}
	
	private static void getFuncionarios() {

		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget target = client.target("http://127.0.0.1:8080/WebService/funcionario").path("funcionarios");

		Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);
		Response response = builder.get();

		List<EmpregadoTO> list = response.readEntity(new GenericType<List<EmpregadoTO>>() { });		

		System.out.println(response.getStatus());
		for (EmpregadoTO empregadoDaVez : list) {
			System.out.println(empregadoDaVez.toString());
		}
	}
	
	private static void uploadFile() {
		
		final Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		 
	    final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("C:\\Users\\lucas.oliveira\\Desktop\\solaire.jpg"));
	    FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
	    final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.field("foo", "bar").bodyPart(filePart);
	      
	    final WebTarget target = client.target("http://localhost:8080/WebService/files/upload");
	    final Response response = target.request().post(Entity.entity(multipart, multipart.getMediaType()));
	     
	    System.out.println(response.getStatus());
	     
	    try {
			formDataMultiPart.close();
			multipart.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	private static void getEmpregadoById() {
		
		Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFeature.class));
		WebTarget target = client.target("http://127.0.0.1:8080/WebService/funcionario").path("funcionarios").path("1600344");
		
		Invocation.Builder builder = target.request(MediaType.APPLICATION_XML);
		Response response = builder.get();
		
		EmpregadoTO empregadoTO = response.readEntity(EmpregadoTO.class);		

		System.out.println(response.getStatus());		
		System.out.println(empregadoTO.toString());
		
	}
}
