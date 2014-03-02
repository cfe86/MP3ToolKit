package view.structure;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the image
	 */
	private Image image;

	/**
	 * Constructor
	 * 
	 * @param image
	 *            the image for the background
	 */
	public ImagePanel(Image image) {
		this.image = image;
		repaint();
	}

	/**
	 * sets a new image. The new Image will be drawn instantly.
	 * 
	 * @param img
	 *            the new image
	 */
	public void setImage(Image img) {
		this.image = img;
		repaint();
	}

	/**
	 * draws the new image
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
}
