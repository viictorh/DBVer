package br.com.dbver.file;

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
public class ExecuteFileDBJUnit {

	private final static Logger logger = Logger.getLogger(ExecuteFileDBJUnit.class);
	private ScriptExecutor scriptExecutor;

	@Before
	public void setUp() {
		logger.debug("Iniciando teste");
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setPort("1433");
		serverConnection.setInstance("SQL2017");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
//		scriptExecutor = new ScriptExecutor(serverConnection, driverJDBC);
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
//		scriptExecutor.scriptsFrom(new File("C:/Users/victor/Downloads"));

	}

}
