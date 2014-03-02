package model.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.TransferHandler;

import logging.LogUtil;

abstract public class FileFolderTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 202925720699164453L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.
	 * TransferSupport)
	 */
	@Override
	public boolean canImport(TransferHandler.TransferSupport info) {
		// we only import FileList
		if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.
	 * TransferSupport)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferHandler.TransferSupport info) {
		if (!info.isDrop()) {
			return false;
		}

		// Check for FileList flavor
		if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
			return false;
		}

		try {
			// Get the fileList that is being dropped.
			Transferable t = info.getTransferable();
			List<File> data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			List<String> folder = new ArrayList<>();
			List<String> files = new ArrayList<>();

			// sort lists in files and folders
			for (File f : data) {
				if (f.isDirectory())
					folder.add(f.getAbsolutePath());
				else if (f.isFile())
					files.add(f.getAbsolutePath());
			}

			// folders to add? -> add
			if (folder.size() > 0) {
				for (String f : folder)
					addFolder(f);
			}

			// files to add? -> add
			if (files.size() > 0)
				addFiles(files);

		} catch (UnsupportedFlavorException | IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error while adding file or folder:\n" + LogUtil.getStackTrace(e), e);
		}

		return true;
	}

	/**
	 * adds a folder
	 * 
	 * @param path
	 *            path to the folder
	 */
	abstract public void addFolder(String path);

	/**
	 * adds files
	 * 
	 * @param files
	 *            paths to the files
	 */
	abstract public void addFiles(List<String> files);
}