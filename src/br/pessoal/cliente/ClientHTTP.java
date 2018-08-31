package br.pessoal.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;


public class ClientHTTP {

//	private static final Logger logger = Logger.getLogger(ClientHTTP.class);

	public static void main(String[] args) {

		clientHTTPAutenticado();
	}

	private static void receberJSON() {
		try {
			URL url = new URL("http://127.0.0.1:8080/WebService/JSON/consultaFuncionario/1600344");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("FALHA: ERRO NO CODIGO HTTP: "+ connection.getResponseCode());
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String resposta;
			System.out.println("RESPOSTA DO WEBSERVICE....");
			while ((resposta = reader.readLine()) != null) {
				System.out.println(resposta);
			}
			
			connection.disconnect();
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		} catch (IOException e) {			 
			e.printStackTrace();
		} 
	}
	
	private static void enviarJSON() {
		
		StringBuilder json = new StringBuilder();
		json.append("{\r\n" + 
				"	\"usuario\": \"LooKs\",\r\n" + 
				"	\"senha\": \"admin\",\r\n" + 
				"	\"tipoUsuario\": \"ADMIN\"\r\n" + 				
				"}");
		
		try {
			URL url = new URL("http://127.0.0.1:8080/WebService/teste/");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(json.toString().getBytes());
			outputStream.flush();
			
//			if (connection.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
//				throw new RuntimeException("FALHA: CODIGO HTTP ERRO: " + connection.getResponseCode());
//			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String resposta;
			System.out.println("RESPOSTA DO SERVIDOR......");
			while ((resposta = reader.readLine()) != null) {
				System.out.println(resposta);
			}
			
			connection.disconnect();
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		} catch (IOException e) {			 
			e.printStackTrace();
		}
	}
	
	private static void clientHTTPAutenticado() {
		
		String token = "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE1MzU3MjI2NDgsImlzcyI6Ik9QRVJBRE9SIiwiZXhwIjoxNTM1ODA5MDUwfQ.jBMnqC8jQNGMdm4wQFe7DR1Os6jdqOV13MSjnHd30dIzRxcGTeq89QwEyBFQBIIglO6EdEV9SjoVzTssU6Ou8w";		
		StringBuilder json = new StringBuilder();
		json.append("{\r\n" + 
				"	\"usuario\": \"LooKs\",\r\n" + 
				"	\"senha\": \"admin\"\r\n" + 				 			
				"}");
		
		try {
			//----------------------INICIO DA REQUISICAO DO TOKEN----------------------------------------
//			URL urlToken = new URL("http://127.0.0.1:8080/WebService/login");
//			HttpURLConnection connectionToken = (HttpURLConnection) urlToken.openConnection();
//			connectionToken.setDoOutput(true);
//			connectionToken.setRequestMethod("POST");
//			connectionToken.setRequestProperty("Content-Type", "application/json");
//			
//			OutputStream outputStream = connectionToken.getOutputStream();
//			outputStream.write(json.toString().getBytes());
//			outputStream.flush();
//			
//			BufferedReader readerToken = new BufferedReader(new InputStreamReader(connectionToken.getInputStream()));		
//			token = readerToken.readLine();
//			connectionToken.disconnect();
//			System.out.println(token);
//			//----------------------FIM DA REQUISICAO DO TOKEN----------------------------------------
			
			
			URL urlServico = new URL("http://127.0.0.1:8080/WebService/teste/milhaToQuilometros/2");
			HttpURLConnection connectionServico = (HttpURLConnection) urlServico.openConnection();
			connectionServico.setRequestMethod("GET");
			connectionServico.setRequestProperty("Authorization", "Bearer "+token);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connectionServico.getInputStream()));
			String respostaServico;
			System.out.println("RESPOSTA DO SERVIDOR......");
			while ((respostaServico = reader.readLine()) != null) {
				System.out.println(respostaServico);
			}
			
			connectionServico.disconnect();
			
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		} catch (IOException e) {			 
			e.printStackTrace();
		}
	}
}
