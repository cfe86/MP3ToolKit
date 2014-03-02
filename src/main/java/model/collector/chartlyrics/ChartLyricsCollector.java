package model.collector.chartlyrics;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import logging.LogUtil;
import model.collector.Lyrics;
import model.collector.interfaces.ILyricsCollector;
import model.exception.CollectorException;
import model.structure.ID3TagData;
import model.util.Util;

public class ChartLyricsCollector implements ILyricsCollector {

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
	 * true if lyrics found, else false
	 */
	private boolean isFound;

	/**
	 * the API url
	 */
	private String lyricsURL = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist={0}&song={1}";

	/**
	 * Constructor
	 */
	public ChartLyricsCollector() {
		this.title = null;
		this.artist = null;

		this.lyrics = new Lyrics();
		this.isFound = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#getCollectorName()
	 */
	@Override
	public String getCollectorName() {
		return "Chart Lyrics";
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
				XMLLyricsParser p = new XMLLyricsParser(lyricsURL.replace("{0}", this.artist).replace("{1}", this.title));
				this.lyrics = p.getLyrics();
				if (this.lyrics != null && this.lyrics.getLyrics().trim().length() > 0)
					this.isFound = true;
			}
		} catch (XMLStreamException | IOException e) {
			logger.log(Level.SEVERE, "Error while getting lyrics:\n" + LogUtil.getStackTrace(e), e);
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
		this.title = Util.stripExtraData(replaceSpecialCharacters(data.getTitle()));
		this.artist = Util.stripExtraData(replaceSpecialCharacters(data.getArtist()));
	}

	/**
	 * replaces some special characters with their ascii code
	 * 
	 * @param str
	 *            the string
	 * 
	 * @return the modified string
	 */
	private String replaceSpecialCharacters(String str) {
		str = str.replace("_", "");
		str = str.replace(" ", "%20");
		str = str.replace("'", "%27");
		str = str.replace("&", "%26");

		return str;
	}
}