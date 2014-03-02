package manager;

import java.util.ArrayList;
import java.util.List;

import config.Config;
import manager.structure.Collector;
import model.collector.chartlyrics.ChartLyricsCollector;
import model.collector.interfaces.ICollector;
import model.collector.interfaces.ICoverArtCollector;
import model.collector.interfaces.IID3DataCollector;
import model.collector.interfaces.ILyricsCollector;
import model.collector.lastfm.LastfmCoverArtCollector;
import model.collector.lastfm.LastfmTrackCollector;
import model.collector.lyricstime.LyricstimeCollector;

public class CollectorManager {

	/**
	 * instance
	 */
	private static CollectorManager instance;

	/**
	 * ID3 data collector ID
	 */
	public final static int ID3DATA_COLLECTOR = 0;

	/**
	 * Cover collector ID
	 */
	public final static int COVER_COLLECTOR = 1;

	/**
	 * Lyrics collector ID
	 */
	public final static int LYRICS_COLLECTOR = 2;

	/**
	 * the registered ID3 data collectors
	 */
	private List<IID3DataCollector> id3dataCollectors;

	/**
	 * all enabled ID3 data collector indentifiers
	 */
	private List<String> usedID3Collectors;

	/**
	 * the registered cover collectors
	 */
	private List<ICoverArtCollector> coverCollectors;

	/**
	 * the enabled cover collector identifiers
	 */
	private List<String> usedCoverCollectors;

	/**
	 * the registered lyrics collectors
	 */
	private List<ILyricsCollector> lyricsCollectors;

	/**
	 * the enabled lyrics collector identifiers
	 */
	private List<String> usedLyricsCollectors;

	/**
	 * gets the instance
	 * 
	 * @return the instance
	 */
	public static CollectorManager getInstance() {
		if (instance == null)
			instance = new CollectorManager();

		return instance;
	}

	/**
	 * registers the collectors
	 */
	private void register() {

		// ***************************
		// Register Collectors here: *
		// ***************************
		// register collectors here, if no order is given in the settings file,
		// this order will be taken
		// id3 data collector
		registerID3DataCollector(new LastfmTrackCollector());
		// cover image collector
		registerCoverCollector(new LastfmCoverArtCollector());
		// lyrics collector
		registerLyricsCollector(new ChartLyricsCollector());
		registerLyricsCollector(new LyricstimeCollector());

		// *************************
		// End Register Collectors *
		// *************************
	}

	/**
	 * Constructor
	 */
	private CollectorManager() {
		id3dataCollectors = new ArrayList<>();
		coverCollectors = new ArrayList<>();
		lyricsCollectors = new ArrayList<>();

		// register all
		register();

		usedID3Collectors = Config.getInstance().getID3DataCollectors();
		usedCoverCollectors = Config.getInstance().getCoverCollectors();
		usedLyricsCollectors = Config.getInstance().getLyricsCollectors();

		// if no valid collector is given, add all
		if (usedID3Collectors.isEmpty())
			usedID3Collectors = fill(this.id3dataCollectors);
		if (usedCoverCollectors.isEmpty())
			usedCoverCollectors = fill(this.coverCollectors);
		if (usedLyricsCollectors.isEmpty())
			usedLyricsCollectors = fill(this.lyricsCollectors);
	}

	/**
	 * creates a list with the identifiers of the given collector list
	 * 
	 * @param collectors
	 *            the collectors
	 * 
	 * @return the identifier list
	 */
	private List<String> fill(List<? extends ICollector> collectors) {
		List<String> result = new ArrayList<>();

		for (ICollector c : collectors)
			result.add(c.getCollectorName());

		return result;
	}

	/**
	 * gets the collector with the given identifier out of the given collector
	 * list
	 * 
	 * @param lst
	 *            given collector list
	 * @param name
	 *            given identifier
	 * 
	 * @return the collector or null, if not found
	 */
	private ICollector getCollector(List<? extends ICollector> lst, String name) {
		for (ICollector c : lst) {
			if (c.getCollectorName().equals(name))
				return c;
		}

		return null;
	}

	/**
	 * sets the enabled collectors
	 * 
	 * @param collectors
	 *            the collectors
	 * @param type
	 *            collector ID
	 */
	public void setCollectors(List<String> collectors, int type) {
		if (type == ID3DATA_COLLECTOR)
			this.usedID3Collectors = collectors;
		else if (type == COVER_COLLECTOR)
			this.usedCoverCollectors = collectors;
		else if (type == LYRICS_COLLECTOR)
			this.usedLyricsCollectors = collectors;
	}

