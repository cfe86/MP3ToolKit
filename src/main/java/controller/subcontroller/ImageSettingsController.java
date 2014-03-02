package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.interfaces.ICmdImageSettings;
import model.util.Graphics;
import model.util.WindowUtils;
import view.subview.id3.ImageSettingsView;

public class ImageSettingsController extends Observable implements FocusListener, ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the window
	 */
	public ImageSettingsView window;

	/**
	 * current width
	 */
	private int width;

	/**
	 * current height
	 */
	private int height;

	/**
	 * true if button is linked, else unlinked
	 */
	private boolean linked;

	/**
	 * linked image
	 */
	private BufferedImage link;

	/**
	 * unlinked image
	 */
	private BufferedImage unlink;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdImageSettings closeCommand;

	/**
	 * Constructor
	 */
	public ImageSettingsController() {
		this.linked = true;
		this.link = Graphics.readImageFromJarWoExc("view/images/id3tag/subwindows/link.png");
		this.unlink = Graphics.readImageFromJarWoExc("view/images/id3tag/subwindows/unlink.png");
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCommand(ICmdImageSettings cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 * 
	 * @param width
	 *            current width
	 * @param height
	 *            current height
	 */
	public void createWindow(int width, int height) {
		this.width = width;
		this.height = height;

		window = new ImageSettingsView();

		window.init();

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow(-1, -1);
			}
		});

		logger.log(Level.FINER, "width: " + this.width + " height: " + this.height);
		window.setImageSize(this.width, this.height);
		window.setFocusListener(this);
	}

	/**
	 * closes the window
	 * 
	 * @param width
	 *            new width
	 * @param height
	 *            new height
	 */
	public void closeWindow(int width, int height) {
		window.dispose();
		closeCommand.call(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("okB"))
			okBPressed();
		else if (e.getActionCommand().equals("cancelB"))
			cancelBPressed();
		else if (e.getActionCommand().equals("linkB"))
			linkBPressed();
		else if (e.getActionCommand().equals("resizeRB"))
			resizeRBPressed();
		else if (e.getActionCommand().equals("deleteRB"))
			deleteRBPressed();
	}

	/**
	 * resize radiobutton selected
	 */
	private void resizeRBPressed() {
		logger.log(Level.FINER, "ResizeRB selected.");
		window.setResizeEnabled(true);
	}

	/**
	 * delete radiobutton selected
	 */
	private void deleteRBPressed() {
		logger.log(Level.FINER, "deleteRB selected.");
		window.setResizeEnabled(false);
	}

	/**
	 * link button pressed
	 */
	private void linkBPressed() {
		logger.log(Level.FINER, "linkB pressed. linked: " + this.linked);

		this.linked = !linked;
		if (this.linked)
			window.setLinkImage(this.link);
		else
			window.setLinkImage(this.unlink);
	}

	/**
	 * ok button pressed
	 */
	private void okBPressed() {
		logger.log(Level.FINER, "okB pressed.");

		// delete
		if (window.isDelete()) {
			closeWindow(0, 0);
		}
		// resize
		else {
			closeWindow(width, height);
		}
	}

	/**
	 * cancel button pressed
	 */
	private void cancelBPressed() {
		logger.log(Level.FINER, "cancelB pressed.");
		closeWindow(-1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusGained(FocusEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
	 */
	@Override
	public void focusLost(FocusEvent e) {
		logger.log(Level.FINER, "Focus Gained. width: " + (e.getSource() == window.getWidthField()));
		// gained width -> height changed
		if (!linked)
			return;

		if (e.getSource() == window.getWidthField()) {
			double factor = (double) window.getImageWidth() / (double) this.width;
			this.height = (int) (height * factor);
			this.width = window.getImageWidth();
			window.setImageSize(width, height);

		} // gained height -> width changed
		else {
			double factor = (double) window.getImageHeight() / (double) this.height;
			this.height = window.getImageHeight();
			this.width = (int) (width * factor);
			window.setImageSize(width, height);
		}
	}
}