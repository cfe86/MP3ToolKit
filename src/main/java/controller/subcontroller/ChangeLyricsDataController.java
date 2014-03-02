package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ChangeLyricsDataModel;
import model.structure.ID3LyricsData;
import model.util.Util;
import model.util.WindowUtils;
import view.subview.id3.ChangeLyricsDataView;

import com.cf.structures.DataDouble;

import controller.interfaces.ICmdChangeLyrics;

public class ChangeLyricsDataController extends Observable implements ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private ChangeLyricsDataModel model;

	/**
	 * the window
	 */
	private ChangeLyricsDataView window;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdChangeLyrics closeCommand;

	/**
	 * Constructor
	 */
	public ChangeLyricsDataController() {
		model = new ChangeLyricsDataModel();
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCommand(ICmdChangeLyrics cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 * 
	 * @param data
	 *            the data list which maps the old data to the new data
	 */
	public void createWindow(List<DataDouble<ID3LyricsData, ID3LyricsData>> data) {

		model.setIndex(-1);
		model.setLyrics(data);

		if (model.isDone())
			return;

		window = new ChangeLyricsDataView();

		window.init();

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow(null);
			}
		});

		Util.sleep(300);

		nextButtonPressed();
	}

	/**
	 * closes the window
	 * 
	 * @param data
	 *            the new lyrics which will be given to the id3 controller, null
	 *            if window is just closed
	 */
	public void closeWindow(List<ID3LyricsData> data) {
		window.dispose();
		closeCommand.call(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("nextTagB"))
			nextButtonPressed();
	}

	/**
	 * next button pressed
	 */
	private void nextButtonPressed() {
		// save data
		logger.log(Level.FINER, "next button pressed.");
		model.createNewLyrics(window.isLyricsChecked());

		model.incrementIndex();

		if (model.isDone()) {
			List<ID3LyricsData> data = model.getAllNewLyrics();
			model.setIndex(-1);
			closeWindow(data);
			return;
		}

		if (window.isRepeatChecked()) {
			nextButtonPressed();
			return;
		}

		window.setCounter(model.getIndex() + 1, model.getListSize());

		window.setCurrLyrics(model.getCurrLyrics().getLyrics());
		window.setNewLyrics(model.getNewLyrics().getLyrics());
		window.setArtist(model.getCurrLyrics().getArtist(), model.getCurrLyrics().getTitle());
	}
}