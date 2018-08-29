package br.pessoal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.pessoal.to.EmpregadoTO;

public class FuncionarioDAO {

	private Connection connection;
	private PreparedStatement statement;
	private ResultSet resultSet;

	private ConnectionPool pool = new ConnectionPool();
	private Logger logger = Logger.getLogger(FuncionarioDAO.class.getName());

	public FuncionarioDAO() {

	}

	public List<EmpregadoTO> getListEmpregados(){

		List<EmpregadoTO> empregados = new ArrayList<>();		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT funcionario.id_pessoa, ")
		.append("funcionario.nome, ")
		.append("funcionario.idade, ")
		.append("profissao.nomeprof, ")
		.append("tiposusuario.usuario ")
		.append("FROM login, tiposusuario, funcionario ")
		.append("INNER JOIN profissao ON funcionario.cod_prof = profissao.id_codprof ")
		.append("WHERE login.tipousuario = tiposusuario.codtipousuario");

		logger.info(sqlStatement.toString());

		try {
			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				EmpregadoTO empregadoTO = new EmpregadoTO();
				empregadoTO.setCodPessoa(resultSet.getInt(1));
				empregadoTO.setNomePessoa(resultSet.getString(2));
				empregadoTO.setIdade(resultSet.getInt(3));
				empregadoTO.setProfissao(resultSet.getString(4));
				empregadoTO.setTipoUsuario(resultSet.getString(5));
				empregados.add(empregadoTO);

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

		return empregados;
	}

	public EmpregadoTO buscarUsuario(String user, String password) {

		EmpregadoTO empregadoTO = new EmpregadoTO();
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT funcionario.id_pessoa, ")
		.append("funcionario.nome, ")
		.append("funcionario.idade, ")
		.append("profissao.nomeprof, ")
		.append("tiposusuario.usuario ")
		.append("FROM login, tiposusuario, funcionario ")
		.append("INNER JOIN profissao ON funcionario.cod_prof = profissao.id_codprof ")
		.append("WHERE login.usuario = 'LooKs' AND ")
		.append("login.senha = 'admin' AND ")
		.append("login.tipousuario = tiposusuario.codtipousuario ");

		logger.info(sqlStatement.toString());

		try {

			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setString(1, user);
			statement.setString(2, password);
			resultSet = statement.executeQuery();


			if (resultSet.next()) {

				empregadoTO.setCodPessoa(resultSet.getInt(1));
				empregadoTO.setNomePessoa(resultSet.getString(2));
				empregadoTO.setIdade(resultSet.getInt(3));
				empregadoTO.setProfissao(resultSet.getString(4));
				empregadoTO.setTipoUsuario(resultSet.getString(5));
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
		return empregadoTO;
	}
}
