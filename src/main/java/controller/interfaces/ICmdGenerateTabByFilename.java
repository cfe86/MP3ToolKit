package controller.interfaces;

import model.ID3TagRegex;

public interface ICmdGenerateTabByFilename {

	/**
	 * transmits the filename regex to the id3 tab controller
	 * 
	 * @param regex
	 *            the id3 regex
	 * @param selectAll
	 *            true if all is selected, else false
	 */
	public void call(ID3TagRegex regex, boolean selectAll);
}