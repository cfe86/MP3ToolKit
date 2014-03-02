package model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import model.structure.UpdateData;
import model.util.FileUtil;
import config.Config;
import config.ConfigReader;
import config.Constants;

public class MainModel {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * downlaoded update data
	 */
	private UpdateData updateData;
	
	/**
	 * Constructor
	 */
	public MainModel() {

	}

	/**
	 * gets the update info from the update url
	 * 
	 * @throws IOException
	 *             thrown if update info is not available
	 */
	public void getUpdateInfo() throws IOException {
		logger.log(Level.FINER, "get last update from: " + Config.getInstance().getUpdateURL());

		URL myURL = new URL(Config.getInstance().getUpdateURL());

		URLConnection urlCon = myURL.openConnection();
		urlCon.connect();

		BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
		String line;
		String info = "";
		while ((line = br.readLine()) != null)
			info += line + "\n";

		String updateDate = ConfigReader.findParameter(info, "lastUpdate", "=");
		String updateLink = ConfigReader.findParameter(info, "updateLink", "=");
		String kb = ConfigReader.findParameter(info, "updateSizeInKb", "=");
		String updateLinkExe = ConfigReader.findParameter(info, "updateLinkExe", "=");
		String kbExe = ConfigReader.findParameter(info, "updateSizeInKbExe", "=");
		String showMsg = ConfigReader.findParameter(info, "showMessage", "=");
		String msg = ConfigReader.findParameter(info, "message", "=");

		this.updateData = new UpdateData(updateDate, updateLink, kb, updateLinkExe, kbExe, showMsg.equals("1"), msg);
	}

	/**
	 * checks if an update is available (update info needs to be downloaded
	 * first
	 * 
	 * @return true if update is available, else false
	 */
	public boolean isUpdateAvailable() {
		logger.log(Level.FINER, "check for update.");
		SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		try {
			Date serverDate = df.parse(this.updateData.getUpdateDate());
			Date currDate = df.parse(Constants.LAST_SOFTWARE_UPDATE);

			return serverDate.after(currDate);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Error while checking if update is available:\n" + LogUtil.getStackTrace(e), e);
			return false;
		}
	}

	/**
	 * updates the software
	 * 
	 * @param path
	 *            the absolute path to this software
	 * @param lang
	 *            the language which should the updater use
	 * 
	 * @throws IOException
	 *             thrown if the updater couldn't be found
	 */
	public void updateSoftware(String path, String lang) throws IOException {

		// args: softwarePath, kb, lang, url
		// exe file
		if (FileUtil.getFileExtension(path).equalsIgnoreCase("exe")) {
			logger.log(Level.FINER, "update software from " + this.updateData.getUpdateLinkExe());
			String cmd = "java -jar " + Constants.UPDATER_PATH + " \"" + path + "\" \"" + this.updateData.getSizeKBExe() + "\" \"" + lang + "\" \""
					+ this.updateData.getUpdateLinkExe() + "\" \"" + path + "\"";
			logger.log(Level.FINER, "call update cmd: " + cmd);
			Runtime.getRuntime().exec(cmd);
		}
		// jar file
		else { 
			logger.log(Level.FINER, "update software from " + this.updateData.getUpdateLink());
			String cmd = "java -jar " + Constants.UPDATER_PATH + " \"" + path + "\" \"" + this.updateData.getSizeKB() + "\" \"" + lang + "\" \"" + this.updateData.getUpdateLink()
					+ "\" \"java -jar " + path + "\"";
			logger.log(Level.FINER, "call update cmd: " + cmd);
			Runtime.getRuntime().exec(cmd);
		}
	}

	/**
	 * writes the updater to the HDD
	 * 
	 * @throws IOException
	 *             thrown if the updater couldn't be written successfully
	 */
	public void writeUpdater() throws IOException {
		InputStream stream;
		stream = FileUtil.class.getResourceAsStream("/" + Constants.UPDATER_PATH_IN_JAR);
		// if this fails try again using relativ paths and ClassLoader
		if (stream == null) {
			stream = ClassLoader.getSystemResourceAsStream(Constants.UPDATER_PATH_IN_JAR);
		}

		if (stream == null)
			throw new IOException("File not found.");

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		byte[] data = new byte[4096];

		int nRead;
		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		FileUtil.writeByteToFile(buffer.toByteArray(), Constants.UPDATER_PATH);
	}
}