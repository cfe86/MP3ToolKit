package model.collector.lastfm;

import java.util.Map;

import model.collector.Album;

public class Cache {

	/**
	 * the buffer size
	 */
	private static int BUFFER_SIZE = 20;

	/**
	 * maps the album name to Album
	 */
	private static Map<String, Album> cache = new CacheMap<>(BUFFER_SIZE);

	/**
	 * resets the cache
	 */
	public static void resetCache() {
		cache = new CacheMap<>(BUFFER_SIZE);
	}

	/**
	 * sets the buffer size
	 * 
	 * @param capacity
	 *            the size
	 */
	public static void setBufferSize(int capacity) {
		BUFFER_SIZE = capacity;
	}

	/**
	 * gets the album from the cache
	 * 
	 * @param name
	 *            album name
	 * 
	 * @return the album if cached, else null
	 */
	public static Album getAlbum(String name) {
		return cache.get(name.toLowerCase());
	}

	/**
	 * adds an album to the cache
	 * 
	 * @param artistAlbum
	 *            the album name
	 * @param album
	 *            the album
	 */
	public static void addAlbum(String artistAlbum, Album album) {
		cache.put(artistAlbum.toLowerCase(), album);
	}
}