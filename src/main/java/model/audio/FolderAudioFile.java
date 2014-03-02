package model.audio;

import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import manager.AudioManager;
import model.ID3TagRegex;
import model.audio.interfaces.IAudioFile;
import model.exception.AudioFileException;
import model.util.FileUtil;
import model.util.OS;

public class FolderAudioFile {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * file path
	 */
	private String filePath;

	/**
	 * new generated path
	 */
	private String newPath;

	/**
	 * new relative path
	 */
	private String newPathRel;

	/**
	 * the used seperator depending on the OS
	 */
	public static String sep = OS.isWindows() ? "\\" : "/";

	/**
	 * constructor
	 */
	public FolderAudioFile(String path) {
		this.filePath = path;
		this.newPath = "";
		this.newPathRel = "";
		logger.log(Level.FINER, "create new folderAudioFile for " + filePath);
	}

	/**
	 * creates the new path from the given regex
	 * 
	 * @param regex
	 *            given regex
	 * @param target
	 *            given target folder
	 */
	public void createNewPath(ID3TagRegex regex, String target) {
		String structure = ".";
		if (regex != null)
			structure = regex.modifyString(FileUtil.getFileNameWithoutExtension(filePath));
		
		String filename = FileUtil.getFileName(filePath);

		this.newPathRel = parseStructure(structure) + filename;
		this.newPath = target + this.newPathRel;
		logger.log(Level.FINER, "new Path from " + this.filePath + " is " + newPath);
	}

	/**
	 * parses the given structure of the form text/text/[text], components in []
	 * are optional only if they are there
	 * 
	 * @param structure
	 *            given structure string e.g. %a/%z/ to generate the structure
	 *            artist/album/file.ext
	 * 
	 * @return the new structure string e.g. if the optional fields are not
	 *         available they will be deleted, if an not optional component is
	 *         not available an "unknown" will be set instead
	 */
	private String parseStructure(String structure) {
		logger.log(Level.FINER, "parse structure: " + structure);
		
		if (structure.trim().equals("."))
			return sep;
		
		// split at /
		structure = structure.replace("\\", "/");
		String[] tmp = structure.split("/");

		String result = sep;
		for (int i = 0; i < tmp.length; i++) {
			String curr = tmp[i].trim();
			// empty -> call it unknown
			if (curr.equals("")) {
				result += "unknown" + sep;
				continue;
			}
			// only [] or [unknown ...] -> continue
			if (curr.equals("[]") || curr.startsWith("[unknown"))
				continue;

			// else delete [ and ] and add it
			if (curr.startsWith("["))
				curr = curr.substring(1);
			if (curr.endsWith("]"))
				curr = curr.substring(0, curr.length() - 1);

			result += curr + sep;

		}

		logger.log(Level.FINER, "parsed structure is: " + result);
		return result;
	}

	/**
	 * creates the new path depending on the given structure and the ID3Tag of
	 * the audio file
	 * 
	 * @param structure
	 *            given structure
	 * @param targetFolder
	 *            given target folder
	 */
	public void createNewPathFromID3Tag(String structure, String targetFolder) {
		try {
			logger.log(Level.FINER, "scan audio file: " + filePath);
			IAudioFile audioFile = AudioManager.getInstance().getAudioFile(filePath, false, false);
			String result = structure;
			result = result.replace(ID3TagRegex.TITLE, audioFile.getTitle().equals("") ? "unknown Title" : audioFile.getTitle());
			result = result.replace(ID3TagRegex.ARTIST, audioFile.getArtist().equals("") ? "unknown Artist" : audioFile.getArtist());
			result = result.replace(ID3TagRegex.ALBUMARTIST, audioFile.getAlbumArtist().equals("") ? "unknown AlbumArtist" : audioFile.getAlbumArtist());
			result = result.replace(ID3TagRegex.ALBUM, audioFile.getAlbum().equals("") ? "unknown Album" : audioFile.getAlbum());
			result = result.replace(ID3TagRegex.YEAR, audioFile.getYear().equals("") ? "unknown Year" : audioFile.getYear());
			result = result.replace(ID3TagRegex.TRACK, audioFile.getCurrTrack().equals("") ? "unknown Track" : audioFile.getCurrTrack());
			result = result.replace(ID3TagRegex.MAXTRACKS, audioFile.getMaxTrack().equals("") ? "unknown max Tracks" : audioFile.getMaxTrack());
			result = result.replace(ID3TagRegex.CD, audioFile.getCD().equals("") ? "unknown CD" : audioFile.getCD());
			result = result.replace(ID3TagRegex.MAXCD, audioFile.getMaxCD().equals("") ? "unknown max CDs" : audioFile.getMaxCD());
			result = result.replace(ID3TagRegex.GENRE, Integer.toString(audioFile.getGenre()));
			result = result.replace(ID3TagRegex.COMMENT, audioFile.getComment().equals("") ? "unknown Comment" : audioFile.getComment());
			result = result.replace(ID3TagRegex.PUBLISHER, audioFile.getPublisher().equals("") ? "unknown Publisher" : audioFile.getPublisher());
			result = result.replace(ID3TagRegex.COMPOSER, audioFile.getComposer().equals("") ? "unknown Composer" : audioFile.getComposer());
			result = result.replace(ID3TagRegex.ORIGARTIST, audioFile.getOriginalArtist().equals("") ? "unknown original Artist" : audioFile.getOriginalArtist());
			result = result.replace(ID3TagRegex.COPYRIGHT, audioFile.getCopyright().equals("") ? "unknown Copyright" : audioFile.getCopyright());
			result = result.replace(ID3TagRegex.URL, audioFile.getURL().equals("") ? "unknown URL" : audioFile.getURL());
			result = result.replace(ID3TagRegex.ENCODEDBY, audioFile.getEncoder().equals("") ? "unknown encoded by" : audioFile.getEncoder());

			String filename = FileUtil.getFileName(filePath);
			this.newPathRel = parseStructure(result) + filename;
			this.newPath = targetFolder + this.newPathRel;
			logger.log(Level.FINER, "new Path from " + this.filePath + " is " + newPath);
		} catch (AudioFileException e) {
			logger.log(Level.SEVERE, "Error while reading id3 tag:\n" + LogUtil.getStackTrace(e), e);
		}

	}

	/**
	 * gets the file path
	 * 
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * gets the new file path
	 * 
	 * @return the new path
	 */
	public String getNewPath() {
		return newPath;
	}

	/**
	 * gets the new relative file path
	 * 
	 * @return the new relative path
	 */
	public String getNewPathRel() {
		return newPathRel;
	}
}