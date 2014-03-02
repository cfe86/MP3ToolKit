package model;

import java.util.ArrayList;
import java.util.List;

import model.structure.ID3ImageData;

import com.cf.structures.DataDouble;

public class ChangeImageDataModel {

	/**
	 * list containing elements with a mapping old image data -> new image data
	 */
	private List<DataDouble<ID3ImageData, ID3ImageData>> images;

	/**
	 * current index of the element in the images list
	 */
	private int currIndex;

	/**
	 * list of elements which represent the new images (if the new image
	 * checkbox is not selected the old image data will be added, if selected
	 * the new image data)
	 */
	private List<ID3ImageData> newImages;

	/**
	 * Constructor
	 */
	public ChangeImageDataModel() {
		images = new ArrayList<DataDouble<ID3ImageData, ID3ImageData>>();
		this.newImages = new ArrayList<ID3ImageData>();
		this.currIndex = -1;
	}

	/**
	 * gets the new images
	 * 
	 * @return list of new image data
	 */
	public List<ID3ImageData> getNewImages() {
		return this.newImages;
	}

	/**
	 * sets the image data mapping list
	 * 
	 * @param images
	 *            the list
	 */
	public void setImages(List<DataDouble<ID3ImageData, ID3ImageData>> images) {
		this.images = images;
	}

	/**
	 * sets the new current index
	 * 
	 * @param index
	 *            the current index
	 */
	public void setIndex(int index) {
		this.currIndex = index;
	}

	/**
	 * gets the current index
	 * 
	 * @return the current index
	 */
	public int getIndex() {
		return this.currIndex;
	}

	/**
	 * increments current index
	 */
	public void incrementIndex() {
		this.currIndex++;
	}

	/**
	 * gets the old image data of the current element
	 * 
	 * @return the old image data
	 */
	public ID3ImageData getCurrImage() {
		return this.images.get(currIndex).getFirst();
	}

	/**
	 * gets the number of elements in the list
	 * 
	 * @return the list length
	 */
	public int getListSize() {
		return this.images.size();
	}

	/**
	 * gets the new image data of the current element
	 * 
	 * @return the new image data
	 */
	public ID3ImageData getNewImage() {
		return this.images.get(currIndex).getSecond();
	}

	/**
	 * adds the current element to the new images list
	 * 
	 * @param newOne
	 *            true if new image data should be added, false if old image
	 *            data should be added
	 */
	public void createNewImage(boolean newOne) {
		if (currIndex == -1)
			return;

		ID3ImageData data = new ID3ImageData(this.images.get(currIndex).getFirst().getIndex());
		data.setImage(!newOne ? this.images.get(currIndex).getFirst().getImage() : this.images.get(currIndex).getSecond().getImage());
		data.setExtension(!newOne ? this.images.get(currIndex).getFirst().getExtension() : this.images.get(currIndex).getSecond().getExtension());
		data.setChanged(newOne);

		this.newImages.add(data);
	}

	/**
	 * checks if all images got selected
	 * 
	 * @return true if done, else false
	 */
	public boolean isDone() {
		return currIndex == this.images.size();
	}
}