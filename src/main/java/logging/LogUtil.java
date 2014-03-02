package logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LogUtil {

	/**
	 * gets the stacktrace of the given exception
	 * 
	 * @param e
	 *            given exception
	 * 
	 * @return the exception stacktrace as a string
	 */
	public static String getStackTrace(Exception e) {
		StringBuffer result = new StringBuffer();

		StackTraceElement[] ele = e.getStackTrace();
		result.append(e.toString() + "\n");
		for (int i = 0; i < ele.length; i++)
			result.append(ele[i].toString() + "\n");

		return result.toString();
	}

	/**
	 * inits logging
	 * 
	 * @throws SecurityException
	 *             thrown if logger can not be used
	 * @throws IOException
	 *             thrown if log.properties not found
	 */
	public static void initLogging() throws SecurityException, IOException {
		// URL url = Dummy.class.getResource("log.properties");
		// FileInputStream fis = new FileInputStream(url.getFile());
		InputStream is = null;
		is = LogUtil.class.getResourceAsStream("/data/log.properties");
		if (is == null) {
			is = ClassLoader.getSystemResourceAsStream("data/log.properties");
		}

		LogManager.getLogManager().readConfiguration(is);
	}

	/**
	 * inits logging
	 * 
	 * @param inBrowser
	 *            true if used in a browser and not an IDE
	 * 
	 * @throws SecurityException
	 *             thrown if logger is not available
	 * @throws IOException
	 *             thrown if log.properties couldn't be read
	 */
	public static void initLogging(boolean inBrowser) throws SecurityException, IOException {
		if (!inBrowser)
			initLogging();
		else {
			LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.FINEST);
		}
	}

	/**
	 * disables logging
	 */
	public static void disableLogging() {
		LogManager.getLogManager().reset();
		LogManager.getLogManager().getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME).setLevel(Level.OFF);
	}
}