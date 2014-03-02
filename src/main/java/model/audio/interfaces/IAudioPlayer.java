package model.audio.interfaces;

import model.exception.AudioPlayerException;

public interface IAudioPlayer {

	/**
	 * its the audio player
	 * 
	 * @param path
	 *            path to the file which should be played
	 * @param maxFrames
	 *            max frames of the file
	 * @param length
	 *            the length in seconds of the file
	 * 
	 * @throws AudioPlayerException
	 *             thrown if player couldn't be initialized
	 */
	public void init(String path, int maxFrames, int length) throws AudioPlayerException;

	/**
	 * plays the file or resumes of paused bfore
	 * 
	 * @throws AudioPlayerException
	 *             thrown if something went wrong
	 */
	public void play() throws AudioPlayerException;

	/**
	 * pauses the current file at the given seconds
	 * 
	 * @param sec
	 *            given seconds
	 * 
	 * @throws AudioPlayerException
	 *             thrown if something went wrong
	 */
	public void pause(int sec) throws AudioPlayerException;

	/**
	 * stops player and resets the file
	 * 
	 * @throws AudioPlayerException
	 *             thrown if something went wrong
	 */
	public void stopPlayer() throws AudioPlayerException;

	/**
	 * jumps to the given seconds
	 * 
	 * @param sec
	 *            given seconds
	 * 
	 * @throws AudioPlayerException
	 *             thrown if something went wrong
	 */
	public void jumpTo(int sec) throws AudioPlayerException;

	/**
	 * true ifthe song is running, else false
	 * 
	 * @return true or false
	 */
	public boolean isRunning();
}