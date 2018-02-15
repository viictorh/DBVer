package br.com.dbver.driver;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.dbver.bean.FileParameter;
import br.com.dbver.util.MapBuilder;
import br.com.dbver.util.ReplaceUtil;

/**
 * 
 * @author victor
 *
 */
public interface DriverJDBC {
	public String getDriverClass();

	public String getServerUrl();

	public String getDbUrl();

	public Pattern getParameterPatten();

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
	public default String generateServerUrl(String server, String user, String password) {
		Map<String, String> map = MapBuilder.<String, String>unordered().put("server", server).put("user", user)
				.put("password", password).build();
		return ReplaceUtil.replaceParams(map, getServerUrl());
	}

	public default String generateDbUrl(String server, String database, String user, String password) {
		Map<String, String> map = MapBuilder.<String, String>unordered().put("server", server).put("databasename", database)
				.put("user", user).put("password", password).build();
		return ReplaceUtil.replaceParams(map, getDbUrl());
	}

	public default Map<String, FileParameter> findParameters(String fileString, File file) {
		Map<String, FileParameter> parameterMap = new HashMap<>();
		Matcher matcher = getParameterPatten().matcher(fileString);
		while (matcher.find()) {
			parameterMap.put(matcher.group(), new FileParameter(file));
		}
		return parameterMap;
	}
}
