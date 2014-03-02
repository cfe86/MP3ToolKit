package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import logging.LogUtil;
import manager.AudioManager;
import manager.CollectorManager;
import model.audio.Genres;
import model.audio.interfaces.IAudioFile;
import model.collector.interfaces.ICoverArtCollector;
import model.collector.interfaces.IID3DataCollector;
import model.collector.interfaces.ILyricsCollector;
import model.exception.AudioFileException;
import model.exception.CollectorException;
import model.progressbar.interfaces.IProgressBar;
import model.structure.FieldReplacerData;
import model.structure.ID3ImageData;
import model.structure.ID3LyricsData;
import model.structure.ID3TagData;
import model.table.ID3TagTableModel;
import model.util.Commons;
import model.util.FileUtil;
import model.util.Graphics;

import com.cf.structures.DataDouble;
import com.mpatric.mp3agic.NotSupportedException;

import config.Config;

public class ID3TagModel {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the loaded audio files
	 */
	private List<IAudioFile> audioFiles;

	/**
	 * all paths of loaded files
	 */
	private Set<String> readPaths;

	/**
	 * index of the currently chosen file
	 */
	private int currIndex;

	/**
	 * process of all loaded custom audio players
	 */
	private List<Process> audioPlayers;

	/**
	 * the stop flag, if set to true all operations should stop
	 */
	private boolean stopFlag;

	/**
	 * Constructor
	 */
	public ID3TagModel() {
		audioFiles = new ArrayList<>();
		readPaths = new HashSet<>();
		currIndex = -1;
		audioPlayers = new ArrayList<>();
		stopFlag = false;
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
	}

