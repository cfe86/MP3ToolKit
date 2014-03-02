package model.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import config.Constants;

public class ImageFilter extends FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		for (int i = 0; i < Constants.VALID_IMAGE_EXTENSION.length; i++) {
			if (f.getAbsolutePath().toLowerCase().endsWith(Constants.VALID_IMAGE_EXTENSION[i]))
				return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		String descr = "";
		for (int i = 0; i < Constants.VALID_IMAGE_EXTENSION.length; i++)
			descr += ".*" + Constants.VALID_IMAGE_EXTENSION[i] + ", ";

		if (descr.trim().endsWith(","))
			descr = descr.trim().substring(0, descr.trim().length() - 1);

		return descr;
	}
}