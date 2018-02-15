package br.com.dbver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.dbver.bean.FileParameter;
import br.com.dbver.bean.FolderExecute;
import br.com.dbver.bean.Settings;
import br.com.dbver.dao.DBExecutor;
import br.com.dbver.dao.Database;
import br.com.dbver.util.MimeTypeUtility;
import br.com.dbver.util.ReplaceUtil;

/**
 * 
 * @author victor
 *
 */
public class ScriptExecutor {

	private Settings settings;

	public ScriptExecutor(Settings settings) {
		this.settings = settings;
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

	public void scriptsFrom(List<FolderExecute> folderExecutes, Map<String, String> parameters) {
		boolean lastConnection = false;
		Connection connection = null;
		for (FolderExecute folderExecute : folderExecutes) {
			try {

				if (lastConnection != folderExecute.isMaster() && connection != null) {
					connection.close();
					connection = null;
				}

				if (connection == null) {
					if (folderExecute.isMaster()) {
						connection = Database.createMasterConnection(settings.getDriverJDBC(),
								settings.getServerConnection());
					} else {
						connection = Database.createConnection(settings.getDriverJDBC(),
								settings.getServerConnection());
					}
				}
				execute(folderExecute, parameters, connection);
				lastConnection = folderExecute.isMaster();
			} catch (Exception e) {

			}
		}
	}

	private void execute(FolderExecute folderExecute, Map<String, String> parameters, Connection connection) {
		List<File> files = Arrays.stream(folderExecute.getFolder().listFiles()).filter(f -> {
			try {
				return "text/sql".equals(MimeTypeUtility.retrieveMimeType(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());

		if (!settings.isRobot() && parameters == null && settings.getDriverJDBC().getParameterPatten() != null) {
			try {
				checkParameters(files);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		DBExecutor dbExecutor = new DBExecutor(settings.getServerConnection(), settings.getDriverJDBC());
		files.forEach(f -> {
			try {
				String fileString = new String(Files.readAllBytes(f.toPath()));
				if (parameters != null) {
					fileString = ReplaceUtil.replaceString(parameters, fileString);
				}

				dbExecutor.executeQuery(connection, fileString);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		});

	}

}
