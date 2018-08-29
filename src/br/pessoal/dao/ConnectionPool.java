package br.pessoal.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.tomcat.dbcp.dbcp2.datasources.SharedPoolDataSource;

public class ConnectionPool {

	public Connection createSharedPoolDataSource() {

		DriverAdapterCPDS adapterCPDS = createAdapaterCPDS();
		try {
			SharedPoolDataSource dataSource= new SharedPoolDataSource();
			dataSource.setConnectionPoolDataSource(adapterCPDS);
			dataSource.setMaxConnLifetimeMillis(15000);
			dataSource.setMaxTotal(10);

			return dataSource.getConnection();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		return null;
	}

	private DriverAdapterCPDS createAdapaterCPDS() {

		DriverAdapterCPDS adapterCPDS = null;
		try {
			adapterCPDS = new DriverAdapterCPDS();
			adapterCPDS.setDriver("org.postgresql.Driver");
			adapterCPDS.setUrl("jdbc:postgresql://127.0.0.1:5432/web");			
			adapterCPDS.setUser("postgres");
			adapterCPDS.setPassword("admin");
			adapterCPDS.setLoginTimeout(1800);
			adapterCPDS.setMaxIdle(5);			
		} catch (ClassNotFoundException e) {			 
			e.printStackTrace();
		}

		return adapterCPDS;
	} 
}
