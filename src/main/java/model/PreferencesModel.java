package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import config.Config;
import manager.CollectorManager;
import manager.structure.Collector;
import model.util.Commons;

public class PreferencesModel {

	/**
	 * all id3 collectors
	 */
	private List<Collector> id3dataCollectors;

	/**
	 * all cover collectors
	 */
	private List<Collector> coverCollectors;

	/**
	 * all lyrics collectors
	 */
	private List<Collector> lyricsCollectors;

	/**
	 * all masks
	 */
	private List<String> masks;

	/**
	 * Constructor
	 */
	public PreferencesModel() {
		this.id3dataCollectors = CollectorManager.getInstance().getCollectors(CollectorManager.ID3DATA_COLLECTOR);
		this.coverCollectors = CollectorManager.getInstance().getCollectors(CollectorManager.COVER_COLLECTOR);
		this.lyricsCollectors = CollectorManager.getInstance().getCollectors(CollectorManager.LYRICS_COLLECTOR);
		this.masks = new ArrayList<>();
	}

	/**
	 * gets the wanted collectors
	 * 
	 * @param type
	 *            collector ID
	 * 
	 * @return wanted collectors
	 */
	public List<String> getCollectors(int type) {
		List<String> result = new ArrayList<>();
		List<Collector> collector = null;
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			collector = id3dataCollectors;
		else if (type == CollectorManager.COVER_COLLECTOR)
			collector = coverCollectors;
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			collector = lyricsCollectors;

		for (Collector c : collector)
			result.add(c.getIdentifier() + (!c.isEnabled() ? " (disabled)" : ""));

		return result;
	}

	/**
	 * changed the collector state. If the collector is enabled it will be
	 * disabled, if it is disabled it is enabled
	 * 
	 * @param index
	 *            index of the collector
	 * @param type
	 *            collector ID
	 */
	public void changeCollectorState(int index, int type) {
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			this.id3dataCollectors.get(index).setEnabled(!this.id3dataCollectors.get(index).isEnabled());
		else if (type == CollectorManager.COVER_COLLECTOR)
			this.coverCollectors.get(index).setEnabled(!this.coverCollectors.get(index).isEnabled());
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			this.lyricsCollectors.get(index).setEnabled(!this.lyricsCollectors.get(index).isEnabled());
	}

	/**
	 * checks if only one collector is enabled
	 * 
	 * @param type
	 *            given collector ID
	 * 
	 * @return true if one collector is enabled, if more than one false
	 */
	public boolean isLastCollector(int type) {
		List<Collector> lst = null;
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			lst = this.id3dataCollectors;
		else if (type == CollectorManager.COVER_COLLECTOR)
			lst = this.coverCollectors;
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			lst = this.lyricsCollectors;

		int count = 0;
		for (Collector c : lst) {
			if (c.isEnabled())
				count++;
		}

		return count <= 1;
	}

	/**
	 * shifts the given collector one position up if possible (in the list to
	 * the left)
	 * 
	 * @param index
	 *            given index of the collector
	 * @param type
	 *            collector ID
	 * 
	 * @return the new index of this collector
	 */
	public int shiftUp(int index, int type) {
		List<Collector> collector = null;
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			collector = this.id3dataCollectors;
		else if (type == CollectorManager.COVER_COLLECTOR)
			collector = this.coverCollectors;
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			collector = this.lyricsCollectors;

		// check if at first pos
		if (index == 0)
			return index;
		// change the current index and switch with index-1
		Collector tmp = collector.get(index);
		collector.set(index, collector.get(index - 1));
		collector.set(index - 1, tmp);

		return index - 1;
	}

	/**
	 * shifts the given collector one position down if possible (in the list to
	 * the right)
	 * 
	 * @param index
	 *            given index of the collector
	 * @param type
	 *            collector ID
	 * 
	 * @return the new index of this collector
	 */
	public int shiftDown(int index, int type) {
		List<Collector> collector = null;
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			collector = this.id3dataCollectors;
		else if (type == CollectorManager.COVER_COLLECTOR)
			collector = this.coverCollectors;
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			collector = this.lyricsCollectors;

		// check if at last pos
		if (collector.size() == index + 1)
			return index;
		// change the current index and switch with index+1
		Collector tmp = collector.get(index);
		collector.set(index, collector.get(index + 1));
		collector.set(index + 1, tmp);

		return index + 1;
	}

	/**
	 * checks if the collector at the given index is enabled
	 * 
	 * @param index
	 *            given collector index
	 * @param type
	 *            collector ID
	 * 
	 * @return true if enabled, else false
	 */
	public boolean isCollectorEnabled(int index, int type) {
		if (type == CollectorManager.ID3DATA_COLLECTOR)
			return this.id3dataCollectors.get(index).isEnabled();
		else if (type == CollectorManager.COVER_COLLECTOR)
			return this.coverCollectors.get(index).isEnabled();
		else if (type == CollectorManager.LYRICS_COLLECTOR)
			return this.lyricsCollectors.get(index).isEnabled();

		return false;
	}

	/**
	 * reads the masks file and sorts the masks
	 * 
	 * @throws IOException
	 *             thrown if the mask file couldn't be read
	 */
	public void readMasks() throws IOException {
		this.masks = Commons.readMasks();
		sortMasks();
	}

	/**
	 * sorts the masks alphabetically, not case sensitive
	 */
	private void sortMasks() {
		Collections.sort(this.masks, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
	}

	/**
	 * writes the masks to the masks file
	 * 
	 * @throws IOException
	 *             thrown if masks couldn't be written
	 */
	public void writeMasks() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Config.getInstance().getMasksPath())));
		for (String mask : this.masks)
			bw.write(mask + "\n");

		bw.close();
	}

	/**
	 * adds the given mask
	 * 
	 * @param mask
	 *            given mask
	 * 
	 * @return true if added, else false if already a known mask
	 */
	public boolean addMask(String mask) {
		// check if mask is already given
		for (String m : this.masks) {
			if (m.equals(mask))
				return false;
		}

		this.masks.add(mask);
		sortMasks();

		return true;
	}

	/**
	 * deletes the mask at the given index
	 * 
	 * @param index
	 *            given mask index
	 */
	public void deleteMask(int index) {
		this.masks.remove(index);
	}

	/**
	 * gets a list with all masks
	 * 
	 * @return the mask list
	 */
	public List<String> getMasks() {
		return this.masks;
	}

	/**
	 * saves the made settings
	 * 
	 * @throws IOException
	 *             thrown if masks file couldn't be written
	 */
	public void saveConfig() throws IOException {
		// id3data
		List<String> id3Data = new ArrayList<>();
		for (Collector c : this.id3dataCollectors) {
			if (c.isEnabled())
				id3Data.add(c.getIdentifier());
		}

		// cover
		List<String> cover = new ArrayList<>();
		for (Collector c : this.coverCollectors) {
			if (c.isEnabled())
				cover.add(c.getIdentifier());
		}

		// lyrics
		List<String> lyrics = new ArrayList<>();
		for (Collector c : this.lyricsCollectors) {
			if (c.isEnabled())
				lyrics.add(c.getIdentifier());
		}

		CollectorManager.getInstance().setCollectors(id3Data, CollectorManager.ID3DATA_COLLECTOR);
		CollectorManager.getInstance().setCollectors(cover, CollectorManager.COVER_COLLECTOR);
		CollectorManager.getInstance().setCollectors(lyrics, CollectorManager.LYRICS_COLLECTOR);

		writeMasks();
	}
}