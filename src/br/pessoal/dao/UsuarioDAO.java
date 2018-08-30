package br.pessoal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import br.pessoal.to.UsuarioTO;

public class UsuarioDAO {

	private Connection connection;
	private PreparedStatement statement;
	private ResultSet resultSet;
	
	ConnectionPool pool = new ConnectionPool();
	private Logger logger = Logger.getLogger(FuncionarioDAO.class);
	
	public boolean isUsuarioReal(UsuarioTO usuarioTO) {
		
		boolean isUserReal = false;
		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT EXISTS( ")
					.append("SELECT * FROM login ")
					.append("INNER JOIN	tiposusuario ON tiposusuario.usuario = ? ")
					.append("WHERE login.usuario = ? ")
					.append("AND ")
					.append("login.senha = ?) ");
		
		try {
			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setString(1, usuarioTO.getTipoUsuario());
			statement.setString(2, usuarioTO.getUsuario());
			statement.setString(3, usuarioTO.getSenha());
			
			logger.info(statement.toString());
			resultSet = statement.executeQuery();
			
			if (resultSet.next()) {
				isUserReal = resultSet.getBoolean(1);
			}
						
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally {

			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {				 
				e.printStackTrace();
			}

		}
		
		return isUserReal;
	}
}
