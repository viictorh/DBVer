package br.com.dbver.driver;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.util.MapBuilder;
import br.com.dbver.util.ReplaceUtil;

public class SQLServerDriver implements DriverJDBC {

	private static final String JDBC_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://%(server):%(port);instanceName=%(instance);dataBaseName=%(databasename);user=%(user);password=%(password)";
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\(.*\\)");
	private static final Pattern BATCH_TERMINATOR_PATTERN = Pattern.compile("(?i)\\sgo(;)?\\s");

	@Override
	public String getDriverClass() {
		return JDBC_DRIVER_CLASS;
	}

	@Override
	public Pattern getParameterPatten() {
		return PARAMETER_PATTERN;
	}

	@Override
	public List<String> prepareQuery(String query) {
		return Arrays.asList(BATCH_TERMINATOR_PATTERN.split(query));
	}

	@Override
	public String generateURLConnection(ServerConnection connection, boolean master) {
		MapBuilder<String, String> builder = MapBuilder.<String, String>unordered()
				.put("server", connection.getServer()).put("port", connection.getPort())
				.put("user", connection.getUser()).put("password", connection.getPassword());

		if (!StringUtils.isBlank(connection.getInstance())) {
			builder.put("instance", connection.getInstance());
		} else {
			builder.put("instance", "");
		}

		if (master) {
			builder.put("databasename", "");
		} else {
			builder.put("databasename", connection.getDatabaseName());
		}
		return ReplaceUtil.replaceParams(builder.build(), DB_URL);
	}

}
