package manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import manager.structure.Controller;
import manager.structure.Tab;
import manager.structure.TabController;
import controller.FolderCreatorController;
import controller.ID3TagController;
import controller.MP3GainController;
import controller.RenameToolController;
import controller.interfaces.AbstractController;
import view.FolderCreatorTab;
import view.ID3TagTab;
import view.MP3GainTab;
import view.RenameToolTab;
import view.interfaces.AbstractTab;

public class TabManager {

	/**
	 * map containing all tabs
	 */
	private Map<String, TabController> tabs;

	/**
	 * the instance
	 */
	private static TabManager instance;

	/**
	 * gets the instance
	 * 
	 * @return the instance
	 */
	public static TabManager getInstance() {
		if (instance == null)
			instance = new TabManager();

		return instance;
	}

	/**
	 * Constructor
	 */
	private TabManager() {
		this.tabs = new HashMap<>();

		// *********************
		// Register Tabs here: *
		// *********************
		// the order is the order of the tabs
		register("id3Tab", new ID3TagTab(), new ID3TagController(), new String[] {});
		register("mp3GainTab", new MP3GainTab(), new MP3GainController(), new String[] {});
		register("RenameTab", new RenameToolTab(), new RenameToolController(), new String[] {});
		register("StructureTab", new FolderCreatorTab(), new FolderCreatorController(), new String[] {});

		// *******************
		// End Register Tabs *
		// *******************

		setObservers();
	}

	/**
	 * registers a new tab
	 * 
	 * @param identifier
	 *            identifer of this tab
	 * @param tab
	 *            the tab
	 * @param controller
	 *            the controller
	 * @param observers
	 *            array with all observers which should get information from
	 *            this controller
	 */
	private void register(String identifier, AbstractTab tab, AbstractController controller, String[] observers) {
		this.tabs.put(identifier, new TabController(identifier, tab, controller, this.tabs.size(), observers));
	}

	/**
	 * sets all the observers which should be registered to each controller
	 */
	private void setObservers() {
		for (TabController con : this.tabs.values()) {
			for (int i = 0; i < con.getObserver().length; i++) {
				if (this.tabs.containsKey(con.getObserver()[i])) {
					con.getController().addObserver(this.tabs.get(con.getObserver()[i]).getController());
				}
			}
		}
	}

	/**
	 * gets a list with all tabs for the main view
	 * 
	 * @return the tab list
	 */
	public List<Tab> getTabs() {
		List<Tab> result = new ArrayList<>();

		for (TabController t : this.tabs.values())
			result.add(new Tab(t.getIdentifier(), t.getTab(), t.getIndex()));

		Collections.sort(result, new Comparator<Tab>() {
			@Override
			public int compare(Tab t1, Tab t2) {
				if (t1.getIndex() > t2.getIndex())
					return 1;
				else if (t1.getIndex() < t2.getIndex())
					return -1;
				else
					return 0;
			}
		});

		return result;
	}

	/**
	 * gets a map with all controllers where the identifier maps to the
	 * controller, for the main controller
	 * 
	 * @return the controller map
	 */
	public Map<String, Controller> getController() {
		Map<String, Controller> result = new HashMap<>();

		for (TabController t : this.tabs.values())
			result.put(t.getIdentifier(), new Controller(t.getController(), t.getIndex(), t.observeMain()));

		return result;
	}
}