package manager.structure;

import controller.interfaces.AbstractController;
import view.interfaces.AbstractTab;

public class TabController {

	/**
	 * tabs identifier
	 */
	private String identifier;

	/**
	 * the tab
	 */
	private AbstractTab tab;

	/**
	 * the controller
	 */
	private AbstractController controller;

	/**
	 * tabs index
	 */
	private int index;

	/**
	 * controller which should observe this controller
	 */
	private String[] observer;

	/**
	 * Constructor
	 * 
	 * @param identifier
	 *            tabs indentifier
	 * @param tab
	 *            the tab
	 * @param controller
	 *            the controller
	 * @param index
	 *            the tab index
	 * @param observer
	 *            controller which should observe this controller
	 */
	public TabController(String identifier, AbstractTab tab, AbstractController controller, int index, String[] observer) {
		this.identifier = identifier;
		this.tab = tab;
		this.controller = controller;
		this.index = index;
		this.observer = observer;
	}

	/**
	 * gets the identifier
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * gets the tab
	 * 
	 * @return the tab
	 */
	public AbstractTab getTab() {
		return tab;
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
	 * @return the tab index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * gets all the observers
	 * 
	 * @return the observers
	 */
	public String[] getObserver() {
		return observer;
	}

	/**
	 * true if the main controller should observe this controller
	 * 
	 * @return true or false
	 */
	public boolean observeMain() {
		for (int i = 0; i < observer.length; i++) {
			if (this.observer[i].equalsIgnoreCase("main"))
				return true;
		}

		return false;
	}
}