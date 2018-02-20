package br.com.dbver.bean;

import br.com.dbver.driver.DriverJDBC;

public class Settings {
	private boolean robot;
	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;

	public Settings(boolean robot, ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this.robot = robot;
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
	}

	public boolean isRobot() {
		return robot;
	}

	public ServerConnection getServerConnection() {
		return serverConnection;
	}

	public DriverJDBC getDriverJDBC() {
		return driverJDBC;
	}

}
