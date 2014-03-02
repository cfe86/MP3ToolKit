package model.audio.player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import logging.LogUtil;
import model.audio.interfaces.IAudioPlayer;
import model.exception.AudioPlayerException;

public class MP3Player implements IAudioPlayer {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the used player
	 */
	private AdvancedPlayer player;

	/**
	 * the file path
	 */
	private String path;

	/**
	 * the max frames
	 */
	private int maxFrames;

	/**
	 * the number frames per seconds
	 */
	private int framesPerSec;

	/**
	 * the current frame
	 */
	private int currentFrame;

	/**
	 * true if song is running, else false
	 */
	private boolean isRunning;

	/**
	 * Constructor
	 */
	public MP3Player() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#init(java.lang.String, int, int)
	 */
	@Override
	public void init(String path, int maxFrames, int length) throws AudioPlayerException {
		this.path = path;
		this.maxFrames = maxFrames;
		this.framesPerSec = maxFrames / length;
		this.currentFrame = 0;
		this.isRunning = false;
	}

	/**
	 * creates the new player
	 * 
	 * @throws AudioPlayerException
	 *             thrown if advanced player couldn't read inputstream
	 */
	private void createPlayer() throws AudioPlayerException {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
			this.player = new AdvancedPlayer(bis);
		} catch (IOException | JavaLayerException e) {
			logger.log(Level.SEVERE, "Error while init audio player:\n" + LogUtil.getStackTrace(e), e);
			throw new AudioPlayerException("Error while init file: " + this.path);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#play()
	 */
	@Override
	public void play() throws AudioPlayerException {
		try {
			createPlayer();
			this.isRunning = true;
			player.play(this.currentFrame, this.maxFrames);
		} catch (JavaLayerException e) {
			logger.log(Level.SEVERE, "Eror while playing file:\n" + LogUtil.getStackTrace(e), e);
			throw new AudioPlayerException("Error while playing file: " + this.path);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#pause(int)
	 */
	@Override
	public void pause(int sec) throws AudioPlayerException {
		if (player != null) {
			player.close();
			this.isRunning = false;
			this.currentFrame = sec * framesPerSec;
			logger.log(Level.FINER, "new current frame: " + this.currentFrame + " paused at " + sec + " seconds.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#stopPlayer()
	 */
	@Override
	public void stopPlayer() throws AudioPlayerException {
		if (player != null)
			player.close();

		this.isRunning = false;
		this.currentFrame = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#jumpTo(int)
	 */
	@Override
	public void jumpTo(int sec) throws AudioPlayerException {
		if (isRunning) {
			if (player != null) {
				player.close();
				this.currentFrame = sec * framesPerSec;
				logger.log(Level.FINER, "new current frame: " + this.currentFrame + " jump to sec " + sec);
				play();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.audio.interfaces.IAudioPlayer#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
}