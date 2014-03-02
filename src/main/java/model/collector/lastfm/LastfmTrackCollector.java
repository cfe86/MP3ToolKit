package model.collector.lastfm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import logging.LogUtil;
import model.audio.Genres;
import model.collector.Album;
import model.collector.Track;
import model.collector.interfaces.IID3DataCollector;
import model.exception.CollectorException;
import model.structure.ID3TagData;
import model.util.Util;

public class LastfmTrackCollector implements IID3DataCollector {

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
	 * the album where this song is in
	 */
	private String album;

	/**
	 * the track
	 */
	private Track theTrack;

	/**
	 * the album
	 */
	private Album theAlbum;

	/**
	 * true if track data could be found, else false
	 */
	private boolean isFound;

	/**
	 * the API key
	 */
	private String apiKey = "92da8a4f1911c555fde3b4985e3682c0";

	/**
	 * the API track url
	 */
	private String trackURL = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo&api_key=" + apiKey + "&artist={0}&track={1}";

	/**
	 * the API album url
	 */
	private String albumURL = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" + apiKey + "&artist={0}&album={1}";

	/**
	 * Constructor
	 * 
	 * @param apiKey
	 *            the api key
	 */
	public LastfmTrackCollector(String apiKey) {
		this();
		this.apiKey = apiKey;
	}

