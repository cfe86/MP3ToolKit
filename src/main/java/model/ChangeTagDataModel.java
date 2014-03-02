package model;

import java.util.ArrayList;
import java.util.List;

import model.structure.ID3TagData;

import com.cf.structures.DataDouble;

public class ChangeTagDataModel {

	/**
	 * list containing elements with a mapping old id3 data -> new id3 data
	 */
	private List<DataDouble<ID3TagData, ID3TagData>> audioFiles;

	/**
	 * current index of the element in the id3 list
	 */
	private int currIndex;

	/**
	 * list of elements which represent the new id3 data (if the new id3data
	 * checkbox is not selected the old id3 data will be added, if selected the
	 * new id3 data)
	 */
	private List<ID3TagData> newAudioFiles;

	/**
	 * Constructor
	 */
	public ChangeTagDataModel() {
		this.audioFiles = new ArrayList<DataDouble<ID3TagData, ID3TagData>>();
		this.currIndex = -1;
		this.newAudioFiles = new ArrayList<ID3TagData>();
	}

	/**
	 * gets the mapping list
	 * 
	 * @return the list
	 */
	public List<DataDouble<ID3TagData, ID3TagData>> getAudioFiles() {
		return audioFiles;
	}

	/**
	 * sets the id3 data mapping list
	 * 
	 * @param audioFiles
	 *            the list
	 */
	public void setAudioFiles(List<DataDouble<ID3TagData, ID3TagData>> audioFiles) {
		this.audioFiles = audioFiles;
		for (int i = 0; i < this.audioFiles.size(); i++)
			this.newAudioFiles.add(new ID3TagData());
	}

	/**
	 * gets the current index
	 * 
	 * @return the index
	 */
	public int getCurrIndex() {
		return currIndex;
	}

	/**
	 * sets the current index
	 * 
	 * @param currIndex
	 *            the new index
	 */
	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}

	/**
	 * gets the number of elements in the list
	 * 
	 * @return the list length
	 */
	public int getListSize() {
		return this.audioFiles.size();
	}

	/**
	 * gets a list with the new id3 data
	 * 
	 * @return the list with new data
	 */
	public List<ID3TagData> getNewAudioFiles() {
		return newAudioFiles;
	}

	/**
	 * increments the index
	 */
	public void incrementIndex() {
		this.currIndex++;
	}

	/**
	 * gets the old id3 data of the current element
	 * 
	 * @return the old id3 data
	 */
	public ID3TagData getCurrID3TagData() {
		return this.audioFiles.get(currIndex).getFirst();
	}

	/**
	 * gets the new id3 data of the current element
	 * 
	 * @return the new id3 data
	 */
	public ID3TagData getNewID3TagData() {
		return this.audioFiles.get(currIndex).getSecond();
	}

	/**
	 * adds the current element to the new id3 data list
	 * 
	 * @param data
	 *            array with booleans. If true, the index if the new data will
	 *            be used, false the old data will be used
	 */
	public void createNewID3TagData(boolean[] data) {
		if (currIndex == -1)
			return;

		this.newAudioFiles.get(currIndex).setIndex(this.audioFiles.get(currIndex).getFirst().getIndex());
		this.newAudioFiles.get(currIndex).setTitle(!data[0] ? this.audioFiles.get(currIndex).getFirst().getTitle() : this.audioFiles.get(currIndex).getSecond().getTitle());
		this.newAudioFiles.get(currIndex).setArtist(!data[1] ? this.audioFiles.get(currIndex).getFirst().getArtist() : this.audioFiles.get(currIndex).getSecond().getArtist());
		this.newAudioFiles.get(currIndex).setAlbumArtist(
				!data[2] ? this.audioFiles.get(currIndex).getFirst().getAlbumArtist() : this.audioFiles.get(currIndex).getSecond().getAlbumArtist());
		this.newAudioFiles.get(currIndex).setAlbum(!data[3] ? this.audioFiles.get(currIndex).getFirst().getAlbum() : this.audioFiles.get(currIndex).getSecond().getAlbum());
		this.newAudioFiles.get(currIndex).setYear(!data[4] ? this.audioFiles.get(currIndex).getFirst().getYear() : this.audioFiles.get(currIndex).getSecond().getYear());
		this.newAudioFiles.get(currIndex).setCurrTrack(
				!data[5] ? this.audioFiles.get(currIndex).getFirst().getCurrTrack() : this.audioFiles.get(currIndex).getSecond().getCurrTrack());
		this.newAudioFiles.get(currIndex)
				.setMaxTrack(!data[6] ? this.audioFiles.get(currIndex).getFirst().getMaxTrack() : this.audioFiles.get(currIndex).getSecond().getMaxTrack());
		this.newAudioFiles.get(currIndex).setCurrCD(!data[7] ? this.audioFiles.get(currIndex).getFirst().getCurrCD() : this.audioFiles.get(currIndex).getSecond().getCurrCD());
		this.newAudioFiles.get(currIndex).setMaxCD(!data[8] ? this.audioFiles.get(currIndex).getFirst().getMaxCD() : this.audioFiles.get(currIndex).getSecond().getMaxCD());
		this.newAudioFiles.get(currIndex).setGenre(!data[9] ? this.audioFiles.get(currIndex).getFirst().getGenre() : this.audioFiles.get(currIndex).getSecond().getGenre());
		this.newAudioFiles.get(currIndex).setComment(!data[10] ? this.audioFiles.get(currIndex).getFirst().getComment() : this.audioFiles.get(currIndex).getSecond().getComment());
		this.newAudioFiles.get(currIndex).setComposer(
				!data[11] ? this.audioFiles.get(currIndex).getFirst().getComposer() : this.audioFiles.get(currIndex).getSecond().getComposer());
		this.newAudioFiles.get(currIndex).setOrigArtist(
				!data[11] ? this.audioFiles.get(currIndex).getFirst().getOrigArtist() : this.audioFiles.get(currIndex).getSecond().getOrigArtist());
		this.newAudioFiles.get(currIndex).setCopyright(
				!data[12] ? this.audioFiles.get(currIndex).getFirst().getCopyright() : this.audioFiles.get(currIndex).getSecond().getCopyright());
		this.newAudioFiles.get(currIndex).setUrl(!data[13] ? this.audioFiles.get(currIndex).getFirst().getUrl() : this.audioFiles.get(currIndex).getSecond().getUrl());
		this.newAudioFiles.get(currIndex).setEncoder(!data[14] ? this.audioFiles.get(currIndex).getFirst().getEncoder() : this.audioFiles.get(currIndex).getSecond().getEncoder());
	}

	/**
	 * gets a boolean array where the index is true if the data has changed in
	 * respect to the old data, else false. e.g. old data is "a" and new data is
	 * "", then nothing new found and its false, if new data is "b" then true
	 * 
	 * @return the array
	 */
	public boolean[] getChanged() {
		return this.audioFiles.get(currIndex).getSecond().getChanged();
	}

	/**
	 * checks if all files got selected
	 * 
	 * @return true if done, else false
	 */
	public boolean isDone() {
		return currIndex == this.audioFiles.size();
	}
}