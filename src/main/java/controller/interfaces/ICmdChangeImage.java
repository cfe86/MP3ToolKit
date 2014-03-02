package controller.interfaces;

import java.util.List;

import model.structure.ID3ImageData;

public interface ICmdChangeImage {

	/**
	 * changes all image data
	 * 
	 * @param data
	 *            list with all image data
	 */
	public void call(List<ID3ImageData> data);
}