package br.com.dbver.driver;

public interface DriverJDBC {
	public String getDriverClass();
	public String getDbUrl(String server, String user, String password);
}
