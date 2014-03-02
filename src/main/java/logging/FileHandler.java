package logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FileHandler extends Handler {

	/**
	 * the writer
	 */
	private BufferedWriter br;

	/**
	 * Constructor
	 */
	public FileHandler() {
		try {
			String time = formateTime(System.currentTimeMillis(), "MM-dd-yyyy_kk-mm-ss");

			String filename = "log_" + time + ".log";

			br = new BufferedWriter(new FileWriter(new File(filename)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#close()
	 */
	@Override
	public void close() throws SecurityException {
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#flush()
	 */
	@Override
	public void flush() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
	 */
	@Override
	public void publish(LogRecord rec) {
		if (getFormatter() == null)
			setFormatter(new LogFormatter());

		String message = getFormatter().format(rec);
		try {
			br.append(message);
			br.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * formats the time using the given format string
	 * 
	 * @param time
	 *            given time
	 * @param format
	 *            given format string
	 * 
	 * @return formatted time
	 */
	private String formateTime(long time, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(new Date(time));
	}
}