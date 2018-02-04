package br.com.dbver.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.driver.DriverJDBC;

public class Database {

	static Connection createConnection(DriverJDBC driverJDBC, ServerConnection serverConnection)
			throws ClassNotFoundException, SQLException {
		Class.forName(driverJDBC.getDriverClass());
		return DriverManager.getConnection(driverJDBC.getDbUrl(serverConnection.getServer(), serverConnection.getUser(),
				serverConnection.getPassword()));
	}

}
