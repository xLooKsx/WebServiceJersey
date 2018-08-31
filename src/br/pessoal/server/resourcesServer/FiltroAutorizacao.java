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

//Define que a classe vai prover as funcionalidades para o seguro e n�o o contrario
@Provider

/*E indica que sera prioriedade de execucao, nesse caso esse filtro vai ser
 * executado apos o filtro de autenticacao, pois a prioriedade de autenticacao
 * � maior que o da autorizacao*/
@Priority(Priorities.AUTHORIZATION)
public class FiltroAutorizacao implements ContainerRequestFilter{

	//O JAX-RS faz a inje��o do ResourceInfo que vai ter os informa��es do m�todo que ta sendo verificado
	@Context
	private ResourceInfo resourceInfo;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		 
		// Pega a classe que contem URL requisitada e extrai os n�vel de permiss�o
		Class<?> classe = resourceInfo.getResourceClass();
		List<NivelPermissao> nivelPermissaoClasse = extrairNivelPermissao(classe);
		
		// Pega o m�todo que contem URL requisitada e extrai os n�vel de permiss�o
		Method metodo = resourceInfo.getResourceMethod();
		List<NivelPermissao> nivelPermissaoMetodo = extrairNivelPermissao(metodo);
		try {
			/*Como modificamos o securityContext na hora de validar o token, para podemos pegar
			 *O login do usu�rio, para fazer a verifica��o se ele tem o n�vel de permiss�o necess�rio
			*para esse endpoint*/
			String login = requestContext.getSecurityContext().getUserPrincipal().getName();
			
			/*Verifica se o usu�rio tem permiss�o pra executar esse m�todo
			 * Os n�veis de acesso do m�todo sobrep�e o da classe*/
			if (nivelPermissaoMetodo.isEmpty()) {
				checarPermissoes(nivelPermissaoClasse, login, requestContext);
			}else {
				checarPermissoes(nivelPermissaoMetodo, login, requestContext);				
			}
		} catch (Exception e) {
			 /*Se caso o usu�rio n�o possui permiss�o � dado um exception,
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
