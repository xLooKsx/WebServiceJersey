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

	public boolean cadastrarFuncionario(EmpregadoTO empregadoTO) {

		boolean operacaoEfetuada = false;
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("INSERT INTO funcionario( ")
		.append("id_pessoa, ")
		.append("nome, ")
		.append("idade, ")
		.append("cod_prof) ")
		.append("VALUES(?, ?, ?, ?)");	

		try {

			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setInt(1, empregadoTO.getCodPessoa());
			statement.setString(2, empregadoTO.getNomePessoa());
			statement.setInt(3, empregadoTO.getIdade());
			statement.setInt(4, getCodProfissao(empregadoTO.getProfissao()));

			logger.info(statement.toString());
			statement.execute();
			operacaoEfetuada = true;

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

		return operacaoEfetuada;
	}

	private int getCodProfissao(String profissao) {

		PreparedStatement statement = null;		

		int codigoProfissao = 1;

		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT id_codprof ")
		.append("FROM profissao ")
		.append("WHERE nomeprof = ? ");	

		try {

			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setString(1, profissao.trim());

			logger.info(statement.toString());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				codigoProfissao = resultSet.getInt(1);
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally {

			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {				 
				e.printStackTrace();
			}

		}

		return codigoProfissao;
	}

	public EmpregadoTO buscarFuncionarioById(Integer id) {

		EmpregadoTO empregadoTO = new EmpregadoTO();
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT funcionario.id_pessoa, ")
		.append("funcionario.nome, ")
		.append("funcionario.idade, ")
		.append("profissao.nomeprof, ")
		.append("tiposusuario.usuario ")
		.append("FROM login, tiposusuario, funcionario ")
		.append("INNER JOIN profissao ON funcionario.cod_prof = profissao.id_codprof ")
		.append("WHERE login.tipousuario = tiposusuario.codtipousuario AND ")
		.append("funcionario.id_pessoa = ? ");	

		try {
			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setInt(1, id);

			logger.info(statement.toString());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {

				empregadoTO.setCodPessoa(resultSet.getInt(1));
				empregadoTO.setNomePessoa(resultSet.getString(2));
				empregadoTO.setIdade(resultSet.getInt(3));
				empregadoTO.setProfissao(resultSet.getString(4));
				//				empregadoTO.setTipoUsuario(resultSet.getString(5));
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


		try {
			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());

			logger.info(statement.toString());
			resultSet = statement.executeQuery();

			while (resultSet.next()) {

				EmpregadoTO empregadoTO = new EmpregadoTO();
				empregadoTO.setCodPessoa(resultSet.getInt(1));
				empregadoTO.setNomePessoa(resultSet.getString(2));
				empregadoTO.setIdade(resultSet.getInt(3));
				empregadoTO.setProfissao(resultSet.getString(4));
				//				empregadoTO.setTipoUsuario(resultSet.getString(5));
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
	
	public void attProfFuncionario(int id, String profissao) {
		
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("UPDATE funcionario ")
					.append("SET cod_prof = ? ")
					.append("WHERE id_pessoa = ? ");
		
		try {
			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setInt(1, id);
			statement.setInt(2, getCodProfissao(profissao));
			
			logger.info(statement.toString());
			statement.execute();
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


		try {

			connection = pool.createSharedPoolDataSource();
			statement = connection.prepareStatement(sqlStatement.toString());
			statement.setString(1, user);
			statement.setString(2, password);

			logger.info(statement.toString());
			resultSet = statement.executeQuery();


			if (resultSet.next()) {

				empregadoTO.setCodPessoa(resultSet.getInt(1));
				empregadoTO.setNomePessoa(resultSet.getString(2));
				empregadoTO.setIdade(resultSet.getInt(3));
				empregadoTO.setProfissao(resultSet.getString(4));
				//				empregadoTO.setTipoUsuario(resultSet.getString(5));
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
