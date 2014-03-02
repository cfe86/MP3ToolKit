package model.collector.lastfm;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import model.collector.Album;
import model.collector.interfaces.ICoverArtCollector;
import model.exception.CollectorException;
import model.structure.ID3TagData;
import model.util.Graphics;
import model.util.Util;

public class LastfmCoverArtCollector implements ICoverArtCollector {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the artist
	 */
	private String artist;

	/**
	 * the album name
	 */
	private String album;

	/**
	 * the album
	 */
	private Album theAlbum;

	/**
	 * true if the album is found, else false
	 */
	private boolean isFound;

	/**
	 * the API key
	 */
	private String apiKey = "92da8a4f1911c555fde3b4985e3682c0";

	/**
	 * the API URL
	 */
	private String albumURL = "http://ws.audioscrobbler.com/2.0/?method=album.getinfo&api_key=" + apiKey + "&artist={0}&album={1}";

	/**
	 * Constructor
	 * 
	 * @param apiKey
	 *            the api key
	 */
	public LastfmCoverArtCollector(String apiKey) {
		this();
		this.apiKey = apiKey;
	}

	/**
	 * Constructor
	 */
	public LastfmCoverArtCollector() {
		this.artist = null;
		this.album = null;
		this.isFound = false;
		this.theAlbum = new Album();
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
			if (this.artist != null && this.album != null && this.album.trim().length() != 0) {
				String cache = (this.artist + this.album).toLowerCase();
				this.theAlbum = Cache.getAlbum(cache);
				logger.log(Level.FINER, "Album: " + this.album + " artist: " + this.artist + " cached: " + (this.theAlbum != null) + " hasImage: "
						+ ((this.theAlbum != null) ? this.theAlbum.isHasImage() : "not cached"));
				if (this.theAlbum == null) {
					XMLAlbumParser p = new XMLAlbumParser(this.albumURL.replace("{0}", this.artist).replace("{1}", this.album));
					this.theAlbum = p.getAlbum();
					Cache.addAlbum(this.artist + this.album, this.theAlbum);
					if (this.album != null)
						this.isFound = true;
				} else
					this.isFound = true;
			}

		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICoverArtCollector#getImageAsBytes()
	 */
	@Override
	public byte[] getImageAsBytes() {
		try {
			if (!this.theAlbum.isHasImage()) {
				byte[] bytes = Graphics.getImageFromURLasByte(this.theAlbum.getImageURL());
				this.theAlbum.setImage(bytes);
				this.theAlbum.setHasImage(true);
				Cache.addAlbum(this.artist + this.album, this.theAlbum);
				logger.log(Level.FINER,
						"add Image with " + this.theAlbum.getImage().length + " bytes to album: " + this.theAlbum.getName() + " from: " + this.theAlbum.getImageURL());
			}

			return this.theAlbum.getImage();
		} catch (IOException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICoverArtCollector#getImage()
	 */
	@Override
	public BufferedImage getImage() {
		try {
			if (!this.theAlbum.isHasImage()) {
				byte[] bytes = Graphics.getImageFromURLasByte(this.theAlbum.getImageURL());
				this.theAlbum.setImage(bytes);
				this.theAlbum.setHasImage(true);
				Cache.addAlbum(this.artist + this.album, this.theAlbum);
				logger.log(Level.FINER,
						"add Image with " + this.theAlbum.getImage().length + " bytes to album: " + this.theAlbum.getName() + " from: " + this.theAlbum.getImageURL());
			}

			return Graphics.getImageFromBytes(this.theAlbum.getImage());
		} catch (IOException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICoverArtCollector#getExtension()
	 */
	@Override
	public String getExtension() {

		String url = this.theAlbum.getImageURL();
		String[] tmp = url.split("\\.");

		if (tmp.length < 1)
			return null;

		return tmp[tmp.length - 1].toLowerCase().replace("jpg", "jpeg");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.collector.interfaces.ICoverArtCollector#id3ImageType()
	 */
	@Override
	public int id3ImageType() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * model.collector.interfaces.ICoverArtCollector#setData(model.structure
	 * .ID3TagData)
	 */
	@Override
	public void setData(ID3TagData data) {
		this.artist = Util.stripExtraData(replaceSpecialCharacters(data.getArtist()));
		this.album = Util.stripExtraData(replaceSpecialCharacters(data.getAlbum()));
	}
}