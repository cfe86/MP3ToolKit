package model.structure;

import java.util.List;

import model.audio.interfaces.IAudioFile;
import model.collector.Track;

public class ID3TagData {

	/**
	 * the index in the loaded list
	 */
	private int index;

	/**
	 * the title
	 */
	private String title;

	/**
	 * the artist
	 */
	private String artist;

	/**
	 * the album artist
	 */
	private String albumArtist;

	/**
	 * the album
	 */
	private String album;

	/**
	 * the year
	 */
	private String year;

	/**
	 * the current track
	 */
	private String currTrack;

	/**
	 * the max tracks
	 */
	private String maxTrack;

	/**
	 * the current cd
	 */
	private String currCD;

	/**
	 * the max cds
	 */
	private String maxCD;

	/**
	 * the genre ID
	 */
	private int genre;

	/**
	 * the comment
	 */
	private String comment;

	/**
	 * the publisher
	 */
	private String publisher;

	/**
	 * the composer
	 */
	private String composer;

	/**
	 * the original artist
	 */
	private String origArtist;

	/**
	 * the copyright
	 */
	private String copyright;

	/**
	 * the url
	 */
	private String url;

	/**
	 * the encoder
	 */
	private String encoder;

	/**
	 * the lyrics
	 */
	private String lyrics;

	/**
	 * true if something is changed, else false
	 */
	boolean[] changed;

	/**
	 * a list with all tracks of this album
	 */
	private List<Track> albumTracks;

	/**
	 * Constructor
	 */
	public ID3TagData() {
		index = -1;
		title = "";
		artist = "";
		albumArtist = "";
		album = "";
		year = "";
		currTrack = "";
		maxTrack = "";
		currCD = "";
		maxCD = "";
		genre = -1;
		comment = "";
		publisher = "";
		composer = "";
		origArtist = "";
		copyright = "";
		url = "";
		encoder = "";
		lyrics = "";
		changed = null;
		albumTracks = null;
	}

	/**
	 * sets the given audio file as the the id3 data
	 * 
	 * @param audioFile
	 *            the given audio file
	 * @param index
	 *            index of this file in the list
	 */
	public void setAudioFile(IAudioFile audioFile, int index) {
		this.index = index;
		title = audioFile.getTitle();
		artist = audioFile.getArtist();
		albumArtist = audioFile.getAlbumArtist();
		album = audioFile.getAlbum();
		year = audioFile.getYear();
		currTrack = audioFile.getCurrTrack();
		maxTrack = audioFile.getMaxTrack();
		currCD = audioFile.getCurrCD();
		maxCD = audioFile.getMaxCD();
		genre = audioFile.getGenre();
		comment = audioFile.getComment();
		publisher = audioFile.getPublisher();
		composer = audioFile.getComposer();
		origArtist = audioFile.getOriginalArtist();
		copyright = audioFile.getCopyright();
		url = audioFile.getURL();
		encoder = audioFile.getEncoder();
		lyrics = audioFile.getLyrics();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCurrTrack() {
		return currTrack;
	}

	public void setCurrTrack(String currTrack) {
		this.currTrack = currTrack;
	}

	public String getMaxTrack() {
		return maxTrack;
	}

	public void setMaxTrack(String maxTrack) {
		this.maxTrack = maxTrack;
	}

	public String getCurrCD() {
		return currCD;
	}

	public void setCurrCD(String currCD) {
		this.currCD = currCD;
	}

	public String getMaxCD() {
		return maxCD;
	}

	public void setMaxCD(String maxCD) {
		this.maxCD = maxCD;
	}

	public int getGenre() {
		return genre;
	}

	public void setGenre(int genre) {
		this.genre = genre;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getOrigArtist() {
		return origArtist;
	}

	public void setOrigArtist(String origArtist) {
		this.origArtist = origArtist;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncoder() {
		return encoder;
	}

	public void setEncoder(String encoder) {
		this.encoder = encoder;
	}

	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean[] getChanged() {
		return this.changed;
	}

	public void setChanged(boolean[] changed) {
		this.changed = changed;
	}

	public void setAlbumTracks(List<Track> tracks) {
		this.albumTracks = tracks;
	}

	public List<Track> getAlbumTracks() {
		return this.albumTracks;
	}
}