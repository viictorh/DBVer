package br.com.dbver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.driver.DriverJDBC;

/**
 * 
 * @author victor
 *
 */
public class Database {

	public static Connection createConnection(DriverJDBC driverJDBC, ServerConnection serverConnection)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverJDBC.getDriverClass());
		return DriverManager.getConnection(driverJDBC.generateDbUrl(serverConnection.getServer(),
				serverConnection.getDatabaseName(), serverConnection.getUser(), serverConnection.getPassword(),
				serverConnection.getInstance(), serverConnection.getPort()));
	}

	public static Connection createMasterConnection(DriverJDBC driverJDBC, ServerConnection serverConnection)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverJDBC.getDriverClass());
		return DriverManager.getConnection(driverJDBC.generateServerUrl(serverConnection.getServer(), serverConnection.getUser(),
						serverConnection.getPassword(), serverConnection.getInstance(), serverConnection.getPort()));
	}

}
