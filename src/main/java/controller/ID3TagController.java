package controller;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import logging.LogUtil;
import model.ID3TagModel;
import model.ID3TagRegex;
import model.audio.Genres;
import model.audio.interfaces.IAudioFile;
import model.exception.AudioFileException;
import model.exception.AudioPlayerException;
import model.exception.ControllerInitException;
import model.filefilter.AudioFilter;
import model.filefilter.ImageFilter;
import model.filefilter.TxtFilter;
import model.progressbar.CounterProgressBar;
import model.progressbar.ChangeID3ProgressBar;
import model.progressbar.InProgressBar;
import model.progressbar.interfaces.ICancelCommand;
import model.progressbar.interfaces.IProgressBar;
import model.structure.FieldReplacerData;
import model.structure.ID3ImageData;
import model.structure.ID3LyricsData;
import model.structure.ID3TagData;
import model.transferhandler.FileFolderTransferHandler;
import model.transferhandler.FolderTransferHandler;
import model.util.ChooserUtil;
import model.util.Commons;
import model.util.FileUtil;
import model.util.Util;
import view.ID3TagTab;
import view.interfaces.AbstractTab;

import com.cf.structures.DataDouble;
import com.mpatric.mp3agic.NotSupportedException;

import config.Config;
import config.Constants;
import controller.interfaces.AbstractController;
import controller.interfaces.ICmdChangeID3Data;
import controller.interfaces.ICmdChangeImage;
import controller.interfaces.ICmdChangeLyrics;
import controller.interfaces.ICmdFieldReplacer;
import controller.interfaces.ICmdGenerateTabByFilename;
import controller.interfaces.ICmdImageSettings;
import controller.subcontroller.AudioPlayerController;
import controller.subcontroller.ChangeImageDataController;
import controller.subcontroller.ChangeLyricsDataController;
import controller.subcontroller.ChangeTagDataController;
import controller.subcontroller.FieldReplacerController;
import controller.subcontroller.GenByNameController;
import controller.subcontroller.ImageSettingsController;

