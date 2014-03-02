package model.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import config.Config;
import logging.LogUtil;
import model.progressbar.interfaces.MP3GainThread;
import model.regex.Regex;

public class MP3Gain {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * list of given files
	 */
	private List<GainMetaData> audioFiles;

	/**
	 * the default gain
	 */
	private int defaultGain;

	/**
	 * the regex to get the important blocks from the mp3gain result
	 */
	private static Regex blockRegex = new Regex("(.*?Recommended \"Track\" dB change:.*?Min mp3 global gain field:[^\\n]*)", Pattern.DOTALL);

	/**
	 * gets the track gain change out of the block regex
	 */
	private static Regex changeRegex = new Regex("(.*?)Recommended \"Track\" dB change:(.*?)Recommended \"Track\" mp3 gain change: (-?\\d*).*", Pattern.DOTALL);

	/**
	 * gets the album gain change out of the block regex
	 */
	private static Regex albumRegex = new Regex(".*Recommended \"Album\" dB change for all files:(.*?)Recommended \"Album\" mp3 gain change for all files: (-?\\d*).*",
			Pattern.DOTALL);

	/**
	 * the current gain process
	 */
	private static Process currentProcess;

	/**
	 * kills the current process if there is one
	 * 
	 * @return true if killed, else false
	 */
	public static boolean killCurrentProcess() {
		if (currentProcess == null)
			return true;

		currentProcess.destroy();
		return true;
	}

	/**
	 * Constructor. default gain is 89.
	 * 
	 * @param filePath
	 *            given audio file path
	 */
	public MP3Gain(String filePath) {
		this(Arrays.asList(new String[] { filePath }), 89);
	}

	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            given file path
	 * @param defaultGain
	 *            a default gain which should be applied
	 */
	public MP3Gain(String filePath, int defaultGain) {
		this(Arrays.asList(new String[] { filePath }), defaultGain);
	}

	/**
	 * Constructor
	 * 
	 * @param audioFiles
	 *            given List with file paths
	 */
	public MP3Gain(List<String> audioFiles) {
		this(audioFiles, 89);
	}

	/**
	 * Constructor
	 * 
	 * @param audioFiles
	 *            given List with file paths
	 * @param defaultGain
	 *            a default gain which should be applied
	 */
	public MP3Gain(List<String> audioFiles, int defaultGain) {
		logger.log(Level.FINER, "init MP3Gain for " + audioFiles.size() + " files. defaulGain: " + defaultGain);

		this.audioFiles = new ArrayList<GainMetaData>();
		this.defaultGain = defaultGain;

		for (String audioFile : audioFiles)
			this.audioFiles.add(new GainMetaData(audioFile, defaultGain));

		String gainPath = Config.getInstance().getMP3GainPath();
		logger.log(Level.FINER, "gain path is: " + gainPath);
	}

