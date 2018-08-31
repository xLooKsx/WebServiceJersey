package br.pessoal.server.resourcesServer;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.Claims;

//Defini que a @seguro que vai utilizar essa classe
@Seguro
//Indica que essa classe vai prover a funcionalidade pra @seguro e n�o o contrario
@Provider
//E indica que sera prioriedade de execucao, pois podemos ter outras classe filtro que devem ser executas em uma ordem expecifica
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutenticacao implements ContainerRequestFilter{

	// o ContainerRequestContext que � o objeto que podemos manipular a request
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		 
		//Verifica se o header AUTHORIZATION existe ou n�o, se existe extrai o token se n�o aborta a requisi��o retornando uma NotAuthorizedException
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new NotAuthorizedException("Authorization header precisa ser provido");
		}
		
		//extrai o token do header
		String token = authorizationHeader.substring("Bearer".length()).trim();
		/*
		 * Verificamos agora se o header � valido ao n�o
		 * Se n�o for valida o processo de valida��o � abortado e retorna um codigo 401 UNAUTHORIZED
		 * Se for valida modificamos o SecurityContext da request
		 * para quando usarmos o getUserPrincipal retorne um login valido*/
		try {
			//metodo que verifica se o token � valido ou n�o
			Claims claims = new LoginService().validaToken(token);
			
			//Se n�o for valido o metodo retorna um objeto nulo que vai gerar uma exception
			if (claims == null || checkDateToken(claims.getExpiration().getTime())) {
				throw new Exception("Token Invalido");
			}
			
			//metodo que modifica o SecurityContext para disponibilizar o login do usuario
			modificarRequestContext(requestContext, claims.getIssuer());
		} catch (Exception e) {
			
			//Caso o token for invalido a requisi��o � abortada e retorna uma resposta com status 401 UNAUTHORIZED
			e.printStackTrace();	
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
	
	private boolean checkDateToken(Long validadeToken) {
		
		Date dataToken = new Date(validadeToken);
		Date dataAtual = new Date();
		
		if (dataToken.before(dataAtual)) {
			return true;
		}
		
		return false;
	}

	//metodo que modifica o RequestContext
	private void modificarRequestContext(ContainerRequestContext requestContext, String login) {
		 
		final SecurityContext securityContext = requestContext.getSecurityContext();
		requestContext.setSecurityContext(new SecurityContext() {
			
			@Override
			public boolean isUserInRole(String role) {				 
				return true;
			}
			
			@Override
			public boolean isSecure() {				 
				return securityContext.isSecure();
			}
			
			@Override
			public Principal getUserPrincipal() {
				return new Principal() {					
					@Override
					public String getName() {						 
						return login;
					}
				};
			}
			
			@Override
			public String getAuthenticationScheme() {				 
				return "Bearer";
			}
		});
	}

}
