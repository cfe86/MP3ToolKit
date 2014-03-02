package controller.interfaces;

import model.structure.FieldReplacerData;

public interface ICmdFieldReplacer {

	/**
	 * transmits the given field replacer data to the id3 tab controller
	 * 
	 * @param data
	 *            the field replacer data
	 */
	public void call(FieldReplacerData data);
}