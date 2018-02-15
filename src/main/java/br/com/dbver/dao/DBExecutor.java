package br.com.dbver.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.driver.DriverJDBC;

/**
 * 
 * @author victor
 *
 */
public class DBExecutor {
	private final static Logger logger = Logger.getLogger(DBExecutor.class);
	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;

	public DBExecutor(ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
	}

	public void executeQuery(String query) throws ClassNotFoundException, SQLException {
		logger.debug("executeQuery(query)");
		logger.debug(query);
		try (Connection connection = Database.createConnection(driverJDBC, serverConnection);
				Statement stmt = connection.createStatement();) {
			stmt.execute(query);
		}
	}

	public void executeQuery(Connection connection, String query) throws ClassNotFoundException, SQLException {
		logger.debug("executeQuery(query)");
		logger.debug(query);
		try (Statement stmt = connection.createStatement();) {
			stmt.execute(query);
		}
	}

}
