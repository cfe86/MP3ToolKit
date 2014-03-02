package manager;

import model.audio.MP3;
import model.audio.interfaces.IAudioFile;
import model.audio.interfaces.IAudioPlayer;
import model.audio.player.MP3Player;
import model.exception.AudioFileException;

public class AudioManager {

	/**
	 * the instance
	 */
	private static AudioManager instance;

	/**
	 * gets the instance
	 * 
	 * @return the instance
	 */
	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();

		return instance;
	}

	/**
	 * Constructor
	 */
	private AudioManager() {

	}

	/**
	 * gets the default audio player
	 * 
	 * @return the player
	 */
	public IAudioPlayer getAudioPlayer() {
		return new MP3Player();
	}

	/**
	 * creates a new audio file
	 * 
	 * @param path
	 *            path to the audio file
	 * @param removeID3v1Tag
	 *            true if id3v1 tag should be removed, else false. can only be
	 *            true if addID3v1Tag is false
	 * @param addID3v1Tag
	 *            true if id3v1 tag should be written additional to id3v2 tag,
	 *            else false, can only be true if removeID3v1Tag is false
	 * 
	 * @return the new audio file
	 * 
	 * @throws AudioFileException
	 *             thrown if audio file coulnd't be created
	 */
	public IAudioFile getAudioFile(String path, boolean removeID3v1Tag, boolean addID3v1Tag) throws AudioFileException {
		MP3 audioFile = new MP3();
		audioFile.init(path, removeID3v1Tag, addID3v1Tag);

		return audioFile;
	}
}