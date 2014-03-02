package model.collector.lyricstime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import model.collector.Lyrics;
import model.regex.Regex;

public class HTMLParser {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the lyrics
	 */
	private Lyrics lyrics;

	/**
	 * the regex the get the lyrics out of the html code
	 */
	private final static Regex regex = new Regex(".*<div id=\"songlyrics\" style=.*?>(.*?)</div>.*", Pattern.DOTALL);

	/**
	 * gets the lyrics
	 * 
	 * @return the lyrics
	 */
	public Lyrics getLyrics() {
		return this.lyrics;
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 *            the url where to find the html code
	 * 
	 * @throws IOException
	 */
	public HTMLParser(String url) throws IOException {
		logger.log(Level.FINER, "parse html. URL: " + url);

		InputStream is = new URL(url).openConnection().getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}

		reader.close();

		String html = sb.toString().trim();
		lyrics = new Lyrics();

		extractLyrics(html);
	}

	/**
	 * extracts the lyrics by using the defined regex and replacing all html
	 * related tags
	 * 
	 * @param html
	 *            the html code of the website
	 */
	private void extractLyrics(String html) {
		if (regex.matches(html)) {
			String lyrics = regex.getGroup(1);
			lyrics = lyrics.replace("<p>", "");
			lyrics = lyrics.replace("</p>", "");
			lyrics = lyrics.replace("<br />", "");

			this.lyrics.setLyrics(lyrics.trim());
		}
	}
}