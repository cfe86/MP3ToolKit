package model.collector.interfaces;

import model.exception.CollectorException;

public interface ICollector {

	/**
	 * inits the collector
	 * 
	 * @throws CollectorException
	 *             thrown if collector couldn't be initialised
	 */
	public void init() throws CollectorException;

	/**
	 * gets the collector name
	 * 
	 * @return the name
	 */
	public String getCollectorName();

	/**
	 * true if data is found, else false
	 * 
	 * @return true or false
	 */
	public boolean isDataFound();
}
