package br.pessoal.server.resourcesServer;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import br.pessoal.numeration.NivelPermissao;

//Define que o seguro vai usar essa classe
@Seguro

//Define que a classe vai prover as funcionalidades para o seguro e não o contrario
@Provider

/*E indica que sera prioriedade de execucao, nesse caso esse filtro vai ser
 * executado apos o filtro de autenticacao, pois a prioriedade de autenticacao
 * é maior que o da autorizacao*/
@Priority(Priorities.AUTHORIZATION)
public class FiltroAutorizacao implements ContainerRequestFilter{

	//O JAX-RS faz a injeção do ResourceInfo que vai ter os informações do método que ta sendo verificado
	@Context
	private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		 
		// Pega a classe que contem URL requisitada e extrai os nível de permissão
		Class<?> classe = resourceInfo.getResourceClass();
		List<NivelPermissao> nivelPermissaoClasse = extrairNivelPermissao(classe);
		
		// Pega o método que contem URL requisitada e extrai os nível de permissão
		Method metodo = resourceInfo.getResourceMethod();
		List<NivelPermissao> nivelPermissaoMetodo = extrairNivelPermissao(metodo);
		try {
			/*Como modificamos o securityContext na hora de validar o token, para podemos pegar
			 *O login do usuário, para fazer a verificação se ele tem o nível de permissão necessário
			*para esse endpoint*/
			String login = requestContext.getSecurityContext().getUserPrincipal().getName();
			
			/*Verifica se o usuário tem permissão pra executar esse método
			 * Os níveis de acesso do método sobrepõe o da classe*/
			if (nivelPermissaoMetodo.isEmpty()) {
				checarPermissoes(nivelPermissaoClasse, login, requestContext);
			}else {
				checarPermissoes(nivelPermissaoMetodo, login, requestContext);				
			}
		} catch (Exception e) {
			 /*Se caso o usuário não possui permissão é dado um exception,
			  * e retorna um resposta com o status 403 FORBIDDEN */
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
		}
	}

	private void checarPermissoes(List<NivelPermissao> nivelPermissaoMetodo, String login, ContainerRequestContext requestContext) {
		 try {
			if (nivelPermissaoMetodo.isEmpty()) {
				return;
			}
			
			boolean temPermissao = false;
			
			//Busca os niveis de acesso do usuario
			NivelPermissao nivelPermissao = new LoginService().buscarNivelPermissao(login);
			for (NivelPermissao nivelPermissaoDaVez : nivelPermissaoMetodo) {
				if (nivelPermissaoDaVez.equals(nivelPermissao)) {
					temPermissao = true;
					break;
				}
			}
			if (!temPermissao) {
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
			}
		} catch (Exception e) {
			 e.printStackTrace();			 
		}
		
	}

	private List<NivelPermissao> extrairNivelPermissao(AnnotatedElement annotatedElement) {
		 
		if (annotatedElement == null) {
			return new ArrayList<NivelPermissao>();
		}else {
			Seguro seguro = annotatedElement.getAnnotation(Seguro.class);
			if (seguro == null) {
				return new ArrayList<NivelPermissao>();
			}else {
				NivelPermissao[] niveisPermitidos = seguro.value();
				return Arrays.asList(niveisPermitidos);
			}
		}		
	}

}
