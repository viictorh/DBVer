package br.com.dbver.main;

import java.io.File;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.driver.SQLServerDriver;
import br.com.dbver.service.ScriptExecutor;

/**
 * 
 * @author victor
 *
 */
public class ExecutionJUnit {
	private final static Logger logger = Logger.getLogger(ExecutionJUnit.class);
	private ScriptExecutor scriptExecutor;
	private File file;

	@Before
	public void setUp() {
		file = new File("C:/Users/victor/Downloads");
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		scriptExecutor = new ScriptExecutor(serverConnection, driverJDBC);
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		scriptExecutor.scriptsFrom(file);

	}

}