	/**
	 * checks if the given identifier is in the given collector list
	 * 
	 * @param name
	 *            given identifier
	 * @param used
	 *            given collector list
	 * 
	 * @return true if found, else false
	 */
	private boolean isIn(String name, List<String> used) {
		for (String str : used) {
			if (name.equals(str))
				return true;
		}

		return false;
	}

	/**
	 * gets the collectors, enabled one first, disabled last
	 * 
	 * @param type
	 *            collector ID
	 * 
	 * @return the wanted collectors
	 */
	public List<Collector> getCollectors(int type) {
		List<Collector> result = new ArrayList<>();

		List<? extends ICollector> lst = null;
		List<String> used = null;
		if (type == ID3DATA_COLLECTOR) {
			lst = this.id3dataCollectors;
			used = this.usedID3Collectors;
		} else if (type == COVER_COLLECTOR) {
			lst = this.coverCollectors;
			used = this.usedCoverCollectors;
		} else if (type == LYRICS_COLLECTOR) {
			lst = this.lyricsCollectors;
			used = this.usedLyricsCollectors;
		}

		// add enabled collectors in this order
		for (String u : used) {
			ICollector c = getCollector(lst, u);
			if (c == null)
				continue;

			result.add(new Collector(c.getCollectorName(), true));
		}
		// add all unabled collectors which arent in this list
		for (ICollector c : lst) {
			if (!isIn(c.getCollectorName(), used))
				result.add(new Collector(c.getCollectorName(), false));
		}

		return result;
	}

	/**
	 * gets all enabled ID3 data Collectors in the correct order
	 * 
	 * @return the enabled ID3 data collectors
	 */
	public List<IID3DataCollector> getID3DataCollectors() {
		List<IID3DataCollector> result = new ArrayList<>();

		for (String name : this.usedID3Collectors) {
			ICollector c = getCollector(this.id3dataCollectors, name);
			if (c != null)
				result.add((IID3DataCollector) c);
		}

		return result;
	}

	/**
	 * gets all enabled cover Collectors in the correct order
	 * 
	 * @return the enabled cover collectors
	 */
	public List<ICoverArtCollector> getCoverArtCollectors() {
		List<ICoverArtCollector> result = new ArrayList<>();

		for (String name : this.usedCoverCollectors) {
			ICollector c = getCollector(this.coverCollectors, name);
			if (c != null)
				result.add((ICoverArtCollector) c);
		}

		return result;
	}

	/**
	 * gets all enabled lyrics Collectors in the correct order
	 * 
	 * @return the enabled lyrics collectors
	 */
	public List<ILyricsCollector> getLyricsCollectors() {
		List<ILyricsCollector> result = new ArrayList<>();

		for (String name : this.usedLyricsCollectors) {
			ICollector c = getCollector(this.lyricsCollectors, name);
			if (c != null)
				result.add((ILyricsCollector) c);
		}

		return result;
	}

	/**
	 * registeres the given ID3 data collector
	 * 
	 * @param collector
	 *            given collector
	 */
	private void registerID3DataCollector(IID3DataCollector collector) {
		if (collector != null)
			this.id3dataCollectors.add(collector);
	}

	/**
	 * registers the given cover collector
	 * 
	 * @param collector
	 *            given collector
	 */
	private void registerCoverCollector(ICoverArtCollector collector) {
		if (collector != null)
			this.coverCollectors.add(collector);
	}

	/**
	 * registers the given lyrics collector
	 * 
	 * @param collector
	 *            given lyrics collector
	 */
	private void registerLyricsCollector(ILyricsCollector collector) {
		if (collector != null)
			this.lyricsCollectors.add(collector);
	}

	/**
	 * saves the collector configuration
	 */
	public void saveConfig() {
		Config.getInstance().setID3DataCollectors(listToString(this.usedID3Collectors));
		Config.getInstance().setCoverCollectors(listToString(this.usedCoverCollectors));
		Config.getInstance().setLyricsCollectors(listToString(this.usedLyricsCollectors));
	}

	/**
	 * joins the given list of strings with comma seperation
	 * 
	 * @param lst
	 *            given list
	 * 
	 * @return the comma joined list
	 */
	private String listToString(List<String> lst) {
		String result = "";
		for (String str : lst) {
			result += str + ",";
		}

		if (result.length() > 0)
			result = result.substring(0, result.length() - 1);

		return result;
	}
}