	/**
	 * calculates the gain of the given file/files (if album gain should be
	 * calculated more than one file should be given)
	 * 
	 * @param forceRecalc
	 *            true if the gain should be recalculated
	 * @param thread
	 *            the given thread for the gui where the in and outpthread
	 *            should be given to
	 * 
	 * @return true if calculated successful, else false
	 * 
	 * @throws IOException
	 *             thrown if something went wrong during calculation
	 */
	public boolean calculateGain(boolean forceRecalc, MP3GainThread thread) throws IOException {
		if (this.audioFiles.size() < 1)
			return false;

		String param = makeFilesToParam();
		String gainPath = Config.getInstance().getMP3GainPath();

		String cmd;
		if (forceRecalc) {
			cmd = gainPath + " -c -s r " + param;
			logger.log(Level.FINER, "calculate gain cmd: " + cmd);
			currentProcess = Runtime.getRuntime().exec(cmd, null, new File("."));
		} else {
			cmd = gainPath + " -c " + param;
			logger.log(Level.FINER, "calculate gain cmd: " + cmd);
			currentProcess = Runtime.getRuntime().exec(cmd, null, new File("."));
		}

		BufferedReader inputStream = new BufferedReader(new InputStreamReader(currentProcess.getInputStream()));
		String line;
		String output = "";

		if (thread != null) {
			thread.setInputStream(currentProcess.getInputStream());
			thread.setErrorStream(currentProcess.getErrorStream());
			if (!thread.isRunning())
				thread.start();
			else
				thread.reset();
		}

		try {
			while ((line = inputStream.readLine()) != null) {
				output += line;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading input stream:\n" + LogUtil.getStackTrace(e), e);
		}

		logger.log(Level.FINER, "result: " + output);
		try {
			currentProcess.waitFor();
		} catch (InterruptedException e) {
			Logger.getLogger(MP3Gain.class.getName()).log(Level.SEVERE, "Error while changing gain:\n" + LogUtil.getStackTrace(e), e);
		}

		int index = 0;
		// get track db and track gain
		for (String str : blockRegex.find(output)) {
			if (changeRegex.matches(str)) {
				audioFiles.get(index).setRecommendedTrackVolumeChange(Double.parseDouble(changeRegex.getGroup(2).trim()));
				audioFiles.get(index).setRecommendedTrackGainChange(Double.parseDouble(changeRegex.getGroup(3).trim()));
			}
			index++;
		}

		double albumDB = 0.0;
		double albumGain = 0.0;
		if (albumRegex.matches(output)) {
			albumDB = Double.parseDouble(albumRegex.getGroup(1).trim());
			albumGain = Double.parseDouble(albumRegex.getGroup(2).trim());
		}

		for (GainMetaData audioFile : audioFiles) {
			audioFile.setRecommendedAlbumVolumeChange(albumDB);
			audioFile.setRecommendedAlbumGainChange(albumGain);
			logger.log(
					Level.FINER,
					"calculated for: " + audioFile.getFilePath() + " Trackvolume: " + audioFile.getTrackVolume() + " TrackVolChange: "
							+ audioFile.getRecommendedTrackVolumeChange() + " AlbumVolume: " + audioFile.getAlbumVolume() + " AlbumVolChange: "
							+ audioFile.getRecommendedAlbumVolumeChange());
		}

		return true;

	}

	/**
	 * joins all audio files and wraps them into " "
	 * 
	 * @return the joined list as a string which can be used as a parameter for
	 *         shell commands
	 */
	private String makeFilesToParam() {
		StringBuffer result = new StringBuffer();

		for (GainMetaData audioFile : this.audioFiles)
			result.append("\"").append(audioFile.getFilePath()).append("\" ");

		return result.toString().trim();
	}

	/**
	 * changes the gain of the given file for the given gain
	 * 
	 * @param path
	 *            the path of the file
	 * @param gain
	 *            the gain value it should be changed for e.g. -3 would change the gain to 89-3
	 * @param thread
	 *            the given thread for the gui where the in and outpthread
	 *            should be given to
	 * 
	 * @return true if gain changed, else false
	 * 
	 * @throws IOException
	 *             thrown if something went wrong during change
	 */
	public static boolean changeTrackGain(String path, double gain, MP3GainThread thread) throws IOException {
		Logger.getLogger(MP3Gain.class.getName()).log(Level.FINER, "change gain for mp3: " + path + " change: " + gain);
				
		String gainPath = Config.getInstance().getMP3GainPath();
		String cmd = gainPath + " -c -r -d " + gain + " \"" + path + "\"";
		Logger.getLogger(MP3Gain.class.getName()).log(Level.FINER, "change Gain cmd: " + cmd);
		Process p = Runtime.getRuntime().exec(cmd, null, new File("."));

		if (thread != null) {
			thread.setInputStream(p.getInputStream());
			thread.setErrorStream(p.getErrorStream());
			if (thread.isRunning())
				thread.nextStep();
			else thread.start();
		}

		try {
			return p.waitFor() == 0;
		} catch (InterruptedException e) {
			Logger.getLogger(MP3Gain.class.getName()).log(Level.SEVERE, "Error while changing gain:\n" + LogUtil.getStackTrace(e), e);
			return false;
		}
	}

	/**
	 * changes the gain of the given audio files for the given gain value
	 * 
	 * @param audioFiles
	 *            the paths of the files
	 * @param gain
	 *            the gain it should be changed to
	 * @param thread
	 *            the given thread for the gui where the in and outpthread
	 *            should be given to
	 * 
	 * @return true if gain changed, else false
	 * 
	 * @throws IOException
	 *             thrown if something went wrong during change
	 */
	public static boolean changeAlbumGain(List<String> audioFiles, double gain, MP3GainThread thread) throws IOException {
		Logger.getLogger(MP3Gain.class.getName()).log(Level.FINER, "change gain for " + audioFiles.size() + " mp3s. change: " + gain);
		String param = "";

		for (String audioFile : audioFiles)
			param += "\"" + audioFile + "\" ";

		param = param.trim();
		String gainPath = Config.getInstance().getMP3GainPath();
		String cmd = gainPath + " -c -a -d " + gain + " " + param;
		Logger.getLogger(MP3Gain.class.getName()).log(Level.FINER, "change Gain cmd: " + cmd);
		Process p = Runtime.getRuntime().exec(cmd, null, new File("."));

		if (thread != null) {
			thread.setInputStream(p.getInputStream());
			thread.setErrorStream(p.getErrorStream());
			thread.start();
		}

		try {
			return p.waitFor() == 0;
		} catch (InterruptedException e) {
			Logger.getLogger(MP3Gain.class.getName()).log(Level.SEVERE, "Error while changing gain:\n" + LogUtil.getStackTrace(e), e);
			return false;
		}
	}

	/**
	 * gets the default gain
	 * 
	 * @return the default gain
	 */
	public int getDefaultGain() {
		return this.defaultGain;
	}

	/**
	 * gets the number of audio files
	 * 
	 * @return number of files
	 */
	public int getNumOfAudioFiles() {
		return this.audioFiles.size();
	}

	/**
	 * gets the gain data at the given index
	 * 
	 * @param index
	 *            the given index
	 * 
	 * @return the gain data
	 */
	public GainMetaData getGainData(int index) {
		return this.audioFiles.get(index);
	}
}