	/**
	 * Constructor
	 */
	public LastfmTrackCollector() {
		this.title = null;
		this.artist = null;
		this.album = null;
		this.theTrack = new Track();
		this.theAlbum = new Album();
		this.isFound = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#getCollectorName()
	 */
	@Override
	public String getCollectorName() {
		return "Lastfm";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICollector#init()
	 */
	@Override
	public void init() throws CollectorException {
		try {
			// search for track information
			if (this.title != null && this.artist != null && this.title.trim().length() != 0 && this.artist.trim().length() != 0) {
				XMLTrackParser p = new XMLTrackParser(this.trackURL.replace("{0}", this.artist).replace("{1}", this.title));
				this.theTrack = p.getTrack();
				if ((this.album == null || this.album.trim().length() == 0) && this.theTrack.getAlbum() != null && this.theTrack.getAlbum().trim().length() != 0)
					this.album = replaceSpecialCharacters(this.theTrack.getAlbum());

				if (this.theTrack != null)
					this.isFound = true;
			}

			// search for album information
			if (this.artist != null && this.album != null && this.album.trim().length() != 0 && this.artist.trim().length() != 0) {
				String cache = (this.artist + this.album).toLowerCase();
				this.theAlbum = Cache.getAlbum(cache);
				logger.log(Level.FINER, "Album: " + this.album + " artist: " + this.artist + " cached: " + (this.theAlbum != null));
				if (this.theAlbum == null) {
					XMLAlbumParser p = new XMLAlbumParser(this.albumURL.replace("{0}", this.artist).replace("{1}", this.album));
					this.theAlbum = p.getAlbum();
					Cache.addAlbum(this.artist + this.album, this.theAlbum);
				}
			}
		} catch (XMLStreamException | IOException e) {
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

	/**
	 * replaces some special characters with its ascii codes
	 * 
	 * @param str
	 *            given string
	 * 
	 * @return modified string
	 */
	private String replaceSpecialCharacters(String str) {
		str = str.replace("_", "");
		str = str.replace(" ", "%20");
		str = str.replace("'", "%27");
		str = str.replace("&", "%26");

		return str;
	}

	/**
	 * pads the first number to the same digits as the 2nd num e.g. 5 and 10
	 * given, result would be 05
	 * 
	 * @param num1
	 *            first num, the num which will be padded
	 * @param num2
	 *            the second num
	 * 
	 * @return the padded number
	 */
	private String padNumbers(int num1, int num2) {
		String result = Integer.toString(num1);

		while (result.length() < Integer.toString(num2).length())
			result = "0" + result;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getTitle()
	 */
	@Override
	public String getTitle() {
		return this.theTrack.getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getArtist()
	 */
	@Override
	public String getArtist() {
		return this.theTrack.getArtist();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getAlbumArtist()
	 */
	@Override
	public String getAlbumArtist() {
		return this.theTrack.getArtist();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getAlbum()
	 */
	@Override
	public String getAlbum() {
		return this.theAlbum.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getYear()
	 */
	@Override
	public String getYear() {
		return this.theAlbum.getYear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getTrack()
	 */
	@Override
	public String getTrack() {
		if (this.theTrack.getTrackNr() == -1)
			return searchTrackNr();
		return searchTrackNr(padNumbers(this.theTrack.getTrackNr(), this.theAlbum.getMaxTracks()));
	}

	/**
	 * searches the track depending on the
	 * 
	 * @param num
	 *            given padded track number which will be the default value of
	 *            no number in the list of tracks can be found
	 * 
	 * @return the track number of this track in this album
	 */
	private String searchTrackNr(String num) {
		String result = searchTrackNr();
		if (num == null)
			return num;

		return result;
	}

	/**
	 * searches the album tracks for this song and returns the track number of
	 * this track in this album
	 * 
	 * @return the track number
	 */
	private String searchTrackNr() {
		for (Track track : this.theAlbum.getTracks()) {
			logger.log(Level.FINER, "search Track: " + this.theTrack.getTitle() + "(" + this.theTrack.getMatchLength() + ") current: " + track.getTitle() + "("
					+ track.getTitle().length() + ")");
			// to compare the tracks replace all special characters like ' and ´
			// to get better results
			if (removeSpecialcharacters(track.getTitle()).toLowerCase().trim().contains(removeSpecialcharacters(this.theTrack.getTitle()).toLowerCase().trim())) {
				// check if the current title is shorter than the old title, if
				// so -> new title, else not new title
				// if (removeSpecialcharacters(track.getTitle()).length() <=
				// this.theTrack.getMatchLength()) {
				this.theTrack.setTrackNr(track.getTrackNr());
				this.theTrack.setMatchLength(track.getTitle().length());
				this.theTrack.setTitle(track.getTitle());

				return padNumbers(this.theTrack.getTrackNr(), this.theAlbum.getMaxTracks());
				// }
			}
		}

		return null;
	}

	/**
	 * replaces some special characters with its ascii codes
	 * 
	 * @param str
	 *            given string
	 * 
	 * @return modified string
	 */
	private String removeSpecialcharacters(String str) {
		str = str.replace("'", "");
		str = str.replace("´", "");
		str = str.replace("`", "");
		str = str.replace(".", "");
		str = str.replace(",", "");

		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getMaxTracks()
	 */
	@Override
	public String getMaxTracks() {
		if (this.theAlbum.getMaxTracks() == 0)
			return null;

		return Integer.toString(this.theAlbum.getMaxTracks());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getCD()
	 */
	@Override
	public String getCD() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getMaxCD()
	 */
	@Override
	public String getMaxCD() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getGenre()
	 */
	@Override
	public int getGenre() {

		int genre = 12;
		for (String tag : this.theTrack.getTags()) {
			genre = Genres.getGenreLoose(tag);
			if (genre >= 0 && genre <= 148)
				return genre;
		}

		return genre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getComment()
	 */
	@Override
	public String getComment() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getComposer()
	 */
	@Override
	public String getComposer() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getOrigArtist()
	 */
	@Override
	public String getOrigArtist() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getCopyright()
	 */
	@Override
	public String getCopyright() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getURL()
	 */
	@Override
	public String getURL() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getEncoder()
	 */
	@Override
	public String getEncoder() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.IID3DataCollector#getAllAlbumTracks()
	 */
	@Override
	public List<Track> getAllAlbumTracks() {
		if (theAlbum == null)
			return new ArrayList<Track>();
		return this.theAlbum.getTracks();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * model.collector.interfaces.IID3DataCollector#setData(model.structure.
	 * ID3TagData)
	 */
	@Override
	public void setData(ID3TagData data) {
		this.title = Util.stripExtraData(replaceSpecialCharacters(data.getTitle()));
		this.artist = Util.stripExtraData(replaceSpecialCharacters(data.getArtist()));
		this.album = Util.stripExtraData(replaceSpecialCharacters(data.getAlbum()));
	}
}