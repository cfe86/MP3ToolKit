package model.audio;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import manager.AudioManager;
import model.ID3TagRegex;
import model.audio.interfaces.IAudioFile;
import model.exception.AudioFileException;
import model.util.FileUtil;

public class RenameAudioFile {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the current name
	 */
	private String currentName;

	/**
	 * the new name
	 */
	private String newName;

	/**
	 * the file path
	 */
	private String path;

	/**
	 * the constructor
	 * 
	 * @param path
	 *            the given file path
	 */
	public RenameAudioFile(String path) {
		this.currentName = FileUtil.getFileNameWithoutExtension(path);
		this.path = path;
		this.newName = this.currentName;
		logger.log(Level.FINER, "created new RenameFile: " + currentName + " from:" + path);
	}

	public String getCurrentName() {
		return currentName;
	}

	public String getNewName() {
		return newName;
	}

	public String getPath() {
		return path;
	}

	/**
	 * the file is analyzed if the new name is not empty
	 * 
	 * @return true if analyzed else false
	 */
	public boolean isAnalyzed() {
		return this.newName.trim().equals("");
	}

	/**
	 * modifies the new name depending on the given parameters
	 * 
	 * @param replaceUnderscore
	 *            replaces '_' with space
	 * @param replaceSpace
	 *            replaces spache with '_'
	 * @param trim
	 *            trims the name
	 */
	public void modifyRename(boolean replaceUnderscore, boolean replaceSpace, boolean trim) {
		if (replaceUnderscore)
			this.newName = this.newName.replace('_', ' ');
		if (replaceSpace)
			this.newName = this.newName.replace(' ', '_');
		if (trim)
			this.newName = this.newName.trim();
	}

	/**
	 * replaces the new name 'replace' with 'with'
	 * 
	 * @param replace
	 *            given replace
	 * @param with
	 *            given replacement
	 */
	public void replaceRename(String replace, String with) {
		this.newName = this.newName.replace(replace, with);
	}

	/**
	 * makes a new name using the given Regex
	 * 
	 * @param regex
	 *            given regex
	 */
	public void makeNewName(ID3TagRegex regex) {
		this.newName = regex.modifyString(currentName);
	}

	/**
	 * renames the given audio file to the new name
	 * 
	 * @param unchanged
	 *            true if the extension should be unchanged
	 * @param upperCase
	 *            true if it should be uppercase and false for lowercase
	 * 
	 * @return true if the name was changed
	 */
	public boolean renameAudioFile(boolean unchanged, boolean upperCase) {
		if (!new File(this.path).exists())
			return false;

		String extension;
		if (unchanged)
			extension = FileUtil.getFileExtension(this.path);
		else if (upperCase)
			extension = FileUtil.getFileExtension(this.path).toUpperCase();
		else
			extension = FileUtil.getFileExtension(this.path).toLowerCase();

		String path = FileUtil.getFilePath(this.path);
		// rename
		return new File(this.path).renameTo(new File(path + "/" + this.newName + "." + extension));
	}

	/**
	 * creates the new Name depending on the given target regex and the ID3Tag
	 * of the audio file
	 * 
	 * @param targetRegex
	 *            given target regex
	 */
	public void createNewNameFromID3Tag(String targetRegex) {
		try {
			logger.log(Level.FINER, "scan audio File: " + path);
			IAudioFile audioFile = AudioManager.getInstance().getAudioFile(path, false, false);
			String result = targetRegex;
			result = result.replace(ID3TagRegex.TITLE, audioFile.getTitle().equals("") ? "unknown Title" : audioFile.getTitle());
			result = result.replace(ID3TagRegex.ARTIST, audioFile.getArtist().equals("") ? "unknown Artist" : audioFile.getArtist());
			result = result.replace(ID3TagRegex.ALBUMARTIST, audioFile.getAlbumArtist().equals("") ? "unknown AlbumArtist" : audioFile.getAlbumArtist());
			result = result.replace(ID3TagRegex.ALBUM, audioFile.getAlbum().equals("") ? "unknown Album" : audioFile.getAlbum());
			result = result.replace(ID3TagRegex.YEAR, audioFile.getYear().equals("") ? "unknown Year" : audioFile.getYear());
			result = result.replace(ID3TagRegex.TRACK, audioFile.getCurrTrack().equals("") ? "unknown Track" : audioFile.getCurrTrack());
			result = result.replace(ID3TagRegex.MAXTRACKS, audioFile.getMaxTrack().equals("") ? "unknown max Tracks" : audioFile.getMaxTrack());
			result = result.replace(ID3TagRegex.GENRE, Integer.toString(audioFile.getGenre()));
			result = result.replace(ID3TagRegex.COMMENT, audioFile.getComment().equals("") ? "unknown Comment" : audioFile.getComment());
			result = result.replace(ID3TagRegex.COMPOSER, audioFile.getComposer().equals("") ? "unknown Composer" : audioFile.getComposer());
			result = result.replace(ID3TagRegex.ORIGARTIST, audioFile.getOriginalArtist().equals("") ? "unknown original Artist" : audioFile.getOriginalArtist());
			result = result.replace(ID3TagRegex.COPYRIGHT, audioFile.getCopyright().equals("") ? "unknown Copyright" : audioFile.getCopyright());
			result = result.replace(ID3TagRegex.URL, audioFile.getURL().equals("") ? "unknown URL" : audioFile.getURL());
			result = result.replace(ID3TagRegex.ENCODEDBY, audioFile.getEncoder().equals("") ? "unknown encoded by" : audioFile.getEncoder());

			this.newName = result;
			logger.log(Level.FINER, "new Name of " + this.currentName + " is " + newName);
		} catch (AudioFileException e) {
			logger.log(Level.SEVERE, "Error while creating new name using ID3Tag:\n" + LogUtil.getStackTrace(e), e);
		}
	}
}