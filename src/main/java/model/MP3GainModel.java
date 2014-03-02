package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Config;
import logging.LogUtil;
import model.audio.GainAudioFile;
import model.audio.MP3Gain;
import model.progressbar.interfaces.MP3GainThread;
import model.table.GainTableModel;
import model.util.Commons;
import model.util.FileUtil;

public class MP3GainModel {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * currently loaded audio files
	 */
	private List<GainAudioFile> audioFiles;

	/**
	 * all paths of loaded files
	 */
	private Set<String> readPaths;

	/**
	 * the stop flag, if set to true all operations should stop
	 */
	private boolean stopFlag;

	/**
	 * Constructor
	 */
	public MP3GainModel() {
		this.audioFiles = new ArrayList<GainAudioFile>();
		this.readPaths = new HashSet<String>();
		this.stopFlag = false;
	}

	/**
	 * true if the stop flag is set, else false
	 * 
	 * @return true or false
	 */
	public boolean isStopFlagSet() {
		return this.stopFlag;
	}

	/**
	 * sets the stop flag and stops all operations
	 * 
	 * @param set
	 *            ture if stop, else false
	 */
	public void setStopFlag(boolean set) {
		logger.log(Level.FINER, "set stop flag: " + set);
		this.stopFlag = set;
		MP3Gain.killCurrentProcess();
	}

	/**
	 * adds the given audio file
	 * 
	 * @param path
	 *            path to audio file
	 * @param sort
	 *            sort audio files after adding
	 * 
	 * @return true if added successfully, else false
	 */
	public boolean addAudioFile(String path, boolean sort) {
		logger.log(Level.FINER, "adding audio file: " + path + " already read: " + readPaths.contains(path));

		if (this.stopFlag)
			return false;

		if (readPaths.contains(path))
			return false;
		if (!Commons.isValidExtension(FileUtil.getFileExtension(path)))
			return false;

		audioFiles.add(new GainAudioFile(path));
		readPaths.add(path);

		if (sort)
			sortAudioFiles();

		return true;
	}

	/**
	 * reads the audio files in the given folder
	 * 
	 * @param path
	 *            path to the folder
	 * @param recursive
	 *            read all subfolders recursively
	 * 
	 * @return true if read successfully, else false
	 */
	public boolean addAudioFiles(String path, boolean recursive) {
		logger.log(Level.FINER, "reading all audio files from: " + path + " recursive: " + recursive);

		if (this.stopFlag)
			return false;

		List<String> files = FileUtil.getFilesFromFolder(path, true);

		for (String file : files) {

			if (this.stopFlag)
				return false;

			logger.log(Level.FINER, "analyse file: " + file);
			if (Commons.isValidExtension(FileUtil.getFileExtension(file))) {
				addAudioFile(file, false);
			}
			// check if it is a folder, if so
			else if (recursive && new File(file).isDirectory()) {
				addAudioFiles(file, recursive);
			}
		}

		sortAudioFiles();

		return true;
	}

	/**
	 * sorts the loaded audio files depending on the filename, not case
	 * sensitive
	 */
	private void sortAudioFiles() {
		Collections.sort(audioFiles, new Comparator<GainAudioFile>() {

			@Override
			public int compare(GainAudioFile arg0, GainAudioFile arg1) {
				return arg0.getPath().compareToIgnoreCase(arg1.getPath());
			}
		});

	}

	/**
	 * deletes the audio file at the given index
	 * 
	 * @param index
	 *            given index
	 * 
	 * @return true if deleted successfully, else false
	 */
	private boolean deleteAudioFile(int index) {
		logger.log(Level.FINER, "delete audio file with index " + index + " exists: " + (index < this.audioFiles.size()));
		if (index >= this.audioFiles.size())
			return false;

		String path = this.audioFiles.get(index).getPath();
		this.audioFiles.remove(index);
		boolean delP = this.readPaths.remove(path);

		logger.log(Level.FINER, "audio file deleted: " + delP);

		return true;
	}

	/**
	 * deletes the audio files at the given indices
	 * 
	 * @param indices
	 *            given indices
	 * 
	 * @return true if deleted successfully, else false
	 */
	public boolean deleteAudioFiles(int[] indices) {
		Integer[] tmp = new Integer[indices.length];
		for (int i = 0; i < indices.length; i++)
			tmp[i] = indices[i];
		List<Integer> ints = Arrays.asList(tmp);
		Collections.sort(ints);

		int offset = 0;
		for (Integer i : ints) {
			deleteAudioFile(i - offset);
			offset++;
		}

		return true;
	}