public class ID3TagController extends AbstractController implements ListSelectionListener, TreeSelectionListener, FocusListener, ChangeListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private ID3TagModel model;

	/**
	 * the window
	 */
	private ID3TagTab window;

	/**
	 * the cancel command for progressbars
	 */
	private ICancelCommand cancelCmd;

	/**
	 * Constructor
	 */
	public ID3TagController() {
		super();
		model = new ID3TagModel();
		cancelCmd = new ICancelCommand() {
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
		this.window = (ID3TagTab) tab;

		this.window.setTreeSelectionListener(this);
		this.window.updateTree(Config.getInstance().getID3Root());
		this.window.setrootTF(Config.getInstance().getID3Root());
		this.window.setTableModel(model.getTableModel());
		this.window.setGenres(Genres.getGenres());
		this.window.setActionListener(this);
		this.window.setListSelectionListener(this);
		this.window.setFocusListener(this);
		this.window.setChangeListener(this);
		this.window.setTableTransferHandler(new FileFolderTransferHandler() {

			private static final long serialVersionUID = -1571064726234858600L;

			@Override
			public void addFolder(String path) {
				addAudioFiles(path);
			}

			@Override
			public void addFiles(List<String> files) {
				addAudioFiles(files);
			}
		});

		this.window.setTreeTransferHandler(new FolderTransferHandler() {

			private static final long serialVersionUID = 3016515574019264191L;

			@Override
			public void setFolder(String folder) {
				rootOpenBPressed(folder, true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable obs, Object obj) {
		logger.log(Level.FINER, this.getClass().getName() + " got message from " + obs.getClass().getName());

	}

	/**
	 * delete image button pressed
	 */
	private void deleteImage() {
		logger.log(Level.FINER, "delete image");
		int[] i = window.getSelectedFiles();
		if (i == null || i.length < 1)
			return;

		model.deleteAudioFileImage();
		try {
			window.setCoverImageData(null, null);
		} catch (IOException e) {
			// should never happen
			logger.log(Level.SEVERE, "Error while resetting cover image:\n" + LogUtil.getStackTrace(e), e);
		}

		updateTable(i);
	}

	/**
	 * rescales the current image to the given width and height
	 * 
	 * @param width
	 *            new width
	 * @param height
	 *            new height
	 */
	private void rescaleImage(int width, int height) {
		logger.log(Level.FINER, "scale image to " + width + "x" + height);
		int[] i = window.getSelectedFiles();
		if (i == null || i.length < 1)
			return;

		try {
			model.rescaleImage(width, height);
			window.setAudioFileData(model.getCurrAudioFile());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while setting id3 data:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("rescaleError");
		}

		updateTable(i);
	}

	/**
	 * replaces the fields of the selected files with the given replacements
	 * 
	 * @param data
	 *            given replacement data
	 */
	private void replaceFields(FieldReplacerData data) {
		int[] indices;
		// get indices
		if (data.isSelectAll()) {
			indices = new int[model.getNumOfAudioFiles()];
			for (int i = 0; i < model.getNumOfAudioFiles(); i++)
				indices[i] = i;
		} else
			indices = window.getSelectedFiles();

		if (indices == null || indices.length == 0)
			return;

		model.replaceFields(indices, data);

		updateTable(indices);
		try {
			window.setAudioFileData(model.getCurrAudioFile());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "File not Found:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("FileNotFound");
		}
	}

	/**
	 * generates the ID3Tag using the filename regex and shows them in the
	 * change window
	 * 
	 * @param data
	 *            the filename regex
	 * @param all
	 *            true if all is selected, else just the chosen audio files
	 */
	private void generateIDTagByFilename(ID3TagRegex data, boolean all) {
		logger.log(Level.FINER, "create ID3Tag using Filename.");

		int[] i;
		if (all) {
			i = new int[model.getNumOfAudioFiles()];
			for (int j = 0; j < model.getNumOfAudioFiles(); j++)
				i[j] = j;
		} else
			i = window.getSelectedFiles();

		if (i == null || i.length == 0) {
			window.showMessage("noFilesSelected");
			return;
		}

		mainWindow.setWindowEnabled(false);
		ChangeTagDataController con = new ChangeTagDataController();
		con.setCloseCmd(new ICmdChangeID3Data() {

			@Override
			public void call(List<ID3TagData> data) {
				mainWindow.setWindowEnabled(true);

				if (data == null)
					return;

				changeId3TagData(data);
			}
		});
		con.createWindow(model.getChangesAudioFileList(i, data));
	}

	/**
	 * changes the id3tag using the given changed data after confirmation
	 * 
	 * @param lst
	 *            given changes
	 */
	private void changeId3TagData(List<ID3TagData> lst) {
		model.makesChanges(lst);

		int[] i = window.getSelectedFiles();

		if (i == null || i.length == 0)
			return;

		updateTable(i);
		try {
			window.setAudioFileData(model.getCurrAudioFile());
		} catch (IOException e) {
			window.showMessage("FileNotFound");
			logger.log(Level.SEVERE, "File not Found.");
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
		if (e.getActionCommand().equals("rootOpenB"))
			rootOpenBPressed("", false);
		else if (e.getActionCommand().equals("deleteAllB"))
			deleteAllBPressed();
		else if (e.getActionCommand().equals("deleteFileB"))
			deleteFileBPressed();
		else if (e.getActionCommand().equals("addFileB"))
			addFileBPressed();
		else if (e.getActionCommand().equals("addFolderB"))
			addFolderBPressed();
		else if (e.getActionCommand().equals("loadB"))
			loadBPressed();
		else if (e.getActionCommand().equals("saveTagsB"))
			saveTagsBPressed();
		else if (e.getActionCommand().equals("addImageB"))
			addImageBPressed();
		else if (e.getActionCommand().equals("ImageSettingsB"))
			ImageSettingsBPressed();
		else if (e.getActionCommand().equals("searchImageB"))
			searchImageBPressed();
		else if (e.getActionCommand().equals("searchLyricsB"))
			searchLyricsBPressed();
		else if (e.getActionCommand().equals("fieldReplacerB"))
			fieldReplacerBPressed();
		else if (e.getActionCommand().equals("searchOnlineB"))
			searchID3TagBPressed();
		else if (e.getActionCommand().equals("generateByNameB"))
			generateByNameBPressed();
		else if (e.getActionCommand().equals("deleteTagB"))
			deleteTagBPressed();
		else if (e.getActionCommand().equals("undoB"))
			undoBPressed();
		else if (e.getActionCommand().equals("loadLyricsB"))
			loadLyricsBPressed();
		else if (e.getActionCommand().equals("saveLyricsB"))
			saveLyricsBPressed();
		else if (e.getActionCommand().equals("deleteLyricsB"))
			deleteLyricsBPressed();
		else if (e.getActionCommand().equals("saveImageB"))
			saveImageBPressed();
		else if (e.getActionCommand().equals("playAudioB"))
			playButtonPressed();
		else if (e.getActionCommand().equals("deleteFileHDDB"))
			deleteFileFromHDDPressed();
		else if (e.getActionCommand().equals("autoAddChB"))
			autoAddChBPressed();
		else if (e.getActionCommand().equals("urlRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("maxTracksRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("copyrightRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("encodedByRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("composerRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("commentRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("publisherRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("albumRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("albumArtistRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("genreRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("titleRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("artistRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("origArtistRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("imageAllRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("lyricsRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("trackRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("yearRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("cdRB"))
			changerRBPressed(e.getActionCommand());
		else if (e.getActionCommand().equals("maxCDRB"))
			changerRBPressed(e.getActionCommand());
	}

	/**
	 * folder is dragged into tree or open folder is pressed
	 * 
	 * @param folder
	 *            the dragged folder, only important when isDrag is true
	 * @param isDrag
	 *            true if dragged, else false
	 */
	private void rootOpenBPressed(String folder, boolean isDrag) {
		logger.log(Level.FINER, "rootOpen button pressed or folder dragged.");
		final File file;
		if (isDrag)
			file = new File(folder);
		else
			file = ChooserUtil.openFolder(window, new File(Config.getInstance().getID3Root()), System.getProperty("user.home"));

		if (!checkFile(file, true))
			return;

		Config.getInstance().setID3Root(file.getAbsolutePath());

		new Thread(new Runnable() {

			@Override
			public void run() {
				window.setrootTF(file.getAbsolutePath());

				InProgressBar bar = new InProgressBar();
				bar.start();
				window.updateTree(file.getAbsolutePath());
				bar.stopBar();
			}
		}).start();
	}

	/**
	 * deletes the selected files from the HDD
	 */
	private void deleteFileFromHDDPressed() {
		logger.log(Level.FINER, "delete file from hdd. index");

		int[] indices = window.getSelectedFiles();

		if (indices == null || indices.length == 0)
			return;

		int r = window.showConfirmationMessage("deleteFilesConfirm");

		if (r != JOptionPane.YES_OPTION)
			return;

		for (int i = 0; i < indices.length; i++) {
			if (!model.deleteFileFromHDD(indices[0]))
				window.showMessage("deleteError", model.getAudioFile(indices[i]).getFilePath());
		}

		updateTable(null);
	}

	/**
	 * delets all files from the list
	 */
	private void deleteAllBPressed() {
		logger.log(Level.FINER, "deleteAll button pressed.");

		model.setCurrIndex(-1);
		window.resetAudioFileData();
		model.clearAudioFiles();
		updateTable(null);
	}

	/**
	 * deletes the selected files from the list
	 */
	private void deleteFileBPressed() {
		logger.log(Level.FINER, "deleteFile button pressed.");

		int[] i = window.getSelectedFiles();
		if (i == null || i.length < 1)
			return;

		model.setCurrIndex(-1);
		window.resetAudioFileData();
		model.deleteAudioFiles(i);
		updateTable(null);
	}

	/**
	 * adds a file to the list
	 */
	private void addFileBPressed() {
		logger.log(Level.FINER, "addFile button pressed.");

		File file = ChooserUtil.openFile(window, new AudioFilter(), new File(Config.getInstance().getID3OpenFile()));

		if (!checkFile(file, false))
			return;

		Config.getInstance().setId3OpenFile(file.getAbsolutePath());

		List<String> lst = new ArrayList<>();
		lst.add(file.getAbsolutePath());

		addAudioFiles(lst);
	}

	/**
	 * adds a folder to the list
	 */
	private void addFolderBPressed() {
		logger.log(Level.FINER, "addFolder button pressed.");

		final File file = ChooserUtil.openFolder(window, new File(Config.getInstance().getID3OpenFolder()));

		if (!checkFile(file, true))
			return;

		Config.getInstance().setId3OpenFolder(file.getAbsolutePath());

		addAudioFiles(file.getAbsolutePath());
	}

	/**
	 * loads the selected folder in the tree
	 */
	private void loadBPressed() {
		logger.log(Level.FINER, "load button pressed.");

		final String path = getTreePath();

		if (path == null || !checkFile(new File(path), true))
			return;

		model.clearAudioFiles();
		addAudioFiles(path);
	}

	/**
	 * saves the changed tags
	 */
	private void saveTagsBPressed() {
		// save made changes if there were some
		ID3TagData data = window.getID3TagData();
		boolean changes = model.makeChanges(data);
		logger.log(Level.FINER, "saveTags button pressed. changes: " + changes);

		final int[] i = window.getSelectedFiles();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// get all indices or just selected ones
				int[] indices;
				if (window.getSaveAllChangedRB()) {
					indices = new int[model.getNumOfAudioFiles()];
					for (int j = 0; j < model.getNumOfAudioFiles(); j++)
						indices[j] = j;
				} else {
					indices = i;
				}
				// check if something got changed
				int changed = model.getChangedNumber(indices);

				if (changed < 1) {
					window.showMessage("noFilesChanged");
					return;
				}

				// check if indices are selected
				if (indices == null || indices.length == 0) {
					window.showMessage("noFilesSelected");
					return;
				}

				// get confirmation
				if (Config.getInstance().isConfirmWriteTags()) {
					int r = window.showConfirmationMessage("confirmWrite");
					if (r != JOptionPane.YES_OPTION)
						return;
				}

				window.setButtonsEnabled(false);
				stopPlayers();

				IProgressBar pb = new ChangeID3ProgressBar(changed);
				try {
					pb.setCancelCommand(cancelCmd);
					pb.start();
					model.writeAudioFiles(indices, pb);
				} catch (NotSupportedException | AudioFileException e) {
					window.showMessage("AudioFileParseError");
					logger.log(Level.SEVERE, "Error while audio file parsing:\n" + LogUtil.getStackTrace(e), e);
				} catch (IOException e) {
					window.showMessage("FileNotFound");
					logger.log(Level.SEVERE, "File not Found.");
				}

				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();
				model.setStopFlag(false);
				window.setButtonsEnabled(true);
				updateTable(i);

			}
		}).start();
	}

	/**
	 * adds an image to the current audio file
	 */
	private void addImageBPressed() {
		logger.log(Level.FINER, "addImage button pressed.");

		File file = ChooserUtil.openFile(window, new ImageFilter(), new File(Constants.DEFAULT_DIR));

		if (!checkFile(file, false))
			return;

		try {
			model.setAudioFileImageFromPath(file.getAbsolutePath());
		} catch (IOException e) {
			window.showMessage("FileNotFound");
			logger.log(Level.SEVERE, "File not Found.");
		}

		int[] i = window.getSelectedFiles();
		updateTable(i);
	}

	/**
	 * saves the image of the file to HDD
	 */
	private void saveImageBPressed() {
		logger.log(Level.FINER, "saveImage button pressed.");

		int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;

		File file = ChooserUtil.saveFile(window, null, new File(Constants.DEFAULT_DIR));

		if (file == null)
			return;

		try {
			model.writeID3TagImage(file.getAbsolutePath());
		} catch (IllegalArgumentException e) {
			window.showMessage("noID3TagImage");
			logger.log(Level.SEVERE, "No id3tag Image to write:\n" + LogUtil.getStackTrace(e), e);
		} catch (IOException e) {
			window.showMessage("FileNotFound");
			logger.log(Level.SEVERE, "File not Found while writing id3tag Image.");
		}
	}

	/**
	 * open the image settings window
	 */
	private void ImageSettingsBPressed() {
		logger.log(Level.FINER, "Image Settings button pressed.");

		int[] size = window.getImageSize();

		if (size == null)
			return;

		mainWindow.setWindowEnabled(false);
		ImageSettingsController con = new ImageSettingsController();
		con.setCloseCommand(new ICmdImageSettings() {

			@Override
			public void call(int width, int height) {
				mainWindow.setWindowEnabled(true);

				if (width == -1 || height == -1)
					return;

				if (width == 0 || height == 0)
					deleteImage();
				// rescale
				else
					rescaleImage(width, height);
			}
		});
		con.createWindow(size[0], size[1]);
	}

	/**
	 * searches for the covers online using the defined Collector
	 */
	private void searchImageBPressed() {
		logger.log(Level.FINER, "searchImage button pressed.");

		final int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new ChangeID3ProgressBar(i.length);
				pb.setCancelCommand(cancelCmd);
				pb.start();
				List<DataDouble<ID3ImageData, ID3ImageData>> data = model.getCoverArtUpdate(i, pb);
				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();

				model.setStopFlag(false);

				mainWindow.setWindowEnabled(false);
				ChangeImageDataController con = new ChangeImageDataController();
				con.setCloseCommand(new ICmdChangeImage() {

					@Override
					public void call(List<ID3ImageData> data) {
						mainWindow.setWindowEnabled(true);

						if (data == null)
							return;

						model.setCoverArt(data);

						int[] i = window.getSelectedFiles();
						updateTable(i);

						try {
							window.setAudioFileData(model.getCurrAudioFile());
						} catch (IOException e) {
							logger.log(Level.SEVERE, "File not Found.");
							window.showMessage("FileNotFound");
						}
					}
				});
				con.createWindow(data);
			}
		}).start();
	}

	/**
	 * searches for lyrics online using the defined Collectors
	 */
	private void searchLyricsBPressed() {
		logger.log(Level.FINER, "searchLyrics button pressed.");

		final int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;

		new Thread(new Runnable() {
			@Override
			public void run() {

				IProgressBar pb = new ChangeID3ProgressBar(i.length);
				pb.setCancelCommand(cancelCmd);
				pb.start();
				List<DataDouble<ID3LyricsData, ID3LyricsData>> data = model.getLyricsAudioFileUpdate(i, pb);
				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();

				model.setStopFlag(false);

				mainWindow.setWindowEnabled(false);
				ChangeLyricsDataController con = new ChangeLyricsDataController();
				con.setCloseCommand(new ICmdChangeLyrics() {

					@Override
					public void call(List<ID3LyricsData> data) {
						mainWindow.setWindowEnabled(true);

						if (data == null)
							return;

						model.setLyrics(data);

						int[] i = window.getSelectedFiles();
						updateTable(i);

						try {
							window.setAudioFileData(model.getCurrAudioFile());
						} catch (IOException e) {
							window.showMessage("FileNotFound");
							logger.log(Level.SEVERE, "File not Found.");
						}
					}
				});
				con.createWindow(data);
			}
		}).start();
	}

	/**
	 * opens the field replacer
	 */
	private void fieldReplacerBPressed() {
		logger.log(Level.FINER, "fieldReplacer button pressed.");

		mainWindow.setWindowEnabled(false);
		FieldReplacerController con = new FieldReplacerController();
		con.setCloseCommand(new ICmdFieldReplacer() {

			@Override
			public void call(FieldReplacerData data) {
				mainWindow.setWindowEnabled(true);

				if (data == null)
					return;

				replaceFields(data);
			}
		});
		con.createWindow();
	}

	/**
	 * searches for the ID3 Tag online using the defined Collectors
	 */
	private void searchID3TagBPressed() {
		logger.log(Level.FINER, "serach ID3tag button pressed.");

		final int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new ChangeID3ProgressBar(i.length);
				pb.setCancelCommand(cancelCmd);
				pb.start();
				List<DataDouble<ID3TagData, ID3TagData>> data = model.getID3DataAudioFileUpdate(i, pb);
				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();

				model.setStopFlag(false);

				mainWindow.setWindowEnabled(false);
				ChangeTagDataController con = new ChangeTagDataController();
				con.setCloseCmd(new ICmdChangeID3Data() {

					@Override
					public void call(List<ID3TagData> data) {
						mainWindow.setWindowEnabled(true);

						if (data == null)
							return;

						changeId3TagData(data);
					}
				});
				con.createWindow(data);
			}
		}).start();
	}

	/**
	 * generates the ID3Tag by Filename window
	 */
	private void generateByNameBPressed() {
		logger.log(Level.FINER, "generateByName button pressed.");

		mainWindow.setWindowEnabled(false);
		GenByNameController con = new GenByNameController();

		con.setCloseCmd(new ICmdGenerateTabByFilename() {

			@Override
			public void call(ID3TagRegex regex, boolean selectAll) {
				mainWindow.setWindowEnabled(true);

				if (regex == null)
					return;

				generateIDTagByFilename(regex, selectAll);
			}
		});

		con.createWindow();
	}

	/**
	 * deletes the tag of the chosen files
	 */
	private void deleteTagBPressed() {
		logger.log(Level.FINER, "deleteTags button pressed.");

		int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;

		model.deleteTags(i);
		window.resetAudioFileData();

		updateTable(i);
	}

	/**
	 * undo button pressed
	 */
	private void undoBPressed() {
		logger.log(Level.FINER, "undo button pressed.");

		final int[] indices = window.getSelectedFiles();

		if (indices == null || indices.length < 1)
			return;

		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new CounterProgressBar();
				try {
					pb.setMax(indices.length);
					pb.setCancelCommand(cancelCmd);
					for (int i = 0; i < indices.length; i++) {
						model.undoChanges(i);
						pb.nextStep();
					}

					Util.sleep(Constants.REFRESH_DELAY);
					pb.stopBar();
					model.setStopFlag(false);
					window.setAudioFileData(model.getCurrAudioFile());
				} catch (AudioFileException e) {
					logger.log(Level.SEVERE, "Error while audio file parsing:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("AudioFileParseError");
				} catch (IOException e) {
					logger.log(Level.SEVERE, "File not Found:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("FileNotFound");
				}

				updateTable(indices);
			}
		}).start();

	}

	/**
	 * loads the lyrics from a file
	 */
	private void loadLyricsBPressed() {
		logger.log(Level.FINER, "loadLyrics button pressed.");

		File f = ChooserUtil.openFile(window, new TxtFilter(), new File(Constants.DEFAULT_DIR));
		if (f == null)
			return;

		try {
			int[] i = window.getSelectedFiles();

			String lyrics = FileUtil.readFile(f);

			window.setLyricsTA(lyrics);

			model.setLyrics(lyrics);
			updateTable(i);
		} catch (IOException e) {
			window.showMessage("FileNotFound");
			logger.log(Level.SEVERE, "File not Found:\n" + LogUtil.getStackTrace(e), e);
		}

	}

	/**
	 * saves the lyrics to a file
	 */
	private void saveLyricsBPressed() {
		logger.log(Level.FINER, "saveLyrics button pressed.");

		File f = ChooserUtil.saveFile(window, null, new File(Constants.DEFAULT_DIR));
		if (f == null)
			return;

		try {
			if (window.getLyricsTA().length() == 0) {
				window.showMessage("noLyrics");
				return;
			}
			FileUtil.writeFile(window.getLyricsTA(), f);
		} catch (IOException e) {
			window.showMessage("FileNotFound");
			logger.log(Level.SEVERE, "File not Found.");
		}
	}

	/**
	 * deletes the selected lyrics
	 */
	private void deleteLyricsBPressed() {
		logger.log(Level.FINER, "deleteLyrivs button pressed.");

		// already empty -> do nothing
		if (window.getLyricsTA().length() == 0)
			return;

		int[] i = window.getSelectedFiles();
		model.setLyrics("");
		window.setLyricsTA("");

		updateTable(i);
	}

	/**
	 * plays the audio file using the defined player
	 */
	private void playButtonPressed() {
		logger.log(Level.FINER, "play audio button pressed. default player: " + Config.getInstance().isUseCustomPlayer());
		// use custom or default player
		int[] i = window.getSelectedFiles();

		if (i == null || i.length < 1)
			return;

		IAudioFile file = model.getAudioFile(i[0]);
		// use custom player
		if (Config.getInstance().isUseCustomPlayer()) {
			try {
				logger.log(Level.FINER, "call custom player cmd: " + Config.getInstance().getCustomPlayerCmd() + " \"" + file.getFilePath() + "\"");
				model.startCustomPlayer(file.getFilePath());
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while loading custom player:\n" + LogUtil.getStackTrace(e), e);
				window.showMessage("AudioPlayerError");
			}
		}
		// use default player
		else {
			try {
				AudioPlayerController con = new AudioPlayerController(file.getFilePath(), file.getName(), (int) file.getAudioLength(), file.getFrameCount());
				addObserver(con);
				con.createWindow();
			} catch (AudioPlayerException e) {
				logger.log(Level.SEVERE, "Error while loading custom player:\n" + LogUtil.getStackTrace(e), e);
				window.showMessage("AudioPlayerError");
			}
		}
	}

	/**
	 * auto add checkbox clicked
	 */
	private void autoAddChBPressed() {
		logger.log(Level.FINER, "autoAddChB pressed: " + window.getautoAddChB());

		if (window.getautoAddChB())
			window.setLoadButtonEnabled(false);
		else
			window.setLoadButtonEnabled(true);
	}

	/**
	 * clicked on of the "change all" radiobuttons
	 * 
	 * @param actionCmd
	 *            the given actioncommand to identify which radiobutton was
	 *            pressed
	 */
	private void changerRBPressed(String actionCmd) {
		window.resetRB();
		// backup last made changes
		ID3TagData data = window.getID3TagData();
		boolean changed = model.makeChanges(data);

		logger.log(Level.FINER, "changerRB pressed: " + actionCmd + " changes: " + changed);

		int[] i = window.getSelectedFiles();
		if (i == null || i.length < 1)
			return;

		switch (actionCmd) {
			case "titleRB": {
				model.changeAudioFileComponent(actionCmd, window.gettitleTF(), i);
				break;
			}
			case "artistRB": {
				model.changeAudioFileComponent(actionCmd, window.getartistTF(), i);
				break;
			}
			case "albumArtistRB": {
				model.changeAudioFileComponent(actionCmd, window.getalbumArtistTF(), i);
				break;
			}
			case "albumRB": {
				model.changeAudioFileComponent(actionCmd, window.getalbumTF(), i);
				break;
			}
			case "yearRB": {
				model.changeAudioFileComponent(actionCmd, window.getyearTF(), i);
				break;
			}
			case "trackRB": {
				model.changeAudioFileComponent(actionCmd, Integer.parseInt(window.getTrackNr()), i);
				break;
			}
			case "maxTracksRB": {
				model.changeAudioFileComponent(actionCmd, window.getMaxTracks(), i);
				break;
			}
			case "cdRB": {
				model.changeAudioFileComponent(actionCmd, window.getCurrCD(), i);
				break;
			}
			case "maxCDRB": {
				model.changeAudioFileComponent(actionCmd, window.getMaxCD(), i);
				break;
			}
			case "genreRB": {
				model.changeAudioFileComponent(actionCmd, window.getGenre(), i);
				break;
			}
			case "publisherRB": {
				model.changeAudioFileComponent(actionCmd, window.getPublisherTF(), i);
				break;
			}
			case "commentRB": {
				model.changeAudioFileComponent(actionCmd, window.getcommentTF(), i);
				break;
			}
			case "composerRB": {
				model.changeAudioFileComponent(actionCmd, window.getcomposerTF(), i);
				break;
			}
			case "origArtistRB": {
				model.changeAudioFileComponent(actionCmd, window.getorigArtistTF(), i);
				break;
			}
			case "copyrightRB": {
				model.changeAudioFileComponent(actionCmd, window.getcopyrightTF(), i);
				break;
			}
			case "urlRB": {
				model.changeAudioFileComponent(actionCmd, window.geturlTF(), i);
				break;
			}
			case "encodedByRB": {
				model.changeAudioFileComponent(actionCmd, window.getencodedByTF(), i);
				break;
			}
			case "imageAllRB": {
				model.changeAudioFileComponent(actionCmd, null, i);
				break;
			}
			case "lyricsRB": {
				model.changeAudioFileComponent(actionCmd, window.getLyricsTA(), i);
				break;
			}
		}

		updateTable(i);
	}

	/*
	 * tree value selected
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event
	 * .TreeSelectionEvent)
	 */
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if (!window.getautoAddChB())
			return;

		final String path = getTreePath();

		if (path == null)
			return;

		if (!new File(path).exists()) {
			logger.log(Level.FINER, "file: " + path + " doesnt exist.");
			window.showMessage("FileNotFound");
			return;
		}

		logger.log(Level.FINER, "selected Tree Element: " + path);

		model.clearAudioFiles();
		addAudioFiles(path);
	}

	/**
	 * gets the Path selected in the Tree
	 */
	private String getTreePath() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) window.getFilesTree().getLastSelectedPathComponent();

		if (node == null)
			return null;

		logger.log(Level.FINER, "selected treenode: " + node.getUserObject());

		String folderPath = FileUtil.getFilePath(window.getrootTF()) + "/" + Util.getPathFromJTree(node);
		logger.log(Level.FINER, "created Path: " + folderPath);

		return folderPath;
	}

	/*
	 * table value changed
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		logger.log(Level.FINER, "Table value changed");

		boolean updateTable = false;
		if (model.getCurrIndex() != -1) {
			updateTable = true;
			ID3TagData data = window.getID3TagData();
			boolean changed = model.makeChanges(data);
			logger.log(Level.FINER, "Table changed. changes: " + changed);
		}

		int[] i = window.getSelectedFiles();
		if (i == null || i.length < 1)
			return;

		logger.log(Level.FINER, "select audio file at index: " + i[0]);

		model.setCurrIndex(i[0]);
		IAudioFile file = model.getCurrAudioFile();
		try {
			window.setAudioFileData(file);
		} catch (IOException e1) {
			window.showMessage("AudioFileImageLoadError");
		}

		if (updateTable)
			updateTable(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {
		ID3TagData data = window.getID3TagData();
		boolean changed = model.makeChanges(data);
		logger.log(Level.FINER, "Focus gained. changes: " + changed);

		if (changed) {
			int[] i = window.getSelectedFiles();
			updateTable(i);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	@Override
	public void stateChanged(ChangeEvent arg0) {
		ID3TagData data = window.getID3TagData();
		boolean changed = model.makeChanges(data);
		logger.log(Level.FINER, "Tab changed. changes: " + changed);

		if (changed) {
			int[] i = window.getSelectedFiles();
			updateTable(i);
		}
	}

	/**
	 * deselects the table listener and updates the table, selects the given
	 * indices or the current audio file or null for no selections
	 * 
	 * @param indices
	 *            given indices or null
	 */
	private void updateTable(int[] indices) {
		window.removeListSelectionListener(this);
		window.saveTableWidth();
		window.setTableModel(model.getTableModel());

		if (indices == null || indices.length < 1) {
			if (model.getCurrIndex() != -1)
				window.setSelectedFile(model.getCurrIndex());
		} else
			window.setSelectedFiles(indices);
		window.setListSelectionListener(this);
	}

	/**
	 * stops all by this application opened player
	 */
	private void stopPlayers() {
		logger.log(Level.FINER, "Stop players.");
		// stop custom player
		setChanged();
		notifyObservers("closePlayer");
		// stop player
		model.stopPlayer();
	}

	/**
	 * adds the folder to the list
	 * 
	 * @param path
	 *            given folder path
	 */
	private void addAudioFiles(final String path) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new CounterProgressBar();
				try {
					int c = Commons.countAudioFiles(path, window.getrecursiveChB());
					pb.setCancelCommand(cancelCmd);

					pb.setMax(c);
					pb.start();
					model.addAudioFiles(path, window.getrecursiveChB(), pb);

				} catch (AudioFileException e) {
					logger.log(Level.SEVERE, "Error while reading audio files:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("AudioFileParseError");
				}

				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();

				model.setCurrIndex(-1);
				window.resetAudioFileData();
				updateTable(null);
				model.setStopFlag(false);

			}
		}).start();
	}

	/**
	 * adds the given files to the list
	 * 
	 * @param files
	 *            given file list
	 */
	private void addAudioFiles(final List<String> files) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				IProgressBar pb = new CounterProgressBar();
				try {
					int c = files.size();
					pb.setCancelCommand(cancelCmd);
					pb.setMax(c);
					pb.start();

					for (String f : files) {
						model.addAudioFile(f);
						pb.nextStep();
					}
				} catch (AudioFileException e) {
					logger.log(Level.SEVERE, "Error while reading audio files:\n" + LogUtil.getStackTrace(e), e);
					window.showMessage("AudioFileParseError");
				}

				Util.sleep(Constants.REFRESH_DELAY);
				pb.stopBar();
				model.setCurrIndex(-1);
				window.resetAudioFileData();
				updateTable(null);
				model.setStopFlag(false);

			}
		}).start();
	}

	/**
	 * checks if the file/folder exists and is not null
	 * 
	 * @param file
	 *            the file or folder
	 * @param isFolder
	 *            true for a folder, else false
	 * 
	 * @return true if valid, else false
	 */
	private boolean checkFile(File file, boolean isFolder) {
		if (file == null)
			return false;

		if (!new File(file.getAbsolutePath()).exists()) {
			if (isFolder) {
				logger.log(Level.FINER, "file: " + file.getAbsolutePath() + " doesnt exist.");
				window.showMessage("FileNotFound");
			} else {
				logger.log(Level.FINER, "folder: " + file.getAbsolutePath() + " doesnt exist.");
				window.showMessage("FolderNotFound");
			}
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see controller.interfaces.AbstractController#saveConfig()
	 */
	@Override
	public void saveConfig() {
		Config.getInstance().setID3Recursive(window.getrecursiveChB());
		Config.getInstance().setID3AutoAdd(window.getautoAddChB());
		Config.getInstance().setID3AllChanged(window.getSaveAllChangedRB());
	}
}