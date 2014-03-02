package model.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class TxtFilter extends FileFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		return f.getAbsolutePath().toLowerCase().endsWith("txt");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return "*.txt";
	}
}