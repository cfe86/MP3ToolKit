package model.audio;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import model.audio.interfaces.IAudioFile;
import model.exception.AudioFileException;
import model.util.FileUtil;

import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class MP3 implements IAudioFile {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the internal mp3 file
	 */
	private Mp3File mp3;

	/**
	 * the file path
	 */
	private String path;

	/**
	 * the temporary file
	 */
	private String tmpPath;

	/**
	 * true if something changed, else false
	 */
	private boolean changed;

	/**
	 * true if this file has an ID3Tag, else false
	 */
	private boolean hasID3Tag;

	/**
	 * true if also the ID3v1 tag should be added
	 */
	private boolean addId3v1Tag;

	/**
	 * Constructor
	 */
	public MP3() {
		this.hasID3Tag = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#isID3TagEmpty()
	 */
	@Override
	public boolean isID3TagEmpty() {
		return !this.hasID3Tag
				|| (getTitle().length() == 0 && getArtist().length() == 0 && getAlbumArtist().length() == 0 && getAlbum().length() == 0 && getYear().length() == 0
						&& getCurrTrack().length() == 0 && getMaxTrack().length() == 0 && getCurrCD().length() == 0 && getCurrTrack().length() == 0 && getComment().length() == 0
						&& getComposer().length() == 0 && getOriginalArtist().length() == 0 && getCopyright().length() == 0 && getURL().length() == 0 && getEncoder().length() == 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#init(java.lang.String, boolean,
	 * boolean)
	 */
	@Override
	public void init(String path, boolean removeID3v1, boolean addId3v1) throws AudioFileException {
		try {
			if (path.contains("\\"))
				path = path.replace("/", "\\");
			this.mp3 = new Mp3File(path);
			this.path = path;
			this.changed = false;
			this.addId3v1Tag = true;

			if (removeID3v1 && this.mp3.hasId3v1Tag())
				this.mp3.removeId3v1Tag();

			if (this.addId3v1Tag && !this.mp3.hasId3v1Tag())
				this.mp3.setId3v1Tag(new ID3v1Tag());

			if (!this.mp3.hasId3v2Tag()) {
				this.mp3.setId3v2Tag(new ID3v24Tag());
				this.hasID3Tag = false;
				logger.log(Level.FINER, "mp3 has not ID3v2 tag, generate one. successful: " + this.mp3.hasId3v2Tag());
			}
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			logger.log(Level.SEVERE, "Error while init Audio file:\n" + LogUtil.getStackTrace(e), e);
			throw new AudioFileException("Error while init Audio file.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getFrameCount()
	 */
	@Override
	public int getFrameCount() {
		return this.mp3.getFrameCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getName()
	 */
	@Override
	public String getName() {
		return FileUtil.getFileName(this.path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#resetAudioFile()
	 */
	@Override
	public void resetAudioFile() throws AudioFileException {
		try {
			this.mp3 = new Mp3File(path);
			this.changed = false;
		} catch (UnsupportedTagException | InvalidDataException | IOException e) {
			logger.log(Level.SEVERE, "Error while reset Audio file:\n" + LogUtil.getStackTrace(e), e);
			throw new AudioFileException("Error while reset Audio file.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getTitle()
	 */
	@Override
	public String getTitle() {
		return mp3.getId3v2Tag().getTitle() == null ? "" : mp3.getId3v2Tag().getTitle().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		changed = true;
		if (title == null || title.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_TITLE);
		else
			this.mp3.getId3v2Tag().setTitle(title);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setTitle(title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getArtist()
	 */
	@Override
	public String getArtist() {
		return mp3.getId3v2Tag().getArtist() == null ? "" : mp3.getId3v2Tag().getArtist().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setArtist(java.lang.String)
	 */
	@Override
	public void setArtist(String artist) {
		changed = true;
		if (artist == null || artist.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_ARTIST);
		else
			this.mp3.getId3v2Tag().setArtist(artist);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setArtist(artist);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getAlbumArtist()
	 */
	@Override
	public String getAlbumArtist() {
		return mp3.getId3v2Tag().getAlbumArtist() == null ? "" : mp3.getId3v2Tag().getAlbumArtist().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setAlbumArtist(java.lang.String)
	 */
	@Override
	public void setAlbumArtist(String artist) {
		changed = true;
		if (artist == null || artist.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_ALBUM_ARTIST);
		else
			this.mp3.getId3v2Tag().setAlbumArtist(artist);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getAlbum()
	 */
	@Override
	public String getAlbum() {
		return mp3.getId3v2Tag().getAlbum() == null ? "" : mp3.getId3v2Tag().getAlbum().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setAlbum(java.lang.String)
	 */
	@Override
	public void setAlbum(String album) {
		changed = true;
		if (album == null || album.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_ALBUM);
		else
			this.mp3.getId3v2Tag().setAlbum(album);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setAlbum(album);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getYear()
	 */
	@Override
	public String getYear() {
		return mp3.getId3v2Tag().getYear() == null ? "" : mp3.getId3v2Tag().getYear().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setYear(java.lang.String)
	 */
	@Override
	public void setYear(String year) {
		changed = true;
		if (year == null || year.length() == 0)
		{
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_YEAR);
			this.mp3.getId3v2Tag().clearFrameSet(ID3v24Tag.ID_YEAR);
		}
		else
			this.mp3.getId3v2Tag().setYear(year);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setYear(year);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getTrack()
	 */
	@Override
	public String getTrack() {
		return mp3.getId3v2Tag().getTrack() == null ? "" : mp3.getId3v2Tag().getTrack().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setTrack(java.lang.String)
	 */
	@Override
	public void setTrack(String track) {
		changed = true;
		if (track == null || track.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_TRACK);
		else
			this.mp3.getId3v2Tag().setTrack(track);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setTrack(track);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getCurrTrack()
	 */
	@Override
	public String getCurrTrack() {

		if (mp3.getId3v2Tag().getTrack() == null || mp3.getId3v2Tag().getTrack().length() == 0)
			return "";

		if (!mp3.getId3v2Tag().getTrack().contains("/"))
			return mp3.getId3v2Tag().getTrack();

		if (mp3.getId3v2Tag().getTrack().startsWith("/"))
			return "";

		String[] tmp = mp3.getId3v2Tag().getTrack().split("/");
		return tmp[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setCurrTrack(java.lang.String)
	 */
	@Override
	public void setCurrTrack(String track) {
		String maxTrack = getMaxTrack();
		if (track.length() == 0 && maxTrack.length() == 0)
			setTrack(null);
		else
			setTrack(padNumber(track, maxTrack) + (maxTrack.length() == 0 ? "" : "/") + maxTrack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setMaxTrack(java.lang.String)
	 */
	@Override
	public void setMaxTrack(String track) {
		String currTrack = getCurrTrack();
		if (track.length() == 0 && currTrack.length() == 0)
			setTrack(null);
		else
			setTrack(currTrack + (track.length() == 0 ? "" : "/") + track);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getMaxTrack()
	 */
	@Override
	public String getMaxTrack() {
		if (mp3.getId3v2Tag().getTrack() == null || mp3.getId3v2Tag().getTrack().length() == 0)
			return "";

		if (!mp3.getId3v2Tag().getTrack().contains("/"))
			return "";

		if (mp3.getId3v2Tag().getTrack().endsWith("/"))
			return "";

		String[] tmp = mp3.getId3v2Tag().getTrack().split("/");
		if (tmp.length < 2)
			return "";

		return tmp[1];
	}

	/**
	 * pads the given number so that they have the same digits e.g. 5 and 10 are
	 * given, so 05 will be returned
	 * 
	 * @param smaller
	 *            the smaller number which should be padded
	 * @param bigger
	 *            the bigger number
	 * 
	 * @return the padded number
	 */
	private String padNumber(String smaller, String bigger) {
		String result = smaller;

		while (result.length() < bigger.length())
			result = "0" + result;

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getComment()
	 */
	@Override
	public String getComment() {
		return mp3.getId3v2Tag().getComment() == null ? "" : mp3.getId3v2Tag().getComment().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setComment(java.lang.String)
	 */
	@Override
	public void setComment(String comment) {
		changed = true;
		if (comment == null || comment.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_COMMENT);
		else
			this.mp3.getId3v2Tag().setComment(comment);

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setComment(comment);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getComposer()
	 */
	@Override
	public String getComposer() {
		return mp3.getId3v2Tag().getComposer() == null ? "" : mp3.getId3v2Tag().getComposer().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setComposer(java.lang.String)
	 */
	@Override
	public void setComposer(String composer) {
		changed = true;
		if (composer == null || composer.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_COMPOSER);
		else
			this.mp3.getId3v2Tag().setComposer(composer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getPublisher()
	 */
	@Override
	public String getPublisher() {
		return mp3.getId3v2Tag().getPublisher() == null ? "" : mp3.getId3v2Tag().getPublisher().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setPublisher(java.lang.String)
	 */
	@Override
	public void setPublisher(String publisher) {
		changed = true;
		if (publisher == null || publisher.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_PUBLISHER);
		else
			this.mp3.getId3v2Tag().setPublisher(publisher);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getOriginalArtist()
	 */
	@Override
	public String getOriginalArtist() {
		return mp3.getId3v2Tag().getOriginalArtist() == null ? "" : mp3.getId3v2Tag().getOriginalArtist().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * model.audio.interfaces.IAudioFile#setOriginalArtist(java.lang.String)
	 */
	@Override
	public void setOriginalArtist(String artist) {
		changed = true;
		if (artist == null || artist.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_ORIGINAL_ARTIST);
		else
			this.mp3.getId3v2Tag().setOriginalArtist(artist);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getCopyright()
	 */
	@Override
	public String getCopyright() {
		return mp3.getId3v2Tag().getCopyright() == null ? "" : mp3.getId3v2Tag().getCopyright().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setCopyright(java.lang.String)
	 */
	@Override
	public void setCopyright(String copyright) {
		changed = true;
		if (copyright == null || copyright.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_COPYRIGHT);
		else
			this.mp3.getId3v2Tag().setCopyright(copyright);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getURL()
	 */
	@Override
	public String getURL() {
		return mp3.getId3v2Tag().getUrl() == null ? "" : mp3.getId3v2Tag().getUrl().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setURL(java.lang.String)
	 */
	@Override
	public void setURL(String url) {
		changed = true;
		if (url == null || url.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_URL);
		else
			this.mp3.getId3v2Tag().setUrl(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getEncoder()
	 */
	@Override
	public String getEncoder() {
		return mp3.getId3v2Tag().getEncoder() == null ? "" : mp3.getId3v2Tag().getEncoder().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setEncoder(java.lang.String)
	 */
	@Override
	public void setEncoder(String encoder) {
		changed = true;
		if (encoder == null || encoder.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_ENCODER);
		else
			this.mp3.getId3v2Tag().setEncoder(encoder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getGenre()
	 */
	@Override
	public int getGenre() {
		return mp3.getId3v2Tag().getGenre();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setGenre(int)
	 */
	@Override
	public void setGenre(int genre) {
		changed = true;
		mp3.getId3v2Tag().setGenre(genre);
		mp3.getId3v2Tag().setGenreDescription(Genres.getGenre(genre));

		if (this.addId3v1Tag)
			this.mp3.getId3v1Tag().setGenre(genre);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getGenreDescription()
	 */
	@Override
	public String getGenreDescription() {
		return Genres.getGenre(mp3.getId3v2Tag().getGenre());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getAlbumImageMimeType()
	 */
	@Override
	public String getAlbumImageMimeType() {
		return mp3.getId3v2Tag().getAlbumImageMimeType() == null ? "" : mp3.getId3v2Tag().getAlbumImageMimeType().trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getAlbumImageFormat()
	 */
	@Override
	public String getAlbumImageFormat() {
		return mp3.getId3v2Tag().getAlbumImageMimeType() == null ? "" : mp3.getId3v2Tag().getAlbumImageMimeType().split("/")[1].toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#getAlbumImage()
	 */
	@Override
	public byte[] getAlbumImage() {
		return mp3.getId3v2Tag().getAlbumImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#writeImage(java.lang.String,
	 * boolean)
	 */
	@Override
	public void writeImage(String filepath, boolean addExtension) throws IOException, IllegalArgumentException {
		if (addExtension) {
			if (filepath.endsWith("."))
				filepath = filepath.substring(0, filepath.length() - 2);

			filepath += "." + getAlbumImageFormat();
		}

		if (getAlbumImage() == null)
			throw new IllegalArgumentException("no Image available.");

		FileUtil.writeByteToFile(getAlbumImage(), filepath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setImage(java.lang.String)
	 */
	@Override
	public void setImage(String filepath) throws IOException {
		changed = true;
		if (filepath == null || filepath.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_IMAGE);
		else {
			String mime = "image/" + FileUtil.getFileExtension(filepath);
			this.mp3.getId3v2Tag().setAlbumImage(FileUtil.readFileInBytes(filepath), mime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#setImage(byte[], java.lang.String)
	 */
	@Override
	public void setImage(byte[] imgBytes, String extension) {
		changed = true;
		if (imgBytes == null || imgBytes.length == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_IMAGE);
		else {
			String mime = "image/" + extension;
			this.mp3.getId3v2Tag().setAlbumImage(imgBytes, mime);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#writeAudioFile(java.lang.String)
	 */
	@Override
	public void writeAudioFile(String filepath) throws NotSupportedException, IOException {
		this.mp3.save(filepath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#save()
	 */
	@Override
	public void save() throws NotSupportedException {
		this.tmpPath = findTmpPath();
		logger.log(Level.FINER, "save " + this.path + " to " + this.tmpPath);
		try {
			writeAudioFile(tmpPath);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while saving audio file:\n" + LogUtil.getStackTrace(e), e);
			boolean del = new File(this.tmpPath).delete();
			logger.log(Level.SEVERE, "deleted tmp file: " + del);
			return;
		}

		boolean del = new File(this.path).delete();
		boolean ren = new File(this.tmpPath).renameTo(new File(this.path));
		logger.log(Level.FINER, "deleted " + this.path + "(" + del + ") and renamed " + tmpPath + "(" + ren + ")");
		this.changed = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioFile#isWriteable()
	 */
	@Override
	public boolean isWriteable() {
		boolean write = new File(this.path).canWrite();
		logger.log(Level.FINER, "is writable: " + write);
		return write;
	}

	/**
	 * finds the temporary path by adding "tmp" until the name is free
	 * 
	 * @return the temporary name
	 */
	private String findTmpPath() {
		String ext = FileUtil.getFileExtension(this.path);
		String result = FileUtil.getFilePath(this.path) + "/" + FileUtil.getFileNameWithoutExtension(this.path) + "tmp";

		while (new File(result + "." + ext).exists())
			result += "p";

		return result + "." + ext;
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getCurrCD()
	 */
	@Override
	public String getCurrCD() {
		if (mp3.getId3v2Tag().getPartOfSet() == null || mp3.getId3v2Tag().getPartOfSet().length() == 0)
			return "";

		if (!mp3.getId3v2Tag().getPartOfSet().contains("/"))
			return mp3.getId3v2Tag().getPartOfSet();

		if (mp3.getId3v2Tag().getPartOfSet().startsWith("/"))
			return "";

		String[] tmp = mp3.getId3v2Tag().getPartOfSet().split("/");
		return tmp[0];
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getMaxCD()
	 */
	@Override
	public String getMaxCD() {
		if (mp3.getId3v2Tag().getPartOfSet() == null || mp3.getId3v2Tag().getPartOfSet().length() == 0)
			return "";

		if (!mp3.getId3v2Tag().getPartOfSet().contains("/"))
			return "";

		if (mp3.getId3v2Tag().getPartOfSet().endsWith("/"))
			return "";

		String[] tmp = mp3.getId3v2Tag().getPartOfSet().split("/");
		if (tmp.length < 2)
			return "";

		return tmp[1];
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getCD()
	 */
	@Override
	public String getCD() {
		return mp3.getId3v2Tag().getPartOfSet() == null ? "" : mp3.getId3v2Tag().getPartOfSet();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#setCD(java.lang.String)
	 */
	@Override
	public void setCD(String cd) {
		changed = true;
		if (cd == null || cd.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_PART_OF_SET);
		else
			this.mp3.getId3v2Tag().setPartOfSet(cd);
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#setCurrCD(java.lang.String)
	 */
	@Override
	public void setCurrCD(String cd) {
		String maxCD = getMaxCD().trim();
		if (cd.length() == 0 && maxCD.length() == 0)
			setCD(null);
		else
			setCD(cd + (maxCD.length() == 0 ? "" : "/") + maxCD);
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#setMaxCD(java.lang.String)
	 */
	@Override
	public void setMaxCD(String cd) {
		String currCD = getCurrCD().trim();
		if (currCD.length() == 0 && cd.length() == 0)
			setCD(null);
		else
			setCD(currCD + (cd.length() == 0 ? "" : "/") + cd);
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getLyrics()
	 */
	@Override
	public String getLyrics() {
		return mp3.getId3v2Tag().getAsyncLyrics() == null ? "" : mp3.getId3v2Tag().getAsyncLyrics();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#setLyrics(java.lang.String)
	 */
	@Override
	public void setLyrics(String lyrics) {
		changed = true;
		if (lyrics == null || lyrics.length() == 0)
			this.mp3.getId3v2Tag().clearFrameSet(AbstractID3v2Tag.ID_LYRICS_ASYNC);
		else
			mp3.getId3v2Tag().setAsyncLyrics(lyrics);
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getFilePath()
	 */
	@Override
	public String getFilePath() {
		return this.path;
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getBitrate()
	 */
	@Override
	public int getBitrate() {
		return this.mp3.getBitrate();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getFrequence()
	 */
	@Override
	public int getFrequence() {
		return this.mp3.getSampleRate();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getLayer()
	 */
	@Override
	public String getLayer() {
		return this.mp3.getLayer();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getMode()
	 */
	@Override
	public String getMode() {
		return this.mp3.getChannelMode();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.mp3.getVersion();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#hasChanged()
	 */
	@Override
	public boolean hasChanged() {
		return this.changed;
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getAudioLength()
	 */
	@Override
	public long getAudioLength() {
		return this.mp3.getLengthInSeconds();
	}

	/*
	 * (non-Javadoc)
	 * @see model.audio.interfaces.IAudioFile#getFileSize()
	 */
	@Override
	public long getFileSize() {
		return new File(this.path).length();
	}
}