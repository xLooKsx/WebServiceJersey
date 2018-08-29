package br.pessoal.server;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
	
	@GET
	@Path("/funcionarios/{idFunc}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getEmpregadoById(@PathParam("idFunc") Integer idFunc) {
		
		if (idFunc <= 0) {
			return Response.noContent().build();
		}
		EmpregadoTO empregadoTO = new FuncionarioDAO().buscarFuncionarioById(idFunc);
		
		GenericEntity<EmpregadoTO> entity = new GenericEntity<EmpregadoTO>(empregadoTO, empregadoTO.getClass());
		return Response.ok().entity(entity).build();
	}
}
