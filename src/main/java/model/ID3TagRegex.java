package model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import model.regex.Regex;

public class ID3TagRegex {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * component shorts
	 */
	public static final String TITLE = "%t";
	public static final String ARTIST = "%a";
	public static final String ALBUMARTIST = "%b";
	public static final String ALBUM = "%z";
	public static final String YEAR = "%y";
	public static final String TRACK = "%n";
	public static final String MAXTRACKS = "%l";
	public static final String CD = "%c";
	public static final String MAXCD = "%x";
	public static final String GENRE = "%g";
	public static final String COMMENT = "%q";
	public static final String PUBLISHER = "%p";
	public static final String COMPOSER = "%s";
	public static final String ORIGARTIST = "%o";
	public static final String COPYRIGHT = "%r";
	public static final String URL = "%u";
	public static final String ENCODEDBY = "%e";

	/**
	 * given source regex
	 */
	private String src;

	/**
	 * given target regx
	 */
	private String target;

	/**
	 * the regex string which is generated from the source to match each file
	 * e.g. (.*?)-(.*?)
	 */
	private String regexString;

	/**
	 * the regex object
	 */
	private Regex regex;

	/**
	 * maps the index to the corresponding short e.g. %t-%n-%a would be 1:=%t,
	 * 2:=%n, 3:=%a
	 */
	private Map<Integer, String> groupIndex;

	/**
	 * array which represents if the generated regex contains the short e.g.
	 * index 0 = 1 means the Title is included
	 */
	private boolean[] containsTags;

	/**
	 * Constructor
	 * 
	 * @param regex
	 *            given regex string
	 */
	public ID3TagRegex(String regex) {
		this.src = regex;
		this.target = "";
		this.groupIndex = new HashMap<Integer, String>();

		findGroupIndex();
		generateRegex();
		generateContainedTags();
	}

	/**
	 * Constructor
	 * 
	 * @param src
	 *            given source regex
	 * @param target
	 *            given target regex
	 */
	public ID3TagRegex(String src, String target) {
		this.src = src;
		this.target = target;
		this.groupIndex = new HashMap<Integer, String>();

		findGroupIndex();
		generateRegex();
		generateContainedTags();
	}

	/**
	 * finds the group index for every shortcut e.g. %t-%n-%a would be 1:=%t,
	 * 2:=%n, 3:=%a
	 */
	private void findGroupIndex() {
		int index = 1;
		for (int i = 0; i < src.length(); i++) {
			// found %
			if (src.charAt(i) == '%') {
				// check if there is one more char
				if (i < src.length() - 1) {
					// check if sequence is valid
					if (isValid(src.charAt(i + 1))) {
						logger.log(Level.FINER, "found groupindex: " + index + " with %" + src.charAt(i + 1));
						this.groupIndex.put(index, "%" + src.charAt(i + 1));
						index++;
					}
				}
			}
		}
	}

	/**
	 * generated the containsTag array
	 */
	private void generateContainedTags() {
		this.containsTags = new boolean[] { containsTag(ID3TagRegex.TITLE), containsTag(ID3TagRegex.ARTIST), containsTag(ID3TagRegex.ALBUMARTIST), containsTag(ID3TagRegex.ALBUM),
				containsTag(ID3TagRegex.YEAR), containsTag(ID3TagRegex.TRACK), containsTag(ID3TagRegex.MAXTRACKS), containsTag(ID3TagRegex.CD), containsTag(ID3TagRegex.MAXCD),
				containsTag(ID3TagRegex.GENRE), containsTag(ID3TagRegex.COMMENT), containsTag(ID3TagRegex.COMPOSER), containsTag(ID3TagRegex.ORIGARTIST),
				containsTag(ID3TagRegex.COPYRIGHT), containsTag(ID3TagRegex.URL), containsTag(ID3TagRegex.ENCODEDBY) };
	}

	/**
	 * true if the given tag is in the regex, else false
	 * 
	 * @param tag
	 *            the tag to check
	 * 
	 * @return true or false
	 */
	private boolean containsTag(String tag) {
		for (String t : this.groupIndex.values())
			if (t.equals(tag))
				return true;

		return false;
	}

