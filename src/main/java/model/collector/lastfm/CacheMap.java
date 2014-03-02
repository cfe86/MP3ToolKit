package model.collector.lastfm;

import java.util.LinkedHashMap;

public class CacheMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3935680935215403526L;

	/**
	 * the capacity
	 */
	private int capacity;

	/**
	 * Constructor
	 * 
	 * @param capacity
	 *            the cache capacity
	 */
	public CacheMap(int capacity) {
		super(capacity);
		this.capacity = capacity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		return capacity < super.size();
	}
}