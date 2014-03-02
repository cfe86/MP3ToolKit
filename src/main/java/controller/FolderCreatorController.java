package controller;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.Config;
import config.Constants;
import controller.interfaces.AbstractController;
import logging.LogUtil;
import model.FolderCreatorModel;
import model.exception.ControllerInitException;
import model.filefilter.AudioFilter;
import model.progressbar.CounterProgressBar;
import model.progressbar.InProgressBar;
import model.progressbar.interfaces.ICancelCommand;
import model.progressbar.interfaces.IProgressBar;
import model.transferhandler.FileFolderTransferHandler;
import model.util.ChooserUtil;
import model.util.Commons;
import model.util.Util;
import view.FolderCreatorTab;
import view.interfaces.AbstractTab;

public class FolderCreatorController extends AbstractController implements MouseListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private FolderCreatorModel model;

	/**
	 * the window
	 */
	private FolderCreatorTab window;

	/**
	 * the cancel command for progressbars
	 */
	private ICancelCommand cancelCmd;

	/**
	 * Constructor
	 */
	public FolderCreatorController() {
		this.model = new FolderCreatorModel();
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
		this.window = (FolderCreatorTab) tab;
		this.window.setMouseListener(this);
		this.window.setActionListener(this);
		this.window.setTableModel(model.getTableModel());

		try {
			this.window.setMasks(Commons.readMasks());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading masks:\n" + LogUtil.getStackTrace(e), e);
		}

		this.window.setTableTransferHandler(new FileFolderTransferHandler() {

			private static final long serialVersionUID = 5074476020036849927L;

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
				addAudioFiles(files);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("showChangesB"))
			showChangesBPressed();
		else if (e.getActionCommand().equals("makeStructureB"))
			makeStructureBPressed();
		else if (e.getActionCommand().equals("targetOpenB"))
			targetOpenBPressed();
		else if (e.getActionCommand().equals("filenameRB"))
			filenameRBPressed();
		else if (e.getActionCommand().equals("id3tagRB"))
			id3tagRBPressed();
		else if (e.getActionCommand().equals("addFileB"))
			addFileBPressed();
		else if (e.getActionCommand().equals("addFolderB"))
			addFolderBPressed();
		else if (e.getActionCommand().equals("deleteFileB"))
			deleteFileBPressed();
		else if (e.getActionCommand().equals("deleteAllB"))
			deleteAllBPressed();
	}

	/**
	 * show changes button pressed
	 */
	private void showChangesBPressed() {
		logger.log(Level.FINER, "Show changes button pressed.");

		if (window.getstructureTF().trim().equals("")) {
			window.showMessage("structureEmpty");
			return;
		}

		if (window.gettargetTF().trim().equals("")) {
			window.showMessage("TargetFolderNotFound");
			return;
		}

		// id3tag
		if (window.getID3TagRB()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					window.setButtonsEnabled(false);
					IProgressBar b = new CounterProgressBar();
					b.setMax(model.getListSize());
					b.setCancelCommand(cancelCmd);
					b.start();
					model.createNewPathID3(window.getstructureTF().trim(), window.gettargetTF().trim(), b);

					Util.sleep(Constants.REFRESH_DELAY);
					b.stopBar();

					model.setStopFlag(false);

					updateTable(true);
				}
			}).start();

		}
		// else filename
		else {
			if (window.getregexTF().trim().equals("")) {
				window.showMessage("regexEmpty");
				return;
			}

			new Thread(new Runnable() {

				@Override
				public void run() {
					window.setButtonsEnabled(false);
					IProgressBar b = new CounterProgressBar();
					b.setMax(model.getListSize());
					b.setCancelCommand(cancelCmd);
					b.start();
					model.createNewPath(window.getstructureTF().trim(), window.getregexTF().trim(), window.gettargetTF().trim(), b);
					window.setTableModel(model.getTableModel());
					Util.sleep(Constants.REFRESH_DELAY);

					model.setStopFlag(false);

					b.stopBar();
					updateTable(true);
				}
			}).start();
		}
	}

	/**
	 * generates the folder structure Button pressed
	 */
	private void makeStructureBPressed() {
		logger.log(Level.FINER, "Make changes button pressed.");

		if (window.gettargetTF().trim().equals("")) {
			logger.log(Level.FINER, "target is empty.");
			window.showMessage("TargetFolderNotFound");
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setButtonsEnabled(false);
				IProgressBar b = new CounterProgressBar();
				b.setMax(model.getListSize());
				b.setCancelCommand(cancelCmd);
				b.start();
				model.createFolderStructure();
				model.generateFolderStructure(window.gettargetTF());
				try {
					model.copyAudioFiles(b);
				} catch (IOException e) {
					window.showMessage("copyError");
				}

				Util.sleep(Constants.REFRESH_DELAY);
				window.setButtonsEnabled(true);
				model.setStopFlag(false);
				b.stopBar();
			}
		}).start();

	}

	/**
	 * opens the target folder open button
	 */
	private void targetOpenBPressed() {
		logger.log(Level.FINER, "Target open button pressed.");

		File folder = ChooserUtil.openFolder(window, window.gettargetTF().length() > 0 ? new File(window.gettargetTF()) : null);
		if (folder == null)
			return;
		window.settargetTF(folder.getAbsolutePath());

		String path = window.gettargetTF();

		if (!new File(path).exists()) {
			logger.log(Level.FINER, "folder: " + path + " doesnt exist.");
			window.showMessage("TargetFolderNotFound");
			return;
		}
	}

	/**
	 * filename radiobutton pressed
	 */
	private void filenameRBPressed() {
		logger.log(Level.FINER, "Filename RadioButton changed: " + window.getFilenameRB());
		window.setRegexEnabled(true);
	}

	/**
	 * id3 radiobutton pressed
	 */
	private void id3tagRBPressed() {
		logger.log(Level.FINER, "ID3Tag RadioButton changed: " + window.getID3TagRB());
		window.setRegexEnabled(false);
	}

	/**
	 * regex help button pressed
	 */
	private void regexHelpPressed() {
		logger.log(Level.FINER, "Regex Help Panel pressed.");
		window.openRegexHelpDialog();
	}

	/**
	 * structure regex help button pressed
	 */
	private void structureHelpPressed() {
		logger.log(Level.FINER, "Structure Help Panel pressed.");
		window.openStructureHelpDialog();
	}

	/**
	 * add file button pressed
	 */
	private void addFileBPressed() {
		logger.log(Level.FINER, "add file button pressed.");

		final File file = ChooserUtil.openFile(window, new AudioFilter(), new File(Config.getInstance().getGeneratorOpenFile()));

		if (file == null)
			return;

		if (!new File(file.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "file: " + file.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FileNotFound");
			return;
		}

		Config.getInstance().setGeneratorOpenFile(file.getAbsolutePath());

		List<String> lst = new ArrayList<>();
		lst.add(file.getAbsolutePath());

		addAudioFiles(lst);
	}

	/**
	 * add folder button pressed
	 */
	private void addFolderBPressed() {
		logger.log(Level.FINEST, "add folder button pressed.");

		final File folder = ChooserUtil.openFolder(window, new File(Config.getInstance().getGeneratorOpenFolder()));

		if (folder == null)
			return;

		if (!new File(folder.getAbsolutePath()).exists()) {
			logger.log(Level.FINER, "folder: " + folder.getAbsolutePath() + " doesnt exist.");
			window.showMessage("FolderNotFound");
			return;
		}

		Config.getInstance().setGeneratorOpenFolder(folder.getAbsolutePath());

		addAudioFiles(folder.getAbsolutePath());
	}

	/**
	 * delete file button pressed
	 */
	private void deleteFileBPressed() {
		logger.log(Level.FINEST, "delete file button pressed.");

		int[] i = window.getSelectedIndices();
		if (i == null || i.length < 1)
			return;

		model.deleteAudioFiles(i);
		updateTable(false);
	}

	/**
	 * delete all files button pressed
	 */
	private void deleteAllBPressed() {
		logger.log(Level.FINEST, "delete all button pressed.");

		model.clearAudioFiles();
		updateTable(false);
	}

	/**
	 * updates the table
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
	 * adds the given list of audio files
	 * 
	 * @param files
	 *            the given file paths
	 */
	private void addAudioFiles(final List<String> files) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar ib = new InProgressBar(400, 200);
				ib.setCancelCommand(cancelCmd);
				ib.start();
				for (String f : files)
					model.addAudioFile(f, true);

				Util.sleep(Constants.REFRESH_DELAY);
				model.setStopFlag(false);
				ib.stopBar();
				updateTable(true);
			}
		}).start();
	}

	/**
	 * adds the given folder
	 * 
	 * @param path
	 *            path to the folder
	 */
	private void addAudioFiles(final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
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
		Config.getInstance().setGeneratorTargetFolder(window.gettargetTF());
		Config.getInstance().setGeneratorStructure(window.getstructureTF());
		Config.getInstance().setGeneratorRecursive(window.getrecursiveChB());
		Config.getInstance().setGeneratorRegex(window.getregexTF());
		Config.getInstance().setGeneratorFilenameSelected(window.getFilenameRB());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * controller.interfaces.AbstractController#mousePressed(java.awt.event.
	 * MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == window.getRegexHelpP())
			regexHelpPressed();
		else if (e.getSource() == window.getStructureHelpP())
			structureHelpPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * controller.interfaces.AbstractController#mouseReleased(java.awt.event
	 * .MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * controller.interfaces.AbstractController#mouseEntered(java.awt.event.
	 * MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.interfaces.AbstractController#mouseExited(java.awt.event.
	 * MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}
}