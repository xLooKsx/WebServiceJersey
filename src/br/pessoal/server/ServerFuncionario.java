package br.pessoal.server;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

//Caso a anotacao @Seguro seja colocada aqui todos os recursos ficaram protegidos
@Path("/funcionario")
public class ServerFuncionario {

	private FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
	private WebServiceUtils utils = new WebServiceUtils();

	@GET
	@Path("/sucesso")
	public Response getSmsPositivo() {
		return Response.ok().entity("Operacao concluida com sucesso").build();
	}

	@GET
	@Path("/falha")
	public Response getSmsNegativo() {		
		return Response.ok().entity("Falha na Operacao").build();
	}

	@GET
	@Path("/funcionarios")
	@Produces(MediaType.APPLICATION_XML)
	public List<EmpregadoTO> getEmpregados(){

		return funcionarioDAO.getListEmpregados();
	}
	
	@Seguro
	@DELETE
	@Path("/rmrf/funcionario/{id}")
	public Response apagarFuncionario(@PathParam("id") Integer id) {
		
		if (id <= 0) {
			return Response.ok().entity("Codigo de Id invalido").build();
		}
		 funcionarioDAO.apagarFuncionario(id);
		return Response.ok().entity("Funcionario Deletado").build();
	}
	
	@PUT
	@Path("/attProfFuncionarios/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response atualizarFuncionario(@PathParam("id") Integer id, String profissao) {
		
		if (!utils.isStringEmptyOrNull(profissao)) {
			return Response.status(400).entity("Favor entre com uma profissao").build();
		}
			funcionarioDAO.attProfFuncionario(id, profissao);			
		return Response.ok().entity("Operacao concluida com sucesso").build();
	}

	@GET
	@Path("/funcionarios/{idFunc}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getEmpregadoById(@PathParam("idFunc") Integer idFunc) {

		if (idFunc <= 0) {
			return Response.noContent().build();
		}
		EmpregadoTO empregadoTO = funcionarioDAO.buscarFuncionarioById(idFunc);

		GenericEntity<EmpregadoTO> entity = new GenericEntity<EmpregadoTO>(empregadoTO, empregadoTO.getClass());
		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/cadFuncionarios")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response adicionarEmpregado(EmpregadoTO empregadoTO) throws URISyntaxException {

		if (empregadoTO == null) {
			return Response.status(400).entity("Funcionario Invalido").build();
		}else if(utils.isEmpregadoVazio(empregadoTO)) {
			return Response.status(400).entity("Funcionario Vazio").build();
		}

		return Response.created(new URI(funcionarioDAO.cadastrarFuncionario(empregadoTO)?"/sucesso":"/falha")).build();
	}
}
