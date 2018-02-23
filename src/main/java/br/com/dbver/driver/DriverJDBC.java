package br.com.dbver.driver;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.dbver.bean.FileParameter;
import br.com.dbver.bean.ServerConnection;

/**
 * 
 * @author victor
 *
 */
public interface DriverJDBC {
	public String getDriverClass();

	public Pattern getParameterPattern();

	public String generateURLConnection(ServerConnection serverConnection, boolean master);

	public List<String> prepareQuery(String query);

	default public Map<String, FileParameter> findParameters(String fileString, File file) {
		Map<String, FileParameter> parameterMap = new HashMap<>();
		Matcher matcher = getParameterPattern().matcher(fileString);
		while (matcher.find()) {
			parameterMap.put(matcher.group(), new FileParameter(file));
		}
		return parameterMap;
	}
	
	public String generateDropDatabaseStatement(ServerConnection serverConnection);
	
	public String preProcessComments(String sql);
}
