package model.audio;

public class GainAudioFile {

	/**
	 * the file path
	 */
	private String path;

	/**
	 * calculated track volume
	 */
	private double trackvolume;

	/**
	 * calculated track gain
	 */
	private double trackGain;

	/**
	 * calculated track volume
	 */
	private double albumVolume;

	/**
	 * calculated album gain
	 */
	private double albumGain;

	/**
	 * true if valid (if gain is calculated) else false
	 */
	private boolean valid;

	/**
	 * rounds the values with x precision, e.g. if 10, precision is 1 digit
	 * after comma
	 */
	private static final int ROUND = 10;

	/**
	 * Constructor
	 * 
	 * @param path
	 *            audio file path
	 */
	public GainAudioFile(String path) {
		this.path = path;
		this.trackvolume = -1;
		this.trackGain = -1;
		this.albumVolume = -1;
		this.albumGain = -1;
		valid = false;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public double getTrackvolume() {
		return trackvolume;
	}

	public void setTrackvolume(double trackvolume) {
		this.trackvolume = round(trackvolume);
	}

	public double getTrackGain() {
		return trackGain;
	}

	public void setTrackGain(double gain) {
		this.trackGain = gain;
	}

	public double getRelativeTrackGain(int target) {
		return target - GainMetaData.DEFAULT_GAIN;
	}
	
	public double getAlbumVolume() {
		return albumVolume;
	}

	public void setAlbumVolume(double albumVolume) {
		this.albumVolume = round(albumVolume);
	}

	public double getAlbumGain() {
		return albumGain;
	}

	public void setAlbumGain(double gain) {
		this.albumGain = gain;
	}
	
	public double getRelativeAlbumGain(int target) {
		return target - GainMetaData.DEFAULT_GAIN;
	}

	public String getPath() {
		return path;
	}

	/**
	 * rounds the given number depending on the given ROUND attribute
	 * 
	 * @param num
	 *            the number
	 * 
	 * @return the round number
	 */
	private double round(double num) {
		return ((double) Math.round(num * ROUND)) / ROUND;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.path + " Track Volume: " + this.trackvolume + " TrackGain: " + this.trackGain + " Album Volume: " + this.albumVolume + " Album Gain: " + this.albumGain
				+ " valid: " + this.valid;
	}
}