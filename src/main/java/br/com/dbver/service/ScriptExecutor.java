package br.com.dbver.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import br.com.dbver.bean.FileParameter;
import br.com.dbver.bean.FolderExecute;
import br.com.dbver.bean.Settings;
import br.com.dbver.dao.Database;
import br.com.dbver.util.MimeTypeUtility;
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

	public void scriptsFrom(List<FolderExecute> foldersExecute, Map<String, String> parameters) {
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
				execute(folderExecute, parameters, connection);
				lastConnection = folderExecute.isMaster();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void checkParameters(List<File> files) throws IOException {
		Map<String, FileParameter> parameters = new HashMap<>();

		for (File file : files) {
			String fileString = new String(Files.readAllBytes(file.toPath()));
			parameters.putAll(settings.getDriverJDBC().findParameters(fileString, file));
		}
		System.out.println(parameters);
		// SHOW UI TO FILL IDENTIFIED PARAMETERS - ParamUI.fill(parameters)

	}

	private void execute(FolderExecute folderExecute, Map<String, String> parameters, Connection connection) {
		List<File> files = readFiles(folderExecute);
		if (!settings.isRobot() && parameters == null && settings.getDriverJDBC().getParameterPatten() != null) {
			try {
				checkParameters(files);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		files.forEach(f -> {
			try {
				String fileString = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8).trim();
				if (parameters != null) {
					fileString = ReplaceUtil.replaceString(parameters, fileString);
				}
				database.executeQuery(connection, fileString);
				logger.info("Arquivo executado com sucesso: " + f.getAbsolutePath());
			} catch (ClassNotFoundException | SQLException | IOException e) {
				logger.error("Erro no arquivo: " + f.getAbsolutePath());
				e.printStackTrace();
			}
		});

	}

	private List<File> readFiles(FolderExecute folderExecute) {
		return Arrays.stream(folderExecute.getFolder().listFiles()).filter(f -> {
			try {
				return "text/sql".equals(MimeTypeUtility.retrieveMimeType(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());
	}
}
