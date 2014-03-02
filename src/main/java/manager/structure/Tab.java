package manager.structure;

import view.interfaces.AbstractTab;

public class Tab {

	/**
	 * the tabs identifier
	 */
	private String identifier;

	/**
	 * the tab
	 */
	private AbstractTab tab;

	/**
	 * the tab index
	 */
	private int index;

	/**
	 * Constructor
	 * 
	 * @param identifier
	 *            tabs identifier
	 * @param tab
	 *            the tab
	 * @param index
	 *            the tab index
	 */
	public Tab(String identifier, AbstractTab tab, int index) {
		this.identifier = identifier;
		this.tab = tab;
		this.index = index;
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
	 * gets the tab index
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
}