package config;

import java.awt.image.BufferedImage;

import model.util.FileUtil;
import model.util.Graphics;
import model.util.PathUtil;

public class Constants {

	/**
	 * debugger on or off
	 */
	public final static boolean DEBUG = true;

	/**
	 * Path to the jar file if a jar, else path to working directory with an
	 * ending / This is neccessary because in some cases relative paths don't
	 * work
	 */
	private final static String JAR_PATH = FileUtil.getFilePath(PathUtil.getJarPath(Constants.class)) + "/";
	private final static String WORKSPACE_PATH = PathUtil.getWorkspacePath().replace("\\", "/");

	/**
	 * the path to the current directory
	 */
	public final static String PATH = PathUtil.isInJar(Constants.class) ? JAR_PATH : WORKSPACE_PATH;

	/**
	 * the version
	 */
	public final static String VERSION = "1.0";

	/**
	 * date of the last software update
	 */
	public final static String LAST_SOFTWARE_UPDATE = "Sun Mar 02 00:54:28 CET 2014";

	/**
	 * the url where the update file can be found
	 */
	public final static String UPDATE_FILE_URL = "https://docs.google.com/uc?authuser=0&id=0B_RbeehtjF99VVlpM2R3TGd0Nm8&export=download";

	/**
	 * relative path to the config path
	 */
	public final static String CONFIG_PATH = "settings.conf";

	/**
	 * the used default directory for chooser and so on
	 */
	public final static String DEFAULT_DIR = PATH; // System.getProperty("user.home");

	/**
	 * path of the updater inside of the jar
	 */
	public final static String UPDATER_PATH_IN_JAR = "data/Updater.jar";

	/**
	 * path where to write the updater jar file on the HDD
	 */
	public final static String UPDATER_PATH = JAR_PATH + "updater.jar";

	/**
	 * used right arrow image
	 */
	public final static BufferedImage RIGHT_ARROW = Graphics.readImageFromJarWoExc("view/images/id3tag/subwindows/arrowRight.png");

	/**
	 * used empty image
	 */
	public final static BufferedImage EMPTY = Graphics.readImageFromJarWoExc("view/images/general/empty.png");

	/**
	 * refresh delay before closing a progressbar
	 */
	public final static int REFRESH_DELAY = 500;

	/**
	 * refresh delay between each table entry is updated e.g. from mp3gain
	 */
	public final static int TABLE_REFRESH_DELAY = 200;

	/**
	 * all valid audio file extensions, not case sensitive
	 */
	public final static String[] VALID_AUDIO_EXTENSIONS = new String[] { "mp3" };

	/**
	 * all valid image extensions, not case sensitive
	 */
	public final static String[] VALID_IMAGE_EXTENSION = new String[] { "png", "jpg", "jpeg", "gif" };

	/**
	 * paypal donation url
	 */
	public final static String donateURL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=8FDUEPAE4FP24";

	/*
	 * Skin IDs
	 */
	public static final int LAF_ACRYL = 0;

	public static final int LAF_AERO = 1;

	public static final int LAF_ALUMINIUM = 2;

	public static final int LAF_BERNSTEIN = 3;

	public static final int LAF_FAST = 4;

	public static final int LAF_GRAPHITE = 5;

	public static final int LAF_HIFI = 6;

	public static final int LAF_LUNA = 7;

	public static final int LAF_MCWIN = 8;

	public static final int LAF_MINT = 9;

	public static final int LAF_NOIRE = 10;

	public static final int LAF_SMART = 11;

	public static final int LAF_TEXTURE = 12;

	/**
	 * skin names
	 */
	public static final String[] LAF_NAMES = new String[] { "Acryl", "Aero", "Aluminium", "Bernstein", "Fast", "Graphite", "HiFi", "Luna", "McWin", "Mint", "Noire", "Smart",
			"Texture" };
}