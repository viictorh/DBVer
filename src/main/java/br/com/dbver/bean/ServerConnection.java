package br.com.dbver.bean;

/**
 * 
 * @author victor
 *
 */
public class ServerConnection {

	private String server;
	private String user;
	private String password;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
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

	@Override
	public String toString() {
		return "ServerConnection [server=" + server + ", user=" + user + ", password=" + password + "]";
	}

}
