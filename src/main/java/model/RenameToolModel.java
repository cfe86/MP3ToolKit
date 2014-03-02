package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.audio.RenameAudioFile;
import model.table.RenameTableModel;
import model.util.Commons;
import model.util.FileUtil;

public class RenameToolModel {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * list with current loaded files
	 */
	private List<RenameAudioFile> audioFiles;

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
	public RenameToolModel() {
		this.audioFiles = new ArrayList<RenameAudioFile>();
		this.readPaths = new HashSet<String>();
		this.stopFlag = false;
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
	 * clears all audio files
	 */
	public boolean clearAudioFiles() {
		this.audioFiles.clear();
		this.readPaths.clear();

		return true;
	}

	/**
	 * analyses the given folder and saves all found audio files
	 * 
	 * @param folderPath
	 *            given folder
	 * @param recursive
	 *            true if sub folders should be checked too, else false
	 * 
	 * @return true if everything worked correctly
	 */
	public boolean addAudioFiles(String folderPath, boolean recursive) {
		logger.log(Level.FINER, "reading all audio files from: " + folderPath + " recursive: " + recursive);

		if (this.stopFlag)
			return false;

		List<String> files = FileUtil.getFilesFromFolder(folderPath, true);

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
	 * deletes the audio file at the given index
	 * 
	 * @param index
	 *            given index
	 * 
	 * @return true if deleted successfully, else false
	 */
	public boolean deleteAudioFile(int index) {
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
	 * reads one audio file and checks if it is already read
	 * 
	 * @param path
	 *            path to audio file
	 * @param sort
	 *            true if audio files should be sorted after adding
	 * 
	 * @return true if it is added, else false
	 */
	public boolean addAudioFile(String path, boolean sort) {

		if (this.stopFlag)
			return false;

		if (this.readPaths.contains(path))
			return false;
		if (!Commons.isValidExtension(FileUtil.getFileExtension(path)))
			return false;

		this.readPaths.add(path);
		this.audioFiles.add(new RenameAudioFile(path));

		if (sort)
			sortAudioFiles();

		return true;
	}

	/**
	 * sorts the audio files alphabetically not case sensitive
	 */
	public void sortAudioFiles() {
		logger.log(Level.FINER, "sort audio files");
		Collections.sort(audioFiles, new Comparator<RenameAudioFile>() {

			@Override
			public int compare(RenameAudioFile arg0, RenameAudioFile arg1) {
				return arg0.getCurrentName().compareToIgnoreCase(arg1.getCurrentName());
			}
		});
	}

	/**
	 * makes some filename modifications
	 * 
	 * @param replaceUnderscore
	 *            replaces underscore with space
	 * @param replaceSpace
	 *            replaces space with underscore
	 * @param trim
	 *            trims the filename
	 */
	public void makeMisc(boolean replaceUnderscore, boolean replaceSpace, boolean trim) {
		logger.log(Level.FINER, "make Misc Options: replace Underscore: " + replaceUnderscore + " replaceSpace: " + replaceSpace + " trim: " + trim);
		for (RenameAudioFile file : this.audioFiles)
			file.modifyRename(replaceUnderscore, replaceSpace, trim);
	}

	/**
	 * replaces all new audio file names with the given replacement
	 * 
	 * @param replace
	 *            given replacer
	 * @param with
	 *            given replacement
	 */
	public void makeReplace(String replace, String with) {
		logger.log(Level.FINER, "replace " + replace + " with " + with);
		for (RenameAudioFile file : this.audioFiles)
			file.replaceRename(replace, with);
	}

	/**
	 * generates the new name for all audio files depending on the given src and
	 * target regex
	 * 
	 * @param src
	 *            src regex
	 * @param target
	 *            target regex
	 */
	public void makeNewNames(String src, String target) {
		logger.log(Level.FINER, "make new name. src: " + src + " target: " + target);
		ID3TagRegex regex = new ID3TagRegex(src, target);
		for (RenameAudioFile audioFile : this.audioFiles)
			audioFile.makeNewName(regex);
	}

	/**
	 * generates the new name for all audio files depending on the given target
	 * regex and the id3tag of the audio file
	 * 
	 * @param target
	 *            given target regex
	 */
	public void makeNewNamesID3Tag(String target) {
		logger.log(Level.FINER, "make new name via ID3Tag target: " + target);
		for (RenameAudioFile file : this.audioFiles)
			file.createNewNameFromID3Tag(target);
	}

	/**
	 * gets the TableModel with all audio files
	 * 
	 * @return the TableModel
	 */
	public RenameTableModel getTableModel() {
		return new RenameTableModel(this.audioFiles);
	}

	/**
	 * renames the audio files files to the new name
	 * 
	 * @param unchanged
	 *            true if the extension should be unchanged
	 * @param upperCase
	 *            true if it should be uppercase and false for lowercase
	 */
	public void renameAudioFiles(boolean unchanged, boolean upperCase) {

		if (this.stopFlag)
			return;

		if (this.audioFiles.size() < 1)
			return;

		for (RenameAudioFile file : this.audioFiles) {
			boolean changed = file.renameAudioFile(unchanged, upperCase);
			logger.log(Level.FINER, "changing audio file: " + file.getPath() + " changed: " + changed);
		}
	}

	/**
	 * checks if audio files are analyzed (if they got a new path)
	 * 
	 * @return true if all files are analyzed
	 */
	public boolean allAudioFilesAnalysed() {
		for (RenameAudioFile f : this.audioFiles) {
			if (!f.isAnalyzed())
				return false;
		}

		return true;
	}
}