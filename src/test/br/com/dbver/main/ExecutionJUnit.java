package br.com.dbver.main;

import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.com.dbver.bean.FolderExecute;
import br.com.dbver.bean.ServerConnection;
import br.com.dbver.bean.Settings;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.driver.SQLServerDriver;
import br.com.dbver.service.ScriptExecutor;
import br.com.dbver.util.MapBuilder;

/**
 * 
 * @author victor
 *
 */
public class ExecutionJUnit {
	// private final static Logger logger =
	// Logger.getLogger(ExecutionJUnit.class);
	private ScriptExecutor scriptExecutor;
	private Settings settings;

	@Before
	public void setUp() {
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setPort("1433");
		//serverConnection.setInstance("SQL2017");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		serverConnection.setDatabaseName("Voscenter");
		settings = new Settings(true, serverConnection, driverJDBC);
		scriptExecutor = new ScriptExecutor(settings);
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		String basePath = "C:\\Users\\sergio.tosta\\Documents\\GIT_BD\\Produto 5.3\\VOSCENTER" + File.separator;
		FolderExecute createMDF = new FolderExecute(new File(basePath + "MDF"), true);
		FolderExecute createConfig = new FolderExecute(new File(basePath + "Configuration"), true);
		FolderExecute createTables = new FolderExecute(new File(basePath + "Tables"), false);
		FolderExecute createSynonyms = new FolderExecute(new File(basePath + "Synonyms"), false);
		FolderExecute createFunctions = new FolderExecute(new File(basePath + "Functions"), false);
		FolderExecute createProcedures = new FolderExecute(new File(basePath + "Procedures"), false);
		FolderExecute createTriggers = new FolderExecute(new File(basePath + "Triggers"), false);
		FolderExecute createIndex = new FolderExecute(new File(basePath + "Index"), false);
		FolderExecute createPreCarga = new FolderExecute(new File(basePath + "PreCarga"), false);
		FolderExecute createConstraints = new FolderExecute(new File(basePath + "Constraints"), false);
		FolderExecute createSynonymsUpdate = new FolderExecute(new File(basePath + "SynonymsUpdate"), false);
		FolderExecute teste = new FolderExecute(new File(basePath + "Teste"), true);

		Map<String, String> parameters = MapBuilder.<String, String>unordered()
				.put("$(varPathMDF)", "C:\\BASES\\")
				.put("$(varPathLDF)", "C:\\BASES\\")
				.put("$(varDBName)", settings.getServerConnection().getDatabaseName()).build();

		scriptExecutor.scriptsFrom(
				Arrays.asList(/*createMDF, createConfig, createTables, createSynonyms, createFunctions, createTriggers,
						createProcedures, createIndex, createPreCarga, createConstraints, createSynonymsUpdate*/teste ),
				parameters);

	}

}
