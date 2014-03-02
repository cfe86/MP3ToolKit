package model;

import java.util.ArrayList;
import java.util.List;

import model.structure.ID3LyricsData;

import com.cf.structures.DataDouble;

public class ChangeLyricsDataModel {

	/**
	 * list containing elements with a mapping old lyrics data -> new lyrics
	 * data
	 */
	private List<DataDouble<ID3LyricsData, ID3LyricsData>> lyrics;

	/**
	 * current index of the element in the lyrics list
	 */
	private int currIndex;

	/**
	 * list of elements which represent the new lyrics (if the new lyrics
	 * checkbox is not selected the old lyrics data will be added, if selected
	 * the new lyrics data)
	 */
	private List<ID3LyricsData> newLyrics;

	/**
	 * Constructor
	 */
	public ChangeLyricsDataModel() {
		lyrics = new ArrayList<DataDouble<ID3LyricsData, ID3LyricsData>>();
		this.newLyrics = new ArrayList<ID3LyricsData>();
		this.currIndex = -1;
	}

	/**
	 * sets the new current index
	 * 
	 * @param index
	 *            new index
	 */
	public void setIndex(int index) {
		this.currIndex = index;
	}

	/**
	 * gets the current index
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return this.currIndex;
	}

	/**
	 * increments the index
	 */
	public void incrementIndex() {
		this.currIndex++;
	}

	/**
	 * gets the new lyrics data of the current element
	 * 
	 * @return the new image data
	 */
	public List<ID3LyricsData> getAllNewLyrics() {
		return this.newLyrics;
	}

	/**
	 * sets the lyrics data mapping list
	 * 
	 * @param lyrics
	 *            the list
	 */
	public void setLyrics(List<DataDouble<ID3LyricsData, ID3LyricsData>> lyrics) {
		this.lyrics = lyrics;
	}

	/**
	 * gets the number of elements in the list
	 * 
	 * @return the list length
	 */
	public int getListSize() {
		return this.lyrics.size();
	}

	/**
	 * gets the old lyrics data of the current element
	 * 
	 * @return the old image data
	 */
	public ID3LyricsData getCurrLyrics() {
		return this.lyrics.get(currIndex).getFirst();
	}

	/**
	 * gets the new lyrics data of the current element
	 * 
	 * @return the new image data
	 */
	public ID3LyricsData getNewLyrics() {
		return this.lyrics.get(currIndex).getSecond();
	}

	/**
	 * adds the current element to the new lyrics list
	 * 
	 * @param newOne
	 *            true if new lyrics data should be added, false if old lyrics
	 *            data should be added
	 */
	public void createNewLyrics(boolean newOne) {
		if (currIndex == -1)
			return;

		ID3LyricsData data = new ID3LyricsData(this.lyrics.get(currIndex).getFirst().getIndex());
		data.setLyrics(!newOne ? this.lyrics.get(currIndex).getFirst().getLyrics() : this.lyrics.get(currIndex).getSecond().getLyrics());
		data.setChanged(newOne);

		this.newLyrics.add(data);
	}

	/**
	 * checks if all lyrics got selected
	 * 
	 * @return true if done, else false
	 */
	public boolean isDone() {
		return currIndex == this.lyrics.size();
	}
}