	/**
	 * adds the given audio file
	 * 
	 * @param path
	 *            path to audio file
	 * 
	 * @return true if added successfully, else false
	 */
	public boolean addAudioFile(String path) throws AudioFileException {
		logger.log(Level.FINER, "adding audio file: " + path + " already read: " + readPaths.contains(path));

		if (stopFlag)
			return false;

		if (readPaths.contains(path))
			return false;
		if (!Commons.isValidExtension(FileUtil.getFileExtension(path)))
			return false;

		audioFiles.add(AudioManager.getInstance().getAudioFile(path, Config.getInstance().isDeleteID3v1Tag(), Config.getInstance().isSetID3v1Tag()));
		readPaths.add(path);

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
	 * @return true if added successfully, else false
	 */
	public boolean addAudioFiles(String path, boolean recursive, IProgressBar bp) throws AudioFileException {
		logger.log(Level.FINER, "reading all audio files from: " + path + " recursive: " + recursive);

		if (stopFlag)
			return false;

		List<String> files = FileUtil.getFilesFromFolder(path, true);

		for (String file : files) {

			if (stopFlag)
				return false;

			logger.log(Level.FINER, "analyse file: " + file);
			// its an audioFiles
			if (Commons.isValidExtension(FileUtil.getFileExtension(file))) {
				addAudioFile(file);
				if (bp != null)
					bp.nextStep();
			}
			// check if it is a folder, if so
			else if (recursive && new File(file).isDirectory()) {
				addAudioFiles(file, recursive, bp);
			}
		}

		return true;
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

		String path = this.audioFiles.get(index).getFilePath();
		this.audioFiles.remove(index);
		boolean delP = this.readPaths.remove(path);

		logger.log(Level.FINER, "audioFile deleted completely: " + delP);

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
	 * starts the custom player with the given file
	 * 
	 * @param path
	 *            path to the given file
	 * 
	 * @throws IOException
	 *             thrown if player couldn't be loaded
	 */
	public void startCustomPlayer(String path) throws IOException {
		logger.log(Level.FINER, "starting custom player cmd: " + Config.getInstance().getCustomPlayerCmd() + " \"" + path + "\"");
		Process p = Runtime.getRuntime().exec(Config.getInstance().getCustomPlayerCmd() + " \"" + path + "\"");

		this.audioPlayers.add(p);
	}

	/**
	 * stops all custom players
	 */
	public void stopPlayer() {
		logger.log(Level.FINER, "Stop players: " + this.audioPlayers.size());
		for (Process p : this.audioPlayers)
			p.destroy();

		this.audioPlayers.clear();
	}

	/**
	 * gets the table model for all loaded mps
	 * 
	 * @return the table model
	 */
	public ID3TagTableModel getTableModel() {
		return new ID3TagTableModel(this.audioFiles);
	}

	/**
	 * gets the audio file at index i
	 * 
	 * @param i
	 *            index
	 * 
	 * @return the audio file
	 */
	public IAudioFile getAudioFile(int i) {
		return this.audioFiles.get(i);
	}

	/**
	 * writes the current selected audio files cover to the given file path
	 * 
	 * @param filePath
	 *            given filepath
	 * 
	 * @throws IllegalArgumentException
	 *             thrown if no album image is available
	 * @throws IOException
	 *             thrown if the album image couldn't be written
	 */
	public void writeID3TagImage(String filePath) throws IllegalArgumentException, IOException {
		this.audioFiles.get(this.currIndex).writeImage(filePath, true);
	}

	/**
	 * sets the image from the given path to the current audio file
	 * 
	 * @param path
	 *            path to the given image
	 * 
	 * @throws IOException
	 *             thrown if image couldn't be read
	 */
	public void setAudioFileImageFromPath(String path) throws IOException {
		this.audioFiles.get(this.currIndex).setImage(path);
	}

	/**
	 * deletes the image of the current selected audio file
	 */
	public void deleteAudioFileImage() {
		this.audioFiles.get(this.currIndex).setImage(null, null);
	}

	/**
	 * sets the image from the given bytes with the given extension to the
	 * current audioFiles
	 * 
	 * @param imgBytes
	 *            given image in bytes
	 * @param extension
	 *            given image extension, e.g. png
	 */
	public void setAudioFileImageFromBytes(byte[] imgBytes, String extension) {
		this.audioFiles.get(this.currIndex).setImage(imgBytes, extension);
	}

	/**
	 * sets the given lyrics as lyrics for the current audioFiles
	 * 
	 * @param lyrics
	 *            given lyrics
	 */
	public void setLyrics(String lyrics) {
		this.audioFiles.get(this.currIndex).setLyrics(lyrics);
	}

	/**
	 * sets the new current index of the selected audio file
	 * 
	 * @param i
	 *            given index
	 */
	public void setCurrIndex(int i) {
		this.currIndex = i;
	}

	/**
	 * deletes the the file with the given index from the list and from the HDD
	 * 
	 * @param index
	 *            given index
	 * 
	 * @return true if deleted, else false
	 */
	public boolean deleteFileFromHDD(int index) {
		boolean del = new File(this.audioFiles.get(index).getFilePath()).delete();

		if (del)
			this.audioFiles.remove(index);

		return del;
	}

	/**
	 * gets the index of the current selected audio file
	 * 
	 * @return current index
	 */
	public int getCurrIndex() {
		return this.currIndex;
	}

	/**
	 * gets the current selected audioFiles
	 * 
	 * @return current audioFiles
	 */
	public IAudioFile getCurrAudioFile() {
		return this.audioFiles.get(this.currIndex);
	}

	/**
	 * compares the current selected audio file with the given ID3Tag data, if
	 * something is different it will be updated in the current audio file
	 * 
	 * @param data
	 *            given ID3Tag data
	 * 
	 * @return true if something is changed, else false
	 */
	public boolean makeChanges(ID3TagData data) {
		return makeChanges(data, currIndex);
	}

	/**
	 * compares the current selected audio file with the given ID3Tag data, if
	 * something is different it will be updated in the current audio file
	 * 
	 * @param data
	 *            given ID3Tag data
	 * @param index
	 *            index of the audio file
	 * 
	 * @return true if something is changed, else false
	 */
	public boolean makeChanges(ID3TagData data, int index) {
		if (currIndex == -1)
			return false;

		boolean changed = false;

		logger.log(Level.FINEST,
				"Title: data: " + data.getTitle() + " audio file: " + audioFiles.get(index).getTitle() + " equal: " + data.getTitle().equals(this.audioFiles.get(index).getTitle()));
		if (!data.getTitle().equals(this.audioFiles.get(index).getTitle())) {
			this.audioFiles.get(index).setTitle(data.getTitle());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Artist: data: " + data.getArtist() + " audio file: " + this.audioFiles.get(index).getArtist() + " equal: "
						+ data.getArtist().equals(this.audioFiles.get(index).getArtist()));
		if (!data.getArtist().equals(this.audioFiles.get(index).getArtist())) {
			this.audioFiles.get(index).setArtist(data.getArtist());
			changed = true;
		}
		logger.log(Level.FINEST, "AlbumArtist: data: " + data.getAlbumArtist() + " audio file: " + this.audioFiles.get(index).getAlbumArtist() + " equal: "
				+ data.getAlbumArtist().equals(this.audioFiles.get(index).getAlbumArtist()));
		if (!data.getAlbumArtist().equals(this.audioFiles.get(index).getAlbumArtist())) {
			this.audioFiles.get(index).setAlbumArtist(data.getAlbumArtist());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Album: data: " + data.getAlbum() + " audio file: " + this.audioFiles.get(index).getAlbum() + " equal: "
						+ data.getAlbum().equals(this.audioFiles.get(index).getAlbum()));
		if (!data.getAlbum().equals(this.audioFiles.get(index).getAlbum())) {
			this.audioFiles.get(index).setAlbum(data.getAlbum());
			changed = true;
		}
		logger.log(Level.FINEST,
				"Year: data: " + data.getYear() + " audio file: " + this.audioFiles.get(index).getYear() + " equal: " + data.getYear().equals(this.audioFiles.get(index).getYear()));
		if (!data.getYear().equals(this.audioFiles.get(index).getYear())) {
			this.audioFiles.get(index).setYear(data.getYear());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"CurrTrack: data: " + data.getCurrTrack() + " audio file: " + this.audioFiles.get(index).getCurrTrack() + " equal: "
						+ data.getCurrTrack().equals(this.audioFiles.get(index).getCurrTrack()));
		if (!data.getCurrTrack().equals(this.audioFiles.get(index).getCurrTrack())) {
			this.audioFiles.get(index).setCurrTrack(data.getCurrTrack());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"MaxTrack: data: " + data.getMaxTrack() + " audio file: " + this.audioFiles.get(index).getMaxTrack() + " equal: "
						+ data.getMaxTrack().equals(this.audioFiles.get(index).getMaxTrack()));
		if (!data.getMaxTrack().equals(this.audioFiles.get(index).getMaxTrack())) {
			this.audioFiles.get(index).setMaxTrack(data.getMaxTrack());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"CurrCD: data: " + data.getCurrCD() + " audio file: " + this.audioFiles.get(index).getCurrCD() + " equal: "
						+ data.getCurrCD().equals(this.audioFiles.get(index).getCurrCD()));
		if (!data.getCurrCD().equals(this.audioFiles.get(index).getCurrCD())) {
			this.audioFiles.get(index).setCurrCD(data.getCurrCD());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"MaxCD: data: " + data.getMaxCD() + " audio file: " + this.audioFiles.get(index).getMaxCD() + " equal: "
						+ data.getMaxCD().equals(this.audioFiles.get(index).getMaxCD()));
		if (!data.getMaxCD().equals(this.audioFiles.get(index).getMaxCD())) {
			this.audioFiles.get(index).setMaxCD(data.getMaxCD());
			changed = true;
		}
		logger.log(Level.FINEST, "Genre: data: " + data.getGenre() + " audio file: " + this.audioFiles.get(index).getGenre() + " equal: "
				+ (data.getGenre() == this.audioFiles.get(index).getGenre()));
		if (data.getGenre() != this.audioFiles.get(index).getGenre()) {
			this.audioFiles.get(index).setGenre(data.getGenre());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Comment: data: " + data.getComment() + " audio file: " + this.audioFiles.get(index).getComment() + " equal: "
						+ data.getComment().equals(this.audioFiles.get(index).getComment()));
		if (!data.getComment().equals(this.audioFiles.get(index).getComment())) {
			this.audioFiles.get(index).setComment(data.getComment());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"publisher: data: " + data.getPublisher() + " audio file: " + this.audioFiles.get(index).getPublisher() + " equal: "
						+ data.getPublisher().equals(this.audioFiles.get(index).getPublisher()));
		if (!data.getPublisher().equals(this.audioFiles.get(index).getPublisher())) {
			this.audioFiles.get(index).setPublisher(data.getPublisher());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Composer: data: " + data.getComposer() + " audio file: " + this.audioFiles.get(index).getComposer() + " equal: "
						+ data.getComposer().equals(this.audioFiles.get(index).getComposer()));
		if (!data.getComposer().equals(this.audioFiles.get(index).getComposer())) {
			this.audioFiles.get(index).setComposer(data.getComposer());
			changed = true;
		}
		logger.log(Level.FINEST, "OrigArtist: data: " + data.getOrigArtist() + " audio file: " + this.audioFiles.get(index).getOriginalArtist() + " equal: "
				+ data.getOrigArtist().equals(this.audioFiles.get(index).getOriginalArtist()));
		if (!data.getOrigArtist().equals(this.audioFiles.get(index).getOriginalArtist())) {
			this.audioFiles.get(index).setOriginalArtist(data.getOrigArtist());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Copyright: data: " + data.getCopyright() + " audio file: " + this.audioFiles.get(index).getCopyright() + " equal: "
						+ data.getCopyright().equals(this.audioFiles.get(index).getCopyright()));
		if (!data.getCopyright().equals(this.audioFiles.get(index).getCopyright())) {
			this.audioFiles.get(index).setCopyright(data.getCopyright());
			changed = true;
		}
		logger.log(Level.FINEST,
				"URL: data: " + data.getUrl() + " audio file: " + this.audioFiles.get(index).getURL() + " equal: " + data.getUrl().equals(this.audioFiles.get(index).getURL()));
		if (!data.getUrl().equals(this.audioFiles.get(index).getURL())) {
			this.audioFiles.get(index).setURL(data.getUrl());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Encoder: data: " + data.getEncoder() + " audio file: " + this.audioFiles.get(index).getEncoder() + " equal: "
						+ data.getEncoder().equals(this.audioFiles.get(index).getEncoder()));
		if (!data.getEncoder().equals(this.audioFiles.get(index).getEncoder())) {
			this.audioFiles.get(index).setEncoder(data.getEncoder());
			changed = true;
		}
		logger.log(
				Level.FINEST,
				"Lyrics: data: " + data.getLyrics() + " audio file: " + this.audioFiles.get(index).getLyrics() + " equal: "
						+ data.getLyrics().equals(this.audioFiles.get(index).getLyrics()));
		if (!data.getLyrics().equals(this.audioFiles.get(index).getLyrics())) {
			this.audioFiles.get(index).setLyrics(data.getLyrics());
			changed = true;
		}

		return changed;
	}

	/**
	 * changes the given audio file id3tag data depending on the given action
	 * command and the given value
	 * 
	 * @param actionCmd
	 *            given action command
	 * @param value
	 *            given value
	 * @param indices
	 *            given audioFiless
	 */
	public void changeAudioFileComponent(String actionCmd, Object value, int[] indices) {
		logger.log(Level.FINER, "change audio file component. actionCmd: " + actionCmd + " value: " + value + " indices: " + indices.length);

		int index;
		int trackNr = -1;
		if (actionCmd.equals("trackRB"))
			trackNr = (int) value;

		for (int i = 0; i < indices.length; i++) {
			index = indices[i];
			switch (actionCmd) {
				case "titleRB": {
					logger.log(Level.FINEST, "change Title for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getTitle()))
						audioFiles.get(index).setTitle(((String) value));
					break;
				}
				case "artistRB": {
					logger.log(Level.FINEST, "change Artist for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getArtist()))
						audioFiles.get(index).setArtist(((String) value));
					break;
				}
				case "albumArtistRB": {
					logger.log(Level.FINEST, "change Album Artist for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getAlbumArtist()))
						audioFiles.get(index).setAlbumArtist(((String) value));
					break;
				}
				case "albumRB": {
					logger.log(Level.FINEST, "change Album for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getAlbum()))
						audioFiles.get(index).setAlbum(((String) value));
					break;
				}
				case "yearRB": {
					logger.log(Level.FINEST, "change Year for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getYear()))
						audioFiles.get(index).setYear(((String) value));
					break;
				}
				case "trackRB": {
					logger.log(Level.FINEST, "change current Track for audio file " + index + " to " + trackNr);
					if (trackNr != Integer.parseInt(this.audioFiles.get(index).getCurrTrack()))
						audioFiles.get(index).setCurrTrack(Integer.toString(trackNr));
					trackNr++;
					break;
				}
				case "maxTracksRB": {
					logger.log(Level.FINEST, "change max tracks for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getMaxTrack()))
						audioFiles.get(index).setMaxTrack(((String) value));
					break;
				}
				case "cdRB": {
					logger.log(Level.FINEST, "change CD for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getCurrCD()))
						audioFiles.get(index).setCurrCD(((String) value));
					break;
				}
				case "maxCDRB": {
					logger.log(Level.FINEST, "change max CD for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getMaxCD()))
						audioFiles.get(index).setMaxCD(((String) value));
					break;
				}
				case "genreRB": {
					logger.log(Level.FINEST, "change Genre for audio file " + index + " to " + ((String) value));
					if (Integer.parseInt((String) value) != this.audioFiles.get(index).getGenre())
						audioFiles.get(index).setGenre((Integer.parseInt((String) value)));
					break;
				}
				case "publisherRB": {
					logger.log(Level.FINEST, "change publisher for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getPublisher()))
						audioFiles.get(index).setPublisher(((String) value));
					break;
				}
				case "commentRB": {
					logger.log(Level.FINEST, "change Comment for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getComment()))
						audioFiles.get(index).setComment(((String) value));
					break;
				}
				case "composerRB": {
					logger.log(Level.FINEST, "change Composer for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getComposer()))
						audioFiles.get(index).setComposer(((String) value));
					break;
				}
				case "origArtistRB": {
					logger.log(Level.FINEST, "change Orig. Artist for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getOriginalArtist()))
						audioFiles.get(index).setOriginalArtist(((String) value));
					break;
				}
				case "copyrightRB": {
					logger.log(Level.FINEST, "change Copyright for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getCopyright()))
						audioFiles.get(index).setCopyright(((String) value));
					break;
				}
				case "urlRB": {
					logger.log(Level.FINEST, "change URL for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getURL()))
						audioFiles.get(index).setURL(((String) value));
					break;
				}
				case "encodedByRB": {
					logger.log(Level.FINEST, "change Encoder for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getEncoder()))
						audioFiles.get(index).setEncoder(((String) value));
					break;
				}
				case "imageAllRB": {
					logger.log(Level.FINEST, "change Albumcover for audio file " + index + " to Albumcover from " + currIndex);
					audioFiles.get(index).setImage(getCurrAudioFile().getAlbumImage(), getCurrAudioFile().getAlbumImageFormat());
					break;
				}
				case "lyricsRB": {
					logger.log(Level.FINEST, "change lyrics for audio file " + index + " to " + ((String) value));
					if (!((String) value).equals(this.audioFiles.get(index).getLyrics()))
						audioFiles.get(index).setLyrics(((String) value));
					break;
				}
			}
		}
	}

	/**
	 * deletes the tag of the current selected audio file
	 */
	public void deleteTags() {
		deleteTags(this.currIndex);
	}

	/**
	 * deletes the tags of the given audio file
	 * 
	 * @param indices
	 *            given indices of the audio file
	 */
	public void deleteTags(int[] indices) {
		int index;
		for (int i = 0; i < indices.length; i++) {
			index = indices[i];
			deleteTags(index);
		}
	}

	/**
	 * delete all tags for audio file from the given index
	 * 
	 * @param i
	 *            given index
	 */
	private void deleteTags(int i) {
		this.audioFiles.get(i).setTitle(null);
		this.audioFiles.get(i).setArtist(null);
		this.audioFiles.get(i).setAlbumArtist(null);
		this.audioFiles.get(i).setAlbum(null);
		this.audioFiles.get(i).setYear(null);
		this.audioFiles.get(i).setTrack(null);
		this.audioFiles.get(i).setCD(null);
		this.audioFiles.get(i).setGenre(13);
		this.audioFiles.get(i).setComment(null);
		this.audioFiles.get(i).setComposer(null);
		this.audioFiles.get(i).setOriginalArtist(null);
		this.audioFiles.get(i).setCopyright(null);
		this.audioFiles.get(i).setURL(null);
		this.audioFiles.get(i).setEncoder(null);
		this.audioFiles.get(i).setLyrics(null);
		this.audioFiles.get(i).setImage(null, null);

	}

	/**
	 * undo the changes of the audio file of the given index
	 * 
	 * @param index
	 *            given index
	 * 
	 * @throws AudioFileException
	 *             thrown if audio file couldn't be reloaded
	 */
	public void undoChanges(int index) throws AudioFileException {
		if (stopFlag)
			return;

		this.audioFiles.get(index).resetAudioFile();
	}

	/**
	 * gets the number of how many audio file of the given indices got changed
	 * 
	 * @param indices
	 *            given indices
	 * 
	 * @return number of changed audio file
	 */
	public int getChangedNumber(int[] indices) {
		// get changed audioFiless
		int changed = 0;
		for (int i = 0; i < indices.length; i++) {
			if (audioFiles.get(indices[i]).hasChanged())
				changed++;
		}

		return changed;
	}

	/**
	 * writes the given audio file to the harddisk
	 * 
	 * @param indices
	 *            given audio file
	 * @param pb
	 *            the progressbar which will be updated for each changed audio
	 *            file
	 * 
	 * @throws NotSupportedException
	 *             thrown if ID3Tag couldn't be generated
	 * @throws IOException
	 *             thrown if new audio file couldn't be written
	 * @throws AudioFileException
	 *             thrown if new audio file couldn't be reread
	 */
	public void writeAudioFiles(int[] indices, IProgressBar pb) throws NotSupportedException, IOException, AudioFileException {
		for (int i = 0; i < indices.length; i++) {

			if (this.stopFlag)
				return;

			logger.log(Level.FINER, "analyse audioFiles: " + indices[i] + " changed: " + audioFiles.get(indices[i]).hasChanged());
			if (audioFiles.get(indices[i]).hasChanged()) {
				if (!audioFiles.get(indices[i]).isWriteable())
					throw new IOException("Couldn't write file: " + audioFiles.get(indices[i]).getFilePath());

				audioFiles.get(indices[i]).save();
				audioFiles.get(indices[i]).resetAudioFile();
				pb.nextStep();
			}
		}
	}

	/**
	 * gets the number of all audio file
	 * 
	 * @return number of all audio file
	 */
	public int getNumOfAudioFiles() {
		return this.audioFiles.size();
	}

	/**
	 * changes the audio file with the given id3tag data
	 * 
	 * @param audioFiles
	 *            given audio file
	 */
	public void makesChanges(List<ID3TagData> audioFiles) {
		for (ID3TagData tag : audioFiles) {
			makeChanges(tag, tag.getIndex());
		}
	}

	/**
	 * replaces the fields of the given audio file with the data
	 * 
	 * @param indices
	 *            given audio file
	 * @param data
	 *            field replacer data
	 */
	public void replaceFields(int[] indices, FieldReplacerData data) {
		int index;
		for (int i = 0; i < indices.length; i++) {
			index = indices[i];
			String result = data.changeField(FieldReplacerData.TITLE, this.audioFiles.get(index).getTitle());
			if (!result.equals(this.audioFiles.get(index).getTitle()))
				this.audioFiles.get(index).setTitle(result);

			result = data.changeField(FieldReplacerData.ARTIST, this.audioFiles.get(index).getArtist());
			if (!result.equals(this.audioFiles.get(index).getArtist()))
				this.audioFiles.get(index).setArtist(result);

			result = data.changeField(FieldReplacerData.ALBUMARTIST, this.audioFiles.get(index).getAlbumArtist());
			if (!result.equals(this.audioFiles.get(index).getAlbumArtist()))
				this.audioFiles.get(index).setAlbumArtist(result);

			result = data.changeField(FieldReplacerData.ALBUM, this.audioFiles.get(index).getAlbum());
			if (!result.equals(this.audioFiles.get(index).getAlbum()))
				this.audioFiles.get(index).setAlbum(result);

			result = data.changeField(FieldReplacerData.YEAR, this.audioFiles.get(index).getYear());
			if (!result.equals(this.audioFiles.get(index).getYear()))
				this.audioFiles.get(index).setYear(result);

			result = data.changeField(FieldReplacerData.TRACK, this.audioFiles.get(index).getTrack());
			if (!result.equals(this.audioFiles.get(index).getTrack()))
				this.audioFiles.get(index).setTrack(result);

			result = data.changeField(FieldReplacerData.MAXTRACKS, this.audioFiles.get(index).getMaxTrack());
			if (!result.equals(this.audioFiles.get(index).getMaxTrack()))
				this.audioFiles.get(index).setMaxTrack(result);

			result = data.changeField(FieldReplacerData.CD, this.audioFiles.get(index).getCD());
			if (!result.equals(this.audioFiles.get(index).getCD()))
				this.audioFiles.get(index).setCD(result);

			result = data.changeField(FieldReplacerData.MAXCD, this.audioFiles.get(index).getMaxCD());
			if (!result.equals(this.audioFiles.get(index).getMaxCD()))
				this.audioFiles.get(index).setMaxCD(result);

			result = data.changeField(FieldReplacerData.COMMENT, this.audioFiles.get(index).getComment());
			if (!result.equals(this.audioFiles.get(index).getComment()))
				this.audioFiles.get(index).setComment(result);

			result = data.changeField(FieldReplacerData.COMPOSER, this.audioFiles.get(index).getComposer());
			if (!result.equals(this.audioFiles.get(index).getComposer()))
				this.audioFiles.get(index).setComposer(result);

			result = data.changeField(FieldReplacerData.ORIGARTIST, this.audioFiles.get(index).getOriginalArtist());
			if (!result.equals(this.audioFiles.get(index).getOriginalArtist()))
				this.audioFiles.get(index).setOriginalArtist(result);

			result = data.changeField(FieldReplacerData.COPYRIGHT, this.audioFiles.get(index).getCopyright());
			if (!result.equals(this.audioFiles.get(index).getCopyright()))
				this.audioFiles.get(index).setCopyright(result);

			result = data.changeField(FieldReplacerData.URL, this.audioFiles.get(index).getURL());
			if (!result.equals(this.audioFiles.get(index).getURL()))
				this.audioFiles.get(index).setURL(result);

			result = data.changeField(FieldReplacerData.ENCODEDBY, this.audioFiles.get(index).getEncoder());
			if (!result.equals(this.audioFiles.get(index).getEncoder()))
				this.audioFiles.get(index).setEncoder(result);

			result = data.changeField(FieldReplacerData.LYRICS, this.audioFiles.get(index).getLyrics());
			if (!result.equals(this.audioFiles.get(index).getLyrics()))
				this.audioFiles.get(index).setLyrics(result);
		}
	}

	/**
	 * gets a List containg a tuple with 2 ID3Tags. The first one is the current
	 * audioFiles data, the 2nd one is the changed data
	 * 
	 * @param indices
	 *            given indices
	 * 
	 * @param regex
	 *            given ID3TagRegex to get the neccessary id3tag data
	 * 
	 * @return the list
	 */
	public List<DataDouble<ID3TagData, ID3TagData>> getChangesAudioFileList(int[] indices, ID3TagRegex regex) {
		List<DataDouble<ID3TagData, ID3TagData>> result = new ArrayList<DataDouble<ID3TagData, ID3TagData>>();

		int index;
		for (int i = 0; i < indices.length; i++) {
			index = indices[i];
			String name = FileUtil.getFileNameWithoutExtension(this.audioFiles.get(index).getFilePath());
			ID3TagData curr = new ID3TagData();
			curr.setAudioFile(this.audioFiles.get(index), index);

			ID3TagData newTag = new ID3TagData();
			newTag.setIndex(index);
			newTag.setTitle(regex.getTag(ID3TagRegex.TITLE, name));
			newTag.setArtist(regex.getTag(ID3TagRegex.ARTIST, name));
			newTag.setAlbumArtist(regex.getTag(ID3TagRegex.ALBUMARTIST, name));
			newTag.setAlbum(regex.getTag(ID3TagRegex.ALBUM, name));
			newTag.setYear(regex.getTag(ID3TagRegex.YEAR, name));
			newTag.setCurrTrack(regex.getTag(ID3TagRegex.TRACK, name));
			newTag.setMaxTrack(regex.getTag(ID3TagRegex.MAXTRACKS, name));
			newTag.setCurrCD(regex.getTag(ID3TagRegex.CD, name));
			newTag.setMaxCD(regex.getTag(ID3TagRegex.MAXCD, name));
			newTag.setGenre(Genres.getGenreLoose(regex.getTag(ID3TagRegex.GENRE, name)));
			newTag.setComment(regex.getTag(ID3TagRegex.COMMENT, name));
			newTag.setComposer(regex.getTag(ID3TagRegex.COMPOSER, name));
			newTag.setOrigArtist(regex.getTag(ID3TagRegex.ORIGARTIST, name));
			newTag.setCopyright(regex.getTag(ID3TagRegex.COPYRIGHT, name));
			newTag.setUrl(regex.getTag(ID3TagRegex.URL, name));
			newTag.setEncoder(regex.getTag(ID3TagRegex.ENCODEDBY, name));
			newTag.setChanged(regex.getContainsTags());
			result.add(new DataDouble<ID3TagData, ID3TagData>(curr, newTag));
		}

		return result;
	}

	/**
	 * updates the given audio file using the enabled collector. Returns a List
	 * with DataDouble containing the current id3 data und the new id3 data
	 * 
	 * @param indices
	 *            given audio file
	 * @param pb
	 *            the progressbar. Each audio file adds one value to the
	 *            progressbar
	 * 
	 * @return the data double list
	 */
	public List<DataDouble<ID3TagData, ID3TagData>> getID3DataAudioFileUpdate(int[] indices, IProgressBar pb) {

		List<DataDouble<ID3TagData, ID3TagData>> result = new ArrayList<>();

		int index;
		pb.nextStep();
		for (int i = 0; i < indices.length; i++) {

			if (stopFlag)
				break;

			if (pb != null)
				pb.nextStep();

			index = indices[i];

			ID3TagData currData = new ID3TagData();
			currData.setAudioFile(this.audioFiles.get(index), index);

			ID3TagData newData = new ID3TagData();
			newData.setIndex(index);

			// get all collectors
			List<IID3DataCollector> collectors = CollectorManager.getInstance().getID3DataCollectors();

			// create id3 data for collector, title, artist and album
			ID3TagData searchData = new ID3TagData();
			searchData.setTitle(this.audioFiles.get(index).getTitle());
			searchData.setArtist(this.audioFiles.get(index).getArtist());
			searchData.setAlbum(this.audioFiles.get(index).getAlbum());
			logger.log(Level.FINER, "getting ID3 tag data for artist: " + searchData.getArtist() + " title: " + searchData.getTitle() + " album: " + searchData.getAlbum());

			IID3DataCollector collector = null;
			// try if one found data, if so its fine, if not try next one
			for (IID3DataCollector c : collectors) {
				logger.log(Level.FINER, "try using collector: " + c.getCollectorName());
				collector = c;
				collector.setData(searchData);
				try {
					collector.init();
				} catch (CollectorException e) {
					logger.log(Level.SEVERE, "Error while getting id3 tag data:\n" + LogUtil.getStackTrace(e), e);
					continue;
				}

				logger.log(Level.FINER, "found using collector " + collector.getCollectorName() + " found: " + collector.isDataFound());
				if (collector.isDataFound())
					break;
			}

			String track = collector.getTrack();
			String title = collector.getTitle();
			String artist = collector.getArtist();
			String albumArtist = collector.getAlbumArtist();
			String album = collector.getAlbum();
			String year = collector.getYear();
			String maxTracks = collector.getMaxTracks();
			String cd = collector.getCD();
			String maxCD = collector.getMaxCD();
			int genre = collector.getGenre();
			String comment = collector.getComment();
			String composer = collector.getComposer();
			String origArtist = collector.getOrigArtist();
			String copyright = collector.getCopyright();
			String url = collector.getURL();
			String encoder = collector.getEncoder();

			logger.log(Level.FINER, "update audio files title: " + title + " artist: " + artist + " Album Artist: " + albumArtist + " album: " + album + " year: " + year
					+ " track: " + track + " maxTrack: " + maxTracks + " cd: " + cd + " maxCD: " + maxCD + " genre: " + genre + " comment: " + comment + " composer: " + composer
					+ " origArtist: " + origArtist + " copyright: " + copyright + " url: " + url + " encoder: " + encoder);

			newData.setTitle((title != null && title.trim().length() != 0) ? title : "");
			newData.setArtist((artist != null && artist.trim().length() != 0) ? artist : "");
			newData.setAlbumArtist((albumArtist != null && albumArtist.trim().length() != 0) ? albumArtist : "");
			newData.setAlbum((album != null && album.trim().length() != 0) ? album : "");
			newData.setYear((year != null && year.trim().length() != 0) ? year : "");
			newData.setCurrTrack((track != null && track.trim().length() != 0) ? track : "");
			newData.setMaxTrack((maxTracks != null && maxTracks.trim().length() != 0) ? maxTracks : "");
			newData.setCurrCD((cd != null && cd.trim().length() != 0) ? cd : "");
			newData.setMaxCD((maxCD != null && maxCD.trim().length() != 0) ? maxCD : "");
			newData.setGenre(genre);
			newData.setComment((comment != null && comment.trim().length() != 0) ? comment : "");
			newData.setComposer((composer != null && composer.trim().length() != 0) ? composer : "");
			newData.setOrigArtist((origArtist != null && origArtist.trim().length() != 0) ? origArtist : "");
			newData.setCopyright((copyright != null && copyright.trim().length() != 0) ? copyright : "");
			newData.setUrl((url != null && url.trim().length() != 0) ? url : "");
			newData.setEncoder((encoder != null && encoder.trim().length() != 0) ? encoder : "");
			// set album tracks if found by the parser
			newData.setAlbumTracks(collector.getAllAlbumTracks());

			boolean[] changed = new boolean[] { (title != null && title.trim().length() != 0), (artist != null && artist.trim().length() != 0),
					(albumArtist != null && albumArtist.trim().length() != 0), (album != null && album.trim().length() != 0), (year != null && year.trim().length() != 0),
					(track != null && track.trim().length() != 0), (maxTracks != null && maxTracks.trim().length() != 0), (cd != null && cd.trim().length() != 0),
					(maxCD != null && maxCD.trim().length() != 0), true, (comment != null && comment.trim().length() != 0), (composer != null && composer.trim().length() != 0),
					(origArtist != null && origArtist.trim().length() != 0), (copyright != null && copyright.trim().length() != 0), (url != null && url.trim().length() != 0),
					(encoder != null && encoder.trim().length() != 0) };

			newData.setChanged(changed);

			result.add(new DataDouble<ID3TagData, ID3TagData>(currData, newData));
		}

		return result;
	}

	/**
	 * updates the given audio file using the enabled collector. Returns a List
	 * with DataDouble containing the current image und the new image data
	 * 
	 * @param indices
	 *            given audio file
	 * @param pb
	 *            the progressbar. Each audio file adds one value to the
	 *            progressbar
	 * 
	 * @return the data double list
	 */
	public List<DataDouble<ID3ImageData, ID3ImageData>> getCoverArtUpdate(int[] indices, IProgressBar pb) {
		List<DataDouble<ID3ImageData, ID3ImageData>> result = new ArrayList<>();

		int index;
		pb.nextStep();
		for (int i = 0; i < indices.length; i++) {

			if (stopFlag)
				break;

			if (pb != null)
				pb.nextStep();

			index = indices[i];

			ID3ImageData currData = new ID3ImageData(index);
			currData.setImage(this.audioFiles.get(index).getAlbumImage());
			currData.setExtension(this.audioFiles.get(index).getAlbumImageFormat());
			currData.setArtist(this.audioFiles.get(index).getArtist());
			currData.setAlbum(this.audioFiles.get(index).getAlbum());

			ID3ImageData newData = new ID3ImageData(index);

			// get all collectors
			List<ICoverArtCollector> collectors = CollectorManager.getInstance().getCoverArtCollectors();

			// create id3 data for collector, title and album
			ID3TagData searchData = new ID3TagData();
			searchData.setArtist(this.audioFiles.get(index).getArtist());
			searchData.setAlbum(this.audioFiles.get(index).getAlbum());
			logger.log(Level.FINER, "getting cover image for artist: " + searchData.getArtist() + " album: " + searchData.getAlbum());

			ICoverArtCollector collector = null;
			for (ICoverArtCollector c : collectors) {
				logger.log(Level.FINER, "try using collector: " + c.getCollectorName());
				collector = c;
				collector.setData(searchData);
				try {
					collector.init();
				} catch (CollectorException e) {
					logger.log(Level.SEVERE, "Error while getting cover image:\n" + LogUtil.getStackTrace(e), e);
					continue;
				}

				logger.log(Level.FINER, "found using collector " + collector.getCollectorName() + " found: " + collector.isDataFound());
				if (collector.isDataFound())
					break;
			}

			BufferedImage img = collector.getImage();

			if (img != null) {
				// if max image length is set
				if (Config.getInstance().isUseMaxImageLength()) {
					// if image is more than max size -> scale it
					if (img.getWidth() > Config.getInstance().getMaxImageLength() && img.getWidth() >= img.getHeight()) {
						img = Graphics.scale(img, ((double) Config.getInstance().getMaxImageLength()) / ((double) img.getWidth()));
					} else if (img.getHeight() > Config.getInstance().getMaxImageLength() && img.getHeight() >= img.getWidth()) {
						img = Graphics.scale(img, ((double) Config.getInstance().getMaxImageLength()) / ((double) img.getHeight()));
					}
				}
				// convert to jpeg if extension isnt jpeg
				if (!collector.getExtension().equalsIgnoreCase("jpeg") && !collector.getExtension().equalsIgnoreCase("jpg")) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						ImageIO.write(img, "jpeg", baos);
						baos.flush();
						newData.setImage(baos.toByteArray());
						baos.close();
						newData.setExtension("jpeg");
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					newData.setImage(collector.getImageAsBytes());
					newData.setExtension("jpeg");
				}
			}
			newData.setArtist(this.audioFiles.get(index).getArtist());
			newData.setAlbum(this.audioFiles.get(index).getAlbum());

			newData.setChanged(collector.getImageAsBytes() != null);

			result.add(new DataDouble<ID3ImageData, ID3ImageData>(currData, newData));
		}

		return result;
	}

	/**
	 * updates the given audio file using the enabled collector. Returns a List
	 * with DataDouble containing the current lyrics and the new lyrics data
	 * 
	 * @param indices
	 *            given audio file
	 * @param pb
	 *            the progressbar. Each audio file adds one value to the
	 *            progressbar
	 * 
	 * @return the data double list
	 */
	public List<DataDouble<ID3LyricsData, ID3LyricsData>> getLyricsAudioFileUpdate(int[] indices, IProgressBar pb) {

		List<DataDouble<ID3LyricsData, ID3LyricsData>> result = new ArrayList<>();

		int index;
		if (pb != null)
			pb.nextStep();

		for (int i = 0; i < indices.length; i++) {

			if (stopFlag)
				break;

			if (pb != null)
				pb.nextStep();

			index = indices[i];

			ID3LyricsData currData = new ID3LyricsData(index);
			currData.setLyrics(this.audioFiles.get(index).getLyrics());
			currData.setArtist(this.audioFiles.get(index).getArtist());
			currData.setTitle(this.audioFiles.get(index).getTitle());

			ID3LyricsData newData = new ID3LyricsData(index);

			// get all collectors
			List<ILyricsCollector> collectors = CollectorManager.getInstance().getLyricsCollectors();

			// create id3 data for collector, title and album
			ID3TagData searchData = new ID3TagData();
			searchData.setArtist(this.audioFiles.get(index).getArtist());
			searchData.setTitle(this.audioFiles.get(index).getTitle());
			logger.log(Level.FINER, "getting lyrics for artist: " + searchData.getArtist() + " title: " + searchData.getTitle());

			ILyricsCollector collector = null;
			for (ILyricsCollector c : collectors) {
				logger.log(Level.FINER, "try using collector: " + c.getCollectorName());
				collector = c;
				collector.setData(searchData);
				try {
					collector.init();
				} catch (CollectorException e) {
					logger.log(Level.SEVERE, "Error while getting lyrics:\n" + LogUtil.getStackTrace(e), e);
					continue;
				}

				logger.log(Level.FINER, "found using collector " + collector.getCollectorName() + " found: " + collector.isDataFound());
				if (collector.isDataFound())
					break;
			}

			newData.setLyrics(collector.getLyrics());
			newData.setArtist(this.audioFiles.get(index).getArtist());
			newData.setTitle(this.audioFiles.get(index).getTitle());

			result.add(new DataDouble<ID3LyricsData, ID3LyricsData>(currData, newData));
		}

		return result;
	}

	/**
	 * sets the covers of all audio files of the given image data list
	 * 
	 * @param data
	 *            data list
	 */
	public void setCoverArt(List<ID3ImageData> data) {
		logger.log(Level.FINER, "set Cover art");
		int index;
		for (ID3ImageData img : data) {
			logger.log(Level.FINER, "set image for id: " + img.getIndex() + " with " + img.getImage().length + " bytes extension: " + img.getExtension());
			index = img.getIndex();
			this.audioFiles.get(index).setImage(img.getImage(), img.getExtension());
		}
	}

	/**
	 * sets the lyrics of all audio files of the given lyrics data list
	 * 
	 * @param data
	 *            data list
	 */
	public void setLyrics(List<ID3LyricsData> data) {
		logger.log(Level.FINER, "set Lyrics.");
		int index;
		for (ID3LyricsData l : data) {
			logger.log(Level.FINER, "set lyrics for id: " + l.getIndex() + " empty: " + (l.getLyrics().trim().length() == 0));
			index = l.getIndex();
			if (l != null && l.getLyrics().trim().length() != 0)
				this.audioFiles.get(index).setLyrics(l.getLyrics());
		}
	}

	/**
	 * rescales an image to the given width and height
	 * 
	 * @param width
	 *            given width
	 * @param height
	 *            given height
	 * 
	 * @throws IOException
	 *             thrown if image couldn't be set
	 */
	public void rescaleImage(int width, int height) throws IOException {
		byte[] bytes = getCurrAudioFile().getAlbumImage();

		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = ImageIO.read(in);
		in.close();

		if (img.getWidth() == width && img.getHeight() == height)
			return;

		img = Graphics.scale(img, width, height);
		logger.log(Level.FINER, "scaled image to width: " + img.getWidth() + " height: " + img.getHeight());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(img, "jpeg", out);

		getCurrAudioFile().setImage(out.toByteArray(), "jpeg");
		out.close();
	}
}