	/**
	 * deletes all audio files
	 * 
	 * @return true if deleted successfully, else false
	 */
	public boolean clearAudioFiles() {
		this.audioFiles.clear();
		this.readPaths.clear();
		return true;
	}

	/**
	 * gets the audio file at the given index
	 * 
	 * @param index
	 *            given index
	 * 
	 * @return the audio file
	 */
	public GainAudioFile getAudioFile(int index) {
		return this.audioFiles.get(index);
	}

	/**
	 * gets the tablemodel depending on the currently loaded audio files
	 * 
	 * @return the table model
	 */
	public GainTableModel getTableModel() {
		return new GainTableModel(audioFiles);
	}

	/**
	 * sets the given indices as invalid, so that they need to be recalculated
	 * 
	 * @param indices
	 *            given indices
	 */
	public void clearAudioFileData(int[] indices) {
		for (int i = 0; i < indices.length; i++)
			audioFiles.get(indices[i]).setValid(false);
	}

	/**
	 * sets all loaded files as invalid, so that they need to be recalculated
	 */
	public void clearAllAudioFileData() {
		for (GainAudioFile file : this.audioFiles)
			file.setValid(false);
	}

	/**
	 * analyses the Track Gain for the audio file at the given index
	 * 
	 * @param index
	 *            given index
	 * @param targetVol
	 *            the given target volume. The gain will be calculated depending
	 *            on the given volume
	 * @param forceRecalc
	 *            true if the track should be recalculated, false to just read
	 *            the infos, the gain will only be calculated if no data is
	 *            saved.
	 * @param pb
	 *            the given progressbar which should be updated
	 * 
	 * @return true if analysed successfully
	 * 
	 * @throws IOException
	 *             thrown if something went wrong while analysing
	 */
	public boolean analyseTrackGain(int index, int targetVol, boolean forceRecalc, MP3GainThread pb) throws IOException {
		logger.log(Level.FINER, "analyse Track: " + index + " with targetVol: " + targetVol + " force Recalc: " + forceRecalc);

		if (this.stopFlag)
			return false;

		if (index >= this.audioFiles.size())
			return false;

		GainAudioFile audioFile = this.audioFiles.get(index);
		MP3Gain g = new MP3Gain(audioFile.getPath(), targetVol);
		g.calculateGain(forceRecalc, pb);

		audioFile.setValid(true);
		audioFile.setTrackvolume(g.getGainData(0).getTrackVolume());
		audioFile.setTrackGain(g.getGainData(0).getTrackGainChange());
		audioFile.setAlbumVolume(g.getGainData(0).getAlbumVolume());
		audioFile.setAlbumGain(g.getGainData(0).getAlbumGainChange());
		logger.log(Level.FINER, "analyse Track: " + this.audioFiles.get(index));

		return true;
	}

	/**
	 * gets the number of loaded files
	 * 
	 * @return number of files
	 */
	public int getNumOfAudioFiles() {
		return this.audioFiles.size();
	}

	/**
	 * analyses the album gain of the audio files at the given indices
	 * 
	 * @param indices
	 *            indices of the audio files
	 * @param targetVol
	 *            the given target volume. The gain will be calculated depending
	 *            on the given volume
	 * @param forceRecalc
	 *            true if the track should be recalculated, false to just read
	 *            the infos, the gain will only be calculated if no data is
	 *            saved.
	 * @param pb
	 *            the given progressbar which should be updated
	 * 
	 * @return true if analysed successfully
	 * 
	 * @throws IOException
	 *             thrown if something went wrong while calculating
	 */
	public boolean analyseAlbumGain(int[] indices, int targetVol, boolean forceRecalc, MP3GainThread pb) throws IOException {
		logger.log(Level.FINER, "analyse Album gain for " + indices.length + " Tracks with targetVol: " + targetVol + " force Recalc: " + forceRecalc);

		if (indices.length < 1)
			return false;

		List<String> audioFiles = new ArrayList<String>();
		for (int i = 0; i < indices.length; i++)
			audioFiles.add(this.audioFiles.get(indices[i]).getPath());

		MP3Gain g = new MP3Gain(audioFiles, targetVol);

		g.calculateGain(forceRecalc, pb);

		for (int i = 0; i < indices.length; i++) {
			this.audioFiles.get(i).setValid(true);
			this.audioFiles.get(i).setTrackvolume(g.getGainData(i).getTrackVolume());
			this.audioFiles.get(i).setTrackGain(g.getGainData(i).getTrackGainChange());
			this.audioFiles.get(i).setAlbumVolume(g.getGainData(i).getAlbumVolume());
			this.audioFiles.get(i).setAlbumGain(g.getGainData(i).getAlbumGainChange());

			logger.log(Level.FINER, "analyse album: " + this.audioFiles.get(i));
		}

		return true;
	}

