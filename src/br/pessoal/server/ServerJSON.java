package br.pessoal.server;

import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.pessoal.dao.FuncionarioDAO;
import br.pessoal.server.resourcesServer.Seguro;
import br.pessoal.server.utils.WebServiceUtils;
import br.pessoal.to.EmpregadoTO;

@Path("/JSON")
public class ServerJSON {

	private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
	private WebServiceUtils utils = new WebServiceUtils();
	
	@GET
	@Path("/consultaFuncionario/{idFunc}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getEmpregadoById(@PathParam("idFunc") Integer idFunc) {

		if (idFunc <= 0) {
			return Response.noContent().build();
		}
		EmpregadoTO empregadoTO = funcionarioDAO.buscarFuncionarioById(idFunc);

		GenericEntity<EmpregadoTO> entity = new GenericEntity<EmpregadoTO>(empregadoTO, empregadoTO.getClass());
		return Response.ok().entity(entity).build();
	}
	
//	@Seguro
	@POST
	@Path("/cadFuncionario")
	@Consumes(MediaType.APPLICATION_JSON)	
	public Response adicionarEmpregado(EmpregadoTO empregadoTO) throws URISyntaxException {

		if (empregadoTO == null) {
			return Response.status(400).entity("Funcionario Invalido").build();
		}else if(utils.isEmpregadoVazio(empregadoTO)) {
			return Response.status(400).entity("Funcionario Vazio").build();
		}

		return Response.status(202).entity("NOVO FUNCIONARIO: " + empregadoTO.toString()).build();
	}
}
