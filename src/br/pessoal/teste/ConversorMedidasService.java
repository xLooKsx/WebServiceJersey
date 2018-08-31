package br.pessoal.teste;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.pessoal.numeration.NivelPermissao;
import br.pessoal.server.resourcesServer.Seguro;

@Path("/teste")
public class ConversorMedidasService {

	@Seguro({NivelPermissao.ADMIN})
	@GET
	@Path("quilometrosToMilha/{quilometros}")
	public Response quilometrosToMilha(@PathParam("quilometros") Double quilometros) {
		
		quilometros /= 1.6;
		return Response.ok(quilometros).build();
	}
	
	@Seguro({NivelPermissao.OPERADOR})
	@GET
	@Path("milhaToQuilometros/{milhas}")
	public Response milhasToQuilometros(@PathParam("milhas") Double milhas) {
		
		milhas *= 1.6;
		return Response.ok(milhas).build();
	}
}
