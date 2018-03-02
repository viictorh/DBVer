package br.com.dbver.sqlserver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
public class CommentJUnit {

	private final static Logger logger = Logger.getLogger(CommentJUnit.class);
	private Database dbExecutor;
	DriverJDBC driverJDBC = new SQLServerDriver();

	@Before
	public void setUp() {
		logger.debug("Iniciando teste");
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
	public void complexFileComments() throws IOException, ClassNotFoundException, SQLException {
		String basePath = "C:\\dbver\\VOSCENTER\\test\\dbo.sp_TActiveMailingFile_Select.sql";
		String fileString = new String(Files.readAllBytes(Paths.get(basePath)), StandardCharsets.UTF_8).trim();
		driverJDBC.preProcessComments(fileString);
		logger.info("Execução concluida com sucesso");
	}

	@Test
	public void simpleQuery() throws ClassNotFoundException, SQLException {
		dbExecutor.executeQuery("SELECT 1\n" + "GO;\n" + "\n" + "-- SELECT 1 GO\n" + "\n" + "SELECT 1\n" + "GO\n" + "\n"
				+ "/*\n" + "SELECT 1 \n" + " go; \n" + " */\n" + "\n" + "SELECT 1 \n" + "go ", true);
	}
}
