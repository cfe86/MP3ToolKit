package model.transferhandler;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.TransferHandler;

import logging.LogUtil;

abstract public class FolderTransferHandler extends TransferHandler {

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

			if (data.size() == 0)
				return false;

			File folder = data.get(0);

			if (!folder.isDirectory())
				return false;

			setFolder(folder.getAbsolutePath());

		} catch (UnsupportedFlavorException | IOException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error while adding file or folder:\n" + LogUtil.getStackTrace(e), e);
		}

		return true;
	}

	/**
	 * sets the given folder
	 * 
	 * @param folder
	 *            path to the given folder
	 */
	abstract public void setFolder(String folder);
}