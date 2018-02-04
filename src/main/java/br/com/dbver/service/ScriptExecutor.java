package br.com.dbver.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.dao.DBExecutor;
import br.com.dbver.driver.DriverJDBC;
import br.com.dbver.util.MimeTypeUtility;

public class ScriptExecutor {

	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;

	public ScriptExecutor(ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
	}

	public void scriptsFrom(File folder) {
		List<File> files = Arrays.stream(folder.listFiles()).filter(f -> {
			try {
				return "text/sql".equals(MimeTypeUtility.retrieveMimeType(f));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}).collect(Collectors.toList());

		DBExecutor dbExecutor = new DBExecutor(serverConnection, driverJDBC);

		files.forEach(f -> {
			try {
				dbExecutor.executeQuery(new String(Files.readAllBytes(f.toPath())));
			} catch (ClassNotFoundException | SQLException | IOException e) {
				e.printStackTrace();
			}
		});

	}

}