	/**
	 * changes the track gain of the audio file at the given index
	 * 
	 * @param index
	 *            index of the audio file
	 * @param targetVol
	 *            the given target volume. The gain will be calculated depending
	 *            on the given volume
	 * @param forceRecalc
	 *            true if the track should be recalculated, false to just read
	 *            the infos, the gain will only be calculated if no data is
	 *            saved.
	 * @param pb
	 *            the given progressbar which should be updated
	 * 
	 * @return true if changed successfully
	 * 
	 * @throws IOException
	 *             thrown if something went wrong while changing
	 */

	public boolean changeGain(int index, int targetVol, boolean forceRecalc, MP3GainThread pb) throws IOException {
		logger.log(Level.FINER, "change track gain for index " + index);

		if (index >= this.audioFiles.size()) {
			logger.log(Level.FINER, "audio file not found");
			return false;
		}

		GainAudioFile audioFile = this.audioFiles.get(index);
		logger.log(Level.FINER, "change gain: " + audioFile);
		if (!audioFile.isValid()) {
			logger.log(Level.FINER, "audio file is not valid. No Gaininformation saved.");
			return false;
		}

		MP3Gain.changeTrackGain(audioFile.getPath(), audioFile.getRelativeAlbumGain(targetVol), pb);

		analyseTrackGain(index, targetVol, false, null);

		return true;
	}

	/**
	 * changes the album gain of the audio file at the given index
	 * 
	 * @param indices
	 *            index of the audio file
	 * @param targetVol
	 *            the given target volume. The gain will be calculated depending
	 *            on the given volume
	 * @param forceRecalc
	 *            true if the track should be recalculated, false to just read
	 *            the infos, the gain will only be calculated if no data is
	 *            saved.
	 * @param pb
	 *            the given progressbar which should be updated
	 * 
	 * @return true if changed successfully
	 * 
	 * @throws IOException
	 *             thrown if something went wrong while changing
	 */
	public boolean changeAlbumGain(int[] indices, int targetVol, boolean forceRecalc, MP3GainThread pb) throws IOException {
		logger.log(Level.FINER, "change album gain for " + indices.length + " audio files.");

		if (indices.length < 1)
			return false;

		List<String> audioFiles = new ArrayList<String>();
		for (int i = 0; i < indices.length; i++) {
			logger.log(Level.FINER, "change album: " + this.audioFiles.get(indices[i]));
			if (!this.audioFiles.get(indices[i]).isValid()) {
				logger.log(Level.FINER, "file " + this.audioFiles.get(indices[i]).getPath() + " is not valid.");
				return false;
			}
			audioFiles.add(this.audioFiles.get(indices[i]).getPath());
		}

		MP3Gain.changeAlbumGain(audioFiles, this.audioFiles.get(0).getRelativeAlbumGain(targetVol), pb);

		analyseAlbumGain(indices, targetVol, false, null);

		return true;
	}

	/**
	 * checks if mp3gain is available
	 * 
	 * @return true if it is available, else false
	 */
	public boolean checkMP3Gain() {
		logger.log(Level.FINER, "check if mp3gain is available.");

		try {
			logger.log(Level.FINER, "call command: " + Config.getInstance().getMP3GainPath() + " -v");
			Process p = Runtime.getRuntime().exec(Config.getInstance().getMP3GainPath() + " -v");

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			String line = br.readLine();
			logger.log(Level.FINER, "line: " + line);

			br.close();

			if (line.toLowerCase().contains("version") && line.toLowerCase().contains("mp3gain"))
				return true;

			if (p.waitFor() == 0)
				return true;
		} catch (IOException | InterruptedException e) {
			logger.log(Level.SEVERE, "Error while starting mp3gain test process:\n" + LogUtil.getStackTrace(e), e);
			return false;
		}

		return false;
	}
}