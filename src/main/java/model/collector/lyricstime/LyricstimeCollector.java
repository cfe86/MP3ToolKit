package model.collector.lyricstime;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import model.collector.Lyrics;
import model.collector.interfaces.ILyricsCollector;
import model.exception.CollectorException;
import model.structure.ID3TagData;
import model.util.Util;

public class LyricstimeCollector implements ILyricsCollector {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the song title
	 */
	private String title;

	/**
	 * the song artist
	 */
	private String artist;

	/**
	 * the lyrics
	 */
	private Lyrics lyrics;

	/**
	 * true if lyrics are found, else false
	 */
	private boolean isFound;

	/**
	 * the API url
	 */
	private String lyricsURL = "http://www.lyricstime.com/{0}-{1}-lyrics.html";

	/**
	 * Constructor
	 */
	public LyricstimeCollector() {
		this.title = null;
		this.artist = null;
		this.isFound = true;
		this.lyrics = new Lyrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#getCollectorName()
	 */
	@Override
	public String getCollectorName() {
		return "lyrics time";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#init()
	 */
	@Override
	public void init() throws CollectorException {
		try {
			if (this.title != null && this.artist != null && this.title.trim().length() != 0 && this.artist.trim().length() != 0) {
				HTMLParser p = new HTMLParser(this.lyricsURL.replace("{0}", this.artist).replace("{1}", this.title));
				this.lyrics = p.getLyrics();
				if (this.lyrics != null && this.lyrics.getLyrics().trim().length() > 0)
					this.isFound = true;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while getting id3 data:\n" + LogUtil.getStackTrace(e), e);
			throw new CollectorException("Error while parsing");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#isDataFound()
	 */
	@Override
	public boolean isDataFound() {
		return this.isFound;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ILyricsCollector#getLyrics()
	 */
	@Override
	public String getLyrics() {
		return lyrics.getLyrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ILyricsCollector#setData(model.structure.
	 * ID3TagData)
	 */
	@Override
	public void setData(ID3TagData data) {
		title = Util.stripExtraData(replaceSpecialCharacters(data.getTitle()));
		artist = Util.stripExtraData(replaceSpecialCharacters(data.getArtist()));
	}

	/**
	 * replaces special characters like Umlaute, space and so on
	 * 
	 * @param str
	 *            the given string
	 * 
	 * @return the modified string
	 */
	private String replaceSpecialCharacters(String str) {
		str = str.replace(" ", "-");
		str = str.replace("'", "-");
		str = str.replace("ä", "ae");
		str = str.replace("ö", "oe");
		str = str.replace("ü", "ue");
		str = str.replace("Ä", "Ae");
		str = str.replace("Ö", "Oe");
		str = str.replace("Ü", "Ue");

		return str;
	}
}