package controller.interfaces;

public interface ICmdImageSettings {

	/**
	 * the new width and height for the image, -1, -1 for nothing, 0 and 0 for
	 * delete
	 * 
	 * @param width
	 *            the new width
	 * @param height
	 *            the new height
	 */
	public void call(int width, int height);
}