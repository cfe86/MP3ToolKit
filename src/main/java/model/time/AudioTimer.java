package model.time;

public class AudioTimer extends Timer {

	/**
	 * should the hours be printed in time string
	 */
	private boolean withHours = false;

	/**
	 * song length in seconds
	 */
	private int songLength;

	/**
	 * set with hours or without
	 * 
	 * @param with
	 *            true, hours are printed, else false
	 */
	public void withHours(boolean with) {
		withHours = with;
	}

	/**
	 * sets the song length in seconds
	 * 
	 * @param sec
	 *            the seconds
	 */
	public void setSongLength(int sec) {
		this.songLength = sec;
	}

	/**
	 * true if the song is long enough to have hours
	 * 
	 * @return true if song is longer than 1 hour, else false
	 */
	public boolean hasHours() {
		return Integer.parseInt(getHours()) > 0;
	}

	/**
	 * true if the song is finished, else false
	 * 
	 * @return true or false
	 */
	public boolean isFinished() {
		if (getSecondsCount() >= this.songLength)
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.time.Timer#getformattedString()
	 */
	@Override
	public String getformattedString() {
		return (withHours ? getHours() + ":" : "") + getMinutes() + ":" + getSeconds();
	}
}