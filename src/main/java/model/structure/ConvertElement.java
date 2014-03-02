package model.structure;

public class ConvertElement {

	/**
	 * the conert from string which will be replaced with convertTo
	 */
	private String convertFrom;

	/**
	 * the convertTo String
	 */
	private String convertTo;

	/**
	 * convert in the current language
	 */
	private String convert;

	/**
	 * to in the current language
	 */
	private String to;

	/**
	 * Constructor
	 */
	public ConvertElement() {
		convertFrom = "";
		convertTo = "";
		convert = "convert";
		to = "to";
	}

	/**
	 * Constructor
	 * 
	 * @param convertFrom
	 *            the convert from element
	 * @param convertTo
	 *            the convert to element
	 */
	public ConvertElement(String convertFrom, String convertTo) {
		this.convertFrom = convertFrom;
		this.convertTo = convertTo;
		convert = "convert";
		to = "to";
	}

	/**
	 * set 'convert' in the current language
	 * 
	 * @param c
	 *            the convert translation
	 */
	public void setConvert(String c) {
		this.convert = c;
	}

	/**
	 * set 'to' in the current language
	 * 
	 * @param to
	 *            the to translation
	 */
	public void setTo(String to) {
		this.to = to;
	}

	public String getConvertFrom() {
		return convertFrom;
	}

	public void setConvertFrom(String convertFrom) {
		this.convertFrom = convertFrom;
	}

	public String getConvertTo() {
		return convertTo;
	}

	public void setConvertTo(String convertTo) {
		this.convertTo = convertTo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return convert + " '" + convertFrom + "' " + to + " '" + convertTo + "'";
	}
}