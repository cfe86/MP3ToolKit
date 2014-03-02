package model.audio;

public class GainMetaData {

	/**
	 * the default gain used by mp3gain
	 */
	public final static int DEFAULT_GAIN = 89;

	/**
	 * the target gain
	 */
	private int targetGain;

	/**
	 * the recommended track gain change
	 */
	private double recommendedTrackGainChange;

	/**
	 * the recommended album gain change
	 */
	private double recommendedAlbumGainChange;

	/**
	 * the recommendd track volume change
	 */
	private double recommendedTrackVolumeChange;

	/**
	 * the recommended album volume change
	 */
	private double recommendedAlbumVolumeChange;

	/**
	 * the file path
	 */
	private String filePath;

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            the filepath
	 * @param targetGain
	 *            the target gain
	 */
	public GainMetaData(String filePath, int targetGain) {
		this.filePath = filePath;
		this.targetGain = targetGain;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setDefaultGain(int defaultGain) {
		this.targetGain = defaultGain;
	}

	public void setRecommendedTrackGainChange(double recommendedTrackGainChange) {
		this.recommendedTrackGainChange = recommendedTrackGainChange;
	}

	public void setRecommendedAlbumGainChange(double recommendedAlbumGainChange) {
		this.recommendedAlbumGainChange = recommendedAlbumGainChange;
	}

	public void setRecommendedTrackVolumeChange(double recommendedTrackVolumeChange) {
		this.recommendedTrackVolumeChange = recommendedTrackVolumeChange;
	}

	public void setRecommendedAlbumVolumeChange(double recommendedAlbumVolumeChange) {
		this.recommendedAlbumVolumeChange = recommendedAlbumVolumeChange;
	}

	public double getRecommendedTrackGainChange() {
		return recommendedTrackGainChange;
	}

	public double getRecommendedAlbumGainChange() {
		return recommendedAlbumGainChange;
	}

	public double getRecommendedTrackVolumeChange() {
		return recommendedTrackVolumeChange;
	}

	public double getRecommendedAlbumVolumeChange() {
		return recommendedAlbumVolumeChange;
	}

	// (target-89) + (gain change * 1.5)
	public double getTrackGainChange() {
		return (targetGain - DEFAULT_GAIN) + (recommendedTrackGainChange * 1.5);
	}

	public double getAlbumGainChange() {
		return (targetGain - DEFAULT_GAIN) + (recommendedAlbumGainChange * 1.5);
	}

	/**
	 * gets the track volume calculated as 89 - recommended track volume change
	 * 
	 * @return the track volume
	 */
	public double getTrackVolume() {
		return DEFAULT_GAIN - this.recommendedTrackVolumeChange;
	}

	/**
	 * gets the album volume calculated as 89 - recommended album volume change
	 * 
	 * @return the album volume
	 */
	public double getAlbumVolume() {
		return DEFAULT_GAIN - this.recommendedAlbumVolumeChange;
	}
}