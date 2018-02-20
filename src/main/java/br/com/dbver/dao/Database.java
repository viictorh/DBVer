package br.com.dbver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.driver.DriverJDBC;

/**
 * 
 * @author victor
 *
 */
public class Database {
	private final static Logger logger = Logger.getLogger(Database.class);
	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;

	public Database(ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
	}

	public Connection createConnection(boolean master) throws ClassNotFoundException, SQLException {
		Class.forName(driverJDBC.getDriverClass());
		return DriverManager.getConnection(driverJDBC.generateURLConnection(serverConnection, master));
	}

	public void executeQuery(String query, boolean master) throws ClassNotFoundException, SQLException {
		logger.debug("executeQuery(query)");
		logger.debug(query);
		try (Connection connection = createConnection(master)) {
			executeQuery(connection, query);
		}
	}

	public void executeQuery(Connection connection, String query) throws ClassNotFoundException, SQLException {
		logger.debug("executeQuery(query)");
		logger.debug(query);
		try (Statement stmt = connection.createStatement();) {
			List<String> queryList = driverJDBC.prepareQuery(query);
			for (String q : queryList) {
				stmt.execute(q);
			}
		}
	}

}
