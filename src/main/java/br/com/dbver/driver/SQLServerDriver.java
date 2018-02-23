package br.com.dbver.driver;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import br.com.dbver.bean.ServerConnection;
import br.com.dbver.util.MapBuilder;
import br.com.dbver.util.ReplaceUtil;

public class SQLServerDriver implements DriverJDBC {
	private final static Logger logger = Logger.getLogger(SQLServerDriver.class);
	private static final String JDBC_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://%(server):%(port);instanceName=%(instance);dataBaseName=%(databasename);user=%(user);password=%(password)";
	private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\(.*\\)");
	private static final Pattern BATCH_TERMINATOR_PATTERN = Pattern.compile("(?i)^(\\s*)go;?(\\s*)$", Pattern.MULTILINE);

	@Override
	public String getDriverClass() {
		return JDBC_DRIVER_CLASS;
	}

	@Override
	public Pattern getParameterPattern() {
		return PARAMETER_PATTERN;
	}

	@Override
	public List<String> prepareQuery(String query) {		
		return Arrays.asList(BATCH_TERMINATOR_PATTERN.split(preProcessComments(query)));
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
		String connectionString = ReplaceUtil.replaceParams(builder.build(), DB_URL);
		logger.debug(connectionString);
		return connectionString;
	}

	@Override
	public String generateDropDatabaseStatement(ServerConnection connection) {
		String statement;

		statement = "IF db_id('" + connection.getDatabaseName() + "') is not null" + System.lineSeparator()
				+ "BEGIN" + System.lineSeparator()				
				+ "		ALTER DATABASE " + connection.getDatabaseName() + " SET SINGLE_USER WITH ROLLBACK IMMEDIATE;" + System.lineSeparator()
				+ "END" + System.lineSeparator()
				+ "GO" + System.lineSeparator()
				+ "IF db_id('" + connection.getDatabaseName() + "') is not null" + System.lineSeparator()
				+ "BEGIN" + System.lineSeparator()				
				+ "		DROP DATABASE " + connection.getDatabaseName() + ";" + System.lineSeparator()
				+ "END" + System.lineSeparator()
				+ "GO";
			
		return statement;
	}
	
	public String preProcessComments(String sql) {
		main: while (true) {
			Pattern p = Pattern.compile("\\/\\*[\\w\\W]*?(?=\\*\\/)\\*\\/");
			Matcher m = p.matcher(sql);

			if (m.find()) {
				int countOpen = (m.group().length() - m.group().replaceAll("\\/\\*", "").length()) / 2;
				int countClose = (m.group().length() - m.group().replaceAll("\\*\\/", "").length()) / 2;
								
				if (countOpen > countClose) {
					sql = sql.replaceFirst(Pattern.quote(m.group()), BATCH_TERMINATOR_PATTERN.matcher(m.group()).replaceAll("presunto!!!"));
					sql = sql.replaceFirst("\\*\\/", "");
					sql = sql.replaceFirst("\\/\\*", "__FIRST_COMM_FOUND__");
					sql = sql.replaceFirst("\\/\\*", "");
					sql = sql.replace("__FIRST_COMM_FOUND__", "/*");
				} else {
					sql = sql.replaceFirst(Pattern.quote(m.group()), BATCH_TERMINATOR_PATTERN.matcher(m.group()).replaceAll("presunto!!!"));
					sql = sql.replaceFirst("\\/\\*", "__ENDED_FIRST_COMM_FOUND__");
					sql = sql.replaceFirst("\\*\\/", "__ENDED_LAST_COMM_FOUND__");
					
					
				}
			} else {
				break main;
			}
		}

		int countOpen = (sql.length() - sql.replaceAll("\\/\\*", "").length()) / 2;
		int countClose = (sql.length() - sql.replaceAll("\\*\\/", "").length()) / 2;
		if (countOpen == 1 && countClose == 0) {
			sql = sql + "*/";
		}
		
		sql = sql.replaceAll("__ENDED_FIRST_COMM_FOUND__", "/*");
		sql = sql.replaceAll("__ENDED_LAST_COMM_FOUND__", "*/");
			
		return sql;
	}
	
}