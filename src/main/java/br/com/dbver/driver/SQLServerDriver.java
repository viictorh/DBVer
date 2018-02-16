package br.com.dbver.driver;

import java.util.regex.Pattern;

public class SQLServerDriver implements DriverJDBC {

	private static final String JDBC_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String SERVER_URL = "jdbc:sqlserver://%(server):%(port);instance=%(instance);user=%(user);password=%(password)";
	private static final String DB_URL = "jdbc:sqlserver://%(server):%(port);instance=%(instance);dataBaseName=%(databasename);user=%(user);password=%(password)";
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\(.*\\)");

	public String getDriverClass() {
		return JDBC_DRIVER_CLASS;
	}

	@Override
	public Pattern getParameterPatten() {
		return PARAMETER_PATTERN;
	}

	@Override
	public String getServerUrl() {
		return SERVER_URL;
	}

	@Override
	public String getDbUrl() {
		return DB_URL;
	}

}
