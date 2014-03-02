package model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Config;
import config.Constants;

public class Commons {

	/**
	 * the logger
	 */
	private static final Logger logger = Logger.getLogger(Commons.class.getName());

	/**
	 * checks if the extension is valid, all valid extension can be found in
	 * Constants
	 * 
	 * @param ext
	 *            the extension to check
	 * 
	 * @return true if extension is valid, else false
	 */
	public static boolean isValidExtension(String ext) {
		if (ext == null)
			return false;

		for (int i = 0; i < Constants.VALID_AUDIO_EXTENSIONS.length; i++) {
			if (ext.equalsIgnoreCase(Constants.VALID_AUDIO_EXTENSIONS[i]))
				return true;
		}

		return false;
	}

	/**
	 * counts the valid audio files of a given folder
	 * 
	 * @param path
	 *            path to the folder
	 * @param recursive
	 *            true if subfolders should be printed too
	 * 
	 * @return the number of valid audio files
	 */
	public static int countAudioFiles(String path, boolean recursive) {
		logger.log(Level.FINER, "counting all audioFiless from: " + path + " recursive: " + recursive);
		List<String> files = FileUtil.getFilesFromFolder(path, true);

		int result = 0;
		for (String file : files) {
			// its an audioFiles
			if (Commons.isValidExtension(FileUtil.getFileExtension(file))) {
				result++;
			}
			// check if it is a folder, if so
			else if (recursive && new File(file).isDirectory()) {
				result += countAudioFiles(file, recursive);
			}
		}

		return result;
	}

	/**
	 * reads the masks from the masks file
	 * 
	 * @return a list with all masks
	 * 
	 * @throws IOException
	 *             thrown if file couldn't be read
	 */
	public static List<String> readMasks() throws IOException {
		logger.log(Level.FINER, "reads the masks file: " + Config.getInstance().getMasksPath() + " found: " + new File(Config.getInstance().getMasksPath()).exists());
		if (!new File(Config.getInstance().getMasksPath()).exists())
			return new ArrayList<String>();

		BufferedReader br = new BufferedReader(new FileReader(new File(Config.getInstance().getMasksPath())));
		String line;
		List<String> masks = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			if (line.trim().length() == 0)
				continue;

			masks.add(line);
		}
		br.close();

		return masks;
	}
}