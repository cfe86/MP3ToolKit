package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import logging.LogUtil;
import model.audio.interfaces.IAudioPlayer;
import model.audio.player.MP3Player;
import model.exception.AudioPlayerException;
import model.util.WindowUtils;
import view.subview.id3.AudioPlayerView;

public class AudioPlayerController extends Observable implements ActionListener, MouseListener, Observer {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the player window
	 */
	private AudioPlayerView window;

	/**
	 * the player
	 */
	private IAudioPlayer player;

	/**
	 * artist name
	 */
	private String name;

	/**
	 * song length
	 */
	private int length;

	/**
	 * Constructor
	 * 
	 * @param path
	 *            path to the file
	 * @param name
	 *            artist name
	 * @param length
	 *            song length
	 * @param maxFrames
	 *            frame count of the song
	 * 
	 * @throws AudioPlayerException
	 *             thrown if player couldn't be initialised
	 */
	public AudioPlayerController(String path, String name, int length, int maxFrames) throws AudioPlayerException {
		logger.log(Level.FINER, "opening audio file. path: " + path + " name: " + name + " length: " + length + " frames: " + maxFrames);
		this.name = name;
		this.length = length;
		this.player = new MP3Player();
		this.player.init(path, maxFrames, length);
	}

	/**
	 * creates the window
	 */
	public void createWindow() {
		window = new AudioPlayerView();
		window.init(name, length);
		window.setVisible(true);

		window.setActionListener(this);
		window.setMouseListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});

		startPressed();
	}

	/**
	 * closes the window
	 */
	public void closeWindow() {
		stopPressed();
		setChanged();
		notifyObservers();
		window.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object obj) {
		logger.log(Level.FINER, this.getClass().getName() + " got message from " + obs.getClass().getName());
		// comes from id3 window --> close player
		if (obs.getClass().getName().equals(controller.ID3TagController.class.getName())) {

			if (!(obj instanceof String))
				return;

			String msg = (String) obj;
			if (!msg.equals("closePlayer"))
				return;

			closeWindow();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("startB"))
			startPressed();
		else if (e.getActionCommand().equals("pauseB"))
			pausePressed();
		else if (e.getActionCommand().equals("stopB"))
			stopPressed();
	}

	/**
	 * stop button pressed
	 */
	private void stopPressed() {
		logger.log(Level.FINER, "stop button pressed. running: " + player.isRunning());
		try {
			if (player.isRunning()) {
				player.stopPlayer();
				window.stopTimer();
			}
		} catch (AudioPlayerException e) {
			logger.log(Level.SEVERE, "Error while stopping player:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("ErrorStop");
		}
	}

	/**
	 * pause button pressed
	 */
	private void pausePressed() {
		logger.log(Level.FINER, "pause button pressed. running: " + player.isRunning());
		try {
			if (player.isRunning()) {
				window.setPauseTimer(true);
				player.pause(window.getSecondCount());
			}
		} catch (AudioPlayerException e) {
			logger.log(Level.SEVERE, "Error while pausing player:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("ErrorPause");
		}
	}

	/**
	 * start button pressed
	 */
	private void startPressed() {
		logger.log(Level.FINER, "start button pressed. running: " + player.isRunning());

		// check if song is finished
		if (window.isTimerFinished())
			stopPressed();

		// check if player is running
		if (player.isRunning())
			return;

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (window.isPaused())
						window.setPauseTimer(false);
					else {
						window.stopTimer();
						window.startTimer();
					}

					player.play();
				} catch (AudioPlayerException e) {
					logger.log(Level.SEVERE, "Error while starting player:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("ErrorPlay");
				}
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		logger.log(Level.FINER, "mouse is released.");

		if (!this.player.isRunning()) {
			window.stopTimer();
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				int sec = window.getSliderTime();
				window.setPauseTimer(true);
				window.setTimer(sec);
				window.setPauseTimer(false);
				try {
					player.jumpTo(sec);
				} catch (AudioPlayerException e) {
					logger.log(Level.SEVERE, "Error while changing current time:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("ErrorSwitchTime");
				}
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}