package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cf.util.Regex;

import model.audio.FolderAudioFile;
import model.progressbar.interfaces.IProgressBar;
import model.table.StructureTableModel;
import model.util.Commons;
import model.util.FileUtil;

public class FolderCreatorModel {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * list with current loaded files
	 */
	private List<FolderAudioFile> audioFiles;

	/**
	 * folders which needs to be generated (can already be generated too)
	 */
	private List<String> folders;

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
	public FolderCreatorModel() {
		this.audioFiles = new ArrayList<FolderAudioFile>();
		this.folders = new ArrayList<String>();
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
	 * returns the number of loaded files
	 * 
	 * @return the number of files
	 */
	public int getListSize() {
		return this.audioFiles.size();
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

		String path = this.audioFiles.get(index).getFilePath();
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
		this.audioFiles.add(new FolderAudioFile(path));

		if (sort)
			sortAudioFiles();

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
	 * sorts the audio files alphabetically not case sensitive
	 */
	public void sortAudioFiles() {
		logger.log(Level.FINER, "sort audio files.");
		Collections.sort(audioFiles, new Comparator<FolderAudioFile>() {

			@Override
			public int compare(FolderAudioFile arg0, FolderAudioFile arg1) {
				return FileUtil.getFileName(arg0.getFilePath()).compareToIgnoreCase(FileUtil.getFileName(arg1.getFilePath()));
			}
		});
	}

	/**
	 * gets the TableModel with all audio files
	 * 
	 * @return the TableModel
	 */
	public StructureTableModel getTableModel() {
		return new StructureTableModel(this.audioFiles);
	}

	/**
	 * creates the new Path from the filename
	 * 
	 * @param strucRegex
	 *            regex for folder structure
	 * @param nameRegex
	 *            regex for file name
	 * @param target
	 *            target folder
	 * @param pb
	 *            the given progressbar which will be updated after each audio
	 *            file
	 */
	public void createNewPath(String strucRegex, String nameRegex, String target, IProgressBar pb) {
		logger.log(Level.FINER, "create new Path from filename");
		ID3TagRegex regex = null;
		if (!strucRegex.equals(".") && !nameRegex.equals("."))
			regex = new ID3TagRegex(nameRegex, strucRegex);

		for (FolderAudioFile audioFile : this.audioFiles) {
			audioFile.createNewPath(regex, target);
			if (pb != null)
				pb.nextStep();
		}
	}

	/**
	 * creates the new Path from the ID3Tag
	 * 
	 * @param structure
	 *            regex for folder structure
	 * @param target
	 *            target folder
	 * @param pb
	 *            the given progressbar which will be updated after each audio
	 *            file
	 */
	public void createNewPathID3(String structure, String target, IProgressBar pb) {
		logger.log(Level.FINER, "create new Path from ID3Tag");
		for (FolderAudioFile audioFile : this.audioFiles) {
			audioFile.createNewPathFromID3Tag(structure, target);
			if (pb != null)
				pb.nextStep();
		}
	}

	/**
	 * creates the folder structure. and saves all subfolders.
	 * generateFolderStructure will generate them.
	 */
	public void createFolderStructure() {
		logger.log(Level.FINER, "create folder structure.");
		Set<String> tmp = new HashSet<String>();

		for (FolderAudioFile audioFile : this.audioFiles) {
			String[] folders = FileUtil.getFilePath(audioFile.getNewPathRel()).split(Regex.replaceRegexChars(FolderAudioFile.sep));
			String f = "";
			for (int i = 0; i < folders.length; i++) {
				f += folders[i].trim();
				if (!f.equals(""))
					tmp.add(f);
				logger.log(Level.FINER, "adding " + f + " to folders to generate.");
				f += FolderAudioFile.sep;
			}
		}

		this.folders = new ArrayList<String>(tmp);
		logger.log(Level.FINER, "found " + this.folders.size() + " folders to generate.");
		Collections.sort(this.folders, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (o1.length() > o2.length())
					return 1;
				else if (o1.length() < o2.length())
					return -1;
				else
					return 0;
			}
		});
	}

	/**
	 * generates the folder Structure on the HDD. Before createFolderStructure
	 * needs to be called.
	 */
	public void generateFolderStructure(String target) {
		logger.log(Level.FINER, "generate folders on HDD");

		boolean created;
		for (String str : this.folders) {
			created = new File(target + str).mkdir();
			logger.log(Level.FINER, "generate folder: " + target + str + " created: " + created);
		}
	}

	/**
	 * copies all audio files to the new destination on the folder Structure
	 * 
	 * @param pb
	 *            the given progressbar which will be updated after each audio
	 *            file
	 * 
	 * @throws IOException
	 *             thrown if a file couldn't be copied
	 */
	public void copyAudioFiles(IProgressBar pb) throws IOException {
		boolean copied;
		for (FolderAudioFile audioFile : this.audioFiles) {
			copied = FileUtil.copyFile(audioFile.getFilePath(), audioFile.getNewPath());
			if (pb != null)
				pb.nextStep();
			logger.log(Level.FINER, "copy audio file from " + audioFile.getFilePath() + " to " + audioFile.getNewPath() + " copied: " + copied);
		}
	}
}