package controller.interfaces;

import java.util.List;

import model.structure.ID3TagData;

public interface ICmdChangeID3Data {

	/**
	 * changes the given id3 data
	 * 
	 * @param data
	 *            list of all id3 data changes
	 */
	public void call(List<ID3TagData> data);
}