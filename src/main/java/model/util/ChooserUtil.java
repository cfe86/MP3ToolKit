package model.util;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import config.Constants;

public class ChooserUtil {

	/**
	 * creates a filechooser to open a folder
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param file
	 *            the selected file, null if no file should be selected
	 * 
	 * @return the chosen Folder as a File Object. Null if no folder was
	 *         selected.
	 */
	public static File openFolder(Component parent, File file) {
		return openFolder(parent, file, "");
	}

	/**
	 * creates a filechooser to open a folder
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param file
	 *            the selected file, null if no file should be selected
	 * @param defaultPath
	 *            the default folder
	 * 
	 * @return the chosen Folder as a File Object. Null if no folder was
	 *         selected.
	 */
	public static File openFolder(Component parent, File file, String defaultPath) {
		File result = null;
		// create JFileChooser and set Filter
		JFileChooser fc;
		if (defaultPath == null || defaultPath.trim().equals(""))
			fc = new JFileChooser(Constants.DEFAULT_DIR);
		else fc = new JFileChooser(defaultPath);
		
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (file != null)
			fc.setSelectedFile(file);

		int returnVal = fc.showOpenDialog(parent);

		// get File and parse it
		if (returnVal == JFileChooser.APPROVE_OPTION)
			result = fc.getSelectedFile();

		return result;
	}

	/**
	 * opens a file dialog to open a file
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param filter
	 *            given FileFilter
	 * @param file
	 *            the selected file, null if no file should be selected
	 * 
	 * @return the chosen File as a File Object. Null if no file was selected.
	 */
	public static File openFile(Component parent, FileFilter filter, File file) {
		File result = null;
		// create JFileChooser and set Filter
		JFileChooser fc;
		if (file == null)
			fc = new JFileChooser(Constants.DEFAULT_DIR);
		else fc = new JFileChooser();
		
		if (filter != null)
			fc.setFileFilter(filter);

		if (file != null)
			fc.setSelectedFile(file);

		int returnVal = fc.showOpenDialog(parent);

		// get File and parse it
		if (returnVal == JFileChooser.APPROVE_OPTION)
			result = fc.getSelectedFile();

		return result;
	}

	/**
	 * opens a file dialog to save a file
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param filter
	 *            given FileFilter
	 * @param file
	 *            the selected file, null if no file should be selected
	 * 
	 * @return the chosen File as a File Object. Null if no file was selected.
	 */
	public static File saveFile(Component parent, FileFilter filter, File file) {
		File result = null;
		// create JFileChooser and set Filter
		JFileChooser fc;
		if (file == null)
			fc = new JFileChooser(Constants.DEFAULT_DIR);
		else fc = new JFileChooser();
		
		if (filter != null)
			fc.setFileFilter(filter);

		if (file != null)
			fc.setSelectedFile(file);

		int returnVal = fc.showSaveDialog(parent);

		// get File and parse it
		if (returnVal == JFileChooser.APPROVE_OPTION)
			result = fc.getSelectedFile();

		return result;
	}

	/**
	 * opens a file dialog with a custom FileChooser
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param filter
	 *            given FileFilter
	 * @param buttonText
	 *            the text of the not cancel button (usually the save or open
	 *            button)
	 * @param file
	 *            the selected file, null if no file should be selected
	 * 
	 * @return the chosen File as a File Object. Null if no file was selected.
	 */
	public static File customFileChooser(Component parent, FileFilter filter, String buttonText, File file) {
		File result = null;
		// create JFileChooser and set Filter
		JFileChooser fc;
		if (file == null)
			fc = new JFileChooser(Constants.DEFAULT_DIR);
		else fc = new JFileChooser();
		
		if (filter != null)
			fc.setFileFilter(filter);

		if (file != null)
			fc.setSelectedFile(file);

		int returnVal = fc.showDialog(parent, buttonText);

		// get File and parse it
		if (returnVal == JFileChooser.APPROVE_OPTION)
			result = fc.getSelectedFile();

		return result;
	}

	/**
	 * opens a color chooser dialog
	 * 
	 * @param parent
	 *            parent component e.g. the window
	 * @param title
	 *            the chooser title
	 * @param initColor
	 *            init color, null if default color (black) should be used
	 * 
	 * @return the selected color or null if the chooser was canceled
	 */
	public static Color colorChooser(Component parent, String title, Color initColor) {
		return JColorChooser.showDialog(parent, title, initColor);
	}
}