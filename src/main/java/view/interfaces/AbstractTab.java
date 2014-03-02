package view.interfaces;

import java.util.Locale;

import javax.swing.JPanel;

import model.exception.TabInitException;

abstract public class AbstractTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8332877415008979092L;

	/**
	 * inits the tab
	 * 
	 * @throws TabInitException
	 *             thrown if the tab couldn't be initialised
	 */
	abstract public void init() throws TabInitException;

	/**
	 * changes the language to the given language
	 * 
	 * @param lang
	 *            given language
	 */
	abstract public void changeLanguage(Locale lang);
}