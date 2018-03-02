package br.com.dbver.sqlserver;

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
public class ConnectionJUnit {

	private final static Logger logger = Logger.getLogger(ConnectionJUnit.class);
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
		serverConnection.setDatabaseName("testDatabase");
		dbExecutor = new Database(serverConnection, driverJDBC);
	}

	@Test
	public void simpleQuery() throws ClassNotFoundException, SQLException {
		dbExecutor.executeQuery("SELECT name, database_id, create_date FROM sys.databases ;", true);
	}

	@Test
	public void createTable() throws ClassNotFoundException, SQLException {
		String query = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TActiveCampaignPriority]') AND type in (N'U'))"
				+ " BEGIN " + " CREATE TABLE [dbo].[TActiveCampaignPriority](" + "	[IdCampaign] [int] NOT NULL,"
				+ "	[IdBatch] [int] NOT NULL," + "	[numUsedFiles] [numeric](18, 0) NULL,"
				+ "	[numPriority] [int] NULL," + "	[numTotalFiles] [numeric](18, 0) NULL,"
				+ "	[numPercentOfUse] [float] NULL," + " CONSTRAINT [PK_TActiveCampaignPriority] PRIMARY KEY CLUSTERED "
				+ "(" + "	[IdCampaign] ASC," + "	[IdBatch] ASC"
				+ ")WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]"
				+ ") ON [PRIMARY]" + " END " + "  " + "";

		dbExecutor.executeQuery(query, true);
		logger.info("Tabela criada: TActiveCampaignPriority]");
	}

	@Test
	public void StringGoTest() throws ClassNotFoundException, SQLException {
		String linebreak = System.getProperty("line.separator");
		String query = "IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[spWebStart]') AND type in (N'P', N'PC'))"
				+ linebreak + "	DROP PROCEDURE [dbo].[spWebStart]; " + linebreak + "go;" + linebreak
				+ " CREATE PROCEDURE [dbo].[spWebStart]" + "(@SessionId	VARCHAR(200)" + ")" + linebreak + "as select 1";

		dbExecutor.executeQuery(query, true);
	}

}
