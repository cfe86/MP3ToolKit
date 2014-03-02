package controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Config;
import controller.interfaces.AbstractController;
import logging.LogUtil;
import model.RenameToolModel;
import model.exception.ControllerInitException;
import model.filefilter.AudioFilter;
import model.progressbar.InProgressBar;
import model.progressbar.interfaces.ICancelCommand;
import model.progressbar.interfaces.IProgressBar;
import model.transferhandler.FileFolderTransferHandler;
import model.util.ChooserUtil;
import model.util.Commons;
import view.RenameToolTab;
import view.interfaces.AbstractTab;

public class RenameToolController extends AbstractController {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private RenameToolModel model;

	/**
	 * the window
	 */
	private RenameToolTab window;

	/**
	 * cancel command
	 */
	private ICancelCommand cancelCmd;

	/**
	 * Constructor
	 */
	public RenameToolController() {
		this.model = new RenameToolModel();
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
		this.window = (RenameToolTab) tab;
		this.window.setMouseListener(this);
		this.window.setActionListener(this);
		this.window.setTableModel(model.getTableModel());

		try {
			this.window.setMasks(Commons.readMasks());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading masks:\n" + LogUtil.getStackTrace(e), e);
		}

		this.window.setTableTransferHandler(new FileFolderTransferHandler() {

			private static final long serialVersionUID = 5506005560109208460L;

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
		if (e.getActionCommand().equals("renameFilesB"))
			renameFilesBPressed();
		else if (e.getActionCommand().equals("showChangesB"))
			makeChangesBPressed();
		else if (e.getActionCommand().equals("addFileB"))
			addFileBPressed();
		else if (e.getActionCommand().equals("addFolderB"))
			addFolderBPressed();
		else if (e.getActionCommand().equals("deleteFileB"))
			deleteFileBPressed();
		else if (e.getActionCommand().equals("deleteAllB"))
			deleteAllBPressed();
		else if (e.getActionCommand().equals("filenameRB"))
			getInfoMethodChanged();
		else if (e.getActionCommand().equals("id3tagRB"))
			getInfoMethodChanged();
		else if (e.getActionCommand().equals("replaceSpaceChB"))
			replaceSpaceChBPressed();
		else if (e.getActionCommand().equals("replaceUnderscoreChB"))
			replaceUnderscoreChBPressed();
	}

	/**
	 * rename files pressed, before the changes needs to be made
	 */
	private void renameFilesBPressed() {
		logger.log(Level.FINER, "Rename files button pressed.");

		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setButtonsEnabled(false);

				if (model.allAudioFilesAnalysed()) {
					logger.log(Level.FINER, "not all audio files analyzed.");
					window.showMessage("notAllAnalyzed");
					return;
				}

				IProgressBar b = new InProgressBar(400, 200);
				b.start();
				model.renameAudioFiles(window.getUnchangedChB(), window.getUpperCaseRB());
				b.stopBar();
				window.setButtonsEnabled(true);

				window.showMessage("done");
			}
		}).start();
	}

	/**
	 * makes the changes and shows them in the table, the changes will be
	 * written by pressing rename files
	 */
	private void makeChangesBPressed() {
		logger.log(Level.FINER, "Make changes button pressed.");

		final String src = window.getsourceTF();
		final String target = window.gettargetTF();

		if (!window.getID3TagRB() && src.trim().equals("")) {
			window.showMessage("srcRegexEmpty");
			return;
		} else if (target.trim().equals("")) {
			window.showMessage("targetRegexEmpty");
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setButtonsEnabled(false);
				IProgressBar b = new InProgressBar(400, 200);
				b.setCancelCommand(cancelCmd);
				b.start();
				if (window.getID3TagRB())
					model.makeNewNamesID3Tag(target);
				else
					model.makeNewNames(src, target);
				model.makeMisc(window.getreplaceUnderscoreChB(), window.getreplaceSpaceChB(), window.gettrimChB());
				if (window.getreplaceChB())
					model.makeReplace(window.getreplaceTF(), window.getreplaceWithTF());

				model.setStopFlag(false);

				b.stopBar();
				updateTable(true);
			}
		}).start();
	}

	/**
	 * add file pressed
	 */
	private void addFileBPressed() {
		logger.log(Level.FINER, "add file button pressed.");

		final File file = ChooserUtil.openFile(window, new AudioFilter(), new File(Config.getInstance().getRenameOpenFile()));

		if (file == null)
			return;

		if (!new File(file.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "file: " + file.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FileNotFound");
			return;
		}

		Config.getInstance().setRenameOpenFile(file.getAbsolutePath());

		new Thread(new Runnable() {

			@Override
			public void run() {
				List<String> lst = new ArrayList<>();
				lst.add(file.getAbsolutePath());
				addAudioFiles(lst);

			}
		}).start();
	}

	/**
	 * add folder pressed
	 */
	private void addFolderBPressed() {
		logger.log(Level.FINER, "add folder button pressed.");

		final File folder = ChooserUtil.openFolder(window, new File(Config.getInstance().getRenameOpenFolder()));

		if (folder == null)
			return;

		if (!new File(folder.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "folder: " + folder.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FolderNotFound");
			return;
		}

		Config.getInstance().setRenameOpenFolder(folder.getAbsolutePath());

		new Thread() {
			public void run() {
				addAudioFiles(folder.getAbsolutePath());
			}
		}.start();
	}

	/**
	 * deletes the file from the list
	 */
	private void deleteFileBPressed() {
		logger.log(Level.FINER, "delete file button pressed.");

		int[] i = window.getSelectedIndices();
		if (i == null || i.length < 1)
			return;

		model.deleteAudioFiles(i);
		updateTable(false);
	}

	/**
	 * deletes all files from the list
	 */
	private void deleteAllBPressed() {
		logger.log(Level.FINER, "delete all button pressed.");

		model.clearAudioFiles();
		updateTable(false);
	}

	/**
	 * replace underscore with space checkbox pressed
	 */
	private void replaceUnderscoreChBPressed() {
		logger.log(Level.FINER, "replace underscore checkbox pressed. selected: " + window.getreplaceUnderscoreChB());

		if (!window.getreplaceUnderscoreChB())
			window.setreplaceUnderscoreChB(false);
		else
			window.setreplaceUnderscoreChB(true);

		window.setreplaceSpaceChB(false);
	}

	/**
	 * replace space with underscore checkbox pressed
	 */
	private void replaceSpaceChBPressed() {
		logger.log(Level.FINER, "replace space checkbox pressed. selected: " + window.getreplaceSpaceChB());

		if (!window.getreplaceSpaceChB())
			window.setreplaceSpaceChB(false);
		else
			window.setreplaceSpaceChB(true);

		window.setreplaceUnderscoreChB(false);
	}

	/**
	 * changed the information method
	 */
	private void getInfoMethodChanged() {
		logger.log(Level.FINER, "get info method changed. id3tag selected: " + window.getID3TagRB());

		if (window.getID3TagRB()) {
			window.setSourceRegexEnabled(false);
		} else {
			window.setSourceRegexEnabled(true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * controller.interfaces.AbstractController#mouseClicked(java.awt.event.
	 * MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		logger.log(Level.FINER, "open help pressed.");
		window.openHelpDialog();
	}

	/**
	 * adds the given folder to the list
	 * 
	 * @param path
	 *            the folder path
	 */
	private void addAudioFiles(String path) {
		IProgressBar ib = new InProgressBar(400, 200);
		ib.setCancelCommand(cancelCmd);
		ib.start();
		model.addAudioFiles(path, window.getrecursiveChB());

		ib.stopBar();

		model.setStopFlag(false);

		updateTable(true);
	}

	/**
	 * adds the given files to the list
	 * 
	 * @param files
	 *            the paths
	 */
	private void addAudioFiles(List<String> files) {
		IProgressBar ib = new InProgressBar(400, 200);
		ib.setCancelCommand(cancelCmd);
		ib.start();
		for (String f : files)
			model.addAudioFile(f, true);

		model.setStopFlag(false);
		ib.stopBar();
		updateTable(true);
	}

	/**
	 * updates the table
	 * 
	 * @param enButtons
	 *            true if all buttons should be enabled too
	 */
	private void updateTable(final boolean enButtons) {
		window.saveTableWidth();
		window.setTableModel(model.getTableModel());

		if (enButtons)
			window.setButtonsEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.interfaces.AbstractController#saveConfig()
	 */
	@Override
	public void saveConfig() {
		Config.getInstance().setRenameReplaceSpaceWithUnderscore(window.getreplaceSpaceChB());
		Config.getInstance().setRenameFilenameSelected(!window.getID3TagRB());
		Config.getInstance().setRenameTrimFilename(window.gettrimChB());
		Config.getInstance().setRenameExtensionLowercase(window.getLowerCaseRB());
		Config.getInstance().setRenameExtensionUppercase(window.getUpperCaseRB());
		Config.getInstance().setRenameRecursive(window.getrecursiveChB());
		Config.getInstance().setRenameSource(window.getsourceTF());
		Config.getInstance().setRenameTarget(window.gettargetTF());
		Config.getInstance().setRenameReplace(window.getreplaceTF());
		Config.getInstance().setRenameReplaceWith(window.getreplaceWithTF());
	}
}