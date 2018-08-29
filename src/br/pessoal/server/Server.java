package br.pessoal.server;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.pessoal.dao.FuncionarioDAO;
import br.pessoal.to.EmpregadoTO;


@Path("/")
public class Server {

	@GET
	@Path("/helloWorld")
	public String getSms() {
		return "Hello World";
	}

	@GET
	@Path("/funcionarios")
	@Produces(MediaType.APPLICATION_XML)
	public List<EmpregadoTO> getEmpregados(){

		return new FuncionarioDAO().getListEmpregados();
	}
}
