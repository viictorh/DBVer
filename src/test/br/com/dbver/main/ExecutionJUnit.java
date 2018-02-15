package br.com.dbver.main;

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
	private final static Logger logger = Logger.getLogger(ExecutionJUnit.class);
	private ScriptExecutor scriptExecutor;
	private Settings settings;

	@Before
	public void setUp() {
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		serverConnection.setDatabaseName("VictorExcluir");
		settings = new Settings(true, serverConnection, driverJDBC);
		scriptExecutor = new ScriptExecutor(settings);
	}

	@Test
	public void test() throws ClassNotFoundException, SQLException {
		FolderExecute createDb = new FolderExecute(
				new File("C:\\Users\\victor.bello\\Downloads\\BDVOSCenter_5_3\\BD\\VOSCENTER\\MDF"), true);
		FolderExecute createTables = new FolderExecute(
				new File("C:\\Users\\victor.bello\\Downloads\\BDVOSCenter_5_3\\BD\\VOSCENTER\\Tables"), false);

		Map<String, String> parameters = MapBuilder.<String, String>unordered()
				.put("$(varPathMDF)", "C:\\Users\\victor.bello\\Downloads\\testeDB")
				.put("$(varPathLDF)", "C:\\Users\\victor.bello\\Downloads\\testeDB")
				.put("$(varDBName)", settings.getServerConnection().getDatabaseName()).build();

		scriptExecutor.scriptsFrom(Arrays.asList(createDb, createTables), parameters);

	}

}
