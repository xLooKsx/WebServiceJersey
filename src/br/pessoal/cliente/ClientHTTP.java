package br.pessoal.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class ClientHTTP {

	private static final Logger logger = Logger.getLogger(ClientHTTP.class);

	public static void main(String[] args) {

		receberJSON();
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
				"	\"PessoaTO\": {\r\n" + 
				"		\"codPessoa\": 3,\r\n" + 
				"		\"nomePessoa\": \"Jacarepagua\",\r\n" + 
				"		\"idade\": 2,\r\n" + 
				"		\"profissao\": \"crianca\"\r\n" + 
				"	}\r\n" + 
				"}");
		
		try {
			URL url = new URL("http://127.0.0.1:8080/WebService/JSON/cadFuncionario");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
		} catch (MalformedURLException e) {			
			e.printStackTrace();
		} catch (IOException e) {			 
			e.printStackTrace();
		}
	}
}