	/**
	 * generates a regex from the src String and escapes all regex characters
	 * e.g. %t-%n-%a would be (.*?)-(.*?)-(.*?)
	 */
	private void generateRegex() {
		this.regexString = src;
		Regex.replaceRegexChars(this.regexString);

		this.regexString = this.regexString.replace(TITLE, "(.*?)");
		this.regexString = this.regexString.replace(ARTIST, "(.*?)");
		this.regexString = this.regexString.replace(ALBUMARTIST, "(.*?)");
		this.regexString = this.regexString.replace(ALBUM, "(.*?)");
		this.regexString = this.regexString.replace(YEAR, "(.*?)");
		this.regexString = this.regexString.replace(TRACK, "(.*?)");
		this.regexString = this.regexString.replace(MAXTRACKS, "(.*?)");
		this.regexString = this.regexString.replace(CD, "(.*?)");
		this.regexString = this.regexString.replace(MAXCD, "(.*?)");
		this.regexString = this.regexString.replace(GENRE, "(.*?)");
		this.regexString = this.regexString.replace(COMMENT, "(.*?)");
		this.regexString = this.regexString.replace(PUBLISHER, "(.*?)");
		this.regexString = this.regexString.replace(COMPOSER, "(.*?)");
		this.regexString = this.regexString.replace(ORIGARTIST, "(.*?)");
		this.regexString = this.regexString.replace(COPYRIGHT, "(.*?)");
		this.regexString = this.regexString.replace(URL, "(.*?)");
		this.regexString = this.regexString.replace(ENCODEDBY, "(.*?)");

		logger.log(Level.FINER, "new Regex string is: " + this.regex);
		this.regex = new Regex(this.regexString, Pattern.CASE_INSENSITIVE);
	}

	/**
	 * checks if %Char is valid
	 * 
	 * @param c
	 *            given Char
	 * 
	 * @return true if valid, else false
	 */
	private boolean isValid(char c) {
		switch (c) {
			case 't':
				return true;
			case 'a':
				return true;
			case 'b':
				return true;
			case 'z':
				return true;
			case 'y':
				return true;
			case 'n':
				return true;
			case 'l':
				return true;
			case 'c':
				return true;
			case 'x':
				return true;
			case 'g':
				return true;
			case 'q':
				return true;
			case 'p':
				return true;
			case 's':
				return true;
			case 'o':
				return true;
			case 'r':
				return true;
			case 'u':
				return true;
			case 'e':
				return true;
		}

		return false;
	}

	/**
	 * modifies the given name e.g. %t-%n-%a to %a-%n-%t from "Track-Nr-Artist"
	 * would return "Artist-Nr-Track"
	 * 
	 * @param name
	 *            given name of the file
	 * 
	 * @return the new name depending on the given src and target
	 */
	public String modifyString(String name) {
		logger.log(Level.FINER, "modify String: " + name + " match: " + this.regex.matches(name));
		if (!this.regex.matches(name))
			return name;

		String result = target;
		for (Integer i : this.groupIndex.keySet()) {
			result = result.replace(this.groupIndex.get(i), this.regex.getGroup(i));
		}

		return result;
	}

	/**
	 * gets the given value for of the given name depending on the given regex
	 * 
	 * @param tag
	 *            given tag (can be found as attributes in ID3TagRegex)
	 * @param name
	 *            given filename
	 * 
	 * @return the value, or null if it doesnt exist
	 */
	public String getTag(String tag, String name) {
		int index = getAttributIndex(tag);

		if (index == -1)
			return "";

		if (regex.matches(name))
			return regex.getGroup(index).trim();

		return "";
	}

	/**
	 * gets the index for the given tag
	 * 
	 * @param tag
	 *            given tag
	 * 
	 * @return the index for the saved regex. -1 if the tag doesn't exists
	 */
	private int getAttributIndex(String tag) {
		for (int index : this.groupIndex.keySet()) {
			if (tag.equals(this.groupIndex.get(index)))
				return index;
		}

		return -1;
	}

	/**
	 * gets the containsTag array
	 * 
	 * @return the array
	 */
	public boolean[] getContainsTags() {
		return containsTags;
	}
}