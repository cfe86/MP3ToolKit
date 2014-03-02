package controller;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import config.Config;
import config.Constants;
import controller.interfaces.AbstractController;
import logging.LogUtil;
import model.MP3GainModel;
import model.exception.ControllerInitException;
import model.filefilter.AudioFilter;
import model.progressbar.AnalyseAlbumGainProgressBar;
import model.progressbar.AnalyseTrackGainProgressBar;
import model.progressbar.ChangeAlbumGainProgressBar;
import model.progressbar.ChangeTrackGainProgressBar;
import model.progressbar.InProgressBar;
import model.progressbar.interfaces.ICancelCommand;
import model.progressbar.interfaces.IProgressBar;
import model.progressbar.interfaces.MP3GainThread;
import model.transferhandler.FileFolderTransferHandler;
import model.util.ChooserUtil;
import model.util.Util;
import view.MP3GainTab;
import view.interfaces.AbstractTab;

public class MP3GainController extends AbstractController {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private MP3GainModel model;

	/**
	 * the window
	 */
	private MP3GainTab window;

	/**
	 * the cancel command
	 */
	private ICancelCommand cancelCmd;

	/**
	 * Constructor
	 */
	public MP3GainController() {
		model = new MP3GainModel();
		this.cancelCmd = new ICancelCommand() {

			@Override
			public void call() {
				model.setStopFlag(true);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * controller.interfaces.AbstractController#init(view.interfaces.AbstractTab
	 * )
	 */
	@Override
	public void init(AbstractTab tab) throws ControllerInitException {
		this.window = (MP3GainTab) tab;
		
		if (!model.checkMP3Gain()) {
			logger.log(Level.FINER, "mp3gain not ready to use.");
			window.showMessage("mp3gainNotReady");
			window.setButtonsEnabled(false);
			return;
		}
		
		this.window.setTableModel(model.getTableModel());
		this.window.setActionListener(this);
		
		this.window.setTableTransferHandler(new FileFolderTransferHandler() {

			private static final long serialVersionUID = 7463122309021766045L;

			@Override
			public void addFolder(final String path) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						addAudioFiles(path);
					}
				}).start();
			}

			@Override
			public void addFiles(final List<String> files) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						addAudioFiles(files);
					}
				}).start();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("addFileB"))
			addFileBPressed();
		else if (e.getActionCommand().equals("deleteAllB"))
			deleteAllBPressed();
		else if (e.getActionCommand().equals("deleteFileB"))
			deleteFileBPressed();
		else if (e.getActionCommand().equals("startTrackGainB"))
			startGainBPressed();
		else if (e.getActionCommand().equals("startAnalyseB"))
			startAnalyseBPressed();
		else if (e.getActionCommand().equals("addFolderB"))
			addFolderBPressed();
	}

	/**
	 * add files pressed
	 */
	private void addFileBPressed() {
		logger.log(Level.FINER, "add File Button pressed.");
		final File file = ChooserUtil.openFile(window, new AudioFilter(), new File(Config.getInstance().getMp3gainOpenFile()));

		if (file == null)
			return;

		if (!new File(file.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "file: " + file.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FileNotFound");
			return;
		}

		Config.getInstance().setMp3gainOpenFile(file.getAbsolutePath());

		List<String> lst = new ArrayList<>();
		lst.add(file.getAbsolutePath());

		addAudioFiles(lst);
	}

	/**
	 * add folder pressed
	 */
	private void addFolderBPressed() {
		logger.log(Level.FINER, "add Folder Button pressed.");

		final File folder = ChooserUtil.openFolder(window, new File(Config.getInstance().getMp3gainOpenFolder()));

		if (folder == null)
			return;

		if (!new File(folder.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "folder: " + folder.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FolderNotFound");
			return;
		}

		Config.getInstance().setMp3gainOpenFolder(folder.getAbsolutePath());

		addAudioFiles(folder.getAbsolutePath());
	}

	/**
	 * deletes all files from the list
	 */
	private void deleteAllBPressed() {
		logger.log(Level.FINER, "delete All Button pressed.");

		model.clearAudioFiles();
		updateTable(false);
	}

	/**
	 * deletes the selected files
	 */
	private void deleteFileBPressed() {
		logger.log(Level.FINER, "delete File Button pressed.");
		int[] i = window.getSelectedIndices();
		if (i == null || i.length < 1)
			return;

		model.deleteAudioFiles(i);
		updateTable(false);
	}

	/**
	 * start change gain pressed, needs to be analysed before first
	 */
	private void startGainBPressed() {
		logger.log(Level.FINER, "start Track Gain Button pressed.");

		// get indicies
		final int[] indices;
		if (!window.isAnalyseSelectAll())
			indices = window.getSelectedIndices();
		else {
			indices = new int[model.getNumOfAudioFiles()];
			for (int i = 0; i < model.getNumOfAudioFiles(); i++)
				indices[i] = i;
		}

		if (indices == null || indices.length == 0)
			return;
		
		int r = window.showConfirmationMessage("confirmChangeGain");
		if (r != JOptionPane.YES_OPTION)
			return;

		// disable buttons and so on
		window.setButtonsEnabled(false);
		window.saveTableWidth();
		window.setTableModel(model.getTableModel());

		// Track Gain
		if (window.isTrackGainSelected()) {

			new Thread() {
				public void run() {

					MP3GainThread pb = new ChangeTrackGainProgressBar(indices.length, 200);
					pb.setCancelCommand(cancelCmd);
					for (int i = 0; i < indices.length; i++) {
						try {
							if (!model.changeGain(indices[i], window.getTargetVolume(), window.getforceRecalcChB(), pb)) {
								window.showMessage("trackFileNotAnalysed", model.getAudioFile(indices[i]).getPath());
								continue;
							}
							Util.sleep(Constants.TABLE_REFRESH_DELAY);
							pb.nextStep();
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Error while changing track gain:\n" + LogUtil.getStackTrace(e), e);
							window.showMessage("GainError");
						} catch (NumberFormatException e) {
							logger.log(Level.SEVERE, "Error while changing track gain:\n" + LogUtil.getStackTrace(e), e);
							window.showMessage("TargetNoNumber");
						}

						if (model.isStopFlagSet()) {
							// reset the last half changed one
							model.clearAudioFileData(new int[] { indices[i] });
							break;
						}
					}

					pb.stopBar();
					model.setStopFlag(false);
					updateTable(true);
					window.showMessage("changeSuccessful");
				}
			}.start();
		}
		// Album Gain
		else {

			new Thread() {
				public void run() {

					MP3GainThread pb = new ChangeAlbumGainProgressBar(indices.length, 200);
					try {
						if (!model.changeAlbumGain(indices, window.getTargetVolume(), window.getforceRecalcChB(), pb)) {
							window.showMessage("albumFileNotAnalysed");
							return;
						}

					} catch (IOException e) {
						logger.log(Level.SEVERE, "Error while changing album gain:\n" + LogUtil.getStackTrace(e), e);
						window.showMessage("GainError");
					} catch (NumberFormatException e) {
						logger.log(Level.SEVERE, "Error while changing album gain:\n" + LogUtil.getStackTrace(e), e);
						window.showMessage("TargetNoNumber");
					}

					if (model.isStopFlagSet())
						model.clearAudioFileData(indices);

					model.setStopFlag(false);
					updateTable(true);
					window.showMessage("changeSuccessful");
				}
			}.start();
		}
	}

	/**
	 * start analyse gain pressed
	 */
	private void startAnalyseBPressed() {
		logger.log(Level.FINER, "start Analyse Button pressed.");

		// get indicies
		final int[] indices;
		if (!window.isAnalyseSelectAll()) {
			indices = window.getSelectedIndices();

			if (indices == null || indices.length == 0)
				return;

			model.clearAudioFileData(indices);
		} else {
			indices = new int[model.getNumOfAudioFiles()];
			for (int i = 0; i < model.getNumOfAudioFiles(); i++)
				indices[i] = i;

			if (indices == null || indices.length == 0)
				return;

			model.clearAllAudioFileData();
		}

		// disable buttons and clear data and so on
		window.setButtonsEnabled(false);
		window.saveTableWidth();
		window.setTableModel(model.getTableModel());

		// Track Gain
		if (window.isTrackGainSelected()) {
			new Thread() {
				public void run() {

					MP3GainThread pb = new AnalyseTrackGainProgressBar(200);
					pb.setCancelCommand(cancelCmd);
					for (int i = 0; i < indices.length; i++) {
						try {
							model.analyseTrackGain(indices[i], window.getTargetVolume(), window.getforceRecalcChB(), pb);
							Util.sleep(Constants.TABLE_REFRESH_DELAY);
						} catch (IOException e) {
							logger.log(Level.SEVERE, "Error while analyse track gain:\n" + LogUtil.getStackTrace(e), e);
							window.showMessage("GainError");
						} catch (NumberFormatException e) {
							logger.log(Level.SEVERE, "Error while analyse track gain:\n" + LogUtil.getStackTrace(e), e);
							window.showMessage("TargetNoNumber");
						}

						if (model.isStopFlagSet()) {
							// reset the last half analysed one
							model.clearAudioFileData(new int[] { indices[i] });
							break;
						}

						updateTable(false);
					}

					model.setStopFlag(false);
					pb.stopBar();
					window.setButtonsEnabled(true);
				}
			}.start();
		}
		// Album Gain
		else {
			new Thread() {
				public void run() {
					MP3GainThread pb = new AnalyseAlbumGainProgressBar(indices.length, 200);
					pb.setCancelCommand(cancelCmd);
					try {
						model.analyseAlbumGain(indices, window.getTargetVolume(), window.getforceRecalcChB(), pb);
					} catch (IOException e) {
						logger.log(Level.SEVERE, "Error while album track gain:\n" + LogUtil.getStackTrace(e), e);
						window.showMessage("GainError");
					} catch (NumberFormatException e) {
						logger.log(Level.SEVERE, "Error while album track gain:\n" + LogUtil.getStackTrace(e), e);
						window.showMessage("TargetNoNumber");
					}

					if (model.isStopFlagSet())
						model.clearAudioFileData(indices);

					model.setStopFlag(false);
					updateTable(true);
				}
			}.start();
		}
	}

	/**
	 * updates the gain table
	 * 
	 * @param enButtons
	 *            true if the buttons should be enabled
	 */
	private void updateTable(final boolean enButtons) {
		window.saveTableWidth();
		window.setTableModel(model.getTableModel());

		if (enButtons)
			window.setButtonsEnabled(true);
	}

	/**
	 * adds the given files to the list
	 * 
	 * @param files
	 *            the given files
	 */
	private void addAudioFiles(final List<String> files) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setButtonsEnabled(false);
				IProgressBar ib = new InProgressBar(400, 200);
				ib.setCancelCommand(cancelCmd);
				ib.start();
				for (String f : files)
					model.addAudioFile(f, true);

				Util.sleep(Constants.REFRESH_DELAY);
				ib.stopBar();
				model.setStopFlag(false);
				updateTable(true);
			}
		}).start();
	}

	/**
	 * adds the given folder to the path
	 * 
	 * @param path
	 *            the folder path
	 */
	private void addAudioFiles(final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setButtonsEnabled(false);
				IProgressBar ib = new InProgressBar(400, 200);
				ib.setCancelCommand(cancelCmd);
				ib.start();
				model.addAudioFiles(path, window.getrecursiveChB());

				Util.sleep(Constants.REFRESH_DELAY);
				model.setStopFlag(false);
				ib.stopBar();
				updateTable(true);
			}
		}).start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.interfaces.AbstractController#saveConfig()
	 */
	@Override
	public void saveConfig() {
		Config.getInstance().setMP3GainRecursiveSelected(window.getrecursiveChB());
		Config.getInstance().setMP3GainForceSelected(window.getforceRecalcChB());
		Config.getInstance().setMP3GainTrackType(window.isTrackGainSelected());
		Config.getInstance().setMP3gainChangeSelected(!window.isChangeGainSelectAll());
		Config.getInstance().setMP3gainAnalyseSelected(!window.isAnalyseSelectAll());
		Config.getInstance().setMP3GainTarget(window.gettargetVolTF());
	}
}