package config;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.util.OS;

public class Config {

	/**
	 * the logger
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the instance
	 */
	private static Config instance;

	/**
	 * mapping config to value
	 */
	private Map<String, String> config;

	/**
	 * gets the singleton
	 * 
	 * @return the Config
	 */
	public static Config getInstance() {
		return instance;
	}

	/**
	 * Constructor for singleton
	 */
	private Config() {
	}

	/**
	 * inits the config
	 * 
	 * @throws IOException
	 */
	public static void init() {
		instance = new Config();
		instance.readConfig();
	}

	/**
	 * reads the config file (default settings.conf)
	 */
	private void readConfig() {
		logger.log(Level.CONFIG, "read config from: " + Constants.PATH + Constants.CONFIG_PATH);

		this.config = new HashMap<>();
		this.config.put("currWidth", "1394");
		this.config.put("currHeight", "923");
		this.config.put("currSkin", "com.jtattoo.plaf.hifi.HiFiLookAndFeel");
		this.config.put("currLanguage", "en_US");
		this.config.put("deleteID3v1Tag", "0");
		this.config.put("setID3v1Tag", "0");
		this.config.put("enableDragAndDrop", "1");
		this.config.put("maskFile", Constants.PATH + "masks.txt");
		this.config.put("updateURL", Constants.UPDATE_FILE_URL);
		this.config.put("useCustomPlayer", "0");
		this.config.put("customPlayerCmd", "");
		this.config.put("confirmWriteTags", "1");
		this.config.put("confirmChangeGain", "1");
		this.config.put("confirmRenameFiles", "1");
		this.config.put("confirmFolderGeneration", "1");

		this.config.put("id3Root", Constants.DEFAULT_DIR);
		this.config.put("id3Recursive", "1");
		this.config.put("id3autoAdd", "0");
		this.config.put("id3allChanged", "1");
		this.config.put("useMaxImageLength", "1");
		this.config.put("maxImageLength", "600");
		this.config.put("id3dataCollectors", "");
		this.config.put("coverCollectors", "");
		this.config.put("lyricsCollectors", "");
		this.config.put("id3OpenFolder", Constants.DEFAULT_DIR);
		this.config.put("id3OpenFile", Constants.DEFAULT_DIR);

		this.config.put("mp3gainAnalyseSelected", "0");
		this.config.put("mp3gainChangeSelected", "0");
		this.config.put("mp3gainTrackType", "1");
		this.config.put("mp3gainForce", "0");
		this.config.put("mp3gainRecursive", "1");
		this.config.put("mp3gainTarget", "89");
		this.config.put("mp3gainOpenFolder", Constants.DEFAULT_DIR);
		this.config.put("mp3gainOpenFile", Constants.DEFAULT_DIR);
		this.config.put("mp3gainPath", OS.isWindows() ? (Constants.PATH + "mp3gain/mp3gain.exe") : "mp3gain");

		this.config.put("renameFilenameSelected", "0");
		this.config.put("renameReplaceSpaceWithUnderscore", "0");
		this.config.put("renameReplaceUnderscoreWithSpace", "0");
		this.config.put("renameExtensionLowercase", "0");
		this.config.put("renameExtensionUppercase", "0");
		this.config.put("renameTrimFilename", "0");
		this.config.put("renameRecursive", "1");
		this.config.put("renameSource", "");
		this.config.put("renameTarget", "");
		this.config.put("renameReplace", "");
		this.config.put("renameReplaceWith", "");
		this.config.put("renameOpenFolder", Constants.DEFAULT_DIR);
		this.config.put("renameOpenFile", Constants.DEFAULT_DIR);

		this.config.put("generatorRecursive", "1");
		this.config.put("generatorFilenameSelected", "0");
		this.config.put("generatorRegex", "");
		this.config.put("generatorTargetFolder", "");
		this.config.put("generatorStructure", "");
		this.config.put("generatorOpenFolder", Constants.DEFAULT_DIR);
		this.config.put("generatorOpenFile", Constants.DEFAULT_DIR);

		if (!new File(Constants.PATH + Constants.CONFIG_PATH).exists()) {
			logger.log(Level.FINER, "Couldn't find config path. Default config will be loaded.");
			return;
		}

		ConfigReader.findParameters(new File(Constants.PATH + Constants.CONFIG_PATH), this.config, "=");

		if (!new File(this.config.get("id3Root")).exists())
			this.config.put("id3Root", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("id3OpenFolder")).exists())
			this.config.put("id3OpenFolder", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("id3OpenFile")).exists())
			this.config.put("id3OpenFile", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("mp3gainOpenFolder")).exists())
			this.config.put("mp3gainOpenFolder", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("mp3gainOpenFile")).exists())
			this.config.put("mp3gainOpenFile", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("renameOpenFolder")).exists())
			this.config.put("renameOpenFolder", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("renameOpenFile")).exists())
			this.config.put("renameOpenFile", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("generatorOpenFolder")).exists())
			this.config.put("generatorOpenFolder", Constants.DEFAULT_DIR);
		if (!new File(this.config.get("generatorOpenFile")).exists())
			this.config.put("generatorOpenFile", Constants.DEFAULT_DIR);
	}

	/**
	 * writes the config file (default settings.conf)
	 * 
	 * @throws IOException
	 *             thrown if config couldn't be read
	 */
	public void writeConfig() throws IOException {
		logger.log(Level.CONFIG, "Write config to: " + Constants.PATH + Constants.CONFIG_PATH);
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Constants.PATH + Constants.CONFIG_PATH)));

		bw.write("###########\n");
		bw.write("# General #\n");
		bw.write("###########\n");
		bw.write("# length and width of the window\n");
		bw.write("currWidth=" + this.config.get("currWidth") + "\n");
		bw.write("currHeight=" + this.config.get("currHeight") + "\n");
		bw.write("# current selected skin\n");
		bw.write("currSkin=" + this.config.get("currSkin") + "\n");
		bw.write("# the current language\n");
		bw.write("currLanguage=" + this.config.get("currLanguage") + "\n");
		bw.write("# the url to the update file\n");
		bw.write("updateURL=" + this.config.get("updateURL") + "\n");
		bw.write("# the path to the mask file\n");
		bw.write("maskFile=" + this.config.get("maskFile") + "\n");
		bw.write("# true if id3v1 tag should be removed\n");
		bw.write("deleteID3v1Tag=" + this.config.get("deleteID3v1Tag") + "\n");
		bw.write("# true if id3v1 tag should be set too\n");
		bw.write("setID3v1Tag=" + this.config.get("setID3v1Tag") + "\n");
		bw.write("# enable drag and drop\n");
		bw.write("enableDragAndDrop=" + this.config.get("enableDragAndDrop") + "\n");
		bw.write("# use custom player\n");
		bw.write("useCustomPlayer=" + this.config.get("useCustomPlayer") + "\n");
		bw.write("# custom player cmd\n");
		bw.write("customPlayerCmd=" + this.config.get("customPlayerCmd") + "\n");
		bw.write("# confirm before writing tags\n");
		bw.write("confirmWriteTags=" + this.config.get("confirmWriteTags") + "\n");
		bw.write("# confirm before rename files\n");
		bw.write("confirmRenameFiles=" + this.config.get("confirmRenameFiles") + "\n");
		bw.write("# confirm change gain\n");
		bw.write("confirmChangeGain=" + this.config.get("confirmChangeGain") + "\n");
		bw.write("# confirm before generate folders\n");
		bw.write("confirmFolderGeneration=" + this.config.get("confirmFolderGeneration") + "\n\n");
		
		bw.write("##############\n");
		bw.write("# ID3 Editor #\n");
		bw.write("##############\n");
		bw.write("# the Root directory\n");
		bw.write("id3Root=" + this.config.get("id3Root") + "\n");
		bw.write("# id3recursive checkbox\n");
		bw.write("id3Recursive=" + this.config.get("id3Recursive") + "\n");
		bw.write("# open file\n");
		bw.write("id3OpenFile=" + this.config.get("id3OpenFile") + "\n");
		bw.write("# open folder\n");
		bw.write("id3OpenFolder=" + this.config.get("id3OpenFolder") + "\n");
		bw.write("# autoadd checkbox\n");
		bw.write("id3autoAdd=" + this.config.get("id3autoAdd") + "\n");
		bw.write("# allChanged checkbox\n");
		bw.write("id3allChanged=" + this.config.get("id3allChanged") + "\n");
		bw.write("# use max image length\n");
		bw.write("useMaxImageLength=" + this.config.get("useMaxImageLength") + "\n");
		bw.write("# the max length of a cover\n");
		bw.write("maxImageLength=" + this.config.get("maxImageLength") + "\n");
		bw.write("# all used id3 data collectors\n");
		bw.write("id3dataCollectors=" + this.config.get("id3dataCollectors") + "\n");
		bw.write("# all used cover image collectors\n");
		bw.write("coverCollectors=" + this.config.get("coverCollectors") + "\n");
		bw.write("# all usec lyrics collectors\n");
		bw.write("lyricsCollectors=" + this.config.get("lyricsCollectors") + "\n\n");

		bw.write("############\n");
		bw.write("# MP3 Gain #\n");
		bw.write("############\n");
		bw.write("# is analyse gain selected\n");
		bw.write("mp3gainAnalyseSelected=" + this.config.get("mp3gainAnalyseSelected") + "\n");
		bw.write("# is change selected selected\n");
		bw.write("mp3gainChangeSelected=" + this.config.get("mp3gainChangeSelected") + "\n");
		bw.write("# is track type selected\n");
		bw.write("mp3gainTrackType=" + this.config.get("mp3gainTrackType") + "\n");
		bw.write("# is force analyse selected\n");
		bw.write("mp3gainForce=" + this.config.get("mp3gainForce") + "\n");
		bw.write("# is recursive selected selected\n");
		bw.write("mp3gainRecursive=" + this.config.get("mp3gainRecursive") + "\n");
		bw.write("# target volume\n");
		bw.write("mp3gainTarget=" + this.config.get("mp3gainTarget") + "\n");
		bw.write("# open file\n");
		bw.write("mp3gainOpenFile=" + this.config.get("mp3gainOpenFile") + "\n");
		bw.write("# open folder\n");
		bw.write("mp3gainOpenFolder=" + this.config.get("mp3gainOpenFolder") + "\n");
		bw.write("# path to the mp3gain application\n");
		bw.write("mp3gainPath=" + this.config.get("mp3gainPath") + "\n\n");

		bw.write("###############\n");
		bw.write("# Rename Tool #\n");
		bw.write("###############\n");
		bw.write("# get information from filename\n");
		bw.write("renameFilenameSelected=" + this.config.get("renameFilenameSelected") + "\n");
		bw.write("# replace space with underscore\n");
		bw.write("renameReplaceSpaceWithUnderscore=" + this.config.get("renameReplaceSpaceWithUnderscore") + "\n");
		bw.write("# replace underscore with space\n");
		bw.write("renameReplaceUnderscoreWithSpace=" + this.config.get("renameReplaceUnderscoreWithSpace") + "\n");
		bw.write("# extension lower case\n");
		bw.write("renameExtensionLowercase=" + this.config.get("renameExtensionLowercase") + "\n");
		bw.write("# extension upper case\n");
		bw.write("renameExtensionUppercase=" + this.config.get("renameExtensionUppercase") + "\n");
		bw.write("# trim filename\n");
		bw.write("renameTrimFilename=" + this.config.get("renameTrimFilename") + "\n");
		bw.write("# recursive selected\n");
		bw.write("renameRecursive=" + this.config.get("renameRecursive") + "\n");
		bw.write("# source field\n");
		bw.write("renameSource=" + this.config.get("renameSource") + "\n");
		bw.write("# target field\n");
		bw.write("renameTarget=" + this.config.get("renameTarget") + "\n");
		bw.write("# replace field\n");
		bw.write("renameReplace=" + this.config.get("renameReplace") + "\n");
		bw.write("# open file\n");
		bw.write("renameOpenFile=" + this.config.get("renameOpenFile") + "\n");
		bw.write("# open folder\n");
		bw.write("renameOpenFolder=" + this.config.get("renameOpenFolder") + "\n");
		bw.write("# replace with field\n");
		bw.write("renameReplaceWith=" + this.config.get("renameReplaceWith") + "\n\n");

		bw.write("####################\n");
		bw.write("# Folder Generator #\n");
		bw.write("####################\n");
		bw.write("# recursive selected\n");
		bw.write("generatorRecursive=" + this.config.get("generatorRecursive") + "\n");
		bw.write("# filename selected\n");
		bw.write("generatorFilenameSelected=" + this.config.get("generatorFilenameSelected") + "\n");
		bw.write("# used regex\n");
		bw.write("generatorRegex=" + this.config.get("generatorRegex") + "\n");
		bw.write("# target folder\n");
		bw.write("generatorTargetFolder=" + this.config.get("generatorTargetFolder") + "\n");
		bw.write("# the structure\n");
		bw.write("generatorStructure=" + this.config.get("generatorStructure") + "\n");
		bw.write("# open file\n");
		bw.write("generatorOpenFile=" + this.config.get("generatorOpenFile") + "\n");
		bw.write("# open folder\n");
		bw.write("generatorOpenFolder=" + this.config.get("generatorOpenFolder"));

		bw.close();
	}

	public void setId3OpenFolder(String folder) {
		this.config.put("id3OpenFolder", folder);
	}

	public String getID3OpenFolder() {
		return this.config.get("id3OpenFolder");
	}

	public void setId3OpenFile(String file) {
		this.config.put("id3OpenFile", file);
	}

	public String getID3OpenFile() {
		return this.config.get("id3OpenFile");
	}

	public void setMp3gainOpenFile(String file) {
		this.config.put("mp3gainOpenFile", file);
	}

	public String getMp3gainOpenFile() {
		return this.config.get("mp3gainOpenFile");
	}

	public void setMp3gainOpenFolder(String file) {
		this.config.put("mp3gainOpenFolder", file);
	}

	public String getMp3gainOpenFolder() {
		return this.config.get("mp3gainOpenFolder");
	}

	public void setRenameOpenFolder(String file) {
		this.config.put("renameOpenFolder", file);
	}

	public String getRenameOpenFolder() {
		return this.config.get("renameOpenFolder");
	}

	public void setRenameOpenFile(String file) {
		this.config.put("renameOpenFile", file);
	}

	public String getRenameOpenFile() {
		return this.config.get("renameOpenFile");
	}

	public void setGeneratorOpenFile(String file) {
		this.config.put("generatorOpenFile", file);
	}

	public String getGeneratorOpenFile() {
		return this.config.get("generatorOpenFile");
	}

	public void setGeneratorOpenFolder(String file) {
		this.config.put("generatorOpenFolder", file);
	}

	public String getGeneratorOpenFolder() {
		return this.config.get("generatorOpenFolder");
	}

	public void setCurrDimension(Dimension dim) {
		this.config.put("currWidth", Integer.toString((int) dim.getWidth()));
		this.config.put("currHeight", Integer.toString((int) dim.getHeight()));
	}

	public Dimension getCurrDimension() {
		return new Dimension(Integer.parseInt(this.config.get("currWidth")), Integer.parseInt(this.config.get("currHeight")));
	}

	public void setCurrentLanguage(Locale locale) {
		this.config.put("currLanguage", locale.toString());
	}

	public Locale getCurrentLanguage() {
		String[] tmp = this.config.get("currLanguage").split("_");
		return new Locale(tmp[0], tmp[1]);
	}

	public void setDeleteID3v1Tag(boolean rem) {
		this.config.put("deleteID3v1Tag", rem ? "1" : "0");
	}

	public boolean isDeleteID3v1Tag() {
		return this.config.get("deleteID3v1Tag").equals("1");
	}

	public void setSetID3v1Tag(boolean set) {
		this.config.put("setID3v1Tag", set ? "1" : "0");
	}

	public boolean isSetID3v1Tag() {
		return this.config.get("setID3v1Tag").equals("1");
	}

	public void setEnableDragAndDrop(boolean rem) {
		this.config.put("enableDragAndDrop", rem ? "1" : "0");
	}

	public boolean isDragAndDropEnabled() {
		return this.config.get("enableDragAndDrop").equals("1");
	}

	public void setMasksPath(String path) {
		this.config.put("maskFile", path);
	}

	public String getMasksPath() {
		return this.config.get("maskFile");
	}

	public void setcurrentSkin(String skin) {
		this.config.put("currSkin", skin);
	}

	public String getCurrentSkin() {
		return this.config.get("currSkin");
	}

	public String getUpdateURL() {
		return this.config.get("updateURL");
	}

	public void setUseCustomPlayer(boolean use) {
		this.config.put("useCustomPlayer", use ? "1" : "0");
	}

	public boolean isUseCustomPlayer() {
		return this.config.get("useCustomPlayer").equals("1");
	}

	public void setCustomPlayerCmd(String cmd) {
		this.config.put("customPlayerCmd", cmd);
	}

	public String getCustomPlayerCmd() {
		return this.config.get("customPlayerCmd");
	}

	public void setConfirmWriteTags(boolean use) {
		this.config.put("confirmWriteTags", use ? "1" : "0");
	}

	public boolean isConfirmWriteTags() {
		return this.config.get("confirmWriteTags").equals("1");
	}
	
	public void setConfirmChangeGain(boolean use) {
		this.config.put("confirmChangeGain", use ? "1" : "0");
	}

	public boolean isConfirmChangeGain() {
		return this.config.get("confirmChangeGain").equals("1");
	}

	public void setConfirmRenameFiles(boolean use) {
		this.config.put("confirmRenameFiles", use ? "1" : "0");
	}

	public boolean isConfirmRenameFiles() {
		return this.config.get("confirmRenameFiles").equals("1");
	}

	public void setConfirmFolderGeneration(boolean use) {
		this.config.put("confirmFolderGeneration", use ? "1" : "0");
	}

	public boolean isConfirmFolderGeneration() {
		return this.config.get("confirmFolderGeneration").equals("1");
	}

	public void setUseMaxImageLength(boolean use) {
		this.config.put("useMaxImageLength", use ? "1" : "0");
	}

	public boolean isUseMaxImageLength() {
		return this.config.get("useMaxImageLength").equals("1");
	}

	public int getMaxImageLength() {
		return Integer.parseInt(this.config.get("maxImageLength"));
	}

	public void setMaxImageLength(String length) {
		this.config.put("maxImageLength", length);
	}

	public void setID3DataCollectors(String collectors) {
		this.config.put("id3dataCollectors", collectors);
	}

	public List<String> getID3DataCollectors() {
		String c = this.config.get("id3dataCollectors");
		List<String> result = new ArrayList<>();
		if (c.trim().length() == 0)
			return result;

		String[] tmp = c.split(",");
		for (int i = 0; i < tmp.length; i++)
			result.add(tmp[i].trim());

		return result;
	}

	public void setCoverCollectors(String collectors) {
		this.config.put("coverCollectors", collectors);
	}

	public List<String> getCoverCollectors() {
		String c = this.config.get("coverCollectors");
		List<String> result = new ArrayList<>();
		if (c.trim().length() == 0)
			return result;

		String[] tmp = c.split(",");
		for (int i = 0; i < tmp.length; i++)
			result.add(tmp[i].trim());

		return result;
	}

	public void setLyricsCollectors(String collectors) {
		this.config.put("lyricsCollectors", collectors);
	}

	public List<String> getLyricsCollectors() {
		String c = this.config.get("lyricsCollectors");
		List<String> result = new ArrayList<>();
		if (c.trim().length() == 0)
			return result;

		String[] tmp = c.split(",");
		for (int i = 0; i < tmp.length; i++)
			result.add(tmp[i].trim());

		return result;
	}

	public void setID3Root(String root) {
		this.config.put("id3Root", root);
	}

	public String getID3Root() {
		return this.config.get("id3Root");
	}

	public void setID3Recursive(boolean rec) {
		this.config.put("id3Recursive", rec ? "1" : "0");
	}

	public boolean isID3Recursive() {
		return this.config.get("id3Recursive").equals("1");
	}

	public void setID3AutoAdd(Boolean aa) {
		this.config.put("id3autoAdd", aa ? "1" : "0");
	}

	public boolean isID3AutoAdd() {
		return this.config.get("id3autoAdd").equals("1");
	}

	public void setID3AllChanged(boolean ac) {
		this.config.put("id3allChanged", ac ? "1" : "0");
	}

	public boolean isID3AllChanged() {
		return this.config.get("id3allChanged").equals("1");
	}

	public void setMP3gainAnalyseSelected(boolean sel) {
		this.config.put("mp3gainAnalyseSelected", sel ? "1" : "0");
	}

	public boolean isMP3gainAnalyseSelected() {
		return this.config.get("mp3gainAnalyseSelected").equals("1");
	}

	public void setMP3gainChangeSelected(boolean sel) {
		this.config.put("mp3gainChangeSelected", sel ? "1" : "0");
	}

	public boolean isMP3gainChangeSelected() {
		return this.config.get("mp3gainChangeSelected").equals("1");
	}

	public void setMP3GainTrackType(boolean sel) {
		this.config.put("mp3gainTrackType", sel ? "1" : "0");
	}

	public boolean isMP3GainTrackType() {
		return this.config.get("mp3gainTrackType").equals("1");
	}

	public void setMP3GainForceSelected(boolean sel) {
		this.config.put("mp3gainForce", sel ? "1" : "0");
	}

	public boolean isMP3GainForceSelected() {
		return this.config.get("mp3gainForce").equals("1");
	}

	public void setMP3GainRecursiveSelected(boolean sel) {
		this.config.put("mp3gainRecursive", sel ? "1" : "0");
	}

	public boolean isMP3GainRecursiveSelected() {
		return this.config.get("mp3gainRecursive").equals("1");
	}

	public void setMP3GainTarget(String target) {
		this.config.put("mp3gainTarget", target);
	}

	public String getMP3GainTarget() {
		return this.config.get("mp3gainTarget");
	}

	public String getMP3GainPath() {
		return this.config.get("mp3gainPath");
	}

	public void setRenameFilenameSelected(boolean sel) {
		this.config.put("renameFilenameSelected", sel ? "1" : "0");
	}

	public boolean isRenameFilenameSelected() {
		return this.config.get("renameFilenameSelected").equals("1");
	}

	public void setRenameReplaceSpaceWithUnderscore(boolean sel) {
		this.config.put("renameReplaceSpaceWithUnderscore", sel ? "1" : "0");
	}

	public boolean isRenameSpaceWithUnderscoreSelected() {
		return this.config.get("renameReplaceSpaceWithUnderscore").equals("1");
	}

	public void setRenameReplaceUnderscoreWithSpace(boolean sel) {
		this.config.put("renameReplaceUnderscoreWithSpace", sel ? "1" : "0");
	}

	public boolean isRenameUnderscoreWithSpaceSelected() {
		return this.config.get("renameReplaceUnderscoreWithSpace").equals("1");
	}

	public void setRenameExtensionLowercase(boolean sel) {
		this.config.put("renameExtensionLowercase", sel ? "1" : "0");
	}

	public boolean isRenameExtensionLowercaseSelected() {
		return this.config.get("renameExtensionLowercase").equals("1");
	}

	public void setRenameExtensionUppercase(boolean sel) {
		this.config.put("renameExtensionUppercase", sel ? "1" : "0");
	}

	public boolean isRenameExtensionUppercaseSelected() {
		return this.config.get("renameExtensionUppercase").equals("1");
	}

	public void setRenameTrimFilename(boolean sel) {
		this.config.put("renameTrimFilename", sel ? "1" : "0");
	}

	public boolean isRenameTrimFilenameSelected() {
		return this.config.get("renameTrimFilename").equals("1");
	}

	public void setRenameRecursive(boolean sel) {
		this.config.put("renameRecursive", sel ? "1" : "0");
	}

	public boolean isRenameRecursiveSelected() {
		return this.config.get("renameRecursive").equals("1");
	}

	public void setRenameSource(String source) {
		this.config.put("renameSource", source);
	}

	public String getRenameSource() {
		return this.config.get("renameSource");
	}

	public void setRenameTarget(String target) {
		this.config.put("renameTarget", target);
	}

	public String getRenameTarget() {
		return this.config.get("renameTarget");
	}

	public void setRenameReplace(String rep) {
		this.config.put("renameReplace", rep);
	}

	public String getRenameReplace() {
		return this.config.get("renameReplace");
	}

	public void setRenameReplaceWith(String rep) {
		this.config.put("renameReplaceWith", rep);
	}

	public String getRenameReplaceWith() {
		return this.config.get("renameReplaceWith");
	}

	public void setGeneratorRecursive(boolean sel) {
		this.config.put("generatorRecursive", sel ? "1" : "0");
	}

	public boolean isGeneratorRecursiveSelected() {
		return this.config.get("generatorRecursive").equals("1");
	}

	public void setGeneratorFilenameSelected(boolean sel) {
		this.config.put("generatorFilenameSelected", sel ? "1" : "0");
	}

	public boolean isGeneratorFilenameSelected() {
		return this.config.get("generatorFilenameSelected").equals("1");
	}

	public void setGeneratorRegex(String r) {
		this.config.put("generatorRegex", r);
	}

	public String getGeneratorRegex() {
		return this.config.get("generatorRegex");
	}

	public void setGeneratorTargetFolder(String f) {
		this.config.put("generatorTargetFolder", f);
	}

	public String getGeneratorTargetFolder() {
		return this.config.get("generatorTargetFolder");
	}

	public void setGeneratorStructure(String s) {
		this.config.put("generatorStructure", s);
	}

	public String getGeneratorStructure() {
		return this.config.get("generatorStructure");
	}
}