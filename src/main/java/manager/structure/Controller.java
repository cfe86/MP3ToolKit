package manager.structure;

import controller.interfaces.AbstractController;

public class Controller {

	/**
	 * the controller
	 */
	private AbstractController controller;

	/**
	 * index of this tab
	 */
	private int tabIndex;

	/**
	 * true if the main window should observe this controller
	 */
	private boolean observeMain;

	/**
	 * Constructor
	 * 
	 * @param controller
	 *            the controller
	 * @param tabIndex
	 *            the tab index
	 * @param observeMain
	 *            true if main controller should observe this controller, else
	 *            false
	 */
	public Controller(AbstractController controller, int tabIndex, boolean observeMain) {
		this.controller = controller;
		this.tabIndex = tabIndex;
		this.observeMain = observeMain;
	}

	/**
	 * gets the controller
	 * 
	 * @return the controller
	 */
	public AbstractController getController() {
		return controller;
	}

	/**
	 * gets the tab index
	 * 
	 * @return the index
	 */
	public int getTabIndex() {
		return tabIndex;
	}

	/**
	 * true if the main controller should observe this controller
	 * 
	 * @return true or false
	 */
	public boolean observeMain() {
		return observeMain;
	}
}