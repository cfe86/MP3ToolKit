package model.exception;

public class ParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1487699679512442771L;

	/**
	 * Constructor
	 */
	public ParserException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 *            given error message
	 */
	public ParserException(String msg) {
		super(msg);
	}
}