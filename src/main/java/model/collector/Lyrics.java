package model.collector;

public class Lyrics {

	/**
	 * the song artist
	 */
	private String artist;

	/**
	 * the song title
	 */
	private String title;

	/**
	 * the song lyrics
	 */
	private String lyrics;

	/**
	 * Constructor
	 */
	public Lyrics() {
		this.artist = "";
		this.title = "";
		this.lyrics = "";
	}

	/**
	 * Constructor
	 * 
	 * @param artist
	 *            song artist
	 * @param title
	 *            song title
	 * @param lyrics
	 *            song lyrics
	 */
	public Lyrics(String artist, String title, String lyrics) {
		this.artist = artist;
		this.title = title;
		this.lyrics = lyrics;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
}