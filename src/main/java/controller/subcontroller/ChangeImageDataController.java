package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ChangeImageDataModel;
import model.structure.ID3ImageData;
import model.util.Util;
import model.util.WindowUtils;
import view.subview.id3.ChangeImageDataView;

import com.cf.structures.DataDouble;

import controller.interfaces.ICmdChangeImage;

public class ChangeImageDataController extends Observable implements ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the model
	 */
	private ChangeImageDataModel model;

	/**
	 * the window
	 */
	private ChangeImageDataView window;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdChangeImage closeCommand;

	/**
	 * Constructor
	 */
	public ChangeImageDataController() {
		model = new ChangeImageDataModel();
	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCommand(ICmdChangeImage cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 * 
	 * @param data
	 *            the data list which maps old data to new data
	 */
	public void createWindow(List<DataDouble<ID3ImageData, ID3ImageData>> data) {

		model.setIndex(-1);
		model.setImages(data);

		if (model.isDone())
			return;

		window = new ChangeImageDataView();

		window.init();

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow(null);
			}
		});

		Util.sleep(300);

		nextButtonPressed();
	}

	/**
	 * close window
	 * 
	 * @param data
	 *            the new image data which will be given to the id3 controller, null if window is just closed
	 */
	public void closeWindow(List<ID3ImageData> data) {
		window.dispose();
		closeCommand.call(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("nextTagB"))
			nextButtonPressed();
	}

	/**
	 * next button pressed
	 */
	private void nextButtonPressed() {
		// save data
		logger.log(Level.FINER, "next button pressed.");
		model.createNewImage(window.getImageChB());

		model.incrementIndex();

		if (model.isDone()) {
			List<ID3ImageData> data = model.getNewImages();
			model.setIndex(-1);
			closeWindow(data);
			return;
		}

		if (window.isRepeatSelected()) {
			nextButtonPressed();
			return;
		}

		window.setCounter(model.getIndex() + 1, model.getListSize());
		try {
			window.setCurrImage(model.getCurrImage());
			window.setNewImage(model.getNewImage());
			window.setArtist(model.getCurrImage().getArtist(), model.getCurrImage().getAlbum());
		} catch (IOException e) {
			window.showMessage("ImageLoadError");
		}
	}
}