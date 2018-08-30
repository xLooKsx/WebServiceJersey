package br.pessoal.teste;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.pessoal.dao.UsuarioDAO;
import br.pessoal.server.utils.WebServiceUtils;
import br.pessoal.to.UsuarioTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/login")
public class LoginService {
	
	private WebServiceUtils utils = new WebServiceUtils();
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	private final static String FRASE_SEGREDO = "1600344";

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response fazerLogin(String credenciaisJSON) {
		
		try {
			//Instancia o objeto Gson que vai ser responsável por transformar o corpo da request que está na variável crendenciaisJson em um objeto java Credencial
			Gson gson = new Gson();
			//aqui o objeto gson transforma a crendenciaisJson pra a variavel usuario do tipo Usuario
			UsuarioTO usuario = gson.fromJson(credenciaisJSON, UsuarioTO.class);
			//Verifica se a credencial é valida, se não for vai dar exception 
			if (utils.isUsuarioVazio(usuario)) {
				return Response.status(500).entity("USUARIO INVALIDO").build();
			}else if(!usuarioDAO.isUsuarioReal(usuario)) {
				return Response.status(500).entity("USUARIO INVALIDO").build();
			}
			
			//Se a for valida gera o token e passa a quantidade de dias que o token vai ser valido nesse caso 1 dia
			String token = gerarToken(usuario.getUsuario(), 1);
			
			//Retorna uma resposta com o status 200 OK com o token gerado
			return Response.ok(token).build();
									
		} catch (Exception e) {

			//Caso ocorra algum erro retorna uma resposta com o status 401 UNAUTHORIZED
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
	}

	private String gerarToken(String usuario, int diasValidos) {
		 
		//Defini qual vai ser o algoritmo da assinatura no caso vai ser o HMAC SHA512
		SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;
		//Data atual, data que o token foi gerado
		Date dataAtual = new Date();
		
		//Define até que data o token é valido, no case é a quantidade de dias que foi passado por parametro
		Calendar expira = Calendar.getInstance();
		expira.add(Calendar.DAY_OF_MONTH, diasValidos);
		
		//Encoda a frase segredo pra base64 pra ser usada na geração do token 
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(FRASE_SEGREDO);
		SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
		
		//finalmente utiliza o JWT builder pra gerar o token
		JwtBuilder construtor = Jwts.builder()
				.setIssuedAt(dataAtual) //Data em que o token Foi gerado
				.setIssuer(usuario) //Coloca o login do usuario, mas pode ser qualque outra informação
				.signWith(algorithm, key) //Coloca o algoritimo de assinatura e a frase de segredo ja encodada
				.setExpiration(expira.getTime()); //Coloca até que data o token é valido
		
		return construtor.compact();
	}

	public Claims validaToken(String token) {
		 
		/*
		 * JJWT vai validar o token caso o token não seja valido ele vai executar um exception
		 * o JJWT usa a frase segredo para descodificar o token e ficando assim possivel
		 * recuperar as informaçoes que colocamos no payload*/
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(FRASE_SEGREDO))
				.parseClaimsJws(token).getBody();
		
		return claims;
	}
}
