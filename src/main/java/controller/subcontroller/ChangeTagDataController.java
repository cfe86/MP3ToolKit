package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ChangeTagDataModel;
import model.structure.ID3TagData;
import model.util.Util;
import model.util.WindowUtils;
import view.subview.id3.ChangeTagDataView;

import com.cf.structures.DataDouble;

import controller.interfaces.ICmdChangeID3Data;

public class ChangeTagDataController extends Observable implements ActionListener, MouseListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private ChangeTagDataModel model;

	/**
	 * the window
	 */
	private ChangeTagDataView window;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdChangeID3Data closeCommand;

	/**
	 * Constructor
	 */
	public ChangeTagDataController() {
		model = new ChangeTagDataModel();
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCmd(ICmdChangeID3Data cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 * 
	 * @param data
	 *            data list which maps the old data to the new data
	 */
	public void createWindow(List<DataDouble<ID3TagData, ID3TagData>> data) {
		model.setCurrIndex(-1);
		model.setAudioFiles(data);

		if (model.isDone())
			return;

		window = new ChangeTagDataView();

		window.init();

		window.setVisible(true);

		window.setActionListener(this);
		window.setMouseListener(this);

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
	 *            the new id3 data which will be given to the id3 controller,
	 *            null if window is just closed
	 */
	public void closeWindow(List<ID3TagData> data) {
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
		else if (e.getActionCommand().equals("selectAllChB"))
			selectAllPressed();
	}

	/**
	 * next button pressed
	 */
	private void nextButtonPressed() {
		// save data
		logger.log(Level.FINER, "next button pressed.");
		model.createNewID3TagData(window.getChangedData());

		model.incrementIndex();

		if (model.isDone()) {
			List<ID3TagData> data = model.getNewAudioFiles();
			model.setCurrIndex(-1);
			closeWindow(data);
			return;
		}

		if (window.isRepeatSelected()) {
			nextButtonPressed();
			return;
		}

		window.setCounter(model.getCurrIndex() + 1, model.getListSize());
		window.setCurrentData(model.getCurrID3TagData());
		window.setNewData(model.getNewID3TagData());
		window.setCheckBoxes(model.getChanged());

		window.fixSize();
	}

	/**
	 * select all checkbox pressed
	 */
	private void selectAllPressed() {
		logger.log(Level.FINER, "Select all selected: " + window.isSelectAll());

		if (window.isSelectAll())
			window.setCheckBoxes(true);
		else
			window.setCheckBoxes(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		logger.log(Level.FINER, "Help button pressed.");
		window.openHelpDialog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// unused
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// unused
	}
}