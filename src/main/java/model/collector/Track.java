package model.collector;

import java.util.ArrayList;
import java.util.List;

public class Track {

	/**
	 * the track title
	 */
	private String title;

	/**
	 * the track artist
	 */
	private String artist;

	/**
	 * the track number
	 */
	private int trackNr;

	/**
	 * the album where this track is in
	 */
	private String album;

	/**
	 * the track tags
	 */
	private List<String> tags;

	/**
	 * the metric which is used to decide which title should be used if more
	 * than 1 could it be
	 */
	private int matchLength;

	/**
	 * Constructor
	 */
	public Track() {
		this.title = "";
		this.artist = "";
		this.album = "";
		this.trackNr = -1;
		this.tags = new ArrayList<String>();
		this.matchLength = Integer.MAX_VALUE;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public List<String> getTags() {
		return tags;
	}

	public String getTitle() {
		return title;
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public int getTrackNr() {
		return trackNr;
	}

	public void setTrackNr(int trackNr) {
		this.trackNr = trackNr;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMatchLength() {
		return matchLength;
	}

	public void setMatchLength(int matchLength) {
		this.matchLength = matchLength;
	}
}