package model.audio.interfaces;

import java.io.IOException;

import com.mpatric.mp3agic.NotSupportedException;

import model.exception.AudioFileException;

public interface IAudioFile {

	/*
	 * image MIME types
	 */
	public final static String MIME_GIF = "image/gif";
	public final static String MIME_JPEG = "image/jpeg";
	public final static String MIME_PNG = "image/png";
	public final static String MIME_SVG = "image/svg+xml";
	public final static String MIME_TIFF = "image/tiff";

	/**
	 * inits the Audio file
	 * 
	 * @param path
	 *            path to the file
	 * @param removeID3v1
	 *            true if id3v1 tag should be removed, only remove or add can be
	 *            true
	 * @param addId3v1
	 *            true if the id3v1 tag should be added additionally, only
	 *            remove or add can be true
	 * 
	 * @throws AudioFileException
	 *             thrown if file couldn't be read
	 */
	public void init(String path, boolean removeID3v1, boolean addId3v1) throws AudioFileException;

	/**
	 * returns true if all ID3Tag entries are empty or if this file does not
	 * have an ID3tag
	 * 
	 * @return true or false
	 */
	public boolean isID3TagEmpty();

	/**
	 * gets the number of frames of this file
	 * 
	 * @return the frame count
	 */
	public int getFrameCount();

	/**
	 * gets the name of the filepath (without path e.g. file/to/file.ext should
	 * return file.ext)
	 * 
	 * @return the filename
	 */
	public String getName();

	/**
	 * resets the audio file to its current state on the harddisk
	 * 
	 * @throws AudioFileException
	 *             thrown if audio file couldn't be reset
	 */
	public void resetAudioFile() throws AudioFileException;

	/**
	 * gets the id3 title
	 * 
	 * @return the title
	 */
	public String getTitle();

	/**
	 * sets the id3 title
	 * 
	 * @param title
	 *            the title
	 */
	public void setTitle(String title);

	/**
	 * gets the id3 artist
	 * 
	 * @return the artist
	 */
	public String getArtist();

	/**
	 * sets the id3 artist
	 * 
	 * @param artist
	 *            the artist
	 */
	public void setArtist(String artist);

	/**
	 * gets the id3 album artist
	 * 
	 * @return the album artist
	 */
	public String getAlbumArtist();

	/**
	 * sets the id3 album artist
	 * 
	 * @param artist
	 *            the album artist
	 */
	public void setAlbumArtist(String artist);

	/**
	 * gets the id3 album
	 * 
	 * @return the album
	 */
	public String getAlbum();

	/**
	 * sets the id3 album
	 * 
	 * @param album
	 *            the album
	 */
	public void setAlbum(String album);

	/**
	 * gets the id3 year
	 * 
	 * @return the year
	 */
	public String getYear();

	/**
	 * sets the id3 year
	 * 
	 * @param year
	 *            the year
	 */
	public void setYear(String year);

	/**
	 * gets the track in the form 01/12 (except something else is saved, check
	 * if it contains /)
	 * 
	 * @return the track in the form [currTrack]/[maxTracks]
	 */
	public String getTrack();

	/**
	 * sets the track in the form 01/12
	 * 
	 * @param track
	 *            the given track
	 */
	public void setTrack(String track);

	/**
	 * gets the current track (track is given in the form
	 * [currTrack]/[maxTracks])
	 * 
	 * @return the current track
	 */
	public String getCurrTrack();

	/**
	 * sets the current track
	 * 
	 * @param track
	 *            the current track
	 */
	public void setCurrTrack(String track);

	/**
	 * gets the max tracks (track is given in the form [currTrack]/[maxTracks])
	 * 
	 * @return the max tracks
	 */
	public String getMaxTrack();

	/**
	 * sets the max tracks
	 * 
	 * @param track
	 *            max tracks
	 */
	public void setMaxTrack(String track);

	/**
	 * gets id3 the comment
	 * 
	 * @return the comment
	 */
	public String getComment();

	/**
	 * sets the id3 comment
	 * 
	 * @param comment
	 *            the comment
	 */
	public void setComment(String comment);

	/**
	 * gets the id3 composer
	 * 
	 * @return the composer
	 */
	public String getComposer();

	/**
	 * sets the id3 composer
	 * 
	 * @param composer
	 *            the composer
	 */
	public void setComposer(String composer);

	/**
	 * gets the id3 publisher
	 * 
	 * @return the publisher
	 */
	public String getPublisher();

	/**
	 * sets the id3 publisher
	 * 
	 * @param publisher
	 *            the publisher
	 */
	public void setPublisher(String publisher);

	/**
	 * gets the id3 original artist
	 * 
	 * @return the original artist
	 */
	public String getOriginalArtist();

	/**
	 * sets the id3 original artist
	 * 
	 * @param artist
	 *            the original artist
	 */
	public void setOriginalArtist(String artist);

	/**
	 * gets the id3 copyright
	 * 
	 * @return the copyright
	 */
	public String getCopyright();

	/**
	 * sets the id3 copyright
	 * 
	 * @param copyright
	 *            the copyright
	 */
	public void setCopyright(String copyright);

