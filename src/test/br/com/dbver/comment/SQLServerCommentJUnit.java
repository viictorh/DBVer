package br.com.dbver.comment;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.dao.Database;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.driver.SQLServerDriver;

/**
 * 
 * @author victor
 *
 */
public class SQLServerCommentJUnit {

	private final static Logger logger = Logger.getLogger(SQLServerCommentJUnit.class);
	private Database dbExecutor;

	@Before
	public void setUp() {
		logger.debug("Iniciando teste");
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setPort("1433");
		serverConnection.setInstance("");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		serverConnection.setDatabaseName("Voscenter");
		dbExecutor = new Database(serverConnection, driverJDBC);
	}

	@Test
	public void simpleQuery() throws ClassNotFoundException, SQLException {
		dbExecutor.executeQuery("SELECT 1\n" + 
				"GO;\n" + 
				"\n" + 
				"-- SELECT 1 GO\n" + 
				"\n" + 
				"SELECT 1\n" + 
				"GO\n" + 
				"\n" + 
				"/*\n" + 
				"SELECT 1 \n" + 
				" go; \n" + 
				" */\n" + 
				"\n" + 
				"SELECT 1 \n" + 
				"go ", true);
	}
}
