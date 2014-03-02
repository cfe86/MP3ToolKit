package controller.subcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.interfaces.ICmdFieldReplacer;
import model.structure.FieldReplacerData;
import model.util.WindowUtils;
import view.subview.id3.FieldReplacerView;

public class FieldReplacerController extends Observable implements ActionListener {

	/**
	 * the logger
	 */
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * the window
	 */
	private FieldReplacerView window;

	/**
	 * the close command which is called after closing the window
	 */
	private ICmdFieldReplacer closeCommand;

	/**
	 * Constructor
	 */
	public FieldReplacerController() {

	}

	/**
	 * sets the close command
	 * 
	 * @param cmd
	 *            the command
	 */
	public void setCloseCommand(ICmdFieldReplacer cmd) {
		this.closeCommand = cmd;
	}

	/**
	 * creates the window
	 */
	public void createWindow() {
		window = new FieldReplacerView();

		window.init();

		window.setVisible(true);

		window.setActionListener(this);

		this.window.setLocation(WindowUtils.getCenteredWindowCoordinates(this.window));

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeWindow(null);
			}
		});
	}

	/**
	 * closes the window
	 * 
	 * @param data
	 *            the fieldreplacer data which should be transmitted to the id3
	 *            controller, null if the window got just closed
	 */
	public void closeWindow(FieldReplacerData data) {
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
		if (e.getActionCommand().equals("startB"))
			startButtonPressed();
		else if (e.getActionCommand().equals("cancelB"))
			cancelButtonPressed();
		else if (e.getActionCommand().equals("selectAllChB"))
			selectAllButtonPressed();
		else if (e.getActionCommand().equals("addB"))
			addButtonPressed();
		else if (e.getActionCommand().equals("deleteB"))
			deleteButtonPressed();
	}

	/**
	 * start button pressed
	 */
	private void startButtonPressed() {
		logger.log(Level.FINER, "start button pressed.");

		FieldReplacerData data = new FieldReplacerData(window.replaceAllSelected(), window.getCheckedFields(), window.getCheckedProcesses(), window.getConverts());
		closeWindow(data);
	}

	/**
	 * cancel button pressed
	 */
	private void cancelButtonPressed() {
		logger.log(Level.FINER, "cancel button pressed.");

		closeWindow(null);
	}

	/**
	 * select all checkbox pressed
	 */
	private void selectAllButtonPressed() {
		logger.log(Level.FINER, "select all button pressed. all selected: " + window.isAllSelected());

		if (window.isAllSelected())
			window.setCheckBoxes(true);
		else
			window.setCheckBoxes(false);
	}

	/**
	 * add button pressed
	 */
	private void addButtonPressed() {
		logger.log(Level.FINER, "add button pressed.");

		window.addConvertElement();
	}

	/**
	 * delete button pressed
	 */
	private void deleteButtonPressed() {
		logger.log(Level.FINER, "delete button pressed. selected index: " + window.getSelectedIndex());

		int index = window.getSelectedIndex();
		if (index == -1)
			return;

		window.deleteConvertElement(index);
	}
}