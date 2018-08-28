package br.pessoal.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/mensagem")
public class Server {

	@GET
	public String getSms() {
		return "Hello World";
	}
}
