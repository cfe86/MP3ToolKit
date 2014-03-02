package logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
	 */
	@Override
	public String format(LogRecord rec) {

		// dont print awt and swing messages
		if (rec.getSourceClassName().contains("java.awt") || rec.getSourceClassName().contains("javax.swing.") || rec.getSourceClassName().contains("sun.awt.")
				|| rec.getSourceClassName().contains("sun.net."))
			return "";

		String time = formateTime(rec.getMillis(), "MM-dd-yyyy, kk:mm:ss,SSS");
		String type = "DEBUG";
		if (rec.getLevel().toString().equalsIgnoreCase("SEVERE"))
			type = "ERROR";
		else if (rec.getLevel().toString().equalsIgnoreCase("WARNING"))
			type = "EXCEPTION";
		else if (rec.getLevel().toString().equalsIgnoreCase("INFO"))
			type = "INFO";
		else if (rec.getLevel().toString().equalsIgnoreCase("CONFIG"))
			type = "CONFIG";

		return time + " " + type + " " + rec.getLoggerName() + " - " + rec.getMessage() + "\n";
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