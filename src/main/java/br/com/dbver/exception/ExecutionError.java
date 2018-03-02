package br.com.dbver.exception;

/**
 * 
 * @author victor.bello
 *
 */
public class ExecutionError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3928983150772593964L;
	private Throwable error;

	public ExecutionError(Throwable error) {
		this.error = error;

	}

	@Override
	public String getMessage() {
		return error.getMessage();
	}

	@Override
	public String toString() {
		return "ExecutionError [error=" + error + "]";
	}
}
