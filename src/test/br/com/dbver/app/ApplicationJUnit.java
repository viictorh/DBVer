package br.com.dbver.app;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import br.com.dbver.bean.FolderExecute;
import br.com.dbver.bean.ServerConnection;
import br.com.dbver.bean.Settings;
import br.com.dbver.bean.Settings.ErrorAction;
import br.com.dbver.dao.Database;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.driver.SQLServerDriver;
import br.com.dbver.exception.ExecutionError;
import br.com.dbver.service.ScriptExecutor;
import br.com.dbver.util.MapBuilder;

/**
 * 
 * @author victor
 *
 */
public class ApplicationJUnit {
	private final static Logger logger = Logger.getLogger(ApplicationJUnit.class);
	private ScriptExecutor scriptExecutor;
	private Settings settings;

	@Before
	public void setUp() {
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setPort("1433");
		// serverConnection.setInstance("SQL2017");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		serverConnection.setDatabaseName("Voscenter");
		settings = new Settings(true, serverConnection, driverJDBC, ErrorAction.DROP);
		scriptExecutor = new ScriptExecutor(settings);
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException, ExecutionError {
		String basePath = "C:\\dbver\\DBVER_BD\\VOSCENTER" + File.separator;
		FolderExecute createMDF = new FolderExecute(new File(basePath + "MDF"), true);
		FolderExecute createConfig = new FolderExecute(new File(basePath + "Configuration"), true);
		FolderExecute createTables = new FolderExecute(new File(basePath + "Tables"), false);
		FolderExecute createSynonyms = new FolderExecute(new File(basePath + "Synonyms"), false);
		FolderExecute createViews = new FolderExecute(new File(basePath + "Views"), false);
		FolderExecute createFunctions = new FolderExecute(new File(basePath + "Functions"), false);
		FolderExecute createProcedures = new FolderExecute(new File(basePath + "Procedures"), false);
		FolderExecute createTriggers = new FolderExecute(new File(basePath + "Triggers"), false);
		FolderExecute createIndex = new FolderExecute(new File(basePath + "Index"), false);
		FolderExecute createPreCarga = new FolderExecute(new File(basePath + "PreCarga"), false);
		FolderExecute createConstraints = new FolderExecute(new File(basePath + "Constraints"), false);
		FolderExecute createSynonymsUpdate = new FolderExecute(new File(basePath + "SynonymsUpdate"), false);

		Map<String, String> parameters = MapBuilder.<String, String>unordered().put("$(varPathMDF)", "C:\\dbver\\Bases")
				.put("$(varPathLDF)", "C:\\dbver\\Bases")
				.put("$(varDBName)", settings.getServerConnection().getDatabaseName()).build();

		scriptExecutor.execute(Arrays.asList(createMDF, createConfig, createTables, createSynonyms, createViews,
				createFunctions, createTriggers, createProcedures, createIndex, createPreCarga, createConstraints,
				createSynonymsUpdate), parameters);
		logger.info("Execution completed successfully");
		dropDatabase();
	}

	// @Test
	public void dropDatabase() throws SQLException {
		Database database = new Database(settings.getServerConnection(), settings.getDriverJDBC());
		database.dropDatabase("voscenter");
		logger.info("Database deleted successfully");
	}

}
