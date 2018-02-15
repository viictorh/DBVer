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
import br.com.dbver.bean.ServerConnection;
import br.com.dbver.dao.DBExecutor;
import br.com.dbver.dao.Database;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.util.MimeTypeUtility;
import br.com.dbver.util.ReplaceUtil;

/**
 * 
 * @author victor
 *
 */
public class ScriptExecutor {

	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;

	public ScriptExecutor(ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
	}

	public void scriptsFrom(File folder, Map<String, String> parameters) {
		List<File> files = Arrays.stream(folder.listFiles()).filter(f -> {
			try {
				return "text/sql".equals(MimeTypeUtility.retrieveMimeType(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());

		if (parameters == null && driverJDBC.getParameterPatten() != null) {
			try {
				checkParameters(files);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// EXECUTE CREATE DATABASE ON MASTER
		// EXECUTE SCRIPTS ON CREATED DATABASE;
		// EXECUTE JOBS ON MASTER
		DBExecutor dbExecutor = new DBExecutor(serverConnection, driverJDBC);

		try (Connection connection = Database.createConnection(driverJDBC, serverConnection)) {
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
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

	}

	private void checkParameters(List<File> files) throws IOException {
		Map<String, FileParameter> parameters = new HashMap<>();

		for (File file : files) {
			String fileString = new String(Files.readAllBytes(file.toPath()));
			parameters.putAll(driverJDBC.findParameters(fileString, file));
		}
		System.out.println(parameters);
		// SHOW UI TO FILL IDENTIFIED PARAMETERS - ParamUI.fill(parameters)

	}

	public void scriptsFrom(File folder) {
		scriptsFrom(folder, null);
	}

}
