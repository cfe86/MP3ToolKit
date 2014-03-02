package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Constants;
import controller.interfaces.ICommand;
import logging.LogUtil;
import main.MP3ToolKitMain;
import manager.CollectorManager;
import model.PreferencesModel;
import model.util.ChooserUtil;
import model.util.PathUtil;
import model.util.WindowUtils;
import view.subview.PreferencesView;

public class PreferencesController extends Observable implements ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the window
	 */
	private PreferencesView window;

	/**
	 * the model
	 */
	private PreferencesModel model;

	/**
	 * the close command which is called after closing the window
	 */
	private ICommand closeCommand;

	/**
	 * Constructor
	 */
	public PreferencesController() {
		this.model = new PreferencesModel();
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCommand(ICommand cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 */
	public void createWindow() {
		window = new PreferencesView();
		window.init();
		window.setVisible(true);

		window.setActionListener(this);
		logger.log(Level.FINER, PathUtil.getJarPath(MP3ToolKitMain.class));

		window.setCollectors(model.getCollectors(CollectorManager.ID3DATA_COLLECTOR), CollectorManager.ID3DATA_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.COVER_COLLECTOR), CollectorManager.COVER_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.LYRICS_COLLECTOR), CollectorManager.LYRICS_COLLECTOR);

		try {
			model.readMasks();
			window.setMasks(model.getMasks());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading mask files:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("ReadMasksError");
		}

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
	}

	/**
	 * closes the window
	 */
	public void closeWindow() {
		window.dispose();
		closeCommand.call();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("openPlayerB"))
			openPlayerButtonPressed();
		else if (e.getActionCommand().equals("id3dataUpB"))
			id3dataUpBPressed();
		else if (e.getActionCommand().equals("id3dataDownB"))
			id3dataDownBPressed();
		else if (e.getActionCommand().equals("id3dataEnableB"))
			id3dataEnableBPressed();
		else if (e.getActionCommand().equals("coverUpB"))
			coverUpBPressed();
		else if (e.getActionCommand().equals("coverDownB"))
			coverDownBPressed();
		else if (e.getActionCommand().equals("coverEnableB"))
			coverEnableBPressed();
		else if (e.getActionCommand().equals("lyricsUpB"))
			lyricsUpBPressed();
		else if (e.getActionCommand().equals("lyricsDownB"))
			lyricsDownBPressed();
		else if (e.getActionCommand().equals("lyricsEnableB"))
			lyricsEnableBPressed();
		else if (e.getActionCommand().equals("addMaskB"))
			addMaskButtonPressed();
		else if (e.getActionCommand().equals("deleteMaskB"))
			deleteMasksButtonPressed();
		else if (e.getActionCommand().equals("okB"))
			okBPressed();
		else if (e.getActionCommand().equals("cancelB"))
			cancelBPressed();
		else if (e.getActionCommand().equals("setId3v1TagChB"))
			setId3v1TagPressed();
		else if (e.getActionCommand().equals("deleteId3v1TagChB"))
			deleteId3v1TagPressed();
	}

	/**
	 * opens the filechooser for the custom player
	 */
	private void openPlayerButtonPressed() {
		logger.log(Level.FINER, "open player button pressed.");
		File f = ChooserUtil.openFile(window, null, new File(Constants.DEFAULT_DIR));

		if (f == null)
			return;

		window.setPlayerCmd(f.getAbsolutePath());
	}

	/**
	 * id3 up button pressed
	 */
	private void id3dataUpBPressed() {
		logger.log(Level.FINER, "track up button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.ID3DATA_COLLECTOR);

		if (index == -1)
			return;

		int newIndex = model.shiftUp(index, CollectorManager.ID3DATA_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.ID3DATA_COLLECTOR), CollectorManager.ID3DATA_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.ID3DATA_COLLECTOR);
	}

	/**
	 * id3 down button pressed
	 */
	private void id3dataDownBPressed() {
		logger.log(Level.FINER, "track down button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.ID3DATA_COLLECTOR);
		if (index == -1)
			return;

		int newIndex = model.shiftDown(index, CollectorManager.ID3DATA_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.ID3DATA_COLLECTOR), CollectorManager.ID3DATA_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.ID3DATA_COLLECTOR);
	}

	/**
	 * id3 dis/enable button pressed
	 */
	private void id3dataEnableBPressed() {
		logger.log(Level.FINER, "track enable button pressed.");

		int index = window.getSelectedCollectorIndex(CollectorManager.ID3DATA_COLLECTOR);

		if (index == -1)
			return;

		setCollectorEnabled(index, CollectorManager.ID3DATA_COLLECTOR);
	}

	/**
	 * cover up button pressed
	 */
	private void coverUpBPressed() {
		logger.log(Level.FINER, "album up button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.COVER_COLLECTOR);

		if (index == -1)
			return;

		int newIndex = model.shiftUp(index, CollectorManager.COVER_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.COVER_COLLECTOR), CollectorManager.COVER_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.COVER_COLLECTOR);
	}

	/**
	 * cover down button pressed
	 */
	private void coverDownBPressed() {
		logger.log(Level.FINER, "album down button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.COVER_COLLECTOR);

		if (index == -1)
			return;

		int newIndex = model.shiftDown(index, CollectorManager.COVER_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.COVER_COLLECTOR), CollectorManager.COVER_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.COVER_COLLECTOR);
	}

	/**
	 * dis/enable button pressed
	 */
	private void coverEnableBPressed() {
		logger.log(Level.FINER, "album enable button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.COVER_COLLECTOR);

		if (index == -1)
			return;

		setCollectorEnabled(index, CollectorManager.COVER_COLLECTOR);
	}

	/**
	 * lyrics up button pressed
	 */
	private void lyricsUpBPressed() {
		logger.log(Level.FINER, "lyrics up button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.LYRICS_COLLECTOR);

		if (index == -1)
			return;

		int newIndex = model.shiftUp(index, CollectorManager.LYRICS_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.LYRICS_COLLECTOR), CollectorManager.LYRICS_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.LYRICS_COLLECTOR);
	}

	/**
	 * lyrics down button pressed
	 */
	private void lyricsDownBPressed() {
		logger.log(Level.FINER, "lyrics down button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.LYRICS_COLLECTOR);

		if (index == -1)
			return;

		int newIndex = model.shiftDown(index, CollectorManager.LYRICS_COLLECTOR);
		window.setCollectors(model.getCollectors(CollectorManager.LYRICS_COLLECTOR), CollectorManager.LYRICS_COLLECTOR);
		window.setSelectedCollectorIndex(newIndex, CollectorManager.LYRICS_COLLECTOR);
	}

	/**
	 * dis/enable button pressed
	 */
	private void lyricsEnableBPressed() {
		logger.log(Level.FINER, "lyrics enable button pressed.");
		int index = window.getSelectedCollectorIndex(CollectorManager.LYRICS_COLLECTOR);

		if (index == -1)
			return;

		setCollectorEnabled(index, CollectorManager.LYRICS_COLLECTOR);
	}

	/**
	 * add mask button pressed
	 */
	private void addMaskButtonPressed() {
		logger.log(Level.FINER, "add mask pressed.");

		String mask = window.getMaskTF();
		if (model.addMask(mask)) {
			window.setMasks(model.getMasks());
			window.setMaskTF("");
		}
	}

	/**
	 * delete mask button pressed
	 */
	private void deleteMasksButtonPressed() {
		logger.log(Level.FINER, "delete mask pressed.");

		int index = window.getSelectedMaskIndex();

		if (index == -1)
			return;

		model.deleteMask(index);

		window.setMasks(model.getMasks());
	}

	/**
	 * ok button pressed
	 */
	private void okBPressed() {
		logger.log(Level.FINER, "ok button pressed.");

		try {
			window.saveConfig();
			model.saveConfig();
			closeWindow();
		} catch (IOException e) {
			logger.log(Level.FINER, "Error while writing masks file:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("WriteMasksError");
		}
	}

	/**
	 * cancel button pressed
	 */
	private void cancelBPressed() {
		logger.log(Level.FINER, "cancel button pressed.");
		closeWindow();
	}

	/**
	 * set id3v1 tag button pressed
	 */
	private void setId3v1TagPressed() {
		logger.log(Level.FINER, "Changed id3v1 tag option. delete: " + window.isDeleteId3v1Tag() + " set: " + window.isSetId3v1Tag());
		if (window.isSetId3v1Tag()) {
			window.setDeleteId3v1Tag(false);
		}
	}

	/**
	 * delete id3v1 tag button pressed
	 */
	private void deleteId3v1TagPressed() {
		logger.log(Level.FINER, "Changed id3v1 tag option. delete: " + window.isDeleteId3v1Tag() + " set: " + window.isSetId3v1Tag());
		if (window.isDeleteId3v1Tag()) {
			window.setSetId3v1Tag(false);
		}

	}

	/**
	 * sets the selected collector dis/enabled
	 * 
	 * @param index
	 *            index of the selected collector
	 * @param type
	 *            collector ID
	 */
	private void setCollectorEnabled(int index, int type) {
		// check if the collector is going to be disabled or enabled, if
		// disabled check if it is the last one
		if (model.isCollectorEnabled(index, type)) {
			if (model.isLastCollector(type)) {
				window.showMessage("oneCollectorNeeded");
				return;
			}
		}

		model.changeCollectorState(index, type);
		window.setCollectors(model.getCollectors(type), type);
	}
}