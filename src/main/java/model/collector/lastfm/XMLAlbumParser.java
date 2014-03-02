package model.collector.lastfm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import model.collector.Album;
import model.collector.Track;
import model.regex.Regex;

public class XMLAlbumParser {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the album
	 */
	private Album album;

	// regex for dd www yyyy, dd:dd --> gets year
	/**
	 * the year regex
	 */
	private static Regex yearRegex = new Regex(".+ .+ (\\d+), .*", Pattern.CASE_INSENSITIVE);

	/**
	 * gets the album
	 * 
	 * @return the album
	 */
	public Album getAlbum() {
		return this.album;
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 *            url where the xml code can be found
	 * 
	 * @throws MalformedURLException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public XMLAlbumParser(String url) throws MalformedURLException, XMLStreamException, IOException {
		logger.log(Level.FINER, "parse Album xml. URL: " + url);

		album = new Album();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(new URL(url).openStream());

		extractAlbum(parser);
	}

	/**
	 * extracts the album
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractAlbum(XMLStreamReader parser) throws XMLStreamException {
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.START_ELEMENT: {
					// check status first
					if (parser.getLocalName().equalsIgnoreCase("lfm")) {
						String status = "failed";
						if (parser.getAttributeCount() > 0)
							status = parser.getAttributeValue(0);

						if (!status.equalsIgnoreCase("ok"))
							return;
					} else if (parser.getLocalName().equalsIgnoreCase("name")) {
						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.album.setName(parser.getText());
						logger.log(Level.FINER, "extract album name: " + this.album.getName());
					} else if (parser.getLocalName().equalsIgnoreCase("artist")) {
						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.album.setArtist(parser.getText());
						logger.log(Level.FINER, "extract artist name: " + this.album.getArtist());
					} else if (parser.getLocalName().equalsIgnoreCase("releasedate")) {
						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.album.setYear(parseYear(parser.getText()));
						logger.log(Level.FINER, "extract year: " + this.album.getYear());
					} else if (parser.getLocalName().equalsIgnoreCase("image")) {
						if (parser.getAttributeCount() > 0)
							this.album.setImageSize(parser.getAttributeValue(0));

						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.album.setImageURL(parser.getText());
						logger.log(Level.FINER, "extract image url: " + this.album.getImageURL() + " with size: " + this.album.getImageSize());
					} else if (parser.getLocalName().equalsIgnoreCase("track")) {
						extractTrack(parser);
					} else if (parser.getLocalName().equalsIgnoreCase("tag")) {
						extractTags(parser);
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
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
	 * extracts the tracks
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractTrack(XMLStreamReader parser) throws XMLStreamException {
		Track track = new Track();
		logger.log(Level.FINER, "create new Track.");
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.START_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("track")) {
						if (parser.getAttributeCount() > 0)
							track.setTrackNr(Integer.parseInt(parser.getAttributeValue(0)));

						logger.log(Level.FINER, "Track#: " + track.getTrackNr());
					} else if (parser.getLocalName().equalsIgnoreCase("name")) {
						parser.next();
						track.setTitle(parser.getText());

						logger.log(Level.FINER, "Track title: " + track.getTitle());
					} else if (parser.getLocalName().equalsIgnoreCase("artist")) {
						parser.next();
						parser.next();
						parser.next();

						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							track.setArtist(parser.getText());

						logger.log(Level.FINER, "Track artist: " + track.getArtist());
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("track")) {
						logger.log(Level.FINER, "adding Track");
						this.album.addTrack(track);
						return;
					}

					break;
				}
			}
			parser.next();
		}
	}

	/**
	 * extracts the tags
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractTags(XMLStreamReader parser) throws XMLStreamException {
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.START_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("name")) {
						parser.next();
						String tag = "";
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS) {
							tag = parser.getText();
							this.album.addTag(tag);
						}
						logger.log(Level.FINER, "added Tag: " + tag);
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("tag")) {
						return;
					}

					break;
				}
			}
			parser.next();
		}
	}

	/**
	 * parses the year using the defined rege
	 * 
	 * @param date
	 *            the date
	 * 
	 * @return the year as a string
	 */
	private String parseYear(String date) {
		if (yearRegex.matches(date))
			return yearRegex.getGroup(1);

		return "";
	}
}
