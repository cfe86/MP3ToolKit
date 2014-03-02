package model.structure;

public class ID3LyricsData {

	/**
	 * given index in the loaded list
	 */
	private int index;
	
	/**
	 * given artist
	 */
	private String artist;
	
	/**
	 * given title
	 */
	private String title;
	
	/**
	 * true if something got changed, else false
	 */
	private boolean changed;
	
	/**
	 * the lyrics
	 */
	private String lyrics;

	/**
	 * Constructor
	 * 
	 * @param index the list index
	 */
	public ID3LyricsData(int index) {
		this.index = index;
		changed = true;
		lyrics = "";
		artist = "";
		title = "";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
		this.changed = lyrics == null ? false : lyrics.trim().length() != 0;
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
}