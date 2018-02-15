package br.com.dbver.connection;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.dao.DBExecutor;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.driver.SQLServerDriver;

/**
 * 
 * @author victor
 *
 */
public class SQLServerConnectionJUnit {

	private final static Logger logger = Logger.getLogger(SQLServerConnectionJUnit.class);
	private DBExecutor dbExecutor;

	@Before
	public void setUp() {
		logger.debug("Iniciando teste");
		DriverJDBC driverJDBC = new SQLServerDriver();
		ServerConnection serverConnection = new ServerConnection();
		serverConnection.setServer("localhost");
		serverConnection.setUser("sa");
		serverConnection.setPassword("S@voxsql");
		dbExecutor = new DBExecutor(serverConnection, driverJDBC);
	}

	@Test
	public void simpleQuery() throws ClassNotFoundException, SQLException {
		dbExecutor.executeQuery("SELECT name, database_id, create_date FROM sys.databases ;");
	}

	@Test
	public void complexQuery() throws ClassNotFoundException, SQLException {
		for (int j = 0; j <= 1; j++) {
			String query = "IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TActiveCampaignPriority"
					+ j + "]') AND type in (N'U'))" + " BEGIN " + " CREATE TABLE [dbo].[TActiveCampaignPriority" + j
					+ "](" + "	[IdCampaign] [int] NOT NULL," + "	[IdBatch] [int] NOT NULL,"
					+ "	[numUsedFiles] [numeric](18, 0) NULL," + "	[numPriority] [int] NULL,"
					+ "	[numTotalFiles] [numeric](18, 0) NULL," + "	[numPercentOfUse] [float] NULL,"
					+ " CONSTRAINT [PK_TActiveCampaignPriority" + j + "] PRIMARY KEY CLUSTERED " + "("
					+ "	[IdCampaign] ASC," + "	[IdBatch] ASC"
					+ ")WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]"
					+ ") ON [PRIMARY]" + " END " + "  " + "";

			dbExecutor.executeQuery(query);
			logger.info("Tabela criada: TActiveCampaignPriority" + j + "]");
		}

	}

}
