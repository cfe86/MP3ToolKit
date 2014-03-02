package model.collector.interfaces;

import model.structure.ID3TagData;

public interface ILyricsCollector extends ICollector {

	/**
	 * gets the lyrics
	 * 
	 * @return the lyrics
	 */
	public String getLyrics();

	/**
	 * sets the data which is neccessary to search for the lyrics
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(ID3TagData data);
}