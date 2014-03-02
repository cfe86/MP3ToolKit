package model.collector.lastfm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import model.collector.Track;

public class XMLTrackParser {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the track
	 */
	private Track track;

	/**
	 * gets the track
	 * 
	 * @return the track
	 */
	public Track getTrack() {
		return this.track;
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 *            the url where the xml code can be found
	 * 
	 * @throws MalformedURLException
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public XMLTrackParser(String url) throws MalformedURLException, XMLStreamException, IOException {
		logger.log(Level.FINER, "parse track xml. URL: " + url);

		this.track = new Track();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(new URL(url).openStream());

		extractTrack(parser);
	}

	/**
	 * extracts the track
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractTrack(XMLStreamReader parser) throws XMLStreamException {
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
							this.track.setTitle(parser.getText());
						logger.log(Level.FINER, "extract title: " + this.track.getTitle());
					} else if (parser.getLocalName().equalsIgnoreCase("artist")) {
						extractArtist(parser);
					} else if (parser.getLocalName().equalsIgnoreCase("album")) {
						if (parser.getAttributeCount() > 0)
							this.track.setTrackNr(Integer.parseInt(parser.getAttributeValue(0)));

						logger.log(Level.FINER, "extract track#: " + this.track.getTrackNr());
						extractAlbum(parser);
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
	 * extracts the artist
	 * 
	 * @param parser
	 *            the parser
	 * 
	 * @throws XMLStreamException
	 */
	private void extractArtist(XMLStreamReader parser) throws XMLStreamException {
		while (parser.hasNext()) {
			switch (parser.getEventType()) {
				case XMLStreamConstants.START_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("name")) {
						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.track.setArtist(parser.getText());
						logger.log(Level.FINER, "extract artist name: " + this.track.getArtist());
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("artist")) {
						return;
					}

					break;
				}
			}
			parser.next();
		}
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
					if (parser.getLocalName().equalsIgnoreCase("artist")) {
						parser.next();

						// artist doesnt match -> no album
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS) {
							if (!parser.getText().equalsIgnoreCase(this.track.getArtist())) {
								this.track.setTrackNr(-1);
								return;
							}
						}
					} else if (parser.getLocalName().equalsIgnoreCase("title")) {
						parser.next();
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							this.track.setAlbum(parser.getText());
						logger.log(Level.FINER, "extract album name: " + this.track.getAlbum());
					}

					break;
				}
				case XMLStreamConstants.END_ELEMENT: {
					if (parser.getLocalName().equalsIgnoreCase("album")) {
						return;
					}

					break;
				}
			}
			parser.next();
		}
	}

	/**
	 * extracts the tag
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
						if (parser.getEventType() == XMLStreamConstants.CHARACTERS)
							tag = parser.getText();
						this.track.addTag(tag);
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
}