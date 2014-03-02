package model.structure;

public class ID3ImageData {

	/**
	 * the image in bytes
	 */
	private byte[] image;

	/**
	 * the extension
	 */
	private String extension;

	/**
	 * the index of this data in the loaded file list
	 */
	private int index;

	/**
	 * true if it is changed
	 */
	private boolean changed;

	/**
	 * the artist
	 */
	private String artist;

	/**
	 * the album
	 */
	private String album;

	/**
	 * Constructor
	 * 
	 * @param index
	 *            given index in the list
	 */
	public ID3ImageData(int index) {
		this.index = index;
		this.image = null;
		this.extension = "";
		this.changed = true;
		this.artist = "";
		this.album = "";
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

	public int getIndex() {
		return index;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
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

	public void setAlbum(String title) {
		this.album = title;
	}
}