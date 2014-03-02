package model.collector.interfaces;

import java.awt.image.BufferedImage;

import model.structure.ID3TagData;

public interface ICoverArtCollector extends ICollector {

	/**
	 * gets the image as bytes
	 * 
	 * @return the image in bytes
	 */
	public byte[] getImageAsBytes();

	/**
	 * gets the image as image
	 * 
	 * @return the image
	 */
	public BufferedImage getImage();

	/**
	 * gets the image extension
	 * 
	 * @return the extension
	 */
	public String getExtension();

	/**
	 * gets the extension MIME type
	 * 
	 * @return the MIME type
	 */
	public int id3ImageType();

	/**
	 * sets the data which is neccessary to search for the cover
	 * 
	 * @param data
	 *            the data
	 */
	public void setData(ID3TagData data);
}