package model.collector;

import java.util.ArrayList;
import java.util.List;

public class Album {

	/**
	 * album artist
	 */
	private String artist;

	/**
	 * album name
	 */
	private String name;

	/**
	 * the image url
	 */
	private String imageURL;

	/**
	 * the image size: medium, large, extra large and so on
	 */
	private String imageSize;

	/**
	 * the year
	 */
	private String year;

	/**
	 * list of tracks in this album
	 */
	private List<Track> tracks;

	/**
	 * tags of this album
	 */
	private List<String> tags;

	/**
	 * the image in bytes
	 */
	private byte[] image;

	/**
	 * the image extension
	 */
	private String extension;

	/**
	 * true if this album has an image, else false
	 */
	private boolean hasImage;

	/**
	 * Constructor
	 */
	public Album() {
		this.artist = "";
		this.name = "";
		this.imageURL = "";
		this.imageSize = "";
		this.year = "";
		this.tracks = new ArrayList<Track>();
		this.tags = new ArrayList<String>();

		this.image = null;
		this.extension = null;
		this.hasImage = false;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public List<Track> getTracks() {
		return tracks;
	}

	public List<String> getTags() {
		return tags;
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public void addTrack(Track track) {
		this.tracks.add(track);
	}

	public int getMaxTracks() {
		return this.tracks.size();
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public boolean isHasImage() {
		return hasImage;
	}

	public void setHasImage(boolean hasImage) {
		this.hasImage = hasImage;
	}
}