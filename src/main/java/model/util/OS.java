package model.util;

import java.io.IOException;

public class OS {

	/**
	 * gets the OS Name
	 * 
	 * @return the OS Name
	 */
	public static String getOSName() {
		return System.getProperty("os.name");
	}

	/**
	 * true if the OS is windows, else false
	 */
	public static boolean isWindows() {
		return getOSName().toLowerCase().contains("win");
	}

	/**
	 * true if the OS is Linux, else false
	 */
	public static boolean isLinux() {
		return getOSName().toLowerCase().contains("linux");
	}

	/**
	 * true if the OS is Mac OS X, else false
	 */
	public static boolean isMacOSX() {
		return getOSName().toLowerCase().contains("mac");
	}

	/**
	 * shuts the system down immediatly
	 * 
	 * @throws RuntimeException
	 */
	public static void shutdownSytem() throws RuntimeException {
		String shutdownCmd = "";

		if (isWindows())
			shutdownCmd = "shutdown.exe -s -t 0";
		else if (isLinux() || isMacOSX())
			shutdownCmd = "shutdown -h now";
		else
			throw new RuntimeException("System shutdown unsupported on this operating system.");

		try {
			Runtime.getRuntime().exec(shutdownCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
