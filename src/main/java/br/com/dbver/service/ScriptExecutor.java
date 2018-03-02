package br.com.dbver.service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import br.com.dbver.bean.FolderExecute;
import br.com.dbver.bean.Settings;
import br.com.dbver.bean.Settings.ErrorAction;
import br.com.dbver.dao.Database;
import br.com.dbver.exception.ExecutionError;
import br.com.dbver.util.ReplaceUtil;

/**
 * 
 * @author victor
 *
 */
public class ScriptExecutor {
	private final static Logger logger = Logger.getLogger(ScriptExecutor.class);
	private Settings settings;
	private Database database;

	public ScriptExecutor(Settings settings) {
		this.settings = settings;
		database = new Database(settings.getServerConnection(), settings.getDriverJDBC());
	}

	public void execute(List<FolderExecute> foldersExecute, Map<String, String> parameters) throws ExecutionError {
		boolean lastConnection = false;
		Connection connection = null;
		try {
			for (FolderExecute folderExecute : foldersExecute) {
				if (lastConnection != folderExecute.isMaster() && connection != null) {
					connection.close();
					connection = null;
				}
				if (connection == null) {
					connection = database.createConnection(folderExecute.isMaster());
				}

				databaseExecution(readFiles(folderExecute), parameters, connection);
				lastConnection = folderExecute.isMaster();
			}
		} catch (Exception e) {
			if (settings.getErrorAction() == ErrorAction.DROP) {
				database.dropDatabase(settings.getServerConnection().getDatabaseName());
			}
			throw new ExecutionError(e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Error closing connection: " + e);
				}
			}
		}
	}

	private void databaseExecution(List<File> files, Map<String, String> parameters, Connection connection)
			throws ExecutionError {
		for (File f : files) {
			try {
				String fileString = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8).trim();
				if (parameters != null) {
					fileString = ReplaceUtil.replaceString(parameters, fileString);
				}
				database.executeQuery(connection, fileString);
				logger.debug("Arquivo executado com sucesso: " + f.getAbsolutePath());
			} catch (Throwable e) {
				logger.error("Erro no arquivo: " + f.getAbsolutePath() + " - ERROR MSG [" + e.getMessage()
						+ "] More detais set log to debug mode");
				logger.debug(e);
				if (settings.getErrorAction() != ErrorAction.CONTINUE) {
					throw new ExecutionError(e);
				}
			}
		}
	}

	private List<File> readFiles(FolderExecute folderExecute) {
		return Arrays.stream(folderExecute.getFolder().listFiles()).filter(f -> {
			return "sql".equalsIgnoreCase(FilenameUtils.getExtension(f.getName()));
		}).collect(Collectors.toList());
	}
}
