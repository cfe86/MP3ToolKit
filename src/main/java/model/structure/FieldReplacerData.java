package model.structure;

import java.util.List;

public class FieldReplacerData {

	/*
	 * the fields
	 */
	public static final String TITLE = "title";
	public static final String ARTIST = "artist";
	public static final String ALBUMARTIST = "albumArtist";
	public static final String ALBUM = "album";
	public static final String YEAR = "year";
	public static final String TRACK = "track";
	public static final String MAXTRACKS = "maxTracks";
	public static final String CD = "cd";
	public static final String MAXCD = "maxCd";
	public static final String GENRE = "genre";
	public static final String COMMENT = "comment";
	public static final String COMPOSER = "composer";
	public static final String ORIGARTIST = "origArtist";
	public static final String COPYRIGHT = "copyright";
	public static final String URL = "url";
	public static final String ENCODEDBY = "encoded";
	public static final String LYRICS = "lyrics";

	/**
	 * array for all fields, if index is true, this field should be replaced
	 */
	private boolean[] checkedFields;

	/**
	 * array for all process actions, if index is true, this process should be
	 * operated
	 */
	private boolean[] checkedProcesses;

	/**
	 * a list with all convert elements
	 */
	private List<ConvertElement> convertElements;

	/**
	 * true if all selected, if this is true all loaded files will be changed,
	 * if false just the selected once
	 */
	private boolean selectAll;

	/**
	 * Constructor
	 * 
	 * @param selectAll
	 *            true if all is selected
	 * @param checkedFields
	 *            the checked fields
	 * @param checkProcesses
	 *            the checked processes
	 * @param convertElements
	 *            the convert Elements
	 */
	public FieldReplacerData(boolean selectAll, boolean[] checkedFields, boolean[] checkProcesses, List<ConvertElement> convertElements) {
		this.checkedFields = checkedFields;
		this.checkedProcesses = checkProcesses;
		this.convertElements = convertElements;
		this.selectAll = selectAll;
	}

	public boolean[] getCheckedFields() {
		return checkedFields;
	}

	public void setCheckedFields(boolean[] checkedFields) {
		this.checkedFields = checkedFields;
	}

	public boolean[] getCheckedProcesses() {
		return checkedProcesses;
	}

	public void setCheckedProcesses(boolean[] checkedProcesses) {
		this.checkedProcesses = checkedProcesses;
	}

	public List<ConvertElement> getConvertElements() {
		return convertElements;
	}

	public void setConvertElements(List<ConvertElement> convertElements) {
		this.convertElements = convertElements;
	}

	public boolean isSelectAll() {
		return selectAll;
	}

	public void setSelectAll(boolean selectAll) {
		this.selectAll = selectAll;
	}

	/**
	 * changes the field with the given value if the field should be changed
	 * 
	 * @param field
	 *            the field (see attributes for all fields)
	 * @param value
	 *            the value which should be changed
	 * 
	 * @return the unmodified value if the field shouldn't be changed, else the
	 *         modified value
	 */
	public String changeField(String field, String value) {
		if (field.equals(FieldReplacerData.TITLE) && !checkedFields[0])
			return value;
		if (field.equals(FieldReplacerData.ARTIST) && !checkedFields[1])
			return value;
		if (field.equals(FieldReplacerData.ALBUMARTIST) && !checkedFields[2])
			return value;
		if (field.equals(FieldReplacerData.ALBUM) && !checkedFields[3])
			return value;
		if (field.equals(FieldReplacerData.YEAR) && !checkedFields[4])
			return value;
		if (field.equals(FieldReplacerData.TRACK) && !checkedFields[5])
			return value;
		if (field.equals(FieldReplacerData.MAXTRACKS) && !checkedFields[6])
			return value;
		if (field.equals(FieldReplacerData.CD) && !checkedFields[7])
			return value;
		if (field.equals(FieldReplacerData.MAXCD) && !checkedFields[8])
			return value;
		if (field.equals(FieldReplacerData.GENRE) && !checkedFields[9])
			return value;
		if (field.equals(FieldReplacerData.COMMENT) && !checkedFields[10])
			return value;
		if (field.equals(FieldReplacerData.COMPOSER) && !checkedFields[11])
			return value;
		if (field.equals(FieldReplacerData.ORIGARTIST) && !checkedFields[12])
			return value;
		if (field.equals(FieldReplacerData.COPYRIGHT) && !checkedFields[13])
			return value;
		if (field.equals(FieldReplacerData.URL) && !checkedFields[14])
			return value;
		if (field.equals(FieldReplacerData.ENCODEDBY) && !checkedFields[15])
			return value;
		if (field.equals(FieldReplacerData.LYRICS) && !checkedFields[16])
			return value;

		// ' ' to '_'
		if (checkedProcesses[0])
			value = value.replace(" ", "_");
		// '_' to ' '
		else if (checkedProcesses[1])
			value = value.replace("_", " ");
		// remove ' '
		else if (checkedProcesses[2])
			value = value.replace(" ", "");

		// all uppercase
		if (checkedProcesses[3])
			value = value.toUpperCase();
		// all lowercase
		else if (checkedProcesses[4])
			value = value.toLowerCase();
		// first letter upper
		else if (checkedProcesses[5]) {
			// split at ' '
			String newValue = "";
			String[] tmp = value.split(" ");
			for (int i = 0; i < tmp.length; i++)
				newValue += makeFirstUppercase(tmp[i]) + " ";

//			tmp = newValue.trim().split("_");
//			for (int i = 0; i < tmp.length; i++)
//				value += makeFirstUppercase(tmp[i]) + "_";

//			value = value.substring(0, value.length() - 1);
			value = newValue.trim();
		}

		// convert list
		if (checkedProcesses[6]) {
			for (ConvertElement ele : convertElements)
				value = value.replace(ele.getConvertFrom(), ele.getConvertTo());
		}

		return value;
	}

	/**
	 * makes the first letter for the given word to uppercase
	 * 
	 * @param word
	 *            given word
	 * 
	 * @return modified word
	 */
	private String makeFirstUppercase(String word) {
		if (word == null || word.length() == 0)
			return word;

		if (word.length() == 1)
			return word.toUpperCase();

		return Character.toString(word.charAt(0)) + word.substring(1);
	}
}