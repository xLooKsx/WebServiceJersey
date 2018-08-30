package br.pessoal.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.log4j.Logger;

@Path("/download")
public class ServerDownload {

	final static Logger logger = Logger.getLogger(ServerDownload.class);
	
	@GET
	@Path("/img")
	public Response downloadImage() {
		
		StreamingOutput output = new StreamingOutput() {			
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				
				
				try {
					String pathAux = this.getClass().getClassLoader().getResource("").getPath();
					java.nio.file.Path path = Paths.get("C:/apache-tomcat-9.0.11/resource/solaire.jpg");					
					byte[] data = Files.readAllBytes(path);
					output.write(data);
					output.flush();				
					
				} catch (Exception e) {
					logger.error("Arquivo nao encontrado: "+e.getMessage());
				}
			}
		};
		return Response.ok(output, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename = solaire.jpg").build();
	}
}
