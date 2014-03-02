package model.exception;

public class CollectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5933154002312175989L;

	/**
	 * Constructor
	 */
	public CollectorException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 *            given error message
	 */
	public CollectorException(String msg) {
		super(msg);
	}
}