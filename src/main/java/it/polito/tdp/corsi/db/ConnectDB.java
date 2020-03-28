package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectDB {
	//definisco la sringa di connessione
	private static final String jdbcURL = "jdbc:mysql://localhost/iscritticorsi";
	//riferimento al data source
	private static HikariDataSource ds;
	
	public static Connection getConnection() {
		if (ds==null) {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(jdbcURL);
			config.setUsername("root");
			config.setPassword("Vale1998Giammy");
			
			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtChacheSize", 250);
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			ds= new HikariDataSource (config);  //se era nullo lo creiamo
		}
		try {
		return ds.getConnection();	
		} catch (SQLException e) {
			System.err.println("Errore di connessione a db");
			throw new RuntimeException(e);
		}
	}

}
