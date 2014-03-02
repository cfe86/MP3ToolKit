package model.exception;

public class AudioPlayerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public AudioPlayerException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param msg
	 *            given error message
	 */
	public AudioPlayerException(String msg) {
		super(msg);
	}
}
