package br.com.dbver.bean;

import br.com.dbver.driver.DriverJDBC;

/**
 * 
 * @author victor.bello
 *
 */
public class Settings {
	private boolean robot;
	private ServerConnection serverConnection;
	private DriverJDBC driverJDBC;
	private ErrorAction errorAction;

	public enum ErrorAction {
		CONTINUE, DROP, STOP;
	}

	public Settings(boolean robot, ServerConnection serverConnection, DriverJDBC driverJDBC, ErrorAction errorAction) {
		this.robot = robot;
		this.serverConnection = serverConnection;
		this.driverJDBC = driverJDBC;
		this.errorAction = errorAction;
	}

	public Settings(boolean robot, ServerConnection serverConnection, DriverJDBC driverJDBC) {
		this(robot, serverConnection, driverJDBC, ErrorAction.STOP);
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

	public ErrorAction getErrorAction() {
		return errorAction;
	}

	public void setErrorAction(ErrorAction errorAction) {
		this.errorAction = errorAction;
	}

	@Override
	public String toString() {
		return "Settings [robot=" + robot + ", serverConnection=" + serverConnection + ", driverJDBC=" + driverJDBC
				+ ", errorAction=" + errorAction + "]";
	}

}
