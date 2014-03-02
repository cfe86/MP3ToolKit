package model.collector.chartlyrics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import model.collector.Lyrics;

public class XMLLyricsParser {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the lyrics
	 */
	private Lyrics lyrics;

	/**
	 * the lyrics text buffer
	 */
	private StringBuffer lyricsBuffer;

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
	 *            the url where to find the xml code
	 * 
	 * @throws MalformedURLException
	 * @throws XMLStreamException
	 * @throws IOException
	 * @throws SocketException
	 */
	public XMLLyricsParser(String url) throws MalformedURLException, XMLStreamException, IOException, SocketException {
		logger.log(Level.FINER, "parse track xml. URL: " + url);

		this.lyrics = new Lyrics();
		this.lyricsBuffer = new StringBuffer();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(new URL(url).openStream());

		extract(parser);
	}

	/**
	 * extracs the lyrics
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extract(XMLStreamReader parser) throws XMLStreamException {
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.START_ELEMENT: {

					if (parser.getLocalName().equalsIgnoreCase("LyricSong")) {
						parser.next();

						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.lyrics.setTitle(parser.getText());
					} else if (parser.getLocalName().equalsIgnoreCase("LyricArtist")) {
						parser.next();

						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.lyrics.setArtist(parser.getText());
					} else if (parser.getLocalName().equalsIgnoreCase("Lyric")) {
						extractLyrics(parser);
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("GetLyricResult")) {
						parser.close();
						return;
					}

					break;
				}
				case XMLStreamConstants.END_DOCUMENT: {
					logger.log(Level.FINER, "Document finished");
					parser.close();
					return;
				}
			}

			parser.next();
		}
	}

	/**
	 * extracts the lyrics
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractLyrics(XMLStreamReader parser) throws XMLStreamException {
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.CHARACTERS: {
					lyricsBuffer.append(parser.getText());
					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("Lyric")) {
						this.lyrics.setLyrics(lyricsBuffer.toString());
						return;
					}
				}
			}

			parser.next();
		}
	}
}