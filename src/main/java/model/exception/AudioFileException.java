package model.exception;

public class AudioFileException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3347499687507043684L;

	/**
	 * Constructor
	 */
	public AudioFileException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 *            given error message
	 */
	public AudioFileException(String msg) {
		super(msg);
	}
}