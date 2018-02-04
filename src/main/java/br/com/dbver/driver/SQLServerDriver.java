package br.com.dbver.driver;

import java.util.Map;

import br.com.dbver.util.MapBuilder;
import br.com.dbver.util.ReplaceUtil;

public class SQLServerDriver implements DriverJDBC {

	private static final String JDBC_DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String DB_URL = "jdbc:sqlserver://%(server);user=%(user);password=%(password)";

	/**
	 * Retorna a URL de conexão com o banco de dados de acordo com os parametros
	 * passados
	 * 
	 * @param server
	 *            - servidor de conexão com o banco
	 * @param user
	 *            - usuário
	 * @param password
	 *            - senha
	 * @return
	 */
	public String getDbUrl(String server, String user, String password) {
		Map<String, String> map = MapBuilder.<String, String>unordered().put("server", server).put("user", user)
				.put("password", password).build();
		return ReplaceUtil.replaceParams(map, DB_URL);
	}

	public String getDriverClass() {
		return JDBC_DRIVER_CLASS;
	}

}
