package controller.interfaces;

import java.util.List;

import model.structure.ID3LyricsData;

public interface ICmdChangeLyrics {

	/**
	 * changes all lyrics
	 * 
	 * @param data
	 *            list with all lyrics
	 */
	public void call(List<ID3LyricsData> data);
}