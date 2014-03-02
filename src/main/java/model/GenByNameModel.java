package model;

public class GenByNameModel {

	/**
	 * Constructor
	 */
	public GenByNameModel() {

	}

	/**
	 * creates a new ID3Regex depending on the given regex string
	 * 
	 * @param regex
	 *            the regex string
	 * 
	 * @return the ID3Regex object
	 */
	public ID3TagRegex generateRegex(String regex) {
		return new ID3TagRegex(regex);
	}
}