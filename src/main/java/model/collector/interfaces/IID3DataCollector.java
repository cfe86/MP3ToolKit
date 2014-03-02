package model.collector.interfaces;

import java.util.List;

import model.collector.Track;
import model.structure.ID3TagData;

public interface IID3DataCollector extends ICollector {

	/**
	 * gets the title
	 * 
	 * @return the title
	 */
	public String getTitle();

	/**
	 * gets the artist
	 * 
	 * @return the artist
	 */
	public String getArtist();

	/**
	 * gets the album artist
	 * 
	 * @return the album artist
	 */
	public String getAlbumArtist();

	/**
	 * gets the album
	 * 
	 * @return the album
	 */
	public String getAlbum();

	/**
	 * gets the year
	 * 
	 * @return the year
	 */
	public String getYear();

	/**
	 * gets the current track
	 * 
	 * @return the currenttrack
	 */
	public String getTrack();

	/**
	 * gets the max tracks
	 * 
	 * @return the max tracks
	 */
	public String getMaxTracks();

	/**
	 * gets the current CD
	 * 
	 * @return the current CD
	 */
	public String getCD();

	/**
	 * gets the max CDs
	 * 
	 * @return the max CDs
	 */
	public String getMaxCD();

	/**
	 * gets the genre ID
	 * 
	 * @return the genre ID
	 */
	public int getGenre();

	/**
	 * gets the comment
	 * 
	 * @return the comment
	 */
	public String getComment();

	/**
	 * gets the composer
	 * 
	 * @return the composer
	 */
	public String getComposer();

	/**
	 * gets the orig artist
	 * 
	 * @return orig artist
	 */
	public String getOrigArtist();

	/**
	 * gets the copyright
	 * 
	 * @return the copyright
	 */
	public String getCopyright();

	/**
	 * gets the url
	 * 
	 * @return the url
	 */
	public String getURL();

	/**
	 * gets the encoder
	 * 
	 * @return the encoder
	 */
	public String getEncoder();

	/**
	 * gets a list with all tracks in this album
	 * 
	 * @return the album tracks
	 */
	public List<Track> getAllAlbumTracks();

	/**
	 * sets the data which is neccessary to search for the id3 data
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(ID3TagData data);
}