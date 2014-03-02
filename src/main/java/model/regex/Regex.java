package model.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	/**
	 * the pattern
	 */
	private Pattern pattern;

	/**
	 * the matcher
	 */
	private Matcher matcher;

	/**
	 * Constructor
	 */
	public Regex(String regex) {
		this.pattern = Pattern.compile(regex);
	}

	/**
	 * constructor
	 * 
	 * @param regex
	 *            the given regex
	 * @param flag
	 *            the given flag. All flags can be found in Pattern. e.g.
	 *            Pattern.MULTILINE
	 */
	public Regex(String regex, int flag) {
		this.pattern = Pattern.compile(regex, flag);
	}

	/**
	 * matches the given String with the regex
	 * 
	 * @param str
	 *            given String
	 * 
	 * @return true if it matches, else false
	 */
	public boolean matches(String str) {
		this.matcher = this.pattern.matcher(str);
		return this.matcher.matches();
	}

	/**
	 * finds all occurrences of the given String and returns them in a List
	 * 
	 * @param str
	 *            given String
	 * 
	 * @return the matches
	 */
	public List<String> find(String str) {
		List<String> result = new ArrayList<String>();
		this.matcher = this.pattern.matcher(str);

		while (this.matcher.find())
			result.add(this.matcher.group());

		return result;
	}

	/**
	 * after Regex.matches() was used gives the given group back
	 * 
	 * @param i
	 *            index of the group
	 * 
	 * @return the group as a String
	 */
	public String getGroup(int i) {
		return this.matcher.group(i);
	}

	/**
	 * replaces all regular expression specific characters with their regex
	 * counterparts, e.g. . with \\.
	 * 
	 * @param str
	 *            given string
	 * 
	 * @return the modified String
	 */
	public static String replaceRegexChars(String str) {
		str = str.replace("\\", "\\\\");
		str = str.replace(".", "\\.");

		return str;
	}

	/**
	 * replaces all regular expression specific characters with their normal
	 * presentation e.g \\. -> .
	 * 
	 * @param str
	 *            given regex String
	 * 
	 * @return the modified String
	 */
	public static String removeRegexChars(String str) {
		str = str.replace("\\\\", "\\");
		str = str.replace("\\.", ".");

		return str;
	}
}