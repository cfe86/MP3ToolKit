package model.exception;

public class ControllerInitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3803345896915357420L;

	/**
	 * Constructor
	 */
	public ControllerInitException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 *            given error message
	 */
	public ControllerInitException(String msg) {
		super(msg);
	}
}