	/**
	 * gets the id3 url
	 * 
	 * @return the url
	 */
	public String getURL();

	/**
	 * sets the id3 url
	 * 
	 * @param url
	 *            the url
	 */
	public void setURL(String url);

	/**
	 * gets the id3 encoder
	 * 
	 * @return the encoder
	 */
	public String getEncoder();

	/**
	 * sets the id3 encoder
	 * 
	 * @param encoder
	 *            the encoder
	 */
	public void setEncoder(String encoder);

	/**
	 * gets the CD in the form [currentCD]/[maxCD] (except something else is
	 * saved, check if it contains /)
	 * 
	 * @return the CD
	 */
	public String getCD();

	/**
	 * sets the id3 CD in the form [currentCD]/[maxCD] as a String
	 * 
	 * @param cd
	 *            CD tag
	 */
	public void setCD(String cd);

	/**
	 * gets the current id3 CD
	 * 
	 * @return the current CD
	 */
	public String getCurrCD();

	/**
	 * sets the current CD
	 * 
	 * @param cd
	 *            the current CD
	 */
	public void setCurrCD(String cd);

	/**
	 * gets the max CC
	 * 
	 * @return the max CD
	 */
	public String getMaxCD();

	/**
	 * sets the max CD
	 * 
	 * @param cd
	 *            the max CD
	 */
	public void setMaxCD(String cd);

	/**
	 * gets the id3 Genre ID
	 * 
	 * @return the genre ID
	 */
	public int getGenre();

	/**
	 * sets the id3 genre as ID and descrption to the file
	 * 
	 * @param genre
	 *            the genre ID
	 */
	public void setGenre(int genre);

	/**
	 * gets the genre Description depending on the genre ID
	 * 
	 * @return the genre description
	 */
	public String getGenreDescription();

	/**
	 * gets the Mime type of the image like image/jpeg
	 * 
	 * @return the mime type
	 */
	public String getAlbumImageMimeType();

	/**
	 * the format like jpeg or png
	 * 
	 * @return image format in lowercase
	 */
	public String getAlbumImageFormat();

	/**
	 * gets the album image in bytes
	 * 
	 * @return the album image
	 */
	public byte[] getAlbumImage();

	/**
	 * write the cover image for this audio file to to the harddisk
	 * 
	 * @param filepath
	 *            file where to save it
	 * @param addExtension
	 *            true if the extension should be added
	 * 
	 * @throws IOException
	 *             thrown if the file couldn't be read
	 * @throws IllegalArgumentException
	 *             thrown if the audio file has no image
	 */
	public void writeImage(String filepath, boolean addExtension) throws IOException, IllegalArgumentException;

	/**
	 * sets a new cover image to the audio file
	 * 
	 * @param filepath
	 *            path to the new image
	 * 
	 * @throws IOException
	 *             thrown if the image couldn't be found
	 */
	public void setImage(String filepath) throws IOException;

	/**
	 * sets the new cover image to the audio file
	 * 
	 * @param imgBytes
	 *            given image in bytes
	 * @param extension
	 *            extension of the image, e.g. png
	 */
	public void setImage(byte[] imgBytes, String extension);

	/**
	 * gets the id3 lyrics
	 * 
	 * @return the lyrics
	 */
	public String getLyrics();

	/**
	 * sets the id3 lyrics
	 * 
	 * @param lyrics
	 *            the lyrics
	 */
	public void setLyrics(String lyrics);

	/**
	 * gets the file path
	 * 
	 * @return the file path
	 */
	public String getFilePath();

	/**
	 * gets the bitrate
	 * 
	 * @return the bitrate
	 */
	public int getBitrate();

	/**
	 * gets the frequency
	 * 
	 * @return the frequency
	 */
	public int getFrequence();

	/**
	 * gets the mpeg layer e.g. iii (should only apply to mp3s)
	 * 
	 * @return the layer
	 */
	public String getLayer();

	/**
	 * gets the mode e.g. joint stereo
	 * 
	 * @return the mode
	 */
	public String getMode();

	/**
	 * gets the mpeg version e.g. 1 (should only apply to mp3s)
	 * 
	 * @return the version
	 */
	public String getVersion();

	/**
	 * true if something got changed, else false
	 * 
	 * @return true or false
	 */
	public boolean hasChanged();

	/**
	 * gets the audio length in seconds
	 * 
	 * @return the audio length
	 */
	public long getAudioLength();

	/**
	 * gets the file size in bytes
	 * 
	 * @return the file siez
	 */
	public long getFileSize();

	/**
	 * saves the modified audio file
	 * 
	 * @param filepath
	 *            path where to save the modified audio file
	 * 
	 * @throws NotSupportedException
	 *             thrown if an error occured while writing
	 * @throws IOException
	 *             thrown if file couldn't be written
	 */
	public void writeAudioFile(String filepath) throws NotSupportedException, IOException;

	/**
	 * saves all made changes to this audio files tag
	 * 
	 * @throws NotSupportedException
	 *             thrown if file couldn't be saved
	 */
	public void save() throws NotSupportedException, IOException;

	/**
	 * checks if the given file at the saved file path is writable
	 * 
	 * @return true if writable, else false
	 */
	public boolean isWriteable();
}