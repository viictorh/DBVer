package br.com.dbver.bean;

/**
 * 
 * @author victor
 *
 */
public class ServerConnection {

	private String server;
	private String instance;
	private String port;
	private String user;
	private String password;
	private String databaseName;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public String toString() {
		return "ServerConnection [server=" + server + ", instance=" + instance + ", port=" + port + ", user=" + user
				+ ", password=" + password + ", databaseName=" + databaseName + "]";
	}

}
