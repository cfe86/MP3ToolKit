package model.progressbar.interfaces;

import java.io.InputStream;

abstract public class MP3GainThread extends IProgressBar {

	/**
	 * Constructor
	 */
	public MP3GainThread() {
		super();
	}

	/**
	 * sets the inputstream
	 * 
	 * @param is
	 *            the inputstream
	 */
	public abstract void setInputStream(InputStream is);

	/**
	 * sets the errorstream
	 * 
	 * @param es
	 *            the errorstream
	 */
	public abstract void setErrorStream(InputStream es);

	/**
	 * resets the progressbar
	 */
	public abstract void reset();

	/**
	 * true if this is running, else false (if a progressbar doesn't need to be
	 * reseted this should be false)
	 * 
	 * @return true if running, else false
	 */
	public abstract boolean isRunning();
}