package manager.structure;

public class Collector {

	/**
	 * the identifier for the collector
	 */
	private String identifier;

	/**
	 * true if this collector is enabled, else false
	 */
	private boolean isEnabled;

	/**
	 * Constructor
	 * 
	 * @param identifier
	 *            the identifier
	 * @param enabled
	 *            true if enabled, else false
	 */
	public Collector(String identifier, boolean enabled) {
		this.identifier = identifier;
		this.isEnabled = enabled;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIdentifier() {
		return identifier;
	}
}