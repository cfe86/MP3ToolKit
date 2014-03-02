package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.interfaces.ICmdGenerateTabByFilename;
import view.subview.id3.GenByNameView;
import logging.LogUtil;
import model.GenByNameModel;
import model.ID3TagRegex;
import model.util.Commons;
import model.util.WindowUtils;

public class GenByNameController implements ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the window
	 */
	private GenByNameView window;

	/**
	 * the model
	 */
	private GenByNameModel model;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdGenerateTabByFilename closeCommand;

	/**
	 * Controller
	 */
	public GenByNameController() {
		model = new GenByNameModel();
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCmd(ICmdGenerateTabByFilename cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 */
	public void createWindow() {
		window = new GenByNameView();

		try {
			window.init();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while opening window:\n" + LogUtil.getStackTrace(e), e);
			window.showMessage("openError");
			return;
		}

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow(null, false);
			}
		});

		try {
			window.setMasks(Commons.readMasks());
		} catch (IOException e) {
			window.showMessage("noMaskFile");
		}
	}

	/**
	 * closes the window
	 * 
	 * @param regex
	 *            the given regex or null, if the window just got closed
	 * @param selectAll
	 *            true if select all is checked, else false
	 */
	public void closeWindow(ID3TagRegex regex, boolean selectAll) {
		window.dispose();
		closeCommand.call(regex, selectAll);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("startB"))
			startBPressed();
		else if (e.getActionCommand().equals("cancelB"))
			cancelBPressed();
	}

	/**
	 * start button pressed
	 */
	private void startBPressed() {

		String reg = window.getRegex();

		if (reg.trim().length() == 0) {
			window.showMessage("regexEmpty");
			return;
		}

		ID3TagRegex regex = model.generateRegex(reg);
		closeWindow(regex, window.isReplaceAll());
	}

	/**
	 * cancel button pressed
	 */
	private void cancelBPressed() {
		logger.log(Level.FINER, "Cancel button pressed.");

		closeWindow(null, false);